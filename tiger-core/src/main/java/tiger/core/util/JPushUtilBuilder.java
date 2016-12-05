/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.util;

import org.apache.log4j.Logger;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.common.util.component.jpush.JPushUtil;
import tiger.core.service.SystemParamService;

import java.util.Map;

/**
 * JPushUtil 工厂
 *
 * @author alfred_yuan
 * @version v 0.1 12:40 alfred_yuan Exp $
 */
public class JPushUtilBuilder {

    private JPushUtilBuilder(){

    }


    private static final Logger logger = Logger.getLogger(JPushUtilBuilder.class);

    /**
     * 获取系统配置,并构造JPushUtil
     *
     * @param systemParamService
     * @return
     */
    public static JPushUtil getJPushUtilInstance(SystemParamService systemParamService) {
        JPushUtil jPushUtil;
        if (EhcacheUtil.contains(SystemParamTypeEnum.JPUSH_PARAM)) {
            if ((jPushUtil = (JPushUtil) EhcacheUtil.get(SystemParamTypeEnum.JPUSH_PARAM)) != null) {
                return jPushUtil;
            }
        }
        Map<String, String> jpushParams = systemParamService.getParamsByType(SystemParamTypeEnum.JPUSH_PARAM);

        jPushUtil = new JPushUtil(jpushParams);
        EhcacheUtil.put(SystemParamTypeEnum.JPUSH_PARAM, jPushUtil);
        return jPushUtil;
    }

}
