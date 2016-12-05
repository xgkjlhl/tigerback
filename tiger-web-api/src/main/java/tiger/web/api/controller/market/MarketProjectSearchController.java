/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.account.support.AccountManager;
import tiger.biz.market.support.MarketProjectSearchManager;
import tiger.common.dal.query.MarketProjectQuery;
import tiger.common.dal.query.TopQuery;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.market.MarketProjectDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.service.market.MarketProjectService;
import tiger.web.api.controller.base.BaseController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 2:14 PM yiliang.gyl Exp $
 */
@RequestMapping(value = "market")
@ResponseBody
@RestController
public class MarketProjectSearchController extends BaseController {

    @Autowired
    private MarketProjectSearchManager marketProjectSearchManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private MarketProjectService marketProjectService;

    /**
     * 获取广场项目
     *
     * @param query
     * @param loginAccount
     * @return
     */
    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public PageResult<List<MarketProjectDomain>> queryProjects(MarketProjectQuery query,
                                                               @RequestParam(value = "loginAccount", required = false) Long loginAccount) {
        return marketProjectSearchManager.queryProject(query, loginAccount);
    }

    /**
     * 获取用户收藏的项目
     *
     * @param account
     * @param query
     * @param loginAccount
     * @return
     */
    @RequestMapping(value = "/projects/userCollection/{account}", method = RequestMethod.GET)
    public PageResult<List<MarketProjectDomain>> getUserCollectedProjects(@PathVariable("account") Long account,
                                                                          MarketProjectQuery query,
                                                                          @RequestParam(value = "loginAccount", required = false) Long loginAccount) {
        if (account == null) {
            return new PageResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL.getCode(), "用户id不能为空");
        }
        query.setAccountId(account);
        return marketProjectSearchManager.queryUserCollectedProjects(query, loginAccount);
    }

    /**
     * 获取热门用户
     *
     * @param topQuery
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/topAccounts", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<List<AccountDomain>> getTopAccounts(@Valid TopQuery topQuery,
                                                          BindingResult bindingResult) {
        List<Long> topAccountIds = marketProjectService.getTopAccountIds(topQuery);

        return new BaseResult<>(accountManager.getAccountsByIds(topAccountIds));
    }

    /**
     * 获取最新项目
     *
     * @param topQuery
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/latestProjects", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<List<MarketProjectDomain>> getLatestProjects(@Valid TopQuery topQuery,
                                                                   BindingResult bindingResult) {
        return new BaseResult<>(marketProjectService.getLatestProjects(topQuery));
    }

}
