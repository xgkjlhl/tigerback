/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util.component.jpush;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author mi.li
 * @version v 0.1 16/4/24 下午9:04 mi.li Exp $
 */
public class JPushUtil {

    // Logger
    private final static Logger logger = Logger.getLogger(JPushUtil.class);

    // Log TAG
    private final static String LOG_TAG = "[ JPUSH ]";
    private static final int MAX_RETRY_TIMES = 3;

    // 常量
    public static final String JPUSH_MASTER_SECRET = "JPUSH_MASTER_SECRET";
    public static final String JPUSH_APP_KEY = "JPUSH_APP_KEY";
    public static final String JPUSH_ALIAS_PREFIX = "JPUSH_ALIAS_PREFIX";

    // alias 前缀
    private final String TIGER_JPUSH_PREFIX;

    // the real JPushClient
    private final JPushClient jpushClient;

    /**
     * 构造函数
     *
     * @param params
     */
    public JPushUtil(Map<String, String> params) {
        checkJPushParam(params);

        // 构造一个最多尝试 MAX_RETRY_TIMES 次的JPushClient
        jpushClient = new JPushClient(params.get(JPUSH_MASTER_SECRET), params.get(JPUSH_APP_KEY), MAX_RETRY_TIMES);
        // 设置alias的前缀
        this.TIGER_JPUSH_PREFIX = params.get(JPUSH_ALIAS_PREFIX);
    }

    /**
     * 发动content给多个aliases
     *
     * @param accountIds
     * @param content
     */
    public void send(List<Long> accountIds, String content) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(generateAccountTokens(accountIds)))
                .setNotification(Notification.alert(content))
                .build();

        sendPayLoad(payload);
    }

    /**
     * 发送content给单个aliases
     *
     * @param accountId
     * @param content
     */
    public void send(Long accountId, String content) {

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(generateAccountToken(accountId)))
                .setNotification(Notification.alert(content))
                .build();

        sendPayLoad(payload);
    }

    /**
     * 推送消息, log结果
     *
     * @param payload
     */
    private void sendPayLoad(PushPayload payload) {
        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info(LOG_TAG + "Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            logger.error(LOG_TAG + "Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            logger.error(LOG_TAG + "Should review the error, and fix the request", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
        } catch (Exception e) {
            logger.error(LOG_TAG + " unknown exception", e);
        }
    }

    /**
     * 根据accountIds生成accountToken List
     *
     * @param accountIds
     * @return
     */
    private List<String> generateAccountTokens(List<Long> accountIds) {
        if (CollectionUtils.isEmpty(accountIds)) {
            return new ArrayList<>();
        }
        List<String> accountTokens = new ArrayList<>(accountIds.size());
        accountIds.forEach(accountId -> accountTokens.add(generateAccountToken(accountId)));
        return accountTokens;
    }

    /**
     * 根据accountIds生成accountToken
     *
     * @param accountId
     * @return
     */
    private String generateAccountToken(Long accountId) {
        if (accountId == null) {
            return null;
        }

        return TIGER_JPUSH_PREFIX + accountId;
    }

    /**
     * 系统参数检查
     *
     * @param config
     */
    private void checkJPushParam(Map<String, String> config) {
        if (!config.containsKey(JPUSH_MASTER_SECRET) ||
                !config.containsKey(JPUSH_APP_KEY) ||
                !config.containsKey(JPUSH_ALIAS_PREFIX)) {
            logger.error("非法的JPUSH配置参数, 系统配置为 [" + config + "]");

            throw new IllegalArgumentException("非法的JPUSH配置参数");
        }
    }

}
