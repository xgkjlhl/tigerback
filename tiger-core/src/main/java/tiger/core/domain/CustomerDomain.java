package tiger.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tiger.common.dal.enums.GenderEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户模型
 *
 * @author HuPeng
 * @version v 0.1 2015年10月21日 上午11:39:13 HuPeng Exp $
 */
public class CustomerDomain extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 4505065384387177405L;

    private Date createTime;

    private Date updateTime;

    /**
     * The name.
     */
    private String name;

    /**
     * The id card.
     */
    private String idCard;

    /**
     * The mobile.
     */
    private String mobile;

    /**
     * The email.
     */
    private String email;

    /**
     * The qq.
     */
    private String qq;

    /**
     * The wx.
     */
    private String wx;

    /**
     * The address.
     */
    private String address;

    /**
     * The account id.
     */
    private Long accountId;

    /**
     * The birthday
     */
    private Date birthday;

    /**
     * 性别
     */
    private GenderEnum gender;

    /**
     * 标签
     */
    private List<TagDomain> tags = new ArrayList<>();

    /**
     * 客户项目数
     */
    private CustomerBizCountDomain loanCounts;

    /**
     * 客户头像
     */
    @CopyIgnore
    private AttachDomain icon;

    /**
     * 创建者信息
     */
    @CopyIgnore
    private AccountBaseDomain accountBaseDomain;

    @CopyIgnore
    @JsonIgnore
    private List<AttachRelateDomain> attachRelateDomains;

    @CopyIgnore
    private List<AttachDomain> attaches;

    private AccountBaseDomain ownerInfo;


    // 工作组id
    private Long workspaceId;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Getter method for property <tt>name</tt>.
     *
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     *
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for property <tt>idCard</tt>.
     *
     * @return property value of idCard
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * Setter method for property <tt>idCard</tt>.
     *
     * @param idCard value to be assigned to property idCard
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * Getter method for property <tt>mobile</tt>.
     *
     * @return property value of mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Setter method for property <tt>mobile</tt>.
     *
     * @param mobile value to be assigned to property mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Getter method for property <tt>email</tt>.
     *
     * @return property value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter method for property <tt>email</tt>.
     *
     * @param email value to be assigned to property email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter method for property <tt>qq</tt>.
     *
     * @return property value of qq
     */
    public String getQq() {
        return qq;
    }

    /**
     * Setter method for property <tt>qq</tt>.
     *
     * @param qq value to be assigned to property qq
     */
    public void setQq(String qq) {
        this.qq = qq;
    }

    /**
     * Getter method for property <tt>wx</tt>.
     *
     * @return property value of wx
     */
    public String getWx() {
        return wx;
    }

    /**
     * Setter method for property <tt>wx</tt>.
     *
     * @param wx value to be assigned to property wx
     */
    public void setWx(String wx) {
        this.wx = wx;
    }

    /**
     * Getter method for property <tt>address</tt>.
     *
     * @return property value of address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter method for property <tt>address</tt>.
     *
     * @param address value to be assigned to property address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter method for property <tt>accountId</tt>.
     *
     * @return property value of accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Setter method for property <tt>accountId</tt>.
     *
     * @param accountId value to be assigned to property accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * Getter method for property <tt>birthday</tt>.
     *
     * @return property value of birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * Setter method for property <tt>birthday</tt>.
     *
     * @param birthday value to be assigned to property birthday
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * Getter method for property <tt>gender</tt>.
     *
     * @return property value of gender
     */
    public GenderEnum getGender() {
        return gender;
    }

    /**
     * Setter method for property <tt>gender</tt>.
     *
     * @param gender value to be assigned to property gender
     */
    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    /**
     * Getter method for property <tt>accountBaseDomain</tt>.
     *
     * @return property value of accountBaseDomain
     */
    public AccountBaseDomain getAccountBaseDomain() {
        return accountBaseDomain;
    }

    /**
     * Setter method for property <tt>accountBaseDomain</tt>.
     *
     * @param accountBaseDomain value to be assigned to property accountBaseDomain
     */
    public void setAccountBaseDomain(AccountBaseDomain accountBaseDomain) {
        this.accountBaseDomain = accountBaseDomain;
    }

    /**
     * Getter method for property <tt>tags</tt>.
     *
     * @return property value of tags
     */
    public List<TagDomain> getTags() {
        return tags;
    }

    /**
     * Setter method for property <tt>tags</tt>.
     *
     * @param tags value to be assigned to property tags
     */
    public void setTags(List<TagDomain> tags) {
        this.tags = tags;
    }

    /**
     * Getter method for property <tt>loanCounts</tt>.
     *
     * @return property value of loanCounts
     */
    public CustomerBizCountDomain getLoanCounts() {
        return loanCounts;
    }

    /**
     * Setter method for property <tt>loanCounts</tt>.
     *
     * @param loanCounts value to be assigned to property loanCounts
     */
    public void setLoanCounts(CustomerBizCountDomain loanCounts) {
        this.loanCounts = loanCounts;
    }

    public AttachDomain getIcon() {
        return icon;
    }

    public void setIcon(AttachDomain icon) {
        this.icon = icon;
    }

    public List<AttachRelateDomain> getAttachRelateDomains() {
        return attachRelateDomains;
    }

    public void setAttachRelateDomains(List<AttachRelateDomain> attachRelateDomains) {
        this.attachRelateDomains = attachRelateDomains;
    }

    public List<AttachDomain> getAttaches() {
        return attaches;
    }

    public void setAttaches(List<AttachDomain> attaches) {
        this.attaches = attaches;
    }


    public Long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public AccountBaseDomain getOwnerInfo() {
        return ownerInfo;
    }

    public void setOwnerInfo(AccountBaseDomain ownerInfo) {
        this.ownerInfo = ownerInfo;
    }
}
