/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.common.util.StringUtil;
import tiger.core.constants.SystemConstants;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.SystemParamService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 渲染和跳转前端界面的中间控制器
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 2:07 PM yiliang.gyl Exp $
 */
@Controller
public class IndexController {

    @Autowired
    SystemParamService systemParamService;

    @RequestMapping(value = "social_auth", method = RequestMethod.GET)
    public ModelAndView socialAuthRedirectUrl(HttpServletRequest request,
                                              HttpServletResponse response) {
        String baseUrl = systemParamService.getValueByTypeAndKey(SystemParamTypeEnum.SOCIAL_CONFIG,
                SystemConstants.SOCIAL_AUTH_URL);
        if (StringUtil.isBlank(baseUrl)) {
            throw new TigerException(ErrorCodeEnum.SYSTEM_EXCEPTION, "系统错误，参数为配置，请联系管理员!");
        }
        //1. 把之前所有的params拼接在一起
        String redirectUrl = new StringBuffer(baseUrl).append("?").append(request.getQueryString()).toString();

        //2. 跳转到配置的接口
        return new ModelAndView("redirect:" + redirectUrl);
    }

}
