/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tiger.biz.market.support.MarketProjectManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.core.base.BaseResult;
import tiger.web.api.controller.base.BaseController;

/**
 * 用户对于市场项目的行为
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 7:34 PM yiliang.gyl Exp $
 */
@RestController
@ResponseBody
@RequestMapping(value = "market")
public class MarketProjectActionController extends BaseController {

    @Autowired
    MarketProjectManager marketProjectManager;

    /**
     * 点赞
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/project/{projectId}/awesome", method = RequestMethod.POST)
    @LoginRequired
    public BaseResult<Integer> awesomeProject(@PathVariable("projectId") Long projectId) {
        return marketProjectManager.awesomeProject(currentAccount().getId(), projectId);
    }

    /**
     * 收藏
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/project/{projectId}/collection", method = RequestMethod.POST)
    @LoginRequired
    public BaseResult<Boolean> collectProject(@PathVariable("projectId") Long projectId) {
        return marketProjectManager.collectProject(currentAccount().getId(), projectId);
    }

    /**
     * 取消点赞
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/project/{projectId}/awesome", method = RequestMethod.DELETE)
    @LoginRequired
    public BaseResult<Integer> deAwesomeProject(@PathVariable("projectId") Long projectId) {
        return marketProjectManager.deAwesomeProject(currentAccount().getId(), projectId);
    }

    /**
     * 取消收藏
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/project/{projectId}/collection", method = RequestMethod.DELETE)
    @LoginRequired
    public BaseResult<Boolean> deCollectProject(@PathVariable("projectId") Long projectId) {
        return marketProjectManager.deCollectProject(currentAccount().getId(), projectId);
    }

    /**
     * 分享项目
     * ~ 生成项目共享二维码
     *
     * @return
     */
    @RequestMapping(value = "/project/{projectId}/sharing", method = RequestMethod.GET)
    public BaseResult<Object> shareProject() {
        return null;
    }
}
