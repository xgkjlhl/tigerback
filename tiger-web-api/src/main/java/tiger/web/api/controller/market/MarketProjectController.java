/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.market.support.MarketProjectSearchManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.core.base.BaseResult;
import tiger.core.domain.market.MarketProjectDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.market.MarketProjectService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.market.ProjectCreateForm;

import javax.validation.Valid;

/**
 * 市场项目管理接口
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 2:14 PM yiliang.gyl Exp $
 */
@RestController
@RequestMapping(value = "market")
public class MarketProjectController extends BaseController {

    @Autowired
    private MarketProjectService marketProjectService;

    @Autowired
    private MarketProjectSearchManager marketProjectSearchManager;


    /**
     * 新建项目
     *
     * @param projectCreateForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<MarketProjectDomain> createMarketProject(@RequestBody @Valid ProjectCreateForm projectCreateForm,
                                                               BindingResult bindingResult) {
        MarketProjectDomain domain = projectCreateForm.convert2Domain();
        checkBackPercent(domain);
        domain.setAccountId(currentAccount().getId());
        return new BaseResult<>(marketProjectService.create(domain));
    }

    /**
     * 删除项目
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.DELETE)
    @ResponseBody
    @LoginRequired
    public BaseResult<Boolean> deleteProject(@PathVariable("projectId") Long projectId) {
        checkIsOwner(projectId);
        if (marketProjectService.delete(projectId)) {
            return new BaseResult<>(true);
        }
        return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL.getCode(), "删除项目失败,数据库错误!");
    }

    /**
     * 更新项目
     *
     * @param projectCreateForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<MarketProjectDomain> updateProject(@PathVariable("projectId") Long projectId,
                                                         @RequestBody @Valid ProjectCreateForm projectCreateForm,
                                                         BindingResult bindingResult) {
        checkIsOwner(projectId);
        MarketProjectDomain domain = projectCreateForm.convert2Domain();
        checkBackPercent(domain);
        domain.setId(projectId);
        if (marketProjectService.update(domain)) {
            return new BaseResult<>(marketProjectSearchManager.readById(projectId, currentAccount().getId()));
        }
        return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL.getCode(), "更新项目失败");
    }

    /**
     * 通过 id 获取项目
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "/project/{projectId}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<MarketProjectDomain> getById(@PathVariable("projectId") Long projectId,
                                                   @RequestParam(value = "loginAccount", required = false) Long loginAccount) {
        MarketProjectDomain domain = marketProjectSearchManager.readById(projectId, loginAccount);
        if (domain == null) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND.getCode(), "没有找到项目");
        } else {
            return new BaseResult<>(domain);
        }
    }

    // ~ private methods

    private void checkIsOwner(Long projectId) {
        if (!marketProjectService.isOwner(projectId, currentAccount().getId())) {
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED, "你没有权限进行该操作");
        }
    }

    private void checkBackPercent(MarketProjectDomain domain) {
        if (domain.getHasBack()) {
            if (domain.getMinBackPercent() == null || domain.getMaxBackPercent() == null) {
                throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL, "请设置返点金额");
            }
            if (domain.getMinBackPercent() > domain.getMaxBackPercent()) {
                throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE, "返点金额设置错误");
            }
        } else {
            domain.setMaxBackPercent(0.0);
            domain.setMinBackPercent(0.0);
        }
    }
}
