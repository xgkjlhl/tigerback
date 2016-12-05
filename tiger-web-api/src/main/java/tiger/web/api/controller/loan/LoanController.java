/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.web.api.controller.loan;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiger.biz.attach.support.AttachRelateManager;
import tiger.biz.customer.support.CustomerManager;
import tiger.biz.loan.support.LoanManager;
import tiger.biz.loan.support.LoanPawnManager;
import tiger.biz.util.CostCalculatorUtil;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoanValidCheck;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.AttachBizTypeEnum;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.DBOrderTypeEnum;
import tiger.common.dal.enums.LoanCustomerTypeEnum;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.common.dal.query.LoanQuery;
import tiger.common.util.JsonUtil;
import tiger.common.util.StringUtil;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.*;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.component.KafkaService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.customer.CustomerUpdateForm;
import tiger.web.api.form.loan.LoanAssignmentForm;
import tiger.web.api.form.loan.LoanCreateForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;


/**
 * 贷款/融资项目接口
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 10, 2015 9:21:55 PM yiliang.gyl Exp $
 */
@RestController
@ResponseBody
public class LoanController extends BaseController {

    private final Logger logger = Logger.getLogger(LoanController.class);

    @Autowired
    private CustomerManager customerManager;

    @Autowired
    private LoanManager loanManager;

    @Autowired
    private LoanPawnManager loanPawnManager;

    @Autowired
    private AttachRelateManager attachRelateManager;

