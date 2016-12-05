/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.workspace.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.WorkspaceDO;
import tiger.common.dal.dataobject.example.WorkspaceExample;
import tiger.common.dal.dto.CountDTO;
import tiger.common.dal.dto.WorkspaceAccountDTO;
import tiger.common.dal.dto.list.AccountWorkspaceListDTO;
import tiger.common.dal.enums.AttachBizTypeEnum;
import tiger.common.dal.enums.RoleEnum;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.common.dal.enums.WorkSpaceTypeEnum;
import tiger.common.dal.persistence.WorkspaceMapper;
import tiger.common.dal.query.AttachRelateQuery;
import tiger.common.dal.redis.RedisComponent;
import tiger.common.util.JsonUtil;
import tiger.common.util.StringUtil;
import tiger.core.constants.RedisCachePrefixConstants;
import tiger.core.constants.SystemConstants;
import tiger.core.domain.AccountBaseDomain;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.AttachDomain;
import tiger.core.domain.workspace.*;
import tiger.core.domain.workspace.convert.WorkspaceConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.AccountService;
import tiger.core.service.AttachRelateService;
import tiger.core.service.AttachService;
import tiger.core.service.SystemParamService;
import tiger.core.service.workspace.RoleAndPermissionService;
import tiger.core.service.workspace.WorkspaceService;

import javax.transaction.Transactional;
import java.util.*;

