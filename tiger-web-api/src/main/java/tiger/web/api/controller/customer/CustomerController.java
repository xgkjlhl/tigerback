/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.attach.support.AttachRelateManager;
import tiger.biz.customer.support.CustomerManager;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.enums.AttachBizTypeEnum;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.enums.PermissionEnum;
import tiger.common.dal.query.CustomerQuery;
import tiger.common.dal.query.LoanQuery;
import tiger.common.dal.query.RecentPayBackCustomerQuery;
import tiger.common.dal.query.SmsQuery;
import tiger.common.util.DateUtil;
import tiger.common.util.StringUtil;
import tiger.core.base.BaseResult;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.*;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.component.KafkaService;
import tiger.web.api.constants.APIConstants;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.controller.loan.LoanController;
import tiger.web.api.form.attach.AttachRelateForm;
import tiger.web.api.form.customer.CustomerAssignmentForm;
import tiger.web.api.form.customer.CustomerCreateForm;
import tiger.web.api.form.customer.CustomerListForm;
import tiger.web.api.form.customer.CustomerUpdateForm;

import javax.validation.Valid;
import java.util.*;

/**
 * 客户相关api
 * TODO: 1. 考虑到职责分离
 *
 * @author HuPeng
 * @version v 0.1 2015年10月1日 上午10:13:05 HuPeng Exp $
 */
@RestController
public class CustomerController extends BaseController {

    /**
     * The customer service.
     */
    @Autowired
    private CustomerManager customerManager;

    @Autowired
    private AttachRelateManager attachRelateManager;

    @Autowired
    private WorkspaceManager workspaceManager;

    @Autowired
    private KafkaService kafkaService;