    @Autowired
    private WorkspaceManager workspaceManager;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 创建一个新的贷款/融资
     * ~ 融资未完成
     *
     * @return the string
     */
    @RequestMapping(value = "loan", method = RequestMethod.POST, params = "operation=createWithAll")
    @Permission(permission = {PermissionEnum.CREATE_LOAN})
    @Transactional
    public BaseResult<LoanDomain> create(@RequestBody @Valid LoanCreateForm loanCreateForm,
                                         BindingResult bindingResult) {
//        JsonUtil.toJson(loanCreateForm);
        System.out.println(JsonUtil.toJson(loanCreateForm));
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 创建合同 [" + loanCreateForm + "]");
        }
        try {
//            Double scheduleCost= CostCalculatorUtil.singlePeriodCost(loanCreateForm.getCostSchedule(),loanCreateForm.getPayTotalCircle());
//            loanCreateForm.setCostSchedule(scheduleCost);
            LoanDomain domain = createWorkspaceLoan(loanCreateForm);

            //1. 关联客户
            CustomerDomain customerDomain = loanCreateForm.getLoaner().convert2Domain();
            if (customerDomain != null) {
                domain.setLoaner(relateLoanCustomer(customerDomain, domain.getId(), LoanCustomerTypeEnum.LOANER));
            }
            if (domain.getHasHolder()) {
                domain.setHolder(relateLoanCustomer(loanCreateForm.getHolder().convert2Domain(),
                    domain.getId(), LoanCustomerTypeEnum.HOLDER));
            }

            //2. 关联抵押物
            if (domain.getType() != LoanPawnTypeEnum.NO_PAWN) {
                LoanPawnDomain loanPawnDomain = loanCreateForm.getPawn().convert2Domain();
                domain.setLoanPawnDomain(relateLoanPawn(loanPawnDomain, domain.getId()));
            }
            //3. 返回贷款项目
            return loanManager.readLoan(domain.getId());
        } catch (Exception e) {
            logger.error(e);
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "新建合同失败");
        }
    }

    /**
     * 创建一个新的贷款/融资
     * ~ 融资未完成
     *
     * @return the string
     */
    @RequestMapping(value = "loan", method = RequestMethod.POST, params = "operation=manualCreate")
    @Permission(permission = {PermissionEnum.CREATE_LOAN})
    @Transactional
    public BaseResult<LoanDomain> manualCreate(@RequestBody @Valid LoanCreateForm loanCreateForm,
                                         BindingResult bindingResult) {
        System.out.println(JsonUtil.toJson(loanCreateForm));
        if (logger.isInfoEnabled()) {
            logger.info("用户 [" + currentAccount().getId() + "] 创建合同 [" + loanCreateForm + "]");
        }
        try {
            //计算分期费用
//            Double scheduleCost= CostCalculatorUtil.singlePeriodCost(loanCreateForm.getCostSchedule(),loanCreateForm.getPayTotalCircle());
//            loanCreateForm.setCostSchedule(scheduleCost);

            LoanDomain domain = loanCreateForm.convert2Domain();
            domain.setWorkspaceId(currentWorkspaceId());
            // 当前用户没有CREATE_LOAN权限; 则抛出异常
            if (!isPermitted(PermissionEnum.CREATE_LOAN)) {
                throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
            }
            domain.setAccountId(currentAccount().getId());
            List<ModifyLoanRecordDomain> modifyLoanRecordDomains=loanCreateForm.getModifyLoanRecordDomains();
            LoanDomain resultDomain= loanManager.manualCreateLoan(domain,modifyLoanRecordDomains);
           // LoanDomain domain = createWorkspaceLoan(loanCreateForm);


            //1. 关联客户
            CustomerDomain customerDomain = loanCreateForm.getLoaner().convert2Domain();
            if (customerDomain != null) {
                resultDomain.setLoaner(relateLoanCustomer(customerDomain, resultDomain.getId(), LoanCustomerTypeEnum.LOANER));
            }
            if (resultDomain.getHasHolder()) {
                resultDomain.setHolder(relateLoanCustomer(loanCreateForm.getHolder().convert2Domain(),
                        resultDomain.getId(), LoanCustomerTypeEnum.HOLDER));
            }

            //2. 关联抵押物
            if (resultDomain.getType() != LoanPawnTypeEnum.NO_PAWN) {
                LoanPawnDomain loanPawnDomain = loanCreateForm.getPawn().convert2Domain();
                resultDomain.setLoanPawnDomain(relateLoanPawn(loanPawnDomain, resultDomain.getId()));
            }
            //3. 返回贷款项目
            return loanManager.readLoan(resultDomain.getId());
        } catch (Exception e) {
            logger.error(e);
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL, e.getMessage());
        }
    }


    /**
     * 创建一个新的贷款
     *
     * @return the string
     */
    @RequestMapping(value = "loan", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.CREATE_LOAN})
    public BaseResult<LoanDomain> createLoan(@RequestBody @Valid LoanCreateForm loanCreateForm,
                                             BindingResult bindingResult) {
        if (logger.isInfoEnabled()) {
            logger.info("用户[" + currentAccount().getId() + "]创建合同 [" + loanCreateForm + "]");
        }

        LoanDomain loan = createWorkspaceLoan(loanCreateForm);

        return new BaseResult<>(loan);
    }

    /**
     * 查看一个项目的还款周期（适用于未创建的项目）
     *
     * @param loanCreateForm = 创建项目需要的参数
     *
     * @return 一组还款列表，包含放款
     * @see LoanCreateForm
     */
    @RequestMapping(value = "loan/payItems", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.CREATE_LOAN})
    public BaseResult<List<LoanRecordDomain>> payItems(@RequestBody @Valid LoanCreateForm loanCreateForm,
                                                       BindingResult bindingResult) {
//        Double scheduleCost= CostCalculatorUtil.singlePeriodCost(loanCreateForm.getCostSchedule(),loanCreateForm.getPayTotalCircle());
//        loanCreateForm.setCostSchedule(scheduleCost);
       // createLoanWithAll();
        return loanManager.getPreviewPayItems(loanCreateForm.convert2Domain());
    }

    public void createLoanWithAll(){
        LoanCreateForm loanCreateForm=new LoanCreateForm();
        List<ModifyLoanRecordDomain> list=new ArrayList<>();
        for(int i=0;i<10;i++){
            ModifyLoanRecordDomain modifyLoanRecordDomain=new ModifyLoanRecordDomain();
            modifyLoanRecordDomain.setAmount(200.0);
            modifyLoanRecordDomain.setInterest(50.0);
            modifyLoanRecordDomain.setCostSchedule(100.0);
            list.add(modifyLoanRecordDomain);
        }
        ModifyLoanRecordDomain lastLoan=new ModifyLoanRecordDomain();
        lastLoan.setAmount(0.0);
        lastLoan.setInterest(0.0);
        lastLoan.setCostSchedule(0.0);
        lastLoan.setBondPerformMoney(500.0);
        lastLoan.setTempMoney(200.0);
        list.add(lastLoan);
        String json=JsonUtil.toJson(list);
        System.out.println(json);
    }

    /**
     * 把一个项目放入垃圾箱
     * ~ 把项目放到垃圾箱
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}", method = RequestMethod.DELETE, params = "operation=trash")
    @Permission(permission = {PermissionEnum.DELETE_LOAN_TEMPORARY_MEMBER, PermissionEnum.DELETE_LOAN_TEMPORARY_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> trashLoan(@PathVariable("id") Long id) {
        Long currentAccountId = currentAccount().getId();
        // 如果当前用户没有 DELETE_LOAN_TEMPORARY_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccountId, PermissionEnum.DELETE_LOAN_TEMPORARY_ALL);

        BaseResult<Boolean> trashResult = loanManager.trashLoan(id);

        // 如果删除成功, 则添加消息和动态
        if (trashResult.getData()) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, id, MessageTypeEnum.LOAN_TEMPORARY_DELETE, null));
        }

        return trashResult;
    }

    /**
     * 把一个项目从垃圾箱恢复
     * ~ 把项目从垃圾箱恢复
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}", method = RequestMethod.PUT, params = "operation=recover")
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_RECOVERY_MEMBER, PermissionEnum.UPDATE_LOAN_RECOVERY_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<Boolean> recoverLoan(@PathVariable("id") Long id) {
        Long currentAccountId = currentAccount().getId();

        // 如果当前用户没有 UPDATE_LOAN_RECOVERY_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccountId, PermissionEnum.UPDATE_LOAN_RECOVERY_ALL);

        BaseResult<Boolean> recoverResult = loanManager.recoverLoan(id);

        // 如果恢复成功, 则添加动态
        if (recoverResult.getData()) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, id, MessageTypeEnum.LOAN_RECOVERY, null));
        }

        return recoverResult;
    }

    /**
     * 把一个项目彻底删除
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}", method = RequestMethod.DELETE, params = "operation=delete")
    @Permission(permission = {PermissionEnum.DELETE_LOAN_PERMANENTLY_MEMBER, PermissionEnum.DELETE_LOAN_PERMANENTLY_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<Boolean> deleteLoan(@PathVariable("id") Long id) {
        Long currentAccountId = currentAccount().getId();

        // 如果当前用户没有 DELETE_LOAN_PERMANENTLY_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccountId, PermissionEnum.DELETE_LOAN_PERMANENTLY_ALL);

        BaseResult<Boolean> deleteResult = loanManager.deleteLoan(id);

        // 如果删除成功, 则添加消息和动态
        if (deleteResult.getData()) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, id, MessageTypeEnum.LOAN_PERMANENTLY_DELETE, null));
        }

        return deleteResult;
    }

    /**
     * 关联项目借款人和担保人
     * ~ 整合到一个api去
     *
     * @param loanId
     * @param customerId
     * @param customerType
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/customer/{customerId}", method = RequestMethod.PUT, params = "operation=relate")
    @Permission(permission = {PermissionEnum.CREATE_LOAN})
    @LoanValidCheck
    public BaseResult<Object> relateLoaner(@PathVariable("id") Long loanId,
                                           @PathVariable("customerId") Long customerId,
                                           @RequestParam("customerType") String customerType) {
        // 判断贷款合同是否为当前用户所有
        loanManager.checkIsOwner(loanId, currentAccount().getId());

        // 检查客户是否存在
        CustomerDomain customer = customerManager.read(customerId);
        if (customer == null || !currentWorkspaceId().equals(customer.getWorkspaceId())) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND, "不存在的客户");
        }

        //~ 获取获取关联客户类型
        LoanCustomerTypeEnum customerTypeEnum = LoanCustomerTypeEnum.getEnumByCode(customerType);
        if (customerTypeEnum == null) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND, "不支持的客户类型");
        }

        return loanManager.relateCustomer(loanId, customerId, customerTypeEnum);
    }

    /**
     * 关联抵押物信息
     * <p>
     * 整合到一个api
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/pawn/{pawnId}", method = RequestMethod.PUT, params = "operation=relate")
    @Permission(permission = {PermissionEnum.CREATE_LOAN})
    @LoanValidCheck
    public BaseResult<Boolean> relatePawn(@PathVariable("id") Long id,
                                          @PathVariable("pawnId") Long pawnId) {
        // 判断贷款合同是否为当前用户所有
        loanManager.checkIsOwner(id, currentAccount().getId());

        //判断抵押物是否是该用户的
        if (!loanPawnManager.isOwner(pawnId, currentAccount().getId())) {
            return new BaseResult<>(ErrorCodeEnum.UNAUTHORIZED);
        }

        return loanManager.relatePawn(id, pawnId);
    }

    /**
     * 获取项目基本信息.
     *
     * @param id the id
     *
     * @return the loan info
     */
    @RequestMapping(value = "loan/{id}", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<LoanDomain> getLoanById(@PathVariable("id") Long id) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccount().getId(), PermissionEnum.VIEW_LOAN_ALL);
        BaseResult<LoanDomain> result=loanManager.readLoan(id);
         LoanDomain loanDomain= result.getData();
        //新增合同查询的时候可以查询到放款收款明细部分
        List<LoanLaunchRecordDomain> loanRecordDomains=loanManager.readLoanRecordDomainByLoanId(id);
        if(loanRecordDomains!=null){
            loanDomain.setLoanRecordDomains(loanRecordDomains);
        }

        return result;
    }

    /**
     * 获取贷款抵押物信息（包括附件信息).
     *
     * @param id the id
     *
     * @return the loan pawn by id
     */
    @RequestMapping(value = "loan/{id}/pawn", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<LoanPawnDomain> getLoanPawnById(@PathVariable("id") Long id) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccount().getId(), PermissionEnum.VIEW_LOAN_ALL);
        BaseResult<LoanPawnDomain> result=loanPawnManager.readLoanPawnByLoanId(id, currentWorkspaceId());
        return result;
    }

    /**
     * 获取贷款人和担保人信息(包括附件信息,列表输出).
     *
     * @param id the id
     *
     * @return the loan loaner by id
     */
    @RequestMapping(value = "loan/{id}/loaner", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<Map<LoanCustomerTypeEnum, CustomerDomain>> getLoanLoanerById(@PathVariable("id") Long id) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccount().getId(), PermissionEnum.VIEW_LOAN_ALL);

        return loanManager.getLoanerById(id);
    }

    /**
     * 更新贷款合同客户的快照
     *
     * @param id the id
     *
     * @return the loan loaner by id
     */
    @RequestMapping(value = "loan/{id}/loaner", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MEMBER, PermissionEnum.UPDATE_LOAN_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> updateLoanLoanerById(@PathVariable("id") Long id,
                                                    @RequestBody CustomerUpdateForm customerUpdateForm) {
        // 如果当前用户没有 UPDATE_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccount().getId(), PermissionEnum.UPDATE_LOAN_ALL);

        CustomerDomain domain = customerUpdateForm.convert2Domain();
        domain.setWorkspaceId(currentWorkspaceId());
        // 检查 参数是否正确
        if (domain == null || domain.getId() == null) {
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER, false);
        }

        return loanManager.updateLoanerById(id, domain);
    }

    /**
     * 获取贷款履约记录(包括详情item).
     *
     * @param id the id
     *
     * @return the loan records by id
     */
    @RequestMapping(value = "loan/{id}/records", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<List<LoanRefineDomain>> getLoanRecordsById(@PathVariable("id") Long id) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccount().getId(), PermissionEnum.VIEW_LOAN_ALL);
        return loanManager.getLoanRecords(id);
    }

    /**
     * 获取贷款异常收支记录
     */
    @RequestMapping(value = "loan/{id}/modifiedRecords", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<List<LoanRecordDomain>> getModifiedLoanRecords(@PathVariable("id") Long id) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccount().getId(), PermissionEnum.VIEW_LOAN_ALL);
        return loanManager.getModifiedLoanRecords(id);
    }

    /**
     * 根据查询条件获取项目基本信息
     *
     * @return the base result
     */
    @RequestMapping(value = "loans", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    public PageResult<List<LoanDomain>> queryLoan(@Valid LoanQuery loanQuery, BindingResult bindingResult) {
        loanQuery = setWorkspaceLoanQuery(loanQuery);

        return loanManager.listMarkedLoanList(loanQuery);
    }

    /**
     * 按照查询条件,获取贷款的数量
     *
     * @param loanQuery
     * @param bindingResult
     *
     * @return
     */
    @RequestMapping(value = "loans", method = RequestMethod.GET, params = "operation=count")
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    public BaseResult<Integer> countLoan(@Valid LoanQuery loanQuery,
                                         BindingResult bindingResult) {
        loanQuery = setWorkspaceLoanQuery(loanQuery);

        return loanManager.countLoan(loanQuery);
    }

    /**
     * 列出一个项目的所有附件
     *
     * @param loanId
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/attaches", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<List<AttachDomain>> listLoanAttachments(@PathVariable("id") Long loanId) {
        // 如果当前用户没有 VIEW_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(loanId, currentAccount().getId(), PermissionEnum.VIEW_LOAN_ALL);

        AttachRelateDomain attachRelateDomain = new AttachRelateDomain();
        attachRelateDomain.setBizType(AttachBizTypeEnum.LOAN);
        attachRelateDomain.setSubjectId(loanId);

        return attachRelateManager.listAttachs(attachRelateDomain);
    }

    /**
     * 关联一个附件
     *
     * @param loanId
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/attach/{attachId}", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MEMBER, PermissionEnum.UPDATE_LOAN_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> relateLoanAttachments(@PathVariable("id") Long loanId,
                                                     @PathVariable("attachId") Long attachId) {
        // 如果当前用户没有 UPDATE_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(loanId, currentAccount().getId(), PermissionEnum.UPDATE_LOAN_ALL);
        AttachRelateDomain attachRelateDomain = makeAttachRelateDomain(loanId, attachId);
        return attachRelateManager.relateAttach(attachRelateDomain, currentAccount().getId());
    }

    /**
     * 删除一个附件
     *
     * @param loanId
     * @param attachId
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/attach/{attachId}", method = RequestMethod.DELETE)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_MEMBER, PermissionEnum.UPDATE_LOAN_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> deleteLoanAttachments(@PathVariable("id") Long loanId,
                                                     @PathVariable("attachId") Long attachId) {
        // 如果当前用户没有 UPDATE_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(loanId, currentAccount().getId(), PermissionEnum.UPDATE_LOAN_ALL);
        AttachRelateDomain attachRelateDomain = makeAttachRelateDomain(loanId, attachId);
        return attachRelateManager.deRelateAttach(attachRelateDomain);
    }

    /**
     * 获取一个贷款的逾期次数 坏账次数 当前还款期数 总期数
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/paymentSituation", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    @LoanValidCheck(isTrashChecked = false)
    public BaseResult<Map<String, Integer>> getLoanPaymentSituationById(@PathVariable("id") Long id) {
        // 如果当前用户没有 UPDATE_LOAN_ALL 权限, 则检查是否为所有者
        checkPermission(id, currentAccount().getId(), PermissionEnum.VIEW_LOAN_ALL);
        return new BaseResult<>(loanManager.getLoanPaymentSituationById(id, currentWorkspaceId()));
    }

    /**
     * 将一个贷款合同指派给其他用户
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/assignment", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_ASSIGN})
    @LoanValidCheck
    public BaseResult<Boolean> assignLoanById(@PathVariable("id") Long id,
                                              @RequestBody @Valid LoanAssignmentForm form) {
        Long targetId = form.getTargetId();
        Long workspaceId = currentWorkspaceId();

        // 检查指派给的用户是否为本工作空间成员
        workspaceManager.checkIsWorkspaceMember(targetId, workspaceId);

        Boolean assignResult = loanManager.assignLoanById(id, targetId);

        // 如果移交成功, 则添加动态
        if (assignResult) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(workspaceId, currentAccount().getId(), id, MessageTypeEnum.LOAN_TRANSFER, null));
        }

        return new BaseResult<>(assignResult);
    }

    // ~ 以下为非路由方法

    /**
     * 检查Query中枚举类型的合法性
     *
     * @param loanQuery
     */
    public static void verifyQuery(LoanQuery loanQuery) {
        if (loanQuery == null) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER);
        }
        // 检查businessType的类型
        if (StringUtil.isNotBlank(loanQuery.getBusinessType())) {
            if (null == BusinessTypeEnum.getEnumByCode(loanQuery.getBusinessType())) {
                throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
            }
        }
        // 检查loanStatus的类型
        if (StringUtil.isNotBlank(loanQuery.getLoanStatus())) {
            checkLoanStatus(loanQuery.getLoanStatus());
        }
        // 检查excludeStatus的类型
        if (!CollectionUtils.isEmpty(loanQuery.getExcludeStatus())) {
            loanQuery.getExcludeStatus().forEach(status -> checkLoanStatus(status));
        }
        //检查pawnType的类型
        if (CollectionUtils.isNotEmpty(loanQuery.getPawnTypes())) {
            for (String type : loanQuery.getPawnTypes()) {
                if (null == LoanPawnTypeEnum.getEnumByCode(type)) {
                    throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
                }
            }
        }
        // 检查order的类型
        if (StringUtil.isNotBlank(loanQuery.getOrder())) {
            if (null == DBOrderTypeEnum.getEnumByCode(loanQuery.getOrder())) {
                throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE, "排序方式仅支持asc和desc");
            }
        }
    }

    /**
     * 检查loanStatus作为贷款状态的合法性
     */
    private static void checkLoanStatus(String loanStatus) {
        if (null == LoanStatusEnum.getEnumByCode(loanStatus)) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE);
        }
    }

    // ~ private methods
    // ~ common methods

    /**
     * 生成一个默认为LOAN关联的附件查询query对象
     *
     * @param subjectId
     * @param attachId
     *
     * @return
     */
    private AttachRelateDomain makeAttachRelateDomain(long subjectId, long attachId) {
        AttachRelateDomain attachRelateDomain = new AttachRelateDomain();
        attachRelateDomain.setBizType(AttachBizTypeEnum.LOAN);
        attachRelateDomain.setSubjectId(subjectId);
        attachRelateDomain.setAttachId(attachId);
        return attachRelateDomain;
    }

    /**
     * 新建贷款项目时关联客户
     *
     * @param customer
     *
     * @return
     */
    private CustomerDomain relateLoanCustomer(CustomerDomain customer, Long loanId, LoanCustomerTypeEnum customerType) {
        if (customer.getId() == null || customer.getId() == 0) {
            customer.setAccountId(currentAccount().getId());
            customer.setWorkspaceId(currentWorkspaceId());

            if (logger.isInfoEnabled()) {
                logger.info("贷款合同[" + loanId + "] 需要新建 [" + customerType + "] 客户参数 [" + customer + "]");
            }

            CustomerDomain newCustomer = customerManager.create(customer);

            if (newCustomer != null) {
                loanManager.relateCustomer(loanId, newCustomer.getId(), customerType);
                return newCustomer;
            } else {
                if (customerType == LoanCustomerTypeEnum.LOANER) {
                    throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "创建贷款人失败");
                } else if (customerType == LoanCustomerTypeEnum.HOLDER) {
                    throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "创建担保人失败");
                } else {
                    throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "客户参数错误");
                }
            }
        } else {
            if (logger.isInfoEnabled()) {
                logger.info("贷款合同[" + loanId + "] 不需要新建客户，直接关联已有客户 [" + customer.getId() + "]");
            }

            loanManager.relateCustomer(loanId, customer.getId(), customerType);
            return customer;
        }
    }

    /**
     * 关联抵押物
     *
     * @param pawn
     * @param loanId
     *
     * @return
     */
    private LoanPawnDomain relateLoanPawn(LoanPawnDomain pawn, Long loanId) {
        // 检查抵押物类型是否为空
        if (pawn.getLoanPawnTypeEnum() == null) {
            logger.error("新建贷款项目 [" + loanId + "] 抵押物为空，不允许新建!");
            throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL, "贷款抵押物类型不合法");
        }
        // 为抵押物增加account id
        pawn.setAccountId(currentAccount().getId());
        // 为抵押物设置当前workspace id
        pawn.setWorkspaceId(currentWorkspaceId());
        pawn = loanPawnManager.create(pawn);
        if (pawn != null) {
            loanManager.relatePawn(loanId, pawn.getId());
        } else {
            logger.error("贷款项目 [" + loanId + "] 关联抵押物失败!");
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "关联抵押物失败");
        }
        return pawn;
    }

    /**
     * 创建一个属于当前工作组的贷款合同
     *
     * @param loanCreateForm
     *
     * @return
     */
    private LoanDomain createWorkspaceLoan(LoanCreateForm loanCreateForm) {
        LoanDomain domain = loanCreateForm.convert2Domain();
        domain.setWorkspaceId(currentWorkspaceId());
        // 当前用户没有CREATE_LOAN权限; 则抛出异常
        if (!isPermitted(PermissionEnum.CREATE_LOAN)) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }
        domain.setAccountId(currentAccount().getId());
        return loanManager.createLoan(domain);

    }


    /**
     * 如果currentAccountId没有permissionAll权限,则检查其是否为loanId的所有者
     *
     * @param loanId
     * @param currentAccountId
     * @param permissionAll
     */
    private void checkPermission(Long loanId, Long currentAccountId, PermissionEnum permissionAll) {
        if (!isPermitted(permissionAll)) {
            loanManager.checkIsOwner(loanId, currentAccountId);
        }
    }


    /**
     * 检查loanQuery是否合法, 并根据用户权限对loanQuery进行相关设置
     *
     * @param loanQuery
     *
     * @return
     */
    private LoanQuery setWorkspaceLoanQuery(LoanQuery loanQuery) {
        // 检查loanQuery的合法性
        verifyQuery(loanQuery);

        // 设置业务类型
        loanQuery.setBusinessType(BusinessTypeEnum.LOAN.getCode());
        // 设置当前的workspaceId
        loanQuery.setWorkspaceId(currentWorkspaceId());

        if (loanQuery.getOwnerId() != null) {
            // 检查所要查看成员是否为本工作空间成员
            workspaceManager.checkIsWorkspaceMember(loanQuery.getOwnerId(), currentWorkspaceId());
        }

        if (!isPermitted(PermissionEnum.VIEW_LOAN_ALL)) {
            Long currentOwnerId = currentAccount().getId();
            // 如果没有 VIEW_LOAN_ALL 权限, 则检查LoanQuery中的ownerId字段
            if (loanQuery.getOwnerId() != null && !loanQuery.getOwnerId().equals(currentOwnerId)) {
                throw new TigerException(ErrorCodeEnum.UNAUTHORIZED, "没有权限查看其他成员的贷款项目");
            }
            // 如果当前用户没有 VIEW_LOAN_ALL 权限, 设置accountID
            loanQuery.setAccountId(currentOwnerId);
        }
        return loanQuery;
    }
}
