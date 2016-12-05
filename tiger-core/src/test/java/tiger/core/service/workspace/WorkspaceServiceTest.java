/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.workspace;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.enums.RoleEnum;
import tiger.common.dal.enums.WorkSpaceTypeEnum;
import tiger.core.AbstractCoreTests;
import tiger.core.domain.AccountBaseDomain;
import tiger.core.domain.workspace.AccountWorkspaceDomain;
import tiger.core.domain.workspace.WorkspaceDomain;
import tiger.core.domain.workspace.WorkspaceMemberListDomain;

import java.util.List;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 4:34 PM yiliang.gyl Exp $
 */
public class WorkspaceServiceTest extends AbstractCoreTests {

    private static Logger logger = Logger.getLogger(WorkspaceServiceTest.class);

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 测试增删改查
     */
    @Test
    public void insertAndDeleteTest() {

        WorkspaceDomain newWorkspace = createWorkspace();

        Assert.assertNotNull(newWorkspace);
        Assert.assertNotNull(newWorkspace.getId());

        logger.info("插入成功:[" + newWorkspace + "]");

        Long id = newWorkspace.getId();

        //~ 测试更新
        newWorkspace.setDescription("test 222");
        Boolean putRc = workspaceService.update(newWorkspace);
        Assert.assertEquals(putRc, true);
        logger.info("更新记录 [" + id + "] 成功");


        //~ 测试删除
        Boolean rc = workspaceService.delete(newWorkspace.getId());
        Assert.assertEquals(rc, true);
        logger.info("删除插入的记录:[" + id + "] 结果:" + rc);

    }

    /**
     * 测试添加和删除用户
     */
    @Test
    public void testUserOperation() {
        WorkspaceDomain workspace = createWorkspace();
        Long workspaceId = workspace.getId();

        AccountWorkspaceDomain accountWorkspace = new AccountWorkspaceDomain();
        accountWorkspace.setAccountId(1003l);
        accountWorkspace.setWorkspaceId(workspace.getId());
        accountWorkspace.getRoles().add(RoleEnum.ADMIN);

        //插入一个管理员
        Boolean rc = workspaceService.addUser(accountWorkspace);

        logger.info("添加用户结果: [" + rc + "]");

        //获取workspace的用户
        List<WorkspaceMemberListDomain> members = workspaceService.getAllWorkspaceMember(workspace.getId());

        logger.info("项目共[" + members.size() + "] 个用户");

        //判断是否是所有者
        logger.info("是否是所有者: " + workspaceService.isOwner(workspaceId, 1000l));

        //是否存在
        logger.info("是否存在: " + workspaceService.isExist(workspaceId));

        logger.info("修改项目所有者: " + workspaceService.transferWorkspace(workspaceId, 1000l, 1003l));


        //删除用户
        if (CollectionUtils.isNotEmpty(members)) {
            AccountBaseDomain user = members.get(0).getAccount();
            Boolean removeRc = workspaceService.removeUser(workspaceId, user.getId());

            logger.info("从项目中移除客户: " + removeRc);
        }

    }


    /**
     * 测试获取用户所有workspace和权限的接口
     *
     * @return
     */
    @Test
    public void testGetUserWorkspace() {
        List<AccountWorkspaceDomain> result = workspaceService.getUserWorkspaces(1000l);
        logger.info("获取到用户团队和权限: [" + result + "]");
    }

    /**
     * @return
     */
    private WorkspaceDomain createWorkspace() {
        WorkspaceDomain workspace = new WorkspaceDomain();
        workspace.setType(WorkSpaceTypeEnum.PERSONAL);
        workspace.setIsVerified(false);
        workspace.setDescription("test");
        workspace.setName("test");
        workspace.setOwnerId(1000l);

        WorkspaceDomain newWorkspace = workspaceService.create(workspace);
        Assert.assertNotNull(newWorkspace);
        Assert.assertNotNull(newWorkspace.getId());

        logger.info("插入成功:[" + newWorkspace + "]");

        return newWorkspace;
    }


}
