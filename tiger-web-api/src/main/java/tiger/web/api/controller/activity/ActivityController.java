/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tiger.biz.activity.support.ActivityManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.query.ActivityQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.workspace.ActivityDomain;
import tiger.web.api.controller.base.BaseController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author mi.li
 * @version v 0.1 16/3/16 下午7:07 mi.li Exp $
 */
@RestController
@RequestMapping("/activity")
public class ActivityController extends BaseController {

    @Autowired
    ActivityManager activityManager;

    @RequestMapping(value = "/activities", method = RequestMethod.GET, params = "scope=list")
    @ResponseBody
    @LoginRequired
    public PageResult<List<ActivityDomain>> allActivities(@Valid ActivityQuery activityQuery,
                                                          BindingResult bingdingResult) {
        activityQuery.setWorkspaceId(currentWorkspaceId());

        return activityManager.listActivities(activityQuery);
    }

}
