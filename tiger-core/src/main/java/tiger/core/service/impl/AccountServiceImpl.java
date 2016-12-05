/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tiger.common.dal.annotation.RedisCaching;
import tiger.common.dal.dataobject.AccountBaseDO;
import tiger.common.dal.dataobject.AccountDO;
import tiger.common.dal.dataobject.example.AccountExample;
import tiger.common.dal.enums.AttachBizTypeEnum;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.common.dal.enums.WorkSpaceTypeEnum;
import tiger.common.dal.persistence.*;
import tiger.common.dal.query.AccountQuery;
import tiger.common.dal.query.AttachRelateQuery;
import tiger.common.dal.redis.RedisComponent;
import tiger.common.util.JsonUtil;
import tiger.common.util.Paginator;
import tiger.common.util.PhoneUtil;
import tiger.common.util.StringUtil;
import tiger.core.base.PageResult;
import tiger.core.constants.RedisCachePrefixConstants;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.AccountBaseDomain;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.AccountResetPwdDomain;
import tiger.core.domain.AttachDomain;
import tiger.core.domain.convert.AccountBaseConvert;
import tiger.core.domain.convert.AccountConvert;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.*;
import tiger.core.service.workspace.WorkspaceService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:09 PM yiliang.gyl Exp $
 */
@Service
public class AccountServiceImpl implements AccountService {

    public static final String PERSONAL_WORKSPACE_NAME = "个人工作空间";
    private Logger logger = Logger.getLogger(AccountServiceImpl.class);

    @Autowired
    AccountMapper accountMapper;

    @Autowired
    AccountLoginLogMapper accountLoginLogMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    AttachService attachService;

    @Autowired
    CompanyService companyService;

    @Autowired
    SystemParamService systemParamService;

    @Autowired
    AttachRelateService attachRelateService;

    @Autowired
    AttachRelateMapper attachRelateMapper;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    RedisComponent redisComponent;

    /**
     * @see AccountService#read(Long)
     */
    @Override
    public AccountDomain read(Long accountId) {
        AccountDO accountDO = accountMapper.selectByPrimaryKey(accountId);
        if (accountDO == null) {
            return null;
        }
        AccountDomain accountDomain = AccountConvert.convert2Domain(accountDO);
        return accountDomain;
    }

    /**
     * @see AccountService#readWithPermissions(Long)
     */
    @Override
    @RedisCaching(key = "account_auth_domain+accountId")
    public AccountDomain readWithPermissions(Long accountId) {
        AccountDomain accountDomain = read(accountId);
        if (accountDomain == null) {
            return null;
        }
        //加入workspace和权限
        accountDomain.setAccountWorkspaceDomains(workspaceService.getUserWorkspaces(accountId));

        redisComponent.saveObject(RedisCachePrefixConstants.ACCOUNT_AUTH_DOMAIN_PREFIX + accountDomain.getId(), JsonUtil.toJson(accountDomain));

        return accountDomain;
    }

