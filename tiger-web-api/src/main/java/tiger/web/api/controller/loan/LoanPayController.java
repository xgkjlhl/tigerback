/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.web.api.controller.loan;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiger.biz.loan.support.LoanManager;
import tiger.biz.loan.support.LoanPayManager;
import tiger.common.dal.annotation.LoanValidCheck;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.BusinessTypeEnum;
import tiger.common.dal.enums.InterestCalTypeEnum;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.common.dal.query.LoanMergeBillItemQuery;
import tiger.common.dal.query.LoanMergeBillQuery;
import tiger.common.dal.query.LoanPaybackQuery;
import tiger.common.util.DateUtil;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanMergedBillDomain;
import tiger.core.domain.LoanPaybackListDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.component.KafkaService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.loan.LoanRecalForm;
import tiger.web.api.form.loan.LoanSinglePayForm;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.validation.Valid;

/**
 * 贷款收付类接口
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 1:14:17 PM yiliang.gyl Exp $
 */
@RestController
@ResponseBody
public class LoanPayController extends BaseController {

    private final Logger logger = Logger.getLogger(LoanPayController.class);

    @Autowired
    private LoanPayManager loanPayManager;

    @Autowired
    private LoanManager loanManager;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 获取用户当前收付账单列表
     *
     * @return the pay backs
     */
    @RequestMapping(value = "/loan/paybacks", method = RequestMethod.GET, params = "scope=all")
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    public BaseResult<List<LoanPaybackListDomain>> getAllPayBacks(@Valid LoanPaybackQuery paybackQuery,
                                                                  BindingResult bindingResult) {
        paybackQuery = setLoanPaybackQuery(paybackQuery);

        checkLoanPaybackQuery(paybackQuery);

        return loanPayManager.getAllRecords(paybackQuery);
    }

    /**
     * 获取用户当前收付账单列表
     *
     * @return the pay backs
     */
    @RequestMapping(value = "/loan/paybacks", method = RequestMethod.GET, params = "scope=list")
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    public PageResult<List<LoanPaybackListDomain>> getPagePayBacks(@Valid LoanPaybackQuery paybackQuery,
                                                                   BindingResult bindingResult) {
        paybackQuery = setLoanPaybackQuery(paybackQuery);

        checkLoanPaybackQuery(paybackQuery);

        return loanPayManager.getRecordsByPage(paybackQuery);
    }

    /**
     * 获取用户款项列表
     */
    @RequestMapping(value = "/loan/paybacks", method = RequestMethod.GET, params = "scope=count")
    @Permission(permission = {PermissionEnum.VIEW_LOAN_MEMBER, PermissionEnum.VIEW_LOAN_ALL})
    public BaseResult<Integer> countPayBacks(@Valid LoanPaybackQuery paybackQuery,
                                             BindingResult bindingResult) {
        paybackQuery = setLoanPaybackQuery(paybackQuery);

        checkLoanPaybackQuery(paybackQuery);

        return loanPayManager.countRecords(paybackQuery);
    }