    /**
     * 创建客户
     *
     * @param form the form
     * @return the base result
     */
    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.CREATE_CUSTOMER_MEMBER, PermissionEnum.CREATE_CUSTOMER_ALL})
    public BaseResult<CustomerDomain> createCustomer(@RequestBody @Valid CustomerCreateForm form,
                                                     BindingResult bindingResult) {
        Long currentAccountId = currentAccount().getId();
        Long workspaceId = currentWorkspaceId();

        CustomerDomain customerDomain = form.convert2Domain();
        customerDomain.setAccountId(currentAccountId);
        customerDomain.setWorkspaceId(workspaceId);

        customerDomain = customerManager.create(customerDomain);

        // 发送消息到kafka
        kafkaService.sendOneToKafka(NotificationKeyEnum.CUSTOMER, new NotificationBasicDomain(workspaceId, currentAccountId, customerDomain.getId(), MessageTypeEnum.ADD_CUSTOMER, null));

        return new BaseResult<>(customerManager.read(customerDomain.getId()));
    }

    /**
     * 根据id获取客户信息
     *
     * @param id the id
     * @return the base result
     */
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<CustomerDomain> readCustomer(@PathVariable("id") Long id) {
        // 检查用户权限
        CustomerDomain customerDomain = checkCustomerIdViewPermission(id);

        return new BaseResult<>(customerDomain);
    }

    /**
     * 更新客户, 不支持更新附件信息
     *
     * @param id   the id
     * @param form the form
     * @return the base result
     */
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_CUSTOMER_MEMBER, PermissionEnum.UPDATE_CUSTOMER_ALL})
    public BaseResult<Boolean> updateCustomer(@PathVariable("id") Long id,
                                              @Valid @RequestBody CustomerUpdateForm form,
                                              BindingResult bindingResult) {
        // 检查用户权限
        checkCustomerIdUpdatePermission(id);

        Long workspaceId = currentWorkspaceId();

        CustomerDomain customerDomain = form.convert2Domain();
        customerDomain.setId(id);
        customerDomain.setWorkspaceId(workspaceId);

        BaseResult<Boolean> updateResult = customerManager.update(customerDomain);

        // 如果更新成功, 则添加消息和动态
        if (updateResult.getData()) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.CUSTOMER, new NotificationBasicDomain(workspaceId, currentAccount().getId(), id, MessageTypeEnum.UPDATE_CUSTOMER, null));
        }

        return updateResult;
    }

    /**
     * 删除客户
     *
     * @param id the id
     * @return the base result
     */
    @RequestMapping(value = "/customer/{id}", method = RequestMethod.DELETE)
    @Permission(permission = {PermissionEnum.DELETE_CUSTOMER_MEMBER, PermissionEnum.DELETE_CUSTOMER_ALL})
    public BaseResult<Boolean> deleteCustomer(@PathVariable("id") Long id) {
        // 检查用户权限
        checkCustomerIdDeletePermission(id);

        BaseResult<Boolean> deleteResult = customerManager.delete(id);

        // 如果删除成功, 则添加消息和动态
        if (deleteResult.getData()) {
            // 发送消息到kafka
            kafkaService.sendOneToKafka(NotificationKeyEnum.CUSTOMER, new NotificationBasicDomain(currentWorkspaceId(), currentAccount().getId(), id, MessageTypeEnum.DELETE_CUSTOMER, null));
        }

        return deleteResult;
    }

    /**
     * 分页查询客户信息
     *
     * @param query
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/customers", method = RequestMethod.GET, params = "scope=list")
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<CustomerDomain>> listCustomer(@Valid CustomerQuery query,
                                                         BindingResult bindingResult) {
        setWorkspaceCustomerQuery(query);

        return customerManager.listCustomers(query);
    }

    /**
     * 列出所有的客户
     *
     * @return
     */
    @RequestMapping(value = "/customers", method = RequestMethod.GET, params = "scope=all")
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<?> listAllCustomer(@Valid CustomerQuery query,
                                         BindingResult bindingResult) {
        setWorkspaceCustomerQuery(query);

        return customerManager.listAllCustomers(query);
    }

    /**
     * 客户搜索(名字或者手机号或者身份证)
     *
     * @param param the param
     * @return the base result
     */
    @RequestMapping(value = "customers/search", method = RequestMethod.GET, params = "scope=all")
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<CustomerDomain>> searchCustomer(@RequestParam(APIConstants.PARAM_PARAM) String param,
                                                           @RequestParam(value = APIConstants.PARAM_OWNER_ID, required = false) Long ownerId) {
        if (StringUtil.isBlank(param)) {
            return new BaseResult<>(new ArrayList<>());
        }

        CustomerQuery query = new CustomerQuery();
        query.setParam(param);
        query.setOwnerId(ownerId);

        query = setWorkspaceCustomerQuery(query);

        return customerManager.search(query);
    }

    /**
     * 客户名字完全匹配搜索
     *
     * @param name the name
     * @return the base result
     */
    @RequestMapping(value = "customers/search", method = RequestMethod.GET, params = {"scope=all", "key=name"})
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<CustomerDomain>> searchCustomerByName(@RequestParam(APIConstants.PARAM_PARAM) String name,
                                                                 @RequestParam(value = APIConstants.PARAM_OWNER_ID, required = false) Long ownerId) {
        if (StringUtil.isBlank(name)) {
            return new BaseResult<>(new ArrayList<>());
        }

        CustomerQuery query = new CustomerQuery();
        query.setParam(name);
        query.setOwnerId(ownerId);

        query = setWorkspaceCustomerQuery(query);

        return customerManager.searchCustomersByName(query);
    }

    /**
     * 新建某个客户的附件
     *
     * @param id
     * @param customerAttachForm
     * @return
     */
    @RequestMapping(value = "/customer/{id}/attach", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_CUSTOMER_MEMBER, PermissionEnum.UPDATE_CUSTOMER_ALL})
    public BaseResult<Boolean> addAttach(@PathVariable("id") long id,
                                         @RequestBody @Valid AttachRelateForm customerAttachForm,
                                         BindingResult bindingResult) {
        checkCustomerIdUpdatePermission(id);

        AttachRelateDomain attachRelateDomain = customerAttachForm.convert2Domain();
        attachRelateDomain.setBizType(AttachBizTypeEnum.CUSTOMER);
        attachRelateDomain.setSubjectId(id);

        return attachRelateManager.relateAttach(attachRelateDomain, currentAccount().getId());
    }

    /**
     * 获取客户所有附件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customer/{id}/attachs", method = RequestMethod.GET, params = "scope=all")
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<AttachDomain>> getAllAttachs(@PathVariable("id") Long id) {
        checkCustomerIdViewPermission(id);

        AttachRelateDomain customerAttaches = new AttachRelateDomain();
        customerAttaches.setBizType(AttachBizTypeEnum.CUSTOMER);
        customerAttaches.setSubjectId(id);

        return attachRelateManager.listAttachs(customerAttaches);
    }

    /**
     * 根据id删除附件
     *
     * @param id       the id
     * @param attachId the attachId
     * @return the base result
     */
    @RequestMapping(value = "/customer/{id}/attach/{aid}", method = RequestMethod.DELETE)
    @Permission(permission = {PermissionEnum.UPDATE_CUSTOMER_MEMBER, PermissionEnum.UPDATE_CUSTOMER_ALL})
    public BaseResult<Boolean> deleteAttach(@PathVariable("id") Long id,
                                            @PathVariable("aid") Long attachId) {
        checkCustomerIdDeletePermission(id);

        AttachRelateDomain customerAttach = new AttachRelateDomain();
        customerAttach.setBizType(AttachBizTypeEnum.CUSTOMER);
        customerAttach.setSubjectId(id);
        customerAttach.setAttachId(attachId);

        return attachRelateManager.deRelateAttach(customerAttach);
    }

    /**
     * 分页查询客户贷款信息
     *
     * @param id
     * @param query
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/customer/{id}/loans", method = RequestMethod.GET, params = "scope=list")
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<LoanDomain>> listLoan(@PathVariable("id") Long id,
                                                 @Valid LoanQuery query,
                                                 BindingResult bindingResult) {
        checkCustomerIdViewPermission(id);

        query.setCustomerId(id);
        LoanController.verifyQuery(query);

        return customerManager.queryLoans(query);
    }


    /**
     * 分页查询客户短信
     *
     * @param id
     * @param query
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/customer/{id}/smses", method = RequestMethod.GET, params = "scope=list")
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<CustomerMsgDomain>> listSmses(@PathVariable("id") Long id,
                                                         @Valid SmsQuery query,
                                                         BindingResult bindingResult) {
        checkCustomerIdViewPermission(id);

        List<Long> ids = new ArrayList<>(SystemConstants.SIZE_ONE);
        ids.add(id);
        query.setCustomerIds(ids);

        return customerManager.querySmses(query);
    }

    /**
     * 获取客户所有标签
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/customer/{id}/tags", method = RequestMethod.GET, params = "scope=all")
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<TagDomain>> listTags(@PathVariable("id") Long id) {
        checkCustomerIdViewPermission(id);

        return new BaseResult<>(customerManager.listTags(id));
    }

    /**
     * 给用户打上一个新标签
     *
     * @param tid
     * @param form
     * @return
     */
    @RequestMapping(value = "/customers/tag/{tid}", method = RequestMethod.POST)
    @Permission(permission = {PermissionEnum.UPDATE_CUSTOMER_MEMBER, PermissionEnum.UPDATE_CUSTOMER_ALL})
    public BaseResult<?> addTags(@PathVariable("tid") Long tid,
                                 @RequestBody @Valid CustomerListForm form,
                                 BindingResult bindingResult) {
        List<Long> ids = form.getIds();
        if (CollectionUtils.isEmpty(ids)) {
            return new BaseResult<>();
        }
        checkCustomerIdsUpdatePermission(ids);

        customerManager.addTags(ids, tid);
        return new BaseResult<>();
    }

    /**
     * 根据id删除客户的一个标签
     *
     * @param id
     * @param tagId
     * @return
     */
    @RequestMapping(value = "/customer/{id}/tag/{tid}", method = RequestMethod.DELETE)
    @Permission(permission = {PermissionEnum.UPDATE_CUSTOMER_MEMBER, PermissionEnum.UPDATE_CUSTOMER_ALL})
    public BaseResult<?> delTag(@PathVariable("id") Long id,
                                @PathVariable("tid") Long tagId) {
        checkCustomerIdUpdatePermission(id);

        customerManager.deleteTag(id, tagId);
        return new BaseResult<>();
    }

    /**
     * 获取所有标签
     *
     * @return
     */
    @RequestMapping(value = "/tags", params = "scope=all", method = RequestMethod.GET)
    @LoginRequired
    public BaseResult<List<TagDomain>> getAllTags() {
        long accountId = currentAccount().getId();

        return new BaseResult<>(customerManager.getAllTags(accountId));
    }

    /**
     * 当月及当日客户生日数
     *
     * @return
     */
    @RequestMapping(value = "/customer/birthday", params = "operation=count", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<Map<String, Integer>> getBirthdayCustomerCount() {

        CustomerQuery query = setWorkspaceCustomerQuery(new CustomerQuery());

        return new BaseResult<>(customerManager.getBirthdayCount(query));
    }

    /**
     * 当月及当日客户生日数
     *
     * @return
     */
    @RequestMapping(value = "/customer/birthday", params = "operation=list", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<Map<String, List<CustomerDomain>>> listBirthdayCustomers() {
        CustomerQuery query = setWorkspaceCustomerQuery(new CustomerQuery());

        return new BaseResult<>(customerManager.listBirthdayCustomers(query));
    }

    /**
     * 查询客户总数
     *
     * @return
     */
    @RequestMapping(value = "/customers", params = "operation=count", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<Map<String, Integer>> getCustomerCount() {

        CustomerQuery query = setWorkspaceCustomerQuery(new CustomerQuery());

        Integer count = customerManager.getCustomerCount(query);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put(SystemConstants.COUNT, count);
        resultMap.put(SystemConstants.PER_MONTH_COUNT, getPerMonthCount(count, currentAccount().getCreateTime()));

        return new BaseResult<>(resultMap);
    }

    /**
     * 删除一组客户
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/customers", params = "operation=delete", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.DELETE_CUSTOMER_MEMBER, PermissionEnum.DELETE_CUSTOMER_ALL})
    public BaseResult<?> deleteCustomers(@RequestBody @Valid CustomerListForm form,
                                         BindingResult bindingResult) {
        List<Long> ids = form.getIds();

        checkCustomerIdsDeletePermission(ids);
        List<CustomerDomain> customerDomains = customerManager.read(ids);
        customerManager.delete(ids);

        //构建customer id和 name的map
        Map<Long, String> customerIdNameMap = new HashMap<>();
        for (CustomerDomain customerDomain : customerDomains) {
            customerIdNameMap.put(customerDomain.getId(), customerDomain.getName());
        }

        // 发送消息到kafka
        final Long workspaceId = currentWorkspaceId();
        final Long accountId = currentAccount().getId();
        final List<NotificationBasicDomain> basicActivities = new ArrayList<>(ids.size());
        ids.forEach(id -> basicActivities.add(new NotificationBasicDomain(workspaceId, accountId, id, MessageTypeEnum.DELETE_CUSTOMER, null)));
        kafkaService.sendAllToKafka(NotificationKeyEnum.CUSTOMER, basicActivities);

        return new BaseResult<>();
    }

    /**
     * 更新客户头像
     *
     * @param id
     * @param iconId
     * @return
     */
    @RequestMapping(value = "customer/{id}/icon/{iconId}", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_CUSTOMER_MEMBER, PermissionEnum.UPDATE_CUSTOMER_ALL})
    public BaseResult<Boolean> updateCustomerIcon(@PathVariable("id") Long id, @PathVariable("iconId") Long iconId) {
        checkCustomerIdUpdatePermission(id);

        return new BaseResult<>(customerManager.updateIcon(currentAccount().getId(), id, iconId));
    }

    /**
     * 将客户指派给其他用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "customer/{id}/assignment", method = RequestMethod.PUT)
    @Permission(permission = {PermissionEnum.UPDATE_CUSTOMER_TRANSFER_MEMBER, PermissionEnum.UPDATE_CUSTOMER_TRANSFER_ALL})
    public BaseResult<Boolean> assignCustomerById(@PathVariable("id") Long id,
                                                  @RequestBody @Valid CustomerAssignmentForm form,
                                                  BindingResult bindingResult) {
        checkCustomerTransferPermission(id);
        workspaceManager.checkIsWorkspaceMember(form.getTargetId(), currentWorkspaceId());

        return customerManager.assignCustomer(id, form.getTargetId());
    }

    /**
     * 获取近期需要收款的客户名单
     *
     * @return
     */
    @RequestMapping(value = "customers/recentPayBackCustomers", method = RequestMethod.GET)
    @Permission(permission = {PermissionEnum.VIEW_CUSTOMER_MEMBER, PermissionEnum.VIEW_CUSTOMER_ALL})
    public BaseResult<List<CustomerDomain>> getRecentPayBackCustoemrs(@Valid RecentPayBackCustomerQuery query,
                                                                      BindingResult bindingResult) {
        query.setWorkspaceId(currentWorkspaceId());
        if (!isPermitted(PermissionEnum.VIEW_CUSTOMER_ALL)
                && isPermitted(PermissionEnum.VIEW_CUSTOMER_MEMBER)) {
            query.setAccountId(currentAccount().getId());
        }

        return customerManager.queryRecentPayBackCustomers(query);
    }

    //~~ PRIVATE METHOD

    /**
     * 检查给定客户（id）的转移客户权限
     *
     * @param id
     */
    private void checkCustomerTransferPermission(Long id) {
        CustomerDomain customerDomain = customerManager.read(id);

        // 检查客户是否属于当前工作空间
        checkWorkspacePermission(customerDomain);

        // 检查用户权限
        if (!isPermitted(PermissionEnum.UPDATE_CUSTOMER_TRANSFER_ALL)
                && isPermitted(PermissionEnum.UPDATE_CUSTOMER_TRANSFER_MEMBER)
                && !customerDomain.getAccountId().equals(currentAccount().getId())) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }
    }

    /**
     * 检查给定客户（id）的读权限
     *
     * @param id
     */
    private CustomerDomain checkCustomerIdViewPermission(Long id) {
        CustomerDomain customerDomain = customerManager.read(id);

        // 检查客户是否属于当前工作空间
        checkWorkspacePermission(customerDomain);

        // 检查用户权限
        if (!isPermitted(PermissionEnum.VIEW_CUSTOMER_ALL)
                && isPermitted(PermissionEnum.VIEW_CUSTOMER_MEMBER)
                && !customerDomain.getAccountId().equals(currentAccount().getId())) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }

        return customerDomain;
    }

    /**
     * 检查给定客户（id）的改权限
     *
     * @param id
     */
    private void checkCustomerIdUpdatePermission(Long id) {
        checkCustomerUpdatePermission(
                customerManager.read(id)
        );
    }

    /**
     * 检查给定用户（domain）的改权限
     *
     * @param domain
     */
    private void checkCustomerUpdatePermission(CustomerDomain domain) {
        // 检查客户是否属于当前工作空间
        checkWorkspacePermission(domain);

        // 检查用户权限
        if (!isPermitted(PermissionEnum.UPDATE_CUSTOMER_ALL)
                && isPermitted(PermissionEnum.UPDATE_CUSTOMER_MEMBER)
                && !domain.getAccountId().equals(currentAccount().getId())) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }
    }

    /**
     * 检查给定客户（ids）的改权限
     *
     * @param ids
     */
    private void checkCustomerIdsUpdatePermission(List<Long> ids) {
        List<CustomerDomain> customers = getCustomerDomains(ids);

        customers.forEach(customer ->
                checkCustomerUpdatePermission(customer)
        );
    }

    /**
     * 检查给定客户（id）的删权限
     *
     * @param id
     */
    private void checkCustomerIdDeletePermission(Long id) {
        checkCustomerDeletePermission(
                customerManager.read(id)
        );
    }

    /**
     * 检查给定客户（domain）的删权限
     *
     * @param customerDomain
     */
    private void checkCustomerDeletePermission(CustomerDomain customerDomain) {
        checkWorkspacePermission(customerDomain);

        if (!isPermitted(PermissionEnum.DELETE_CUSTOMER_ALL)
                && isPermitted(PermissionEnum.DELETE_CUSTOMER_MEMBER)
                && !customerDomain.getAccountId().equals(currentAccount().getId())) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }
    }

    /**
     * 检查给定客户（ids）的删权限
     *
     * @param ids
     */
    private void checkCustomerIdsDeletePermission(List<Long> ids) {
        List<CustomerDomain> customers = getCustomerDomains(ids);

        customers.forEach(customer ->
                checkCustomerDeletePermission(customer)
        );
    }

    /**
     * 检查客户是否存在, 且属于当前workspace
     *
     * @param domain
     */
    private void checkWorkspacePermission(CustomerDomain domain) {
        // 检查客户是否存在
        if (null == domain) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        // 检查workspaceId是否与当前workspaceId相符
        if (!domain.getWorkspaceId().equals(currentWorkspaceId())) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        }
    }

    /**
     * 获取ids对应的客户列表,
     * 如果ids为空 或 与返回结果大小不符 则抛出异常
     *
     * @param ids
     * @return
     */
    private List<CustomerDomain> getCustomerDomains(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER);
        }

        List<CustomerDomain> customers = customerManager.read(ids);

        if (CollectionUtils.isEmpty(customers) || customers.size() != ids.size()) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        return customers;
    }

    /**
     * 检查并设置CustomerQuery
     *
     * @param query
     * @return
     */
    private CustomerQuery setWorkspaceCustomerQuery(CustomerQuery query) {
        if (query == null) {
            return null;
        }
        if (query.getOwnerId() != null) {
            // 检查所要查看成员是否为本工作空间成员
            workspaceManager.checkIsWorkspaceMember(query.getOwnerId(), currentWorkspaceId());
        }

        if (!isPermitted(PermissionEnum.VIEW_CUSTOMER_ALL) && isPermitted(PermissionEnum.VIEW_CUSTOMER_MEMBER)) {
            Long currentAccountId = currentAccount().getId();
            // 如果当前用户没有 VIEW_CUSTOMER_ALL 权限, 则检查CustomerQuery中的ownerId字段
            if (query.getOwnerId() != null && !currentAccountId.equals(query.getOwnerId())) {
                throw new TigerException(ErrorCodeEnum.UNAUTHORIZED, "没有权限查看其他成员的客户");
            }
            // 如果当前用户没有 VIEW_CUSTOMER_ALL 权限, 设置accountID
            query.setAccountId(currentAccountId);
        }

        query.setWorkspaceId(currentWorkspaceId());

        return query;
    }

    /**
     * 本月客户计数
     *
     * @param count
     * @param createTime
     * @return
     */
    private int getPerMonthCount(int count, Date createTime) {
        int diffCount = DateUtil.getDiffMonths(new Date(), createTime).intValue() + 1;
        return count / diffCount;
    }
}
