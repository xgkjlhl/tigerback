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
 * @version $ID: v 0.1 12:01 PM yiliang.gyl Exp $
 */
public class QQAuth extends TemplateAuth {

    public QQAuth(String appId, String secret, String redirectUrl) {
        super(appId, secret, redirectUrl);
    }

    private static final String LOGIN_URL = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%s&scope=get_user_info";

    private static final String AUTH_TOKEN_URL = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&state=%s&redirect_uri=%s";

    private static final String AUTH_URL = "https://graph.qq.com/oauth2.0/me?access_token=%s";

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
    public AccountBindDomain getAuthUserInfo(String backCode, String state) {
        //1. 通过code换取token
        String url = getAuthTokenUrl(backCode, state);
        OAuthRequest request = new OAuthRequest(Verb.GET, url);

        Response response = request.send();
        String token = getTokenFromString(response.getBody());

        //2. 通过token换取openid
        String meUrl = String.format(AUTH_URL, token);
        OAuthRequest meRequest = new OAuthRequest(Verb.GET, meUrl);
        String responseBody = meRequest.send().getBody();

        responseBody = responseBody.subSequence(responseBody.indexOf("{"), responseBody.indexOf("}") + 1).toString();
        JSONObject jsonObject = new JSONObject(responseBody);

        try {
            AccountBindDomain accountBindDomain = new AccountBindDomain();
            accountBindDomain.setAccessToken(token);
            accountBindDomain.setOpenId(jsonObject.getString("openid"));

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
            logger.error("获取第三方用户信息失败, code: [" + backCode + "]");
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

    /**
     * 从QQ返回的string里获取token
     *
     * @param tokenStr
     * @return
     */
    private String getTokenFromString(String tokenStr) {
        String[] str = tokenStr.split("&");
        for (String kvStr : str) {
            String[] keyValues = kvStr.split("=");
            if (keyValues.length == 2 && keyValues[0].equals("access_token")) {
                return keyValues[1];
            }
        }
        return null;
    }

}