    /**
     * 获取一个项目当前的收付账单列表
     * <p>
     * ~ 未放款项目获取账单为放款账单
     * ~ 还款中的项目获取到未还款的账单
     * operation = list ~设置操作方式为列表
     * </p>
     *
     * @param id 项目id
     *
     * @return 一组账单
     */
    @RequestMapping(value = "loan/{id}/bills", method = RequestMethod.GET, params = "operation=list")
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_COLLECT_BILL_MEMBER, PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL})
    @LoanValidCheck
    public BaseResult<List<LoanRecordDomain>> getLoanBill(@PathVariable("id") long id) {
        // 如果没有UPDATE_LOAN_COLLECT_BILL_ALL权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }
        return loanPayManager.bills(id);
    }

    /**
     * 根据选择的账单 id 生成合并账单
     * operation = merge ~设置操作方式为合并
     *
     * @return 账单集合，用于还款确认
     */
    @RequestMapping(value = "loan/{id}/bills", method = RequestMethod.PUT, params = "operation=merge")
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_COLLECT_BILL_MEMBER, PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL})
    @LoanValidCheck
    public BaseResult<LoanMergedBillDomain> getMergedBillByIds(@PathVariable("id") long id,
                                                               @RequestBody @Valid LoanMergeBillQuery loanMergeBillQuery,
                                                               BindingResult bindingResult) {
        // 如果没有UPDATE_LOAN_COLLECT_BILL_ALL权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }
        loanMergeBillQuery.setLoanId(id);
        //1. 参数检查
        checkMergeBillParam(loanMergeBillQuery);

        //2. 获取账单
        return loanPayManager.getMergedBills(loanMergeBillQuery);
    }

    /**
     * 确认还款
     * operation = pay
     *
     * @param id 项目id
     */
    @RequestMapping(value = "loan/{id}/bills", method = RequestMethod.POST, params = "operation=pay")
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_COLLECT_BILL_MEMBER, PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> payMergedBills(@PathVariable("id") long id,
                                              @RequestBody @Valid LoanMergeBillQuery loanMergeBillQuery,
                                              BindingResult bindingResult) {
        // 如果没有UPDATE_LOAN_COLLECT_BILL_ALL权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }
        //1.参数检查
        checkMergeBillParam(loanMergeBillQuery);

        //2.根据账单，执行还款操作
        BaseResult<Boolean> mergePayResult = loanPayManager.payMergedBills(loanMergeBillQuery, createTempLoan(id));

        return mergePayResult;
    }

    /**
     * 项目放款
     *
     * @param id     项目id
     * @param billId 放款账单id
     *
     * @return 成功或者失败
     */
    @RequestMapping(value = "loan/{id}/bill/{billId}", method = RequestMethod.PUT, params = "operation=launch")
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_LAUNCH_MEMBER, PermissionEnum.UPDATE_LOAN_LAUNCH_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> launchLoan(@PathVariable("id") long id,
                                          @PathVariable("billId") long billId,
                                          @RequestParam("launchDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date launchDate) {
        Long currentAccountId = currentAccount().getId();
        // 如果没有UPDATE_LOAN_LAUNCH_ALL权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_LAUNCH_ALL)) {
            loanManager.checkIsOwner(id, currentAccountId);
        }

//        BaseResult<Boolean> launchResult = loanPayManager.launch(id, billId, launchDate, currentWorkspaceId());
        BaseResult<Boolean> launchResult = loanPayManager.manuaLaunch(id, billId, launchDate, currentWorkspaceId());
        // 如果放款成功, 则添加消息和动态
        if (launchResult.getData()) {
            // 发送消息到kafka
            LoanRecordDomain loanRecordDomain = loanPayManager.getLoanRecordById(billId);
            String[] operationParmas = new String[]{NumberFormat.getNumberInstance(Locale.CHINA).format(Math.abs(loanRecordDomain.getActualAmount()))};
            kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, id, MessageTypeEnum.LOAN_LAUNCH_WITH_MONEY, operationParmas));
        }

        return launchResult;
    }

    /**
     * 根据还款时间来修正某一期还款的接口
     *
     * @param id     项目id
     * @param billId 项目还款账单id（对应某一期还款id）
     */
    @RequestMapping(value = "loan/{id}/bill/{billId}", method = RequestMethod.GET, params = "operation=calculate")
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_COLLECT_BILL_MEMBER, PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL})
    @LoanValidCheck
    public BaseResult<LoanRecordDomain> trimLoanRecord(@PathVariable("id") long id,
                                                       @PathVariable("billId") long billId,
                                                       @Valid LoanRecalForm loanRecalForm,
                                                       BindingResult bindingResult) {
        // 如果没有UPDATE_LOAN_COLLECT_BILL_ALL权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }
        return loanPayManager.recalBill(id, billId, loanRecalForm.getActualPayDate());
    }

    /**
     * 单笔还款
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/bill/{billId}", method = RequestMethod.POST, params = "operation=pay")
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_COLLECT_BILL_MEMBER, PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL})
    @LoanValidCheck
    public BaseResult<Boolean> paySingleBill(@PathVariable("id") long id,
                                             @PathVariable("billId") long billId,
                                             @RequestBody @Valid LoanSinglePayForm loanSinglePayForm,
                                             BindingResult bindingResult) {
        // 如果没有UPDATE_LOAN_COLLECT_BILL_ALL权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_COLLECT_BILL_ALL)) {
            loanManager.checkIsOwner(id, currentAccount().getId());
        }

        LoanRecordDomain loanRecordDomain = loanSinglePayForm.convert2Domain();
        Date currentDate = DateUtil.getDayEnd(new Date());
        if (loanRecordDomain.getActualDate().after(currentDate)) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER, "实收日期不能晚于当前日期");
        }
        // 设置相关信息
        loanRecordDomain.setLoanId(id);
        loanRecordDomain.setId(billId);
        loanRecordDomain.setWorkspaceId(currentWorkspaceId());
        //调用还款业务接口
        return loanPayManager.paySingleBill(loanRecordDomain, createTempLoan(id));
    }

    /**
     * 撤销最近一笔还款记录
     *
     * @param id
     *
     * @return
     */
    @RequestMapping(value = "loan/{id}/bill/cancellation", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_LOAN_PAYBACK_MEMBER, PermissionEnum.UPDATE_LOAN_PAYBACK_ALL})
    @LoanValidCheck
    public BaseResult<LoanRecordDomain> cancelLastPayback(@PathVariable("id") long id) {
        Long currentAccountId = currentAccount().getId();
        // 如果没有UPDATE_LOAN_PAYBACK_ALL权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.UPDATE_LOAN_PAYBACK_ALL)) {
            loanManager.checkIsOwner(id, currentAccountId);
        }

        BaseResult<LoanRecordDomain> cancelResult = loanPayManager.cancelLastPayback(id);

        // 发送消息到kafka
        kafkaService.sendOneToKafka(NotificationKeyEnum.LOAN, new NotificationBasicDomain(currentWorkspaceId(), currentAccountId, id, MessageTypeEnum.LOAN_CANCEL, null));

        return cancelResult;
    }

    //~ private methods

    /**
     * 检查合并账单的参数
     *
     * @param loanMergeBillQuery
     */
    public static void checkMergeBillParam(LoanMergeBillQuery loanMergeBillQuery) {
        // 如果是结清，选择计息方式
        if (loanMergeBillQuery.isEnd()) {
            InterestCalTypeEnum type = InterestCalTypeEnum.getEnumByCode(loanMergeBillQuery.getInterestCalType());
            if (type == null) {
                throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "请选择正确的计息方式");
            }
        }
        // 必须选择至少一个账单
        if (CollectionUtils.isEmpty(loanMergeBillQuery.getBills())) {
            throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL.getCode(), "没有选择任何账单");
        }
        // 实际还款时间不能超过当前日期
        List<LoanMergeBillItemQuery> billItemList = loanMergeBillQuery.getBills();
        Date currentDate = new Date();
        for (LoanMergeBillItemQuery billItem : billItemList) {
            if (billItem.getActualPayDate().after(DateUtil.getDayEnd(currentDate)))
                throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER.getCode(), "实收日期不能超过当前日期");
        }
    }

    // ~ Private Method

    /**
     * 设置LoanPaybackQuery中的accountId 以及 排除处于 垃圾箱 状态
     *
     * @param query
     *
     * @return
     */
    private LoanPaybackQuery setLoanPaybackQuery(LoanPaybackQuery query) {
        // 设置当前workspaceId
        query.setWorkSpaceId(currentWorkspaceId());

        // 如果没有 VIEW_LOAN_ALL 权限,则检查是否贷款合同的所有者
        if (!isPermitted(PermissionEnum.VIEW_LOAN_ALL)) {
            query.setAccountId(currentAccount().getId());
        }

        // 排除垃圾箱的记录
        query.getExcludeStates().add(LoanStatusEnum.TRASH.getCode());

        return query;
    }

    /**
     * 创建一个只有 id, workspaceId accountId的Loan
     *
     * @param id
     *
     * @return
     */
    private LoanDomain createTempLoan(Long id) {
        LoanDomain loan = new LoanDomain();
        loan.setWorkspaceId(currentWorkspaceId());
        loan.setAccountId(currentAccount().getId());
        loan.setId(id);

        return loan;
    }

    /**
     * 检查LoanPaybackQuery的枚举设置是否合法
     *
     * @param paybackQuery
     */
    private void checkLoanPaybackQuery(LoanPaybackQuery paybackQuery) {
        if (BusinessTypeEnum.getEnumByCode(paybackQuery.getBusinessType()) == null) {
            throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL, "业务类型不能为空");
        }
        if (!CollectionUtils.isEmpty(paybackQuery.getStates())) {
            for (String state : paybackQuery.getStates()) {
                if (LoanStatusEnum.getEnumByCode(state) == null) {
                    throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER, "不合法的贷款项目状态类型");
                }
            }
        }
        if (!CollectionUtils.isEmpty(paybackQuery.getTypes())) {
            for (String type : paybackQuery.getTypes()) {
                if (LoanActionEnum.getEnumByCode(type) == null) {
                    throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER, "不合法的贷款操作类型");
                }
            }
        }
    }
}
