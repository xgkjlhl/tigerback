/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.social.auth;

import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;
import tiger.core.domain.AccountBindDomain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:12 PM yiliang.gyl Exp $
 */
public class QQMobileAuth extends TemplateAuth{

    public QQMobileAuth(String appId, String secret, String redirectUrl) {
        super(appId, secret, redirectUrl);
    }

    private static final String LOGIN_URL = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%s&scope=get_user_info";

    private static final String AUTH_TOKEN_URL = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&state=%s&redirect_uri=%s";

    private static final String USER_INFO_URL = "https://graph.qq.com/user/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";

    /**
     * @see TemplateAuth#getAuthUrl(String)
     */
    @Override
    public String getAuthUrl(String additionCode) {
        try {
            return String.format(LOGIN_URL, this.appId, URLEncoder.encode(this.redirectUrl, "UTF-8"), additionCode);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * @see TemplateAuth#getAuthUserInfo(String, String)
     */
    @Override
    public AccountBindDomain getAuthUserInfo(String token, String openId) {
        try {
            AccountBindDomain accountBindDomain = new AccountBindDomain();
            accountBindDomain.setAccessToken(token);
            accountBindDomain.setOpenId(openId);

            //2. 通过token和 open id获取用户信息
            String userInfoUrl = String.format(USER_INFO_URL, accountBindDomain.getAccessToken(), this.appId,
                    accountBindDomain.getOpenId());
            OAuthRequest userInfoRequest = new OAuthRequest(Verb.GET, userInfoUrl);
            Response userInfoResponse = userInfoRequest.send();
            JSONObject userInfoJson = new JSONObject(userInfoResponse.getBody());

            accountBindDomain.setAccountName(userInfoJson.getString("nickname"));
            accountBindDomain.setIconUrl(userInfoJson.getString("figureurl_qq_1"));
            return accountBindDomain;
        } catch (Exception e) {
            logger.error("获取第三方用户信息失败, openId: [" + openId + "]");
            return null;
        }
    }

    /**
     * 获取跳转认证的url
     *
     * @param code
     * @param state
     * @return
     */
    private String getAuthTokenUrl(String code, String state) {
        try {
            return String.format(AUTH_TOKEN_URL, this.appId, this.secret, code, state, URLEncoder.encode(this.redirectUrl, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }

    }


}
