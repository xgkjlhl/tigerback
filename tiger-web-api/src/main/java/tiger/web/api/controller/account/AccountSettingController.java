/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.api.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tiger.biz.account.support.AccountSettingManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.enums.AccountSettingBizTypeEnum;
import tiger.core.base.BaseResult;
import tiger.core.domain.AccountSettingDomain;
import tiger.core.domain.listDomain.AccountSettingListDomain;
import tiger.core.service.AccountSettingService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.user.NotificationPushUpdateForm;

import javax.validation.Valid;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 20:02 alfred_yuan Exp $
 */
@Controller
@RequestMapping("/account")
public class AccountSettingController extends BaseController {

    @Autowired
    private AccountSettingService accountSettingService;

    @Autowired
    private AccountSettingManager accountSettingManager;

    /**
     * 获取账户消息推送配置
     *
     * @return
     */
    @RequestMapping(value = "/setting/notification", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<List<AccountSettingListDomain>> getNotificationSetting() {
        return new BaseResult<>(accountSettingManager.getNotificationSetting(currentAccountIdWithoutException()));
    }

    /**
     * 更新账户消息推送配置
     */
    @RequestMapping(value = "/setting/notification", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Boolean> updateNotificationSetting(@RequestBody @Valid NotificationPushUpdateForm form,
                                                         BindingResult bindingResult) {
        Long currentAccountId = currentAccount().getId();

        AccountSettingDomain accountSettingDomain = form.convert2Domain();
        // 设置相关的属性
        accountSettingDomain.setBizType(AccountSettingBizTypeEnum.ACCOUNT);
        accountSettingDomain.setAccountId(currentAccountId);
        accountSettingDomain.setSubjectId(currentAccountId);

        return new BaseResult<>(accountSettingManager.updateNotificationSetting(accountSettingDomain));
    }
}
