package tiger.core.service;

import tiger.common.dal.dto.list.AccountWorkspaceListDTO;
import tiger.common.dal.query.CustomerQuery;
import tiger.common.dal.query.LoanQuery;
import tiger.common.dal.query.SmsQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.CustomerDomain;
import tiger.core.domain.CustomerMsgDomain;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.TagDomain;

import java.util.List;
import java.util.Map;

/**
 * 客户相关核心层数据交互接口
 *
 * @author HuPeng
 * @version v 0.1 2015年10月1日 上午10:24:08 HuPeng Exp $
 */
public interface CustomerService {

    CustomerDomain read(Long id);

    /**
     * 分页查询借贷记录
     *
     * @param query the query
     * @return the page result
     */
    PageResult<List<LoanDomain>> queryLoans(LoanQuery query);

    /**
     * 获取客户所有标签
     *
     * @param customerId
     * @return
     */
    List<TagDomain> listAllTags(Long customerId);

    /**
     * 分页查询客户信息
     *
     * @param query
     * @return
     */
    PageResult<List<CustomerMsgDomain>> querySmses(SmsQuery query);

    /**
     * 分页查询客户信息
     *
     * @param query
     * @return
     */
    PageResult<List<CustomerDomain>> query(CustomerQuery query);

    /**
     * 删除客户
     *
     * @param id
     */
    boolean deleteCustomer(Long id);

    /**
     * 获取今天生日的客户数量
     *
     * @param query
     * @return
     */
    Integer getTodayBirthCustomerCount(CustomerQuery query);


    /**
     * 获取今天生日的客户列表
     *
     * @param query
     * @return
     */
    List<CustomerDomain> getTodayBirthCustomers(CustomerQuery query);

    /**
     * 获取当月生日的客户数量
     *
     * @param query
     * @return
     */
    Integer getMonthBirthCustomerCount(CustomerQuery query);

    /**
     * 获取当月生日的客户列表
     *
     * @param query
     * @return
     */
    List<CustomerDomain> getMonthBirthCustomers(CustomerQuery query);

    /**
     * 获取当前用户客户数
     *
     * @param query
     * @return
     */
    Integer getCustomerCount(CustomerQuery query);

    /**
     * 获取用户的所有客户
     *
     * @param query
     * @return
     */
    List<CustomerDomain> getAllCustomer(CustomerQuery query);

    /**
     * 客户是否存在该标签
     *
     * @param customerId
     * @param tagId
     * @return
     */
    boolean hasTag(Long customerId, Long tagId);

    /**
     * 删除客户标签关系
     *
     * @param customerId
     * @param tagId
     * @return
     */
    boolean deleteTag(Long customerId, Long tagId);

    /**
     * 根据电话列表获取顾客列表
     *
     * @param mobiles
     * @return
     */
    List<CustomerDomain> getCustomersByMobiles(Long id, List<String> mobiles);

    /**
     * 根据id获取对象
     *
     * @param id
     * @return
     */
    CustomerDomain getById(Long id);

    /**
     * 批量删除客户
     *
     * @param ids
     */
    boolean deleteCustomers(List<Long> ids);

    /**
     * 根据id列表读取客户
     *
     * @param ids
     * @return
     */
    List<CustomerDomain> read(List<Long> ids);

    /**
     * 搜索当前用户下的客户
     *
     * @param query
     * @return
     */
    List<CustomerDomain> search(CustomerQuery query);

    /**
     * 更新客户头像
     *
     * @param id
     * @param iconId
     * @return
     */
    boolean updateIcon(Long id, Long iconId);

    /**
     * 创建客户
     *
     * @param customerDomain
     * @return
     */
    CustomerDomain create(CustomerDomain customerDomain);

    /**
     * 更新客户
     *
     * @param customerDomain
     * @return
     */
    boolean update(CustomerDomain customerDomain);

    /**
     * 添加标签
     *
     * @param ids
     * @param tagId
     */
    boolean addTags(List<Long> ids, Long tagId);

    /**
     * 根据姓名获取客户列表
     */
    List<CustomerDomain> searchByName(CustomerQuery query);

    /**
     * 指派客户给其他用户
     *
     * @param customerId
     * @param accountId
     * @return
     */
    Boolean assignCustomerById(Long customerId, Long accountId);

    /**
     * 给customerId增加tagId的标签
     *
     * @param customerId
     * @param tagId
     * @return
     */
    boolean addTag(Long customerId, Long tagId);

    /**
     * 根据customerIds获取相关的tag
     *
     * @param customerIds
     * @return
     */
    List<TagDomain> listTagsByCustomerIds(List<Long> customerIds);

    /**
     * 获取customerIds的tag数量
     *
     * @param customerIds
     * @return
     */
    Map<Long, Integer> countTagsByCustomerId(List<Long> customerIds);

    /**
     * 获取workspaceIds下所有客户的id
     *
     * @param workspaceIds
     * @return
     */
    List<AccountWorkspaceListDTO> getWorkspaceCustomerIds(List<Long> workspaceIds);

    /**
     * 根据customerIds获取所有客户信息, 包括已经删除了的
     *
     * @param customerIds
     * @return
     */
    List<CustomerDomain> readAll(List<Long> customerIds);
}
