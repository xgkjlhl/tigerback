package tiger.core.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.CustomerDO;
import tiger.common.dal.dataobject.CustomerMsgDO;
import tiger.common.dal.dataobject.LoanDO;
import tiger.common.dal.dataobject.TagDO;
import tiger.common.dal.dataobject.example.CustomerExample;
import tiger.common.dal.dto.list.AccountWorkspaceListDTO;
import tiger.common.dal.dto.CountDTO;
import tiger.common.dal.persistence.CustomerMapper;
import tiger.common.dal.persistence.CustomerMsgMapper;
import tiger.common.dal.persistence.LoanMapper;
import tiger.common.dal.persistence.TagMapper;
import tiger.common.dal.query.CustomerQuery;
import tiger.common.dal.query.LoanQuery;
import tiger.common.dal.query.SmsQuery;
import tiger.common.util.*;
import tiger.core.base.PageResult;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.*;
import tiger.core.domain.convert.CustomerConverter;
import tiger.core.domain.convert.CustomerMsgConverter;
import tiger.core.domain.convert.LoanConvert;
import tiger.core.domain.convert.TagConverter;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.AccountService;
import tiger.core.service.AttachService;
import tiger.core.service.CustomerService;

import java.util.*;

/**
 * 客户相关核心层数据交互实现
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private CustomerMsgMapper customerMsgMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AttachService attachService;

    @Autowired
    private AccountService accountService;

    @Override
    public CustomerDomain read(Long id) {
        CustomerDO customerDO = customerMapper.selectByPrimaryKey(id);
        if (null == customerDO || customerDO.getIsDel().equals(Boolean.TRUE)) {
            return null;
        }
        CustomerDomain customerDomain = CustomerConverter.convert2Domain(customerDO);
        customerDomain.setIcon(getIcon(customerDO.getIcon()));
        if (customerDO.getAccountId() != null) {
            customerDomain.setOwnerInfo(accountService.getBaseInfo(customerDO.getAccountId()));
        }
        return customerDomain;
    }

    /**
     * @see tiger.core.service.CustomerService#queryLoans(tiger.common.dal.query.LoanQuery)
     */
    @Override
    public PageResult<List<LoanDomain>> queryLoans(LoanQuery query) {
        Paginator paginator = new Paginator();
        int count = loanMapper.countCustomerLoan(query);
        paginator.setItems(count);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum());
        List<LoanDO> list = loanMapper.queryCustomerLoan(query, paginator.getOffset(),
                paginator.getLength());
        PageResult<List<LoanDomain>> result = new PageResult<>();
        result.setData(LoanConvert.convertDOsToDomains(list));
        result.setPaginator(paginator);
        return result;
    }

    /**
     * @see tiger.core.service.CustomerService#listAllTags(Long)
     */
    @Override
    public List<TagDomain> listAllTags(Long customerId) {
        List<TagDO> tagList = tagMapper.getTagsByCustomerId(customerId);
        return TagConverter.convert2Domain(tagList);
    }

    /**
     * @see tiger.core.service.CustomerService#querySmses(SmsQuery)
     */
    @Override
    public PageResult<List<CustomerMsgDomain>> querySmses(SmsQuery query) {
        Paginator paginator = new Paginator();
        int count = customerMsgMapper.countCustomerSmses(query);
        paginator.setItems(count);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum());
        List<CustomerMsgDO> list = customerMsgMapper.queryCustomerSmses(query,
                paginator.getOffset(), paginator.getLength());
        PageResult<List<CustomerMsgDomain>> result = new PageResult<>();
        result.setData(CustomerMsgConverter.convert2Domain(list));
        result.setPaginator(paginator);
        return result;
    }

    /**
     * @see tiger.core.service.CustomerService#query(tiger.common.dal.query.CustomerQuery)
     */
    @Override
    public PageResult<List<CustomerDomain>> query(CustomerQuery query) {
        //构建分页查询器
        Paginator paginator = new Paginator();
        int count = customerMapper.countCustomer(query);
        paginator.setItems(count);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum());
        //查询客户列表
        List<CustomerDO> customerDOs = customerMapper.queryCustomer(query, paginator.getOffset(),
                paginator.getLength());
        PageResult<List<CustomerDomain>> result = new PageResult<>();
        //转换成客户模型
        List<CustomerDomain> customerDomains = setCustomersIcon(customerDOs);
        result.setData(customerDomains);
        result.setPaginator(paginator);
        return result;
    }

    /**
     * @see CustomerService#deleteCustomer(Long)
     */
    @Override
    public boolean deleteCustomer(Long id) {
        //判断是否存在关联的贷款信息
        checkCustomerStatus(id);

        return customerMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Integer getTodayBirthCustomerCount(CustomerQuery query) {
        Date now = new Date();
        return customerMapper.countCustomerByBirth(query,  now, now);
    }

    @Override
    public List<CustomerDomain> getTodayBirthCustomers(CustomerQuery query) {
        Date now = new Date();
        return setCustomersIcon(
                customerMapper.listCustomersByBirth(query,  now, now)
        );
    }

    @Override
    public Integer getMonthBirthCustomerCount(CustomerQuery query) {
        Date beginDate = DateUtil.getDayBegin(new Date());
        Date endDate = DateUtil.addDays(DateUtil.addMonths(beginDate, 1), -1);
        return customerMapper.countCustomerByBirth(query, beginDate, endDate);
    }

    @Override
    public List<CustomerDomain> getMonthBirthCustomers(CustomerQuery query) {
        Date beginDate = DateUtil.getDayBegin(new Date());
        Date endDate = DateUtil.addDays(DateUtil.addMonths(beginDate, 1), -1);
        return setCustomersIcon(
                customerMapper.listCustomersByBirth(query, beginDate, endDate)
        );
    }

    @Override
    public Integer getCustomerCount(CustomerQuery query) {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        if (null != query.getWorkspaceId())
            criteria.andWorkspaceIdEqualTo(query.getWorkspaceId());
        if (null != query.getAccountId())
            criteria.andAccountIdEqualTo(query.getAccountId());
        criteria.andIsDelEqualTo(ByteUtil.BYTE_ZERO);
        return customerMapper.countByExample(example);
    }

    @Override
    public List<CustomerDomain> getAllCustomer(CustomerQuery query) {
        List<CustomerDO> customerDOs = customerMapper.queryAllCustomer(query);
        return setCustomersIcon(customerDOs);
    }

    @Override
    public boolean hasTag(Long customerId, Long tagId) {
        return customerMapper.countTag(customerId, tagId) > 0;
    }

    @Override
    public boolean deleteTag(Long customerId, Long tagId) {
        return customerMapper.deleteTag(customerId, tagId) > 0;
    }

    @Override
    public List<CustomerDomain> getCustomersByMobiles(Long id, List<String> mobiles) {
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        criteria.andMobileIn(mobiles);
        criteria.andAccountIdEqualTo(id);

        List<CustomerDO> customerDOs = customerMapper.selectByExample(example);
        return setCustomersIcon(customerDOs);
    }

    @Override
    public CustomerDomain getById(Long id) {
        CustomerDO customerDO = customerMapper.selectByPrimaryKey(id);
        CustomerDomain domain = new CustomerDomain();
        BeanUtil.copyPropertiesWithIgnores(customerDO, domain);
        return domain;
    }

    @Override
    public boolean deleteCustomers(List<Long> ids) {
        checkCustomerStatus(ids);

        CustomerExample example = new CustomerExample();
        example.createCriteria().andIdIn(ids);
        CustomerDO customerDO = new CustomerDO();
        customerDO.setIsDel(Boolean.TRUE);
        return customerMapper.updateByExampleSelective(customerDO, example) > 0;
    }

    @Override
    public List<CustomerDomain> read(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids))
            return new ArrayList<>();
        CustomerExample example = new CustomerExample();
        example.createCriteria().andIdIn(ids).andIsDelEqualTo(ByteUtil.BYTE_ZERO);
        List<CustomerDO> customerDOs = customerMapper.selectByExample(example);
        return setCustomersIcon(customerDOs);
    }

    /**
     * @see tiger.core.service.CustomerService#search(CustomerQuery)
     */
    @Override
    public List<CustomerDomain> search(CustomerQuery query) {
        List<CustomerDO> customerDOList = customerMapper.search(query);
        return CustomerConverter.convert2Domain(customerDOList);
    }

    @Override
    public boolean updateIcon(Long id, Long iconId) {
        return customerMapper.updateIcon(id, iconId) > 0;
    }

    @Override
    public CustomerDomain create(CustomerDomain customerDomain) {
        CustomerDO customerDO = CustomerConverter.convert2DO(customerDomain);
        // 判断是否新建成功
        if (customerMapper.insert(customerDO) > 0) {
            return CustomerConverter.convert2Domain(customerDO);
        }
        return null;
    }

    @Override
    public boolean update(CustomerDomain customerDomain) {
        CustomerDO customerDO = CustomerConverter.convert2DO(customerDomain);
        return customerMapper.updateByPrimaryKeySelective(customerDO) > 0;
    }

    /**
     * @see CustomerService#addTags(List, Long)
     */
    @Override
    public boolean addTags(List<Long> ids, Long tagId) {
        if (CollectionUtils.isEmpty(ids) || tagId == null) {
            return true;
        }

        return customerMapper.addTags(ids, tagId) == ids.size();
    }

    /**
     * @see CustomerService#searchByName(CustomerQuery)
     */
    @Override
    public List<CustomerDomain> searchByName(CustomerQuery query) {
        if (StringUtil.isBlank(query.getParam())) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER, "搜索姓名不能为空");
        }
        CustomerExample example = new CustomerExample();
        CustomerExample.Criteria criteria = example.createCriteria();
        if (null != query.getAccountId())
            criteria.andAccountIdEqualTo(query.getAccountId());
        if (null != query.getWorkspaceId())
            criteria.andWorkspaceIdEqualTo(query.getWorkspaceId());
        criteria.andNameEqualTo(query.getParam());
        criteria.andIsDelEqualTo(ByteUtil.BYTE_ZERO);
        example.setOrderByClause(SystemConstants.CREATE_TIME_DESC);

        return setCustomersIcon(customerMapper.selectByExample(example));
    }

    /**
     * @see CustomerService#assignCustomerById(Long, Long)
     */
    @Override
    public Boolean assignCustomerById(Long customerId, Long accountId) {
        CustomerDO customerDO = new CustomerDO();
        CustomerExample example = new CustomerExample();
        customerDO.setAccountId(accountId);
        example.createCriteria().andIdEqualTo(customerId);
        return customerMapper.updateByExampleSelective(customerDO, example) > 0;
    }

    /**
     * @see CustomerService#addTag(Long, Long)
     */
    @Override
    public boolean addTag(Long customerId, Long tagId) {
        if (customerId == null || tagId == null) {
            return true;
        }

        return customerMapper.addTag(customerId, tagId) > 0;
    }

    /**
     * @see CustomerService#listTagsByCustomerIds(List)
     */
    @Override
    public List<TagDomain> listTagsByCustomerIds(List<Long> customerIds) {
        if (CollectionUtils.isEmpty(customerIds)) {
            return new ArrayList<>();
        }

        return TagConverter.convert2Domain(tagMapper.getTagsByCustomerIds(customerIds));
    }

    /**
     * @see CustomerService#countTagsByCustomerId(List)
     */
    @Override
    public Map<Long, Integer> countTagsByCustomerId(List<Long> customerIds) {
        if (CollectionUtils.isEmpty(customerIds)) {
            return new HashMap<>();
        }

        List<CountDTO> tagCount = tagMapper.countTagsByCustomerId(customerIds);
        Map<Long, Integer> tagCountMap = new HashMap<>(customerIds.size());
        // 设置默认值
        customerIds.forEach(customerId -> tagCountMap.put(customerId, 0));
        // 设置查询结果
        tagCount.forEach(count -> tagCountMap.put(count.getId(), count.getCount()));

        return tagCountMap;
    }

    /**
     * @see CustomerService#getWorkspaceCustomerIds(List)
     */
    @Override
    public List<AccountWorkspaceListDTO> getWorkspaceCustomerIds(List<Long> workspaceIds) {
        if (CollectionUtils.isEmpty(workspaceIds)) {
            return new ArrayList<>();
        }

        return customerMapper.getWorkspaceCustomerIds(workspaceIds);
    }

    /**
     * @see CustomerService#readAll(List)
     */
    @Override
    public List<CustomerDomain> readAll(List<Long> customerIds) {
        if (CollectionUtils.isEmpty(customerIds))
            return new ArrayList<>();

        CustomerExample example = new CustomerExample();
        example.createCriteria().andIdIn(customerIds);

        return CustomerConverter.convert2Domain(customerMapper.selectByExample(example));

    }
    // ~ Private

    /**
     * 根据附件id获取头像url
     *
     * @param id
     * @return
     */
    private AttachDomain getIcon(Long id) {
        if (null != id && id > 0) {
            AttachDomain attachDomain = attachService.getAttachWithSignedUrlById(id);
            if (null != attachDomain) {
                return attachDomain;
            }
        }
        return null;
    }

    /**
     * 设置客户头像
     *
     * @param customerDOs
     * @return
     */
    private List<CustomerDomain> setCustomersIcon(List<CustomerDO> customerDOs) {
        if (CollectionUtils.isEmpty(customerDOs)) {
            return new ArrayList<>();
        }

        // 获取所有customer的头像列表
        List<Long> iconIds = new ArrayList<>(customerDOs.size());
        customerDOs.forEach(customer -> iconIds.add(customer.getIcon()));
        List<AttachDomain> icons = attachService.getAttachesWithSignedUrlByIds(iconIds);

        // 构建HashMap
        Map<Long, AttachDomain> iconMap = new HashMap<>(iconIds.size());
        icons.forEach(icon -> iconMap.put(icon.getId(), icon));

        List<CustomerDomain> customers = new ArrayList<>(customerDOs.size());
        // 默认的空头像
        customerDOs.forEach(customer -> {
            // 转换
            CustomerDomain customerDomain = CustomerConverter.convert2Domain(customer);
            // 设置头像
            customerDomain.setIcon(iconMap.get(customer.getIcon()));
            // 添加
            customers.add(customerDomain);
        });

        return customers;
    }

    /**
     * check 客户是否能删除
     */
    private void checkCustomerStatus(Long id) {
        if (id == null) {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER, "参数错误, 非法的客户");
        }

        LoanQuery loanQuery = new LoanQuery();
        loanQuery.setBeginDate(null);
        loanQuery.setCustomerId(id);
        if (loanMapper.countCustomerLoan(loanQuery) > 0) {
            throw new TigerException(ErrorCodeEnum.BIZ_STATUS_ERROR, "该客户有合同关联，无法删除");
        }
    }

    /**
     * check 客户ids是否能删除
     *
     * @param ids
     */
    private void checkCustomerStatus(List<Long> ids) {
        List<CountDTO> loanCounts = loanMapper.countCustomerLoans(ids);
        loanCounts.forEach(loancount -> {
            if (loancount.getCount() > 0) {
                throw new TigerException(ErrorCodeEnum.BIZ_STATUS_ERROR, "该客户有合同关联，无法删除");
            }
        });
    }
}
