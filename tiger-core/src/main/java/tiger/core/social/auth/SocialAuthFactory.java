/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.social.auth;

import tiger.common.dal.enums.AccountSocialTypeEnum;
import tiger.common.util.StringUtil;
import tiger.core.domain.AccountBindDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;

/**
 * 第三方认证工厂类
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 12:25 PM yiliang.gyl Exp $
 */
public class SocialAuthFactory {

    private String appId;

    private String secret;

    private String redirectUrl;

    private TemplateAuth templateAuth;

    public SocialAuthFactory(String appId, String secret, String redirectUrl, AccountSocialTypeEnum socialType) {

        if (StringUtil.isBlank(appId) || StringUtil.isBlank(secret)
                || StringUtil.isBlank(redirectUrl)) {
            throw new TigerException(ErrorCodeEnum.PARAMETERS_IS_NULL, "第三方授权信息缺失");
        }
        this.appId = appId;
        this.secret = secret;
        this.redirectUrl = redirectUrl;

        this.initTemplateAuth(socialType);
    }

    /**
     * 初始化构造对象
     *
     * @param socialType
     */
    private void initTemplateAuth(AccountSocialTypeEnum socialType) {
        if (socialType == AccountSocialTypeEnum.WECHAT
                || socialType == AccountSocialTypeEnum.WECHATMOBILE) {
            this.templateAuth = new WechatAuth(this.appId, this.secret, this.redirectUrl);
        } else if (socialType == AccountSocialTypeEnum.QQ
                ) {
            this.templateAuth = new QQAuth(this.appId, this.secret, this.redirectUrl);
        } else if (socialType == AccountSocialTypeEnum.QQMOBILE ||
                socialType == AccountSocialTypeEnum.QQMOBILEANDROID) {
            this.templateAuth = new QQMobileAuth(this.appId, this.secret, this.redirectUrl);
        } else {
            throw new TigerException(ErrorCodeEnum.ILLEGAL_PARAMETER, "不支持的第三方授权");
        }
    }

    /**
     * 获取跳转Url
     *
     * @param additionCode
     * @return
     */
    public String getRedirectUrl(String additionCode) {
        return templateAuth.getAuthUrl(additionCode);
    }


    /**
     * 获取用户信息
     *
     * @param backCode
     * @return
     */
    public AccountBindDomain getAuthUserInfo(String backCode, String state) {
        return templateAuth.getAuthUserInfo(backCode, state);
    }


}
