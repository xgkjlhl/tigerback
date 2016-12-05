/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.user;

import tiger.common.dal.enums.GenderEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.AccountDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.Size;

/**
 * @author zhangbin
 * @version v0.1 2015/9/19 15:17
 */
public class AccountUpdateForm extends BaseForm implements FormInterface {

    /**
     * 用户性别
     */
    @CopyIgnore
    private String gender;

    /**
     * 用户名
     */
    @Size(min = 1, max = 10, message = "姓名长度应在1到10之间")
    private String userName;

    /**
     * 身份证号码
     */
    @Size(max = 32, message = "身份证长度应小于32")
    private String idCard;

    /**
     * 业务范围
     */
    @Size(max = 512, message = "业务范围长度应小于512")
    private String businessScope;

    /**
     * 地址
     */
    @Size(max = 128, message = "详细地址长度应小于128")
    private String address;

    /**
     * 地区编码
     */
    private String regionCode;

    /**
     * 自我介绍
     */
    @Size(max = 1024, message = "个人简介长度应小于1024")
    private String selfIntroduction;


    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public AccountDomain convert2Domain() {
        AccountDomain domain = new AccountDomain();
        BeanUtil.copyPropertiesWithIgnores(this, domain);
        domain.setGender(GenderEnum.getEnumByCode(this.gender));
        return domain;
    }

}
