package tiger.web.api.form.customer;

import org.springframework.format.annotation.DateTimeFormat;
import tiger.common.dal.enums.GenderEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.AttachRelateDomain;
import tiger.core.domain.CustomerDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;


/**
 * The Class CustomerCreateForm.
 */
public class CustomerUpdateForm extends BaseForm implements FormInterface {

    /**
     * The id.
     */
    private Long id;

    /**
     * The name.
     */
    @Size(min = 1, max = 10, message = "姓名长度应在1到10之间")
    private String name;

    /**
     * The mobile.
     */
    @Size(max = 32, message = "手机号码长度应小于32")
    private String mobile;

    /**
     * The id card.
     */
    @Size(max = 32, message = "身份证长度应小于32")
    private String idCard;

    /**
     * The email.
     */
    @Size(max = 32, message = "电子邮箱长度应小于32")
    private String email;

    /**
     * The qq.
     */
    @Size(max = 32, message = "QQ长度应小于32")
    private String qq;

    /**
     * The wx.
     */
    @Size(max = 32, message = "微信长度应小于32")
    private String wx;

    /**
     * The account id.
     */
    private Long accountId;

    /**
     * The address.
     */
    @Size(max = 64, message = "住址长度应小于64")
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @CopyIgnore
    private String gender;

    private List<AttachRelateDomain> attachRelateDomains;

    /**
     * Convert2 do.
     *
     * @return the customer do
     * @see FormInterface#convert2Domain()
     */
    @Override
    public CustomerDomain convert2Domain() {
        CustomerDomain domain = new CustomerDomain();

        BeanUtil.copyPropertiesWithIgnores(this, domain);
        // 设置性别
        domain.setGender(GenderEnum.getEnumByCode(this.getGender()));

        return domain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    /**
     * Getter for property 'attachRelateDomains'.
     *
     * @return Value for property 'attachRelateDomains'.
     */
    public List<AttachRelateDomain> getAttachRelateDomains() {
        return attachRelateDomains;
    }

    /**
     * Setter for property 'attachRelateDomains'.
     *
     * @param attachRelateDomains Value to set for property 'attachRelateDomains'.
     */
    public void setAttachRelateDomains(List<AttachRelateDomain> attachRelateDomains) {
        this.attachRelateDomains = attachRelateDomains;
    }
}