    /**
     * @see AccountService#batchRead(List)
     */
    @Override
    public List<AccountDomain> batchRead(List<Long> accountIds) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andIdIn(accountIds);
        return AccountConvert.convert2Domain(accountMapper.selectByExample(accountExample));
    }

    /**
     * @see AccountService#readByMobile(String)
     */
    @Override
    public AccountDomain readByMobile(String mobile) {
        AccountExample example = new AccountExample();
        AccountExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);

        List<AccountDO> accountDOs = accountMapper.selectByExample(example);

        if (accountDOs.size() > SystemConstants.SIZE_ONE) {
            logger.error("获取到" + accountDOs.size() + "个相同手机号为[" + mobile + "]的用户.");
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        } else if (accountDOs.size() == SystemConstants.SIZE_ONE) {
            AccountDomain accountDomain = AccountConvert.convert2Domain(accountDOs.
                    get(SystemConstants.FIRST_INDEX));

            return accountDomain;
        }
        return null;
    }


    /**
     * @see AccountService#deleteLoginToken(Long, String)
     */
    @Override
    public Boolean deleteLoginToken(Long accountId, String loginToken) {
        int rc = accountLoginLogMapper.delete(accountId, loginToken);
        return rc > 0;
    }

    /**
     * @see AccountService#resetPasswordById(Long, String)
     */
    @Override
    public Boolean resetPasswordById(Long accountId, String newPassword) {
        AccountDO accountDO = new AccountDO();
        accountDO.setId(accountId);
        accountDO.setPassword(newPassword);
        int rc = accountMapper.updateByPrimaryKeySelective(accountDO);
        return (rc > 0);
    }

    /**
     * @see AccountService#updateAccount(AccountDomain)
     */
    @Override
    public boolean updateAccount(AccountDomain accountDomain) {
        AccountDO accountDO = AccountConvert.convert2DO(accountDomain);
        return accountMapper.updateByPrimaryKeySelective(accountDO) > 0;
    }

    /**
     * @see AccountService#updateAccountHeaderIcon(Long, Long)
     */
    @Override
    public int updateAccountHeaderIcon(Long accountId, Long attachId) {
        AccountDO accountDO = new AccountDO();
        accountDO.setId(accountId);
        accountDO.setIconId(attachId);
        return accountMapper.updateByPrimaryKeySelective(accountDO);
    }

    /**
     * @see AccountService#updateAccountMobile(Long, String)
     */
    @Override
    public Boolean updateAccountMobile(Long accountId, String mobile) {
        if (!PhoneUtil.isValidMobile(mobile)) {
            return false;
        }
        AccountDO accountDO = new AccountDO();
        accountDO.setId(accountId);
        accountDO.setMobile(mobile);

        return accountMapper.updateByPrimaryKeySelective(accountDO) > 0;
    }

    /**
     * 查询手机号是否已被注册
     *
     * @param phoneNum
     * @return
     */
    @Override
    public boolean isMobileExist(String phoneNum) {
        AccountExample example = new AccountExample();
        example.createCriteria()
                .andMobileEqualTo(phoneNum);
        List<AccountDO> AccountDOs = accountMapper.selectByExample(example);
        return (AccountDOs.size() > 0);
    }

    /**
     * 根据手机号码，改密码
     *
     * @param resetPwdDomain
     * @return
     */
    @Override
    public boolean resetPasswordByMobile(AccountResetPwdDomain resetPwdDomain) {
        AccountDomain domain = readByMobile(resetPwdDomain.getMobile());
        if (domain == null) {
            logger.error("未找到手机号码为[" + resetPwdDomain.getMobile() + "]的用户");
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        return resetPasswordById(domain.getId(), resetPwdDomain.getPassword());
    }

    /**
     * 根据原始密码改密码
     *
     * @param resetPassDomain
     * @return
     */
    @Override
    public boolean resetPasswordByOldPassword(AccountResetPwdDomain resetPassDomain) {
        if (!isCorrectPassword(resetPassDomain.getAccountId(), resetPassDomain.getOldPassword())) {
            logger.error("编号为[" + resetPassDomain.getId() + "]的用户使用错误的密码[" + resetPassDomain.getOldPassword() + "]将密码更新为" + resetPassDomain.getPassword());
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL, "账号的旧密码错误");
        }
        return resetPasswordById(resetPassDomain.getAccountId(), resetPassDomain.getPassword());
    }

    @Override
    public boolean delete(Long id) {
        return accountMapper.deleteByPrimaryKey(id) > 0;
    }

    // TODO 更改为团队版后,这个方法就没什么用了
    @Deprecated
    @Override
    public HashMap<String, String> getExtParamById(Long accountId) {
        AccountDomain accountDomain = read(accountId);
        if (accountDomain == null) {
            return null;
        }

        return getExtParamByAccount(accountDomain);
    }

    // TODO 更改为团队版后,这个方法就没什么用了
    @Deprecated
    @Override
    public HashMap<String, String> getExtParamByAccount(AccountDomain accountDomain) {
        HashMap<String, String> extParam = accountDomain.getExtParams();
        Map<String, String> loanConfig = systemParamService.getParamsByType(SystemParamTypeEnum.LOAN_CONFIG);
        // 设置合同逾期和坏账
        if (extParam == null) {
            extParam = new HashMap<>();
        }
        if (null == extParam.get(SystemConstants.BAD_LOAN_DAY)) {
            extParam.put(SystemConstants.BAD_LOAN_DAY, loanConfig.getOrDefault(SystemConstants.BAD_LOAN_DAY, SystemConstants.DEFAULT_BAD_LOAN_DAY));
        }
        if (null == extParam.get(SystemConstants.OVER_DUE_DAY)) {
            extParam.put(SystemConstants.OVER_DUE_DAY, loanConfig.getOrDefault(SystemConstants.OVER_DUE_DAY, SystemConstants.DEFAULT_OVER_DUE_DAY));
        }
        return extParam;
    }

    /**
     * 增加一个新的Account
     */
    @Override
    @Transactional
    public AccountDomain addAccount(AccountDomain domain) {
        AccountDO accountDO = AccountConvert.convert2DO(domain);
        //1. 添加account
        int count = accountMapper.insert(accountDO);
        // 如果失败，这里最好不要返回false，而是抛出异常，让前台知道错误码
        if (count != 1) {
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        }

        //2. 为account 添加一个默认的工作空间
        WorkspaceDomain workspace = new WorkspaceDomain();
        workspace.setOwnerId(accountDO.getId());
        workspace.setIsVerified(true);
        workspace.setType(WorkSpaceTypeEnum.PERSONAL);
        workspace.setName(PERSONAL_WORKSPACE_NAME);
        workspace.setIconId(workspaceService.getPersonalIconId());
        workspaceService.create(workspace);

        return read(accountDO.getId());
    }

    /**
     * @see tiger.core.service.AccountService#query(tiger.common.dal.query.AccountQuery)
     */
    @Override
    public PageResult<List<AccountDomain>> query(AccountQuery query) {
        //构建分页查询器
        Paginator paginator = new Paginator();
        int count = accountMapper.countAccount(query);
        paginator.setItems(count);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum());
        //查询客户列表
        List<AccountDO> accountDOs = accountMapper.queryAccount(query, paginator.getOffset(),
                paginator.getLength());
        PageResult<List<AccountDomain>> result = new PageResult<>();
        //转换成客户模型
        List<AccountDomain> accountDomains = AccountConvert.convert2Domain(accountDOs);
        result.setData(accountDomains);
        result.setPaginator(paginator);
        return result;
    }

    /**
     * @see AccountService#queryAll(AccountQuery)
     */
    @Override
    public List<AccountDomain> queryAll(AccountQuery query) {
        if (CollectionUtils.isEmpty(query.getIds())) {
            return new ArrayList<>();
        }

        return AccountConvert.convert2Domain(accountMapper.queryAllAccount(query));
    }

    /**
     * @see AccountService#getDefaultIconId()
     * 随机获取一个默认头像码
     */
    @Override
    public Long getDefaultIconId() {
        List<AttachDomain> defaultIconIds;
        AttachRelateQuery attachRelateQuery = new AttachRelateQuery();
        attachRelateQuery.setBizTypeEnum(AttachBizTypeEnum.DEFAULT_ICON);
        defaultIconIds = attachRelateService.listAttaches(attachRelateQuery);
        if (CollectionUtils.isEmpty(defaultIconIds)) {
            logger.error("系统未设置默认头像!!");
            return null;
        }
        int index = RandomUtils.nextInt(0, defaultIconIds.size());
        return defaultIconIds.get(index).getId();

    }

    //~ private methods


    /**
     * 统一用户密码检查工具
     *
     * @param accountId
     * @param password
     * @return
     */
    private boolean isCorrectPassword(Long accountId, String password) {
        String originPassword = read(accountId).getPassword();
        return StringUtil.equals(password, originPassword);
    }

    /**
     * @see AccountService#read(Long)
     */
    @Override
    public AccountBaseDomain getBaseInfo(Long accountId) {
        if (accountId == null) {
            return null;
        }

        AccountBaseDO accountBaseDO = accountMapper.getBaseInfo(accountId);

        return AccountBaseConvert.convert2Domain(accountBaseDO);
    }


    /**
     * @see AccountService#getBaseInfos(List)
     */
    @Override
    public List<AccountBaseDomain> getBaseInfos(List<Long> accountIds) {
        if (CollectionUtils.isEmpty(accountIds)) {
            return new ArrayList<>();
        }

        return AccountBaseConvert.convert2Domain(accountMapper.getBaseInfos(accountIds));
    }
    /**
     * 将该用户设置为禁用，使其不能登录系统
     * */
    @Override
    public int changeAccountStatus(Map<String, Object> map) {
        if(map==null){
            return 0;
        }
        return accountMapper.changeAccountStatus(map);
    }

}
