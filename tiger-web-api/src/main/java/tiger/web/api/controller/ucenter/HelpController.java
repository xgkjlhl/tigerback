/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.ucenter;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.annotation.Permission;
import tiger.common.dal.query.HelpQuery;
import tiger.common.util.StringUtil;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.UcenterHelpCatalogDomain;
import tiger.core.domain.UcenterHelpDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.service.UcenterHelpService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.ucenter.HelpForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 客户帮助类接口
 *
 * @author mi.lim
 * @version v 0.1 2015年9月28日 下午8:21:25 mi.li Exp $
 */
@RestController
@ResponseBody
public class HelpController extends BaseController {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static Logger logger = Logger.getLogger(PostController.class);
    @Autowired
    UcenterHelpService ucenterHelpService;

    /**
     * 获取所有常用帮助
     *
     * @return
     */
    @RequestMapping(value = "/ucenter/commonHelps", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<List<UcenterHelpDomain>> commonHelps() {
        if (logger.isInfoEnabled()) {
            logger.info("获取所有常用帮助");
        }
        List<UcenterHelpDomain> list = ucenterHelpService.getCommonHelps();

        return new BaseResult<>(list);
    }

    /**
     * 获取所有helps，或指定catalog下地所有helps
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/helps", method = RequestMethod.GET, params = "scope=all")
    @ResponseBody
    public BaseResult<List<UcenterHelpDomain>> allHelps(HttpServletRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info("获取所有helps");
        }
        Long catalogId = null;
        if (StringUtil.isNotBlank(request.getParameter("catalogId"))) {
            catalogId = Long.parseLong(request.getParameter("catalogId"));
        }
        return new BaseResult<>(ucenterHelpService.getAllHelps(catalogId));
    }

    /**
     * 分页获取所有helps，或指定catalog下地所有helps
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/helps", method = RequestMethod.GET, params = "scope=list")
    @ResponseBody
    public PageResult<List<UcenterHelpDomain>> listHelps(HttpServletRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info("分页获取所有helps");
        }
        HelpQuery hq = new HelpQuery();
        hq.setPageSize(DEFAULT_PAGE_SIZE);
        if (StringUtil.isNotBlank(request.getParameter("pageNum"))) {
            hq.setPageNum(Integer.parseInt(request.getParameter("pageNum")));
        }
        if (StringUtil.isNotBlank(request.getParameter("catalogId"))) {
            hq.setCatalogId(Long.parseLong(request.getParameter("catalogId")));
        }

        return ucenterHelpService.getListHelps(hq);
    }

    /**
     * 根据id获取帮助
     *
     * @param helpId
     * @return
     */
    @RequestMapping(value = "ucenter/help/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<UcenterHelpDomain> getHelpById(@PathVariable("id") long helpId) {
        if (logger.isInfoEnabled()) {
            logger.info("获取帮助 [" + helpId + "]");
        }
        UcenterHelpDomain domain = ucenterHelpService.getHelpById(helpId);
        //获取catalog name
        String catalogName = ucenterHelpService.getCatalogById(domain.getCatalogId()).getName();
        domain.setCatalogName(catalogName);
        return new BaseResult<>(domain);
    }

    /**
     * 获取所有catalogs
     *
     * @return
     */
    @RequestMapping(value = "/ucenter/catalogs", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<List<UcenterHelpCatalogDomain>> allCatalogs() {
        if (logger.isInfoEnabled()) {
            logger.info("获取所有catalogs");
        }
        return ucenterHelpService.getAllCatalogs();
    }

    /**
     * 获取某一catalog
     *
     * @return
     */
    @RequestMapping(value = "/ucenter/catalog/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<UcenterHelpCatalogDomain> getCatalog(@PathVariable("id") long catalogId) {
        if (logger.isInfoEnabled()) {
            logger.info("获取catalog [" + catalogId + "]");
        }
        UcenterHelpCatalogDomain domain = ucenterHelpService.getCatalogById(catalogId);
        return new BaseResult<UcenterHelpCatalogDomain>(domain);
    }

    /**
     * 搜索help
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/ucenter/search", method = RequestMethod.GET, params = "scope=help")
    @ResponseBody
    @LoginRequired
    public BaseResult<List<UcenterHelpDomain>> searchHelp(HttpServletRequest request) {
        if (!StringUtil.isNotBlank(request.getParameter("keyword")))
            return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL);

        String keyword = request.getParameter("keyword");

        if (logger.isInfoEnabled()) {
            logger.info("搜索help [" + keyword + "]");
        }

        return ucenterHelpService.searchHelp(keyword);
    }

    // ~ private methods

    /**
     * Check return code.
     *
     * @param rc the rc
     * @return true, if successful
     */
    private BaseResult<Object> checkReturnCode(int rc) {
        if (rc > 0) {
            return new BaseResult<>(true);
        } else {
            return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL, false);
        }
    }

    // ~ manager

    /**
     * 新增帮助
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/ucenter/help", method = RequestMethod.POST)
    @ResponseBody
    @Permission
    public BaseResult<UcenterHelpDomain> addHelp(@Valid @RequestBody HelpForm form, BindingResult bindingResult) {
        UcenterHelpDomain domain = form.convert2Domain();

        if (null == domain.getIsCommon())
            domain.setIsCommon(false);
        else
            domain.setIsCommon(domain.getIsCommon());

        Long catalogId = domain.getCatalogId();
        //默认类别为1
        if (null == catalogId)
            domain.setCatalogId((long) 1);
        //检查catalogId是否存在
        ucenterHelpService.getCatalogById(domain.getCatalogId());

        UcenterHelpDomain ucenterHelpDomain = ucenterHelpService.addHelp(domain);
        return new BaseResult<>(ucenterHelpDomain);
    }

    /**
     * 根据id更新帮助
     *
     * @param form
     * @param bindingResult
     * @param id
     * @return
     */
    @RequestMapping(value = "ucenter/help/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Permission
    public BaseResult<Object> updateHelpById(@RequestBody @Valid HelpForm form,
                                             BindingResult bindingResult,
                                             @PathVariable("id") long id) {
        if (bindingResult.hasErrors()) {
            return new BaseResult<Object>(ErrorCodeEnum.ILLEGAL_PARAMETER,
                    bindingResult.getAllErrors());
        }
        UcenterHelpDomain domain = ucenterHelpService.getHelpById(id);
        domain.setTitle(form.getTitle());
        domain.setContent(form.getContent());
        if (null != form.getCatalogId()) {
            //检查catalogId是否存在
            ucenterHelpService.getCatalogById(form.getCatalogId());
            domain.setCatalogId(form.getCatalogId());
        }

        int rc = ucenterHelpService.updateHelp(domain);
        return checkReturnCode(rc);
    }

    /**
     * 根据id删除帮助
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "ucenter/help/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Permission
    public BaseResult<Object> deleteHelpById(@PathVariable("id") long id) {
        int rc = ucenterHelpService.deleteHelp(id);
        return checkReturnCode(rc);
    }

    /**
     * 将一个帮助设为常用
     *
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "ucenter/help/{id}/common", method = RequestMethod.PUT)
    @ResponseBody
    @Permission
    public BaseResult<Object> commonHelpById(@PathVariable("id") long id, HttpServletRequest request) {
        UcenterHelpDomain domain = ucenterHelpService.getHelpById(id);
        if (!StringUtil.isNotBlank(request.getParameter("isCommon"))) {
            return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL);
        }
        domain.setIsCommon(Boolean.parseBoolean(request.getParameter("isCommon")));
        int rc = ucenterHelpService.updateHelp(domain);
        return checkReturnCode(rc);
    }
}
