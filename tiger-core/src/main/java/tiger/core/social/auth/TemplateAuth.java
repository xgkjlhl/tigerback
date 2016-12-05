/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.social.auth;

import org.apache.log4j.Logger;
import tiger.core.domain.AccountBindDomain;

/**
 * 第三方抽象模板类
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 11:54 AM yiliang.gyl Exp $
 */
public abstract class TemplateAuth {

    protected Logger logger = Logger.getLogger(TemplateAuth.class);

    protected String appId;

    protected String secret;

    protected String redirectUrl;

    /**
     * 构造方法
     *
     * @param appId
     * @param secret
     * @param redirectUrl
     */
    public TemplateAuth(String appId, String secret, String redirectUrl) {
        this.appId = appId;
        this.secret = secret;
        this.redirectUrl = redirectUrl;
    }

    /**
     * 获取认证URL
     *
     * @param additionCode
     * @return
     */
    public String getAuthUrl(String additionCode) {
        return null;
    }



    /**
     * 根据OAuth的反馈code获取用户信息
     *
     * @param backCode
     * @return
     */
    public AccountBindDomain getAuthUserInfo(String backCode, String state) {
        return null;
    }


}
