/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.market;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.market.support.RegionManager;
import tiger.common.dal.query.RegionQuery;
import tiger.core.base.BaseResult;
import tiger.core.domain.market.RegionDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.market.RegionService;
import tiger.web.api.constants.APIConstants;

import javax.validation.Valid;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 21:58 alfred_yuan Exp $
 */
@RestController
@RequestMapping(value = "market")
public class RegionController {

    Logger logger = Logger.getLogger(RegionController.class);

    @Autowired
    private RegionService regionService;

    @Autowired
    private RegionManager regionManager;

    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<List<RegionDomain>> listAllRegions(@Valid RegionQuery query,
                                                        BindingResult bindingResult) {
        if (logger.isInfoEnabled()) {
            logger.info("查询所有地区编码, 参数为 ["+ query +"]");
        }
        checkQueryValidity(query);

        return new BaseResult<>(regionService.queryPackedRegions(query));
    }

    @RequestMapping(value = "/region/support", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<List<RegionDomain>> listSupportedRegions() {

        return new BaseResult<>(regionService.getSupportedRegions());
    }

    @RequestMapping(value = "/region/support/QRCode", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<RegionDomain> listSupportedRegionQRCodes(@RequestParam(APIConstants.PARAM_CODE) String code) {

        return regionManager.listSupportedRegionQRCodes(code);
    }

    /**
     * query合法的情况为id,code,和name三者中有且只能有一个非空
     * @param query
     */
    private void checkQueryValidity(RegionQuery query) {
        int notNullSum = 0;
        if (CollectionUtils.isNotEmpty(query.getIds())) {
            notNullSum += 1;
        }
        if (CollectionUtils.isNotEmpty(query.getCodes())) {
            notNullSum += 1;
        }
        if (CollectionUtils.isNotEmpty(query.getNames())) {
            notNullSum += 1;
        }
        if (notNullSum == 1) {
            return ;
        }
        logger.error("请求参数非法, 参数为 ["+ query + "]");
        throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER, "现在仅支持根据id,code,和name中的一种来进行搜索");
    }
}
