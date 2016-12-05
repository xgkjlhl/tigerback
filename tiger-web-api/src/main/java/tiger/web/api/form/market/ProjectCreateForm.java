/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.market;

import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.market.ContactInfoDomain;
import tiger.core.domain.market.MarketProjectDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 2:47 PM yiliang.gyl Exp $
 */
public class ProjectCreateForm extends BaseForm implements FormInterface {

    @NotNull(message = "请输入期限")
    private Integer period;

    @NotNull(message = "请输入金额")
    private Double amount;

    @Size(max = 500, message = "描述长度必须小于500个字符")
    private String description;

    @NotNull(message = "标题不能为空")
    @Size(min = 1, max = 50, message = "标题长度必须在1到50个字符之间")
    private String title;

    private Boolean hasBack;

    @NotNull(message = "没有设置抵押方式")
    private String pawnType;

    private Double minBackPercent;

    private Double maxBackPercent;

    @NotNull(message = "请设置投放地点")
    private String address;

    private String addressKey;

    @NotNull(message = "请设置联系人姓名")
    @Size(min = 1, max = 32, message = "联系人姓名长度应在1到32之间")
    private String name;

    @NotNull(message = "请输入联系人电话")
    @Size(min = 1, max = 32, message = "联系人电话长度应在1到32之间")
    private String mobilePhone;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getHasBack() {
        return hasBack;
    }

    public void setHasBack(Boolean hasBack) {
        this.hasBack = hasBack;
    }

    public String getPawnType() {
        return pawnType;
    }

    public void setPawnType(String pawnType) {
        this.pawnType = pawnType;
    }

    public Double getMinBackPercent() {
        return minBackPercent;
    }

    public void setMinBackPercent(Double minBackPercent) {
        this.minBackPercent = minBackPercent;
    }

    public Double getMaxBackPercent() {
        return maxBackPercent;
    }

    public void setMaxBackPercent(Double maxBackPercent) {
        this.maxBackPercent = maxBackPercent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressKey() {
        return addressKey;
    }

    public void setAddressKey(String addressKey) {
        this.addressKey = addressKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @Override
    public MarketProjectDomain convert2Domain() {
        MarketProjectDomain marketProjectDomain = new MarketProjectDomain();
        BeanUtil.copyPropertiesWithIgnores(this, marketProjectDomain);
        marketProjectDomain.setContactInfo(new ContactInfoDomain(this.name, this.mobilePhone));

        if (StringUtil.isNotBlank(this.pawnType)) {
            marketProjectDomain.setPawnType(LoanPawnTypeEnum.getEnumByCode(this.pawnType));
        }

        return marketProjectDomain;
    }
}
