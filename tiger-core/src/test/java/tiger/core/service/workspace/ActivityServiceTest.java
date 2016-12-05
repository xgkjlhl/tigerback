/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.workspace;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tiger.common.dal.enums.BizObjectTypeEnum;
import tiger.common.dal.query.ActivityQuery;
import tiger.core.AbstractCoreTests;
import tiger.core.domain.workspace.ActivityDomain;

import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 16/3/16 下午7:07 mi.li Exp $
 */
public class ActivityServiceTest extends AbstractCoreTests {

    private static Logger logger = Logger.getLogger(ActivityServiceTest.class);

    @Autowired
    private ActivityService activityService;

    /**
     * 测试增删
     */
    @Test
    public void insertAndDeleteTest() {

        //~ 测试插入
        ActivityDomain activityDomain = createActivity();

        Assert.assertNotNull(activityDomain);
        Assert.assertNotNull(activityDomain.getId());
        Assert.assertNotNull(activityDomain.getOperationMessage());
        Assert.assertNotNull(activityDomain.getOperationParams());

        logger.info("插入成功:[" + activityDomain + "]");

        Long id = activityDomain.getId();

        //~ 测试删除
        Boolean rc = activityService.delete(id);

        Assert.assertEquals(rc, true);

        logger.info("删除插入的记录:[" + id + "] 结果:" + rc);

    }

    /**
     * 测试按条件查询
     */
    @Test
    public void listSelective() {

        ActivityDomain activityDomain = createActivity();

        ActivityQuery activityQuery = new ActivityQuery();
        activityQuery.setWorkspaceId(activityDomain.getWorkspaceId());
        activityQuery.setLoanId(activityDomain.getObjectId());
        activityQuery.setOperatorId(activityDomain.getOperatorId());

        List<ActivityDomain> result = activityService.listActivities(activityQuery).getData();

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.get(0).getId());

        logger.info("按条件查询成功:[" + result + "]");

    }

    private ActivityDomain createActivity(){
        ActivityDomain activityDomain = new ActivityDomain();
        activityDomain.setObjectId((long) 1);
        activityDomain.setWorkspaceId((long) 1);
        activityDomain.setOperatorId((long) 2);
        activityDomain.setObjectType(BizObjectTypeEnum.LOAN);
       // activityDomain.setOperation(ActivityOperationTypeEnum.LOAN_PAY);
        String[] optParams = {"1","2"};
        activityDomain.setOperationParams(optParams);

        ActivityDomain newActivityDomain = activityService.insert(activityDomain);
        Assert.assertNotNull(newActivityDomain);
        Assert.assertNotNull(newActivityDomain.getId());

        logger.info("插入成功:[" + newActivityDomain + "]");
        return newActivityDomain;
    }


}
