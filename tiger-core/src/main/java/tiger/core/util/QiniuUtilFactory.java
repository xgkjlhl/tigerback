/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.util;

import org.apache.log4j.Logger;
import tiger.common.dal.enums.AttachTypeEnum;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.common.util.component.qiniu.*;
import tiger.core.service.SystemParamService;

import java.util.Map;

/**
 * QiniuUtil工厂
 *
 * @author alfred.yx
 * @version $ID: v 0.1 下午5:27 alfred.yx Exp $
 */
public class QiniuUtilFactory {
    private QiniuUtilFactory() {
    }

    private static final Logger LOGGER = Logger.getLogger(QiniuUtilFactory.class);

    /**
     * 获取QiniuUtil实例
     *
     * @param attachType
     * @param systemParamService
     * @return
     */
    public static QiniuUtil getQiniuUtilInstance(AttachTypeEnum attachType, SystemParamService systemParamService) {
        QiniuUtil qiniuUtil;
        if (EhcacheUtil.contains(attachType)) {
            if ((qiniuUtil = (QiniuUtil) EhcacheUtil.get(attachType)) != null) {
                return qiniuUtil;
            }
        }

        QiniuBaseHandler handler;
        Map<String, String> configMap;

        switch (attachType) {
            case PUBLIC:
                configMap = systemParamService.getParamsByType(SystemParamTypeEnum.QINIU_PUBLIC_PARAM);
                checkQiniuParam(configMap);
                handler = new QiniuPublicHandler(QiniuHelper.getAuth(configMap));
                break;
            case SECRET:
                configMap = systemParamService.getParamsByType(SystemParamTypeEnum.QINIU_SECRET_PARAM);
                checkQiniuParam(configMap);
                handler = new QiniuSecretHandler(QiniuHelper.getAuth(configMap));
                break;
            default:
                return null;
        }

        qiniuUtil = new QiniuUtil(configMap, handler);
        EhcacheUtil.put(attachType, qiniuUtil);
        return qiniuUtil;
    }

    /**
     * 检查系统配置
     *
     * @param config
     */
    private static void checkQiniuParam(Map<String, String> config) {
        if (!config.containsKey("ACCESS_KEY")
                || !config.containsKey("SECRET_KEY")
                || !config.containsKey("BUCKET")
                || !config.containsKey("CALLBACK_URL")
                || !config.containsKey("QINIU_DOMAIN_URL")
                ) {
            LOGGER.error("非法的七牛配置参数, 系统配置为 [" + config + "]");
            throw new IllegalArgumentException("非法的七牛配置参数");
        }
    }
}
