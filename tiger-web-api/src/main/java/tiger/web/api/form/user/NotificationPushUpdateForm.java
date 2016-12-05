/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.api.form.user;

import tiger.common.dal.enums.AccountSettingTypeEnum;
import tiger.core.domain.AccountSettingDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;

/**
 * @author alfred_yuan
 * @version v 0.1 20:24 alfred_yuan Exp $
 */
public class NotificationPushUpdateForm extends BaseForm implements FormInterface {

    @NotNull(message = "请正确设置参数")
    private AccountSettingTypeEnum settingType;

    @NotNull(message = "请设置正确的参数值")
    private Boolean value;

    public AccountSettingTypeEnum getSettingType() {
        return settingType;
    }

    public void setSettingType(AccountSettingTypeEnum settingType) {
        this.settingType = settingType;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public AccountSettingDomain convert2Domain() {
        return new AccountSettingDomain(settingType, value.toString());
    }

}
