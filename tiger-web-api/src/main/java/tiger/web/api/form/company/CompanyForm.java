/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.company;

import tiger.core.domain.CompanyDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.Size;

/**
 * @author zhangbin
 * @version v0.1 2015/10/1 15:28
 */
public class CompanyForm extends BaseForm implements FormInterface {

    /**
     * 公司名
     */
    @Size(max = 32, message = "公司名称长度应小于32")
    private String name;

    /**
     * 公司电话
     */
    @Size(max = 32, message = "公司电话长度应小于32")
    private String phone;

    /**
     * 公司地址
     */
    @Size(max = 32, message = "公司地址长度应小于32")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public CompanyDomain convert2Domain() {
        CompanyDomain companyDomain = new CompanyDomain();

        companyDomain.setName(this.name);
        companyDomain.setPhone(this.phone);
        companyDomain.setAddress(this.address);

        return companyDomain;
    }
}