/**
 * 团队空间服务接口实现
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 11:52 AM yiliang.gyl Exp $
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final Logger logger = Logger.getLogger(WorkspaceServiceImpl.class);

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleAndPermissionService roleAndPermissionService;

    @Autowired
    private SystemParamService systemParamService;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private AttachRelateService attachRelateService;

    @Autowired
    private AttachService attachService;

    /**
     * @see WorkspaceService#create(WorkspaceDomain)
     */
    @Override
    @Transactional
    public WorkspaceDomain create(WorkspaceDomain workspaceDomain) {
        // 设置默认的贷款设置
        workspaceDomain.setExtParams(generateDefaultLoanSetting());
        WorkspaceDO workspaceDO = WorkspaceConvert.convert2DO(workspaceDomain);
        //1. 插入实体
        int rc = workspaceMapper.insert(workspaceDO);
        if (rc > 0) {
            //2. 插入创建者为owner
            AccountWorkspaceDomain accountWorkspace = new AccountWorkspaceDomain();
            accountWorkspace.setAccountId(workspaceDomain.getOwnerId());
            accountWorkspace.getRoles().add(RoleEnum.OWNER);
            accountWorkspace.setWorkspaceId(workspaceDO.getId());

            // 创建成功则返回带拥有者的workspaceDomain
            if (addUser(accountWorkspace)) {
                return read(workspaceDO.getId());
            }
        }
        // 创建失败,抛出异常
        throw new TigerException(ErrorCodeEnum.DB_EXCEPTION, "数据库失败，插入信息错误");
    }

    /**
     * @see WorkspaceService#delete(Long)
     */
    @Override
    @Transactional
    public Boolean delete(Long id) {
        //1. 删除了workspace信息
        Boolean result = workspaceMapper.deleteByPrimaryKey(id) > 0;
        //2. 删除成员关系信息
        if (result) {
            try {
                workspaceMapper.removeAllUser(id);
            } catch (Exception e) {
                logger.error("删除空间 [" + id + "] 的合同其他信息失败");
            }
        }
        return result;
    }

    /**
     * @see WorkspaceService#update(WorkspaceDomain)
     */
    @Override
    public Boolean update(WorkspaceDomain workspaceDomain) {
        if (workspaceDomain.getId() == null) {
            return false;
        }
        return workspaceMapper.updateByPrimaryKeySelective(WorkspaceConvert.convert2DO(workspaceDomain)) > 0;
    }

    /**
     * @see WorkspaceService#read(Long)
     */
    @Override
    public WorkspaceDomain read(Long id) {
        WorkspaceDomain workspace = WorkspaceConvert.convert2Domain(workspaceMapper.selectByPrimaryKey(id));

        if (workspace == null) {
            return null;
        }

        // 设置所有者信息
        workspace.setOwner(accountService.read(workspace.getOwnerId()));
        // 设置头像信息
        workspace.setIcon(attachService.getAttachWithSignedUrlById(workspace.getIconId()));

        return workspace;
    }

    /**
     * @see WorkspaceService#batchRead(List)
     */
    @Override
    public List<WorkspaceDomain> batchRead(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        WorkspaceExample example = new WorkspaceExample();
        example.createCriteria().andIdIn(ids);
        List<WorkspaceDO> workspaceDOs = workspaceMapper.selectByExample(example);
        return WorkspaceConvert.convert2Domains(workspaceDOs);
    }

    /**
     * @see WorkspaceService#isOwner(Long, Long)
     */
    @Override
    public Boolean isOwner(Long workspaceId, Long accountId) {
        //1. 判断所有者id是否一致
        WorkspaceExample example = new WorkspaceExample();
        example.createCriteria().andIdEqualTo(workspaceId).andOwnerIdEqualTo(accountId);
        Boolean isOwner = workspaceMapper.countByExample(example) > 0;

        //2. 是否在角色上的所有者
        List<WorkspaceAccountDTO> accountRoles = workspaceMapper.getUserWorkSpaceRoles(workspaceId, accountId);
        Boolean hasRole = false;
        for (WorkspaceAccountDTO dto : accountRoles) {
            if (dto.getRole().equals(RoleEnum.OWNER.getCode())) {
                hasRole = true;
                break;
            }
        }
        return (isOwner && hasRole);
    }

    /**
     * @see WorkspaceService#isExist(Long)
     */
    @Override
    public Boolean isExist(Long projectId) {
        WorkspaceExample example = new WorkspaceExample();
        example.createCriteria().andIdEqualTo(projectId);
        return workspaceMapper.countByExample(example) > 0;
    }

    /**
     * @see WorkspaceService#getAllWorkspaceMember(Long)
     */
    @Override
    public List<WorkspaceMemberListDomain> getAllWorkspaceMember(Long workspaceId) {
        List<WorkspaceMemberListDomain> results = new ArrayList<>();

        //找到对应关系
        List<WorkspaceAccountDTO> accountDTOs = workspaceMapper.getWorkSpaceUsers(workspaceId);
        if (CollectionUtils.isEmpty(accountDTOs)) {
            return results;
        }

        // 生成 accountId -> List<RoleEnum> 映射关系
        Map<Long, List<RoleEnum>> accountRoleMap = genRoleMap(accountDTOs);

        //准备查询条件
        Set<Long> accountIdSet = new HashSet<>(accountDTOs.size());
        accountDTOs.forEach(account -> accountIdSet.add(account.getAccountId()));
        //查询用户
        List<AccountBaseDomain> accounts = accountService.getBaseInfos(new ArrayList<>(accountIdSet));

        //查询结果包装处理
        WorkspaceMemberListDomain workspaceMember;
        for (AccountBaseDomain account : accounts) {
            workspaceMember = new WorkspaceMemberListDomain();
            workspaceMember.setAccount(account);
            if (accountRoleMap.containsKey(account.getId())) {
                workspaceMember.setRoles(accountRoleMap.get(account.getId()));
            }
            results.add(workspaceMember);
        }

        // 排序
        sortWorkspaceMembers(results);

        return results;
    }

    /**
     * @see WorkspaceService#getAllWorkspaceMemberIds(Long)
     */
    @Override
    public List<Long> getAllWorkspaceMemberIds(Long workspaceId) {
        // 使用Set避免重复的accountId
        Set<Long> ids = new HashSet<>();

        List<WorkspaceAccountDTO> accountDTOs = workspaceMapper.getWorkSpaceUsers(workspaceId);
        accountDTOs.forEach(workspaceMember -> ids.add(workspaceMember.getAccountId()));

        return new ArrayList<>(ids);
    }

    /**
     * @see WorkspaceService#getUserWorkspace(Long, Long)
     */
    @Override
    public AccountWorkspaceDomain getUserWorkspace(Long accountId, Long workspaceId) {
        AccountWorkspaceDomain workspace = new AccountWorkspaceDomain();

        workspace.setWorkspaceId(workspaceId);
        workspace.setWorkspace(read(workspaceId));
        workspace.setAccountId(accountId);
        workspace.setAccount(accountService.read(accountId));

        //~ roles and permissions
        List<WorkspaceAccountDTO> rolesDto = workspaceMapper.getUserWorkSpaceRoles(workspaceId, accountId);

        List<RoleEnum> roles = new ArrayList<>();
        rolesDto.forEach(p -> roles.add(RoleEnum.getEnumByCode(p.getRole())));
        sortRoles(roles);
        workspace.setRoles(roles);

        //permission
        if (CollectionUtils.isNotEmpty(roles)) {
            workspace.setPermissions(roleAndPermissionService.getPermissionsOfRoles(roles));
        } else {
            workspace.setPermissions(new ArrayList<>());
        }
        return workspace;
    }

    /**
     * @see WorkspaceService#getUserWorkspaces(Long)
     */
    @Override
    public List<AccountWorkspaceDomain> getUserWorkspaces(Long accountId) {
        List<AccountWorkspaceDomain> result = new ArrayList<>();

        //1. 获取空间 ids
        List<Long> workspaceIds = workspaceMapper.getWorkspaceIdsByAccountId(accountId);
        if (CollectionUtils.isEmpty(workspaceIds)) {
            return result;
        }
        //2. 获取用户空间角色列表
        List<WorkspaceAccountDTO> rolesDtos = workspaceMapper.getAllUserWorkSpaceRoles(accountId);

        //3. 获取角色
        Map<Long, AccountWorkspaceDomain> workspaceMap = new HashMap<>();
        for (WorkspaceAccountDTO dto : rolesDtos) {
            RoleEnum role = RoleEnum.getEnumByCode(dto.getRole());
            if (role == null) {
                continue;
            }
            if (workspaceMap.containsKey(dto.getWorkspaceId())) {
                workspaceMap.get(dto.getWorkspaceId()).getRoles().add(role);
            } else {
                AccountWorkspaceDomain workspace = new AccountWorkspaceDomain();
                workspace.setAccountId(accountId);
                workspace.setWorkspaceId(dto.getWorkspaceId());
                workspace.getRoles().add(role);
                workspaceMap.put(dto.getWorkspaceId(), workspace);
            }
        }

        //4.获取权限
        result = new ArrayList<>(workspaceMap.values());
        result.forEach(p -> p.setPermissions(
                roleAndPermissionService.getPermissionsOfRoles(p.getRoles())));

        return result;
    }

    /**
     * @see WorkspaceService#removeUser(Long, Long)
     */
    @Override
    @Transactional
    public Boolean removeUser(Long workspaceId, Long accountId) {
        return workspaceMapper.removeUser(workspaceId, accountId) > 0;
    }

    /**
     * @see WorkspaceService#addUser(AccountWorkspaceDomain)
     */
    @Override
    @Transactional
    public Boolean addUser(AccountWorkspaceDomain accountWorkspace) {
        List<WorkspaceAccountDTO> dtos = new ArrayList<>();
        if (CollectionUtils.isEmpty(accountWorkspace.getRoles())) {
            throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL, "用户角色未设置");
        }

        for (RoleEnum role : accountWorkspace.getRoles()) {
            WorkspaceAccountDTO wad = new WorkspaceAccountDTO();
            wad.setAccountId(accountWorkspace.getAccountId());
            wad.setRole(role.getCode());
            wad.setWorkspaceId(accountWorkspace.getWorkspaceId());
            dtos.add(wad);
        }
        return workspaceMapper.addUserAndRoles(dtos) > 0;
    }

    /**
     * @see WorkspaceService#changeUserRole(AccountWorkspaceDomain)
     */
    @Override
    @Transactional
    public Boolean changeUserRole(AccountWorkspaceDomain accountWorkspaceDomain) {
        if (removeUser(accountWorkspaceDomain.getWorkspaceId(), accountWorkspaceDomain.getAccountId())) {
            return addUser(accountWorkspaceDomain);
        } else {
            return false;
        }
    }

    /**
     * @see WorkspaceService#transferWorkspace(Long, Long, Long)
     */
    @Override
    @Transactional
    public Boolean transferWorkspace(Long workspaceId, Long ownerId, Long accountId) {
        if (!isOwner(workspaceId, ownerId)) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED, "该用户没有移交团队空间的权限");
        }
        //1.管理员改成普通用户
        AccountWorkspaceDomain ownerChange = new AccountWorkspaceDomain(workspaceId, ownerId);
        Set<RoleEnum> ownerRoleEnumSet = new HashSet<>(ownerChange.getRoles());
        ownerRoleEnumSet.add(RoleEnum.CUSTOMER);
        ownerRoleEnumSet.remove(RoleEnum.OWNER);
        ownerChange.setRoles(new ArrayList<>(ownerRoleEnumSet));
        Boolean changeOwner = changeUserRole(ownerChange);

        //2.普通用户改成所有者
        AccountWorkspaceDomain accountChange = new AccountWorkspaceDomain(workspaceId, accountId);
        Set<RoleEnum> accountRoleEnumSet = new HashSet<>(accountChange.getRoles());
        accountRoleEnumSet.add(RoleEnum.OWNER);
        accountRoleEnumSet.remove(RoleEnum.CUSTOMER);
        accountChange.setRoles(new ArrayList<>(accountRoleEnumSet));
        Boolean changeAccount = changeUserRole(accountChange);

        //3.修改项目所有者id
        WorkspaceDomain workspace = new WorkspaceDomain();
        workspace.setId(workspaceId);
        workspace.setOwnerId(accountId);
        Boolean updateOwnerId = update(workspace);

        if (changeOwner && changeAccount && updateOwnerId) {
            return true;
        } else {
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION, "数据库错误，更新失败");
        }
    }

    /**
     * @see WorkspaceService#getWorkspacesByAccountId(Long)
     */
    @Override
    public List<WorkspaceDomain> getWorkspacesByAccountId(Long accountId) {
        if (accountId == null) {
            return new ArrayList<>();
        }

        // 1. 获取用户工作组
        List<WorkspaceDomain> accountWorkspaces = WorkspaceConvert.convert2Domains(
                workspaceMapper.getWorkspacesByAccountId(accountId)
        );

        // 如果为空,则提前返回
        if (CollectionUtils.isEmpty(accountWorkspaces)) {
            return accountWorkspaces;
        }

        // 排序, 私人空间排到第一个
        accountWorkspaces.sort((a, b) -> {
            if (a == null) {
                return 1;
            }
            if (b == null) {
                return -1;
            }

            return a.getType().compareTo(b.getType());
        });

        // 2. 设置头像信息
        List<Long> iconIds = new ArrayList<>(accountWorkspaces.size());
        List<Long> accountIds = new ArrayList<>(accountWorkspaces.size());
        accountWorkspaces.forEach(workspace -> {
            iconIds.add(workspace.getIconId());
            accountIds.add(workspace.getOwnerId());
        });

        // 获取头像可访问链接
        List<AttachDomain> icons = attachService.getAttachesWithSignedUrlByIds(iconIds);
        List<AccountDomain> owners = accountService.batchRead(accountIds);
        if (CollectionUtils.isEmpty(icons) || CollectionUtils.isEmpty(owners)) {
            return accountWorkspaces;
        }

        // 构建hashmap,加快寻找速度
        Map<Long, AttachDomain> iconMap = new HashMap(icons.size());
        icons.forEach(icon -> iconMap.put(icon.getId(), icon));
        Map<Long, AccountDomain> ownerMap = new HashMap<>(owners.size());
        owners.forEach(owner -> ownerMap.put(owner.getId(), owner));

        // 设置头像
        accountWorkspaces.forEach(workspace -> {
            workspace.setIcon(iconMap.get(workspace.getIconId()));
            workspace.setOwner(ownerMap.get(workspace.getOwnerId()));
        });

        return accountWorkspaces;
    }

    /**
     * @see WorkspaceService#isMember(Long, Long)
     */
    @Override
    public Boolean isMember(Long workspaceId, Long accountId) {
        return CollectionUtils.isNotEmpty(workspaceMapper.getUserWorkSpaceRoles(workspaceId, accountId));
    }

    /**
     * @see WorkspaceService#removeMemberRoles(AccountWorkspaceDomain)
     */
    @Override
    @Transactional
    public Boolean removeMemberRoles(AccountWorkspaceDomain accountWorkspaceDomain) {
        if (accountWorkspaceDomain.getAccountId() == null || accountWorkspaceDomain.getWorkspaceId() == null) {
            return false;
        }

        List<RoleEnum> roleEnumList = accountWorkspaceDomain.getRoles();
        List<String> roles = new ArrayList<>(roleEnumList.size());

        roleEnumList.forEach(role -> {
            if (!role.equals(RoleEnum.OWNER)) {
                roles.add(role.getCode());
            }
        });

        if (CollectionUtils.isEmpty(roles)) {
            return true;
        }

        if (roles.size() == workspaceMapper.removeUserRoles(accountWorkspaceDomain.getAccountId(), accountWorkspaceDomain.getWorkspaceId(), roles)) {
            return true;
        }

        throw new TigerException(ErrorCodeEnum.DB_EXCEPTION, "数据库异常, 移除成员角色失败");
    }

    /**
     * 生成邀请链接
     *
     * @param workspaceId
     * @param accountId
     * @return
     * @see WorkspaceService#generateInvitation(Long, Long)
     */
    @Override
    public WorkspaceInvitationDomain generateInvitation(Long workspaceId, Long accountId) {
        WorkspaceInvitationDomain invitationDomain = new WorkspaceInvitationDomain(accountId, workspaceId);
        String baseUrl = systemParamService.getValueByTypeAndKey(SystemParamTypeEnum.DEFAULT,
                SystemConstants.INVITATION_URL);
        if (StringUtil.isBlank(baseUrl)) {
            return null;
        }
        invitationDomain.setUrl(baseUrl + "?code=" + invitationDomain.getKey());
        String key = RedisCachePrefixConstants.concreteKey(RedisCachePrefixConstants.WORKSPACE_VERIFY_PREFIX,
                invitationDomain.getKey());
        Integer rc = redisComponent.saveObject(key, JsonUtil.toJson(invitationDomain));
        if (rc == null) {
            return null;
        }
        return invitationDomain;
    }

    /**
     * @see WorkspaceService#getExtParamById(Long)
     */
    @Override
    public Map<String, String> getExtParamById(Long workspaceId) {
        WorkspaceDomain accountDomain = read(workspaceId);

        // 如果返回结果为空,则直接返回
        if (accountDomain == null) {
            return null;
        }

        // 获取当前默认贷款设置
        Map<String, String> loanConfig = systemParamService.getParamsByType(SystemParamTypeEnum.LOAN_CONFIG);

        // 设置额外设置
        return setExtParam(accountDomain, loanConfig);
    }

    /**
     * @see WorkspaceService#countWorkspaceMember(List)
     */
    @Override
    public Map<Long, Integer> countWorkspaceMember(List<Long> workspaceIds) {
        // 如果参数为空,则返回空列表
        if (CollectionUtils.isEmpty(workspaceIds)) {
            return new HashMap();
        }

        List<CountDTO> countResults = workspaceMapper.countWorkspaceMembers(workspaceIds);

        Map<Long, Integer> memberCountMap = new HashMap<>(workspaceIds.size());
        // 设置初值
        workspaceIds.forEach(workspaceId -> memberCountMap.put(workspaceId, 0));
        // 设置查询结果
        countResults.forEach(count -> memberCountMap.put(count.getId(), count.getCount()));

        return memberCountMap;
    }

    /**
     * @see WorkspaceService#getSequenceIconId(int)
     */
    public Long getSequenceIconId(int teamCount) {
        AttachRelateQuery attachRelateQuery = new AttachRelateQuery();
        attachRelateQuery.setBizTypeEnum(AttachBizTypeEnum.WORKSPACE_DEFAULT_ICON);

        List<AttachDomain> defaultIconIds = attachRelateService.listAttaches(attachRelateQuery);
        if (CollectionUtils.isEmpty(defaultIconIds)) {
            logger.error("系统未设置团队工作空间默认头像!!");
            return null;
        }

        // 按照id排序, 保证循环效果
        defaultIconIds.sort(Comparator.nullsLast(Comparator.comparingLong(AttachDomain::getId)));
        return defaultIconIds.get(teamCount % defaultIconIds.size()).getId();
    }

    /**
     * @see WorkspaceService#getPersonalIconId()
     */
    public Long getPersonalIconId() {
        AttachRelateQuery attachRelateQuery = new AttachRelateQuery();
        attachRelateQuery.setBizTypeEnum(AttachBizTypeEnum.WORKSPACE_PERSONAL_ICON);

        List<AttachDomain> defaultIconIds = attachRelateService.listAttaches(attachRelateQuery);
        if (CollectionUtils.isEmpty(defaultIconIds)) {
            logger.error("系统未设置个人工作空间默认头像!!");
            return null;
        }

        return defaultIconIds.get(SystemConstants.FIRST_INDEX).getId();
    }

    /**
     * @see WorkspaceService#countJoinedWorkspaces(Long)
     */
    @Override
    public int countJoinedWorkspaces(Long accountId) {
        if (accountId == null) {
            return 0;
        }

        return workspaceMapper.getWorkspaceIdsByAccountId(accountId).size();
    }

    /**
     * @see WorkspaceService#getWorkspaceMember(Long, Long)
     */
    @Override
    public WorkspaceMemberDetailDomain getWorkspaceMember(Long workspaceId, Long accountId) {
        if (workspaceId == null || accountId == null) {
            return null;
        }

        List<WorkspaceAccountDTO> accountDTOs = workspaceMapper.getUserWorkSpaceRoles(workspaceId, accountId);
        if (CollectionUtils.isEmpty(accountDTOs)) {
            return null;
        }

        // 生成 accountId -> List<RoleEnum> 映射关系
        Map<Long, List<RoleEnum>> roleMap = genRoleMap(accountDTOs);

        WorkspaceMemberDetailDomain workspaceMember = new WorkspaceMemberDetailDomain();
        workspaceMember.setAccount(accountService.read(accountId));
        workspaceMember.setRoles(roleMap.getOrDefault(accountId, new ArrayList<>()));

        Date joinDate = accountDTOs.stream()
                .map(WorkspaceAccountDTO::getCreateTime)
                .min(Date::compareTo)
                .get();
        workspaceMember.setJoinDate(joinDate);

        return workspaceMember;
    }


    /**
     * @see WorkspaceService#countOwnedWorkspaces(Long, WorkSpaceTypeEnum)
     */
    public int countOwnedWorkspaces(Long accountId, WorkSpaceTypeEnum type) {
        if (accountId == null) {
            return 0;
        }

        WorkspaceExample example = new WorkspaceExample();
        WorkspaceExample.Criteria workspaceCriteria = example.createCriteria().andOwnerIdEqualTo(accountId);
        if (type != null) {
            workspaceCriteria.andTypeEqualTo(type.getCode());
        }

        return workspaceMapper.countByExample(example);
    }

    /**
     * @see WorkspaceService#getAllWorkspaceMemberIds(Long)
     */
    @Override
    public Map<Long, Map<String, String>> getExtParamsByIds(List<Long> workspaceIds) {
        List<WorkspaceDomain> workspaceDomains = batchRead(workspaceIds);

        if (CollectionUtils.isEmpty(workspaceDomains)) {
            return new HashMap();
        }

        // 获取当前默认贷款设置
        Map<String, String> loanConfig = systemParamService.getParamsByType(SystemParamTypeEnum.LOAN_CONFIG);

        // 放入到对应的Map中
        Map<Long, Map<String, String>> extParamMap = new HashMap(workspaceDomains.size());
        workspaceDomains.forEach(workspaceDomain -> extParamMap.put(workspaceDomain.getId(), setExtParam(workspaceDomain, loanConfig)));

        return extParamMap;
    }

    /**
     * @see WorkspaceService#getAccountWorkspaceIdMap(Set)
     */
    @Override
    public Map<Long, List<Long>> getAccountWorkspaceIdMap(Set<Long> accountIdSet) {
        if (CollectionUtils.isEmpty(accountIdSet)) {
            return new HashMap<>();
        }

        // 设置默认值
        Map<Long, List<Long>> accountWorkspaceIdMap = new HashMap<>(accountIdSet.size());
        accountIdSet.forEach(accountId -> accountWorkspaceIdMap.put(accountId, new ArrayList<>()));

        // 获取查询结果
        List<AccountWorkspaceListDTO> accountWorkspaceListDTOs = workspaceMapper.getAccountWorkspaceIds(accountIdSet);

        // 处理结果
        accountWorkspaceListDTOs.forEach(accountWorkspaceListDTO -> {
            accountWorkspaceIdMap.get(accountWorkspaceListDTO.getAccountId()).add(accountWorkspaceListDTO.getWorkspaceId());
        });

        return accountWorkspaceIdMap;
    }

    // ~ Private method

    /**
     * 生成默认的 workspace 贷款设置
     *
     * @return
     */
    private Map<String, String> generateDefaultLoanSetting() {
        Map<String, String> loanSetting = new HashMap<>();

        loanSetting.put(SystemConstants.OVER_DUE_DAY, SystemConstants.DEFAULT_OVER_DUE_DAY);
        loanSetting.put(SystemConstants.BAD_LOAN_DAY, SystemConstants.DEFAULT_BAD_LOAN_DAY);

        return loanSetting;
    }

    /**
     * 根据loanConfig里的设置, 设置workspace的额外设置信息
     *
     * @param workspaceDomain
     * @param loanConfig
     * @return
     */
    private Map<String, String> setExtParam(WorkspaceDomain workspaceDomain, Map<String, String> loanConfig) {
        // 获取当前workspace设置
        Map<String, String> extParam = workspaceDomain.getExtParams();

        // 设置合同逾期和坏账
        if (extParam == null) {
            extParam = new HashMap<>();
        }

        extParam.putIfAbsent(SystemConstants.BAD_LOAN_DAY, loanConfig.getOrDefault(SystemConstants.BAD_LOAN_DAY, SystemConstants.DEFAULT_BAD_LOAN_DAY));
        extParam.putIfAbsent(SystemConstants.OVER_DUE_DAY, loanConfig.getOrDefault(SystemConstants.OVER_DUE_DAY, SystemConstants.DEFAULT_OVER_DUE_DAY));

        return extParam;
    }

    /**
     * 对团队成员按照 所有者 管理员 成员 进行排序
     *
     * @param members
     */
    private void sortWorkspaceMembers(List<WorkspaceMemberListDomain> members) {
        if (CollectionUtils.isEmpty(members)) {
            return;
        }

        // 先将角色按照order排序
        members.forEach(member -> {
            if (CollectionUtils.isNotEmpty(member.getRoles())) {
                sortRoles(member.getRoles());
            }
        });

        members.sort(Comparator.nullsLast((first, second) ->
                first.getRoles().get(SystemConstants.FIRST_INDEX).getOrder().compareTo(second.getRoles().get(SystemConstants.FIRST_INDEX).getOrder())
        ));
    }

    /**
     * 根据用户在工作空间的角色,生成 accountId -> List<accountRole>的映射关系
     *
     * @param workspaceAccounts
     * @return
     */
    private Map<Long, List<RoleEnum>> genRoleMap(List<WorkspaceAccountDTO> workspaceAccounts) {
        Map<Long, List<RoleEnum>> accountRoleMap = new HashMap<>(workspaceAccounts.size());

        workspaceAccounts.forEach(accountDTO -> {
            RoleEnum role = RoleEnum.getEnumByCode(accountDTO.getRole());
            Long accountId = accountDTO.getAccountId();
            if (role != null) {
                if (accountRoleMap.containsKey(accountId)) {
                    accountRoleMap.get(accountId).add(role);
                } else {
                    List<RoleEnum> list = new ArrayList<>();
                    list.add(role);
                    accountRoleMap.put(accountId, list);
                }
            }
        });

        // roles排序
        accountRoleMap.forEach((accountId, roles) -> sortRoles(roles));

        return accountRoleMap;
    }

    /**
     * role排序
     *
     * @param roles
     */
    private void sortRoles(List<RoleEnum> roles) {
        roles.sort(Comparator.nullsLast(Comparator.comparing(RoleEnum::getOrder)));
    }
}
