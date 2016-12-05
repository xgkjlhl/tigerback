/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.mng.form.system;

import org.springframework.format.annotation.DateTimeFormat;
import tiger.common.dal.enums.GenderEnum;
import tiger.common.util.BeanUtil;
import tiger.core.domain.AccountDomain;
import tiger.web.mng.form.BaseForm;
import tiger.web.mng.form.FormInterface;

import java.util.Date;

/**
 * @author hupeng
 * @version v0.1 2015/10/25 12:28
 */
public class AccountUpdateForm extends BaseForm implements FormInterface {

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 业务范围
     */
    private String businessScope;

    /**
     * 地址
     */
    private String address;

    /**
     * 生日日期
     */
    @DateTimeFormat(pattern = "yyyy年MM月dd日")
    private Date birthday;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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
        domain.setBirthday(this.birthday);
        domain.setGender(GenderEnum.getEnumByCode(this.gender));
        domain.setUserName(this.userName);
        domain.setIdCard(this.idCard);
        domain.setBusinessScope(this.businessScope);
        domain.setAddress(this.address);
        return domain;
    }
}
