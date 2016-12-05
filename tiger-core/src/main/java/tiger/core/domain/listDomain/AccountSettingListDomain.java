/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.listDomain;

import tiger.common.dal.enums.AccountSettingBizTypeEnum;
import tiger.common.dal.enums.AccountSettingTypeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

/**
 * 用户设置领域模型--用于列表返回
 * @author alfred_yuan
 * @version v 0.1 19:02 alfred_yuan Exp $
 */
public class AccountSettingListDomain extends BaseDomain {

    private static final long serialVersionUID = -404040060432231167L;

    private AccountSettingBizTypeEnum bizType;

    private AccountSettingTypeEnum settingType;

    private String settingValue;

    public AccountSettingBizTypeEnum getBizType() {
        return bizType;
    }

    public void setBizType(AccountSettingBizTypeEnum bizType) {
        this.bizType = bizType;
    }

    public AccountSettingTypeEnum getSettingType() {
        return settingType;
    }

    public void setSettingType(AccountSettingTypeEnum settingType) {
        this.settingType = settingType;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
}
