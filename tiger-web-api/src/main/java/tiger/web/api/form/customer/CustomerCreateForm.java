package tiger.web.api.form.customer;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;
import tiger.common.dal.enums.GenderEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.AttachDomain;
import tiger.core.domain.AttachRelateDomain;
import tiger.core.domain.CustomerDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;
import tiger.web.api.form.attach.AttachRelateForm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The Class CustomerCreateForm.
 */
public class CustomerCreateForm extends BaseForm implements FormInterface {

    private Long id;

    /**
     * The name.
     */
    @NotNull(message = "客户姓名不能为空")
    @Size(min = 1, max = 10, message = "姓名长度应在1到10之间")
    private String name;

    /**
     * The mobile.
     */
    @Size(max = 32, message = "手机长度应小于32")
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
     * QQ
     */
    @Size(max = 32, message = "QQ长度应小于32")
    private String qq;

    /**
     * 微信
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


    /**
     * 客户生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别
     */
    private String gender;

    /**
     * 附件列表对象
     */
    private List<AttachRelateForm> attachs;


    /**
     * 头像id
     */
    private Long iconId;

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
        domain.setGender(GenderEnum.getEnumByCode(this.gender));
        // 设置附件信息
        if (CollectionUtils.isNotEmpty(attachs)) {
            List<AttachRelateDomain> attachRelateDomains = new ArrayList<>(attachs.size());
            attachs.forEach(attachRelateForm -> attachRelateDomains.add(attachRelateForm.convert2Domain()));
            domain.setAttachRelateDomains(attachRelateDomains);
        }
        // 设置头像信息
        if (iconId != null && iconId > 0) {
            AttachDomain icon = new AttachDomain();
            icon.setId(iconId);
            domain.setIcon(icon);
        }
        return domain;
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
        this.mobile = StringUtil.trim(mobile);
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

    public List<AttachRelateForm> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<AttachRelateForm> attachs) {
        this.attachs = attachs;
    }

    public Long getIconId() {
        return iconId;
    }

    public void setIconId(Long iconId) {
        this.iconId = iconId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
