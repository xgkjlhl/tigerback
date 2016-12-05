/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.account.support.AccountManager;
import tiger.biz.account.support.SocialAccountManager;
import tiger.biz.workspace.support.WorkspaceManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.enums.AccountSocialTypeEnum;
import tiger.common.util.StringUtil;
import tiger.core.base.BaseResult;
import tiger.core.domain.AccountBindDomain;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.AccountLoginLogDomain;
import tiger.core.domain.AccountSocialAuthDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.web.api.constants.APIConstants;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.user.SocialAuthForm;
import tiger.web.api.form.user.SocialBindForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 第三方账号账号登录接口
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 5:57 PM yiliang.gyl Exp $
 */
@RestController
@RequestMapping("social")
@ResponseBody
public class SocialAccountController extends BaseController {

    @Autowired
    SocialAccountManager socialAccountManager;

    @Autowired
    AccountManager accountManager;

    @Autowired
    WorkspaceManager workspaceManager;

    /**
     * 获取认证的跳转地址
     *
     * @param socialType
     * @return
     */
    @RequestMapping(value = "/url", method = RequestMethod.GET)
    public BaseResult<String> getRedirectUrl(@RequestParam("socialType") String socialType,
                                             @RequestParam("bindType") String bindType,
                                             HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        AccountSocialTypeEnum socialTypeEnum = AccountSocialTypeEnum.getEnumByCode(socialType);
        if (socialTypeEnum == null) {
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE.getCode(), "不支持的第三方账户类型");
        }

        return socialAccountManager.getSocialRedirectUrl(socialTypeEnum, bindType, ipAddress);
    }

    /**
     * 登录用户绑定一个第三方账户
     *
     * @param socialAuthForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    @LoginRequired
    public BaseResult<Object> bindSocial(@RequestBody SocialAuthForm socialAuthForm,
                                         BindingResult bindingResult) {
        AccountSocialTypeEnum socialTypeEnum = socialAuthForm.getSocialTypeFromState();
        if (socialTypeEnum == null) {
            return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER_VALUE.getCode(), "不支持的第三方账户类型");
        }
        String stateId = socialAuthForm.getState();

        if(socialTypeEnum == AccountSocialTypeEnum.QQMOBILE ||
                socialTypeEnum == AccountSocialTypeEnum.QQMOBILEANDROID){
            stateId = socialAuthForm.getOpenId();
        }

        return new BaseResult<>(socialAccountManager.bindSocialAccount(currentAccount().getId(), socialTypeEnum,
                socialAuthForm.getCode(),
                stateId));
    }

    /**
     * 获取用户所有绑定的第三方账户
     *
     * @return
     */
    @RequestMapping(value = "/binds", method = RequestMethod.GET)
    @LoginRequired
    public BaseResult<List<AccountBindDomain>> binds() {
        return socialAccountManager.getAccountSocialBinds(currentAccount().getId());
    }

    /**
     * 解除绑定接口
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/bind/{id}", method = RequestMethod.DELETE)
    @LoginRequired
    public BaseResult<Object> deBind(@PathVariable("id") Long id) {
        return new BaseResult<>(socialAccountManager.deleteBindSocial(currentAccount().getId(), id));
    }

    /**
     * 用户社交账号认证接口
     *
     * @return the string
     */
    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    public BaseResult<AccountSocialAuthDomain> thirdPartAuth(@RequestBody SocialAuthForm socialAuthForm,
                                                             BindingResult bindingResult,
                                                             HttpServletRequest request,
                                                             HttpServletResponse response) {
        String ipAddress = request.getRemoteAddr();
        AccountSocialTypeEnum socialType = socialAuthForm.getSocialTypeFromState();

        AccountSocialAuthDomain socialAuthDomain;
        if (socialType == AccountSocialTypeEnum.QQMOBILE ||
                socialType == AccountSocialTypeEnum.QQMOBILEANDROID) {
            if (socialAuthForm.getOpenId() == null) {
                return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL.getCode(), "请输入openId");
            }
            socialAuthDomain = socialAccountManager.authSocialAccountByToken(socialType, socialAuthForm.getCode(),
                    socialAuthForm.getOpenId());
        } else {
            socialAuthDomain = socialAccountManager.authSocialAccount(socialType,
                    socialAuthForm.getCode(),
                    socialAuthForm.getState(), ipAddress);
        }
        if (socialAuthDomain == null) {
            return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL.getCode(), "没有找到授权信息");
        }
        if (!socialAuthDomain.getCanBindAccount()) {
            String token = accountManager.createToken(new AccountLoginLogDomain(socialAuthDomain.getAccount().getId(), "",
                    request.getRemoteAddr()), 1);
            response.setHeader(APIConstants.HEADER_TOKEN, token);
        }

        // 给account 加入 workspace
        if(socialAuthDomain.getAccount() != null){
            socialAuthDomain.getAccount().setWorkspaces(workspaceManager.getUserWorkspaces(
                    socialAuthDomain.getAccount()));
        }
        return new BaseResult<>(socialAuthDomain);

    }

    /**
     * 用户绑定社交账户
     *
     * @param socialBindForm
     * @param bindingResult
     * @param request
     * @return
     */
    @RequestMapping(value = "/authentication", method = RequestMethod.PUT)
    public BaseResult<AccountDomain> bindThirdPartAccount(@RequestBody SocialBindForm socialBindForm, BindingResult bindingResult,
                                                          @RequestParam(value = APIConstants.PARAM_EXPIRE_DAY, required = false, defaultValue = APIConstants.TOKEN_DEFAULT_EXPIRE_DAY) int expireDay,
                                                          HttpServletRequest request,
                                                          HttpServletResponse response) {
        AccountDomain account;
        if (socialBindForm.getBindOrNot()) {
            if (StringUtil.isBlank(socialBindForm.getPassword()) || StringUtil.isBlank(socialBindForm.getUsername())) {
                return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL.getCode(), "账号和密码不能为空");
            }
            //1. 账号密码登录
            account = accountManager.login(socialBindForm.getUsername(),
                    socialBindForm.getPassword());

            //2. 第三方账户绑定更换主体
            AccountBindDomain bindDomain = socialAccountManager.changeAccountSocialBind(
                    socialBindForm.getBindId(), account.getId());
            if (bindDomain == null) {
                return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL.getCode(), "账户绑定失败");
            }
        } else {
            //1. 绑定账户并现在注册用户
            account = socialAccountManager.loginByBindId(socialBindForm.getBindId());
        }
        if (account == null) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND.getCode(), "登录失败,不存在的账户");
        }
        //3. 登录
        response.setHeader(APIConstants.HEADER_TOKEN,
                accountManager.createToken(new AccountLoginLogDomain(account.getId(), "",
                        request.getRemoteAddr()), 1));

        //4. 设置团队列表
        account.setWorkspaces(workspaceManager.getUserWorkspaces(account));

        //5. 设置头像
        accountManager.setCompanyAndIcon(account);

        return new BaseResult<>(account);
    }


}
