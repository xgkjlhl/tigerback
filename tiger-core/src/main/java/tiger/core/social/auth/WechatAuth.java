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
 * 微信认证服务实现
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 12:01 PM yiliang.gyl Exp $
 */
public class WechatAuth extends TemplateAuth {

    public WechatAuth(String appId, String secret, String redirectUrl) {
        super(appId, secret, redirectUrl);
    }

    private static final String LOGIN_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    private static final String AUTH_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    /**
     * @see TemplateAuth#getAuthUrl(String)
     */
    @Override
    public String getAuthUrl(String additionCode) {
        try {
            return String.format(LOGIN_URL, this.appId, URLEncoder.encode(this.redirectUrl, "UTF-8"), additionCode);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @see TemplateAuth#getAuthUserInfo(String, String)
     */
    @Override
    public AccountBindDomain getAuthUserInfo(String backCode, String state) {
        //1. 通过code去获取微信用户信息
        String url = String.format(AUTH_URL, this.appId, this.secret, backCode);
        OAuthRequest request = new OAuthRequest(Verb.GET, url);

        Response response = request.send();
        String responseBody = response.getBody();
        JSONObject jsonObject = new JSONObject(responseBody);

        try {
            AccountBindDomain accountBindDomain = new AccountBindDomain();
            accountBindDomain.setOpenId(jsonObject.getString("openid"));
            accountBindDomain.setAccessToken(jsonObject.getString("access_token"));

            //2. 根据token 和openId 获取用户信息
            String userInfoUrl = String.format(USER_INFO_URL, accountBindDomain.getAccessToken(),
                    accountBindDomain.getOpenId());
            OAuthRequest userInfoRequest = new OAuthRequest(Verb.GET, userInfoUrl);
            Response userInfoResponse = userInfoRequest.send();
            JSONObject userInfoJson = new JSONObject(userInfoResponse.getBody());

            accountBindDomain.setIconUrl(userInfoJson.getString("headimgurl"));
            accountBindDomain.setAccountName(userInfoJson.getString("nickname"));
            accountBindDomain.setUnionId(userInfoJson.getString("unionid"));
            return accountBindDomain;
        } catch (Exception e) {
            logger.error("获取第三方用户信息失败, code: [" + backCode + "]");
            return null;
        }
    }
}
