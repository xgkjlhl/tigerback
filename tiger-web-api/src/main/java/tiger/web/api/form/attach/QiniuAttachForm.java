/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.attach;

import com.fasterxml.jackson.core.type.TypeReference;
import tiger.common.dal.enums.AttachTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.JsonConverterUtil;
import tiger.common.util.StringUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.AttachDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import java.util.Map;

/**
 * The Class QiniuAttachForm.
 *
 * @author alfred.yx
 * @version v 0.1 Oct 5, 2015 9:09:00 PM alfred Exp $
 */
public class QiniuAttachForm extends BaseForm implements FormInterface {

    /**
     * The url.
     */
    private String url;

    /**
     * The name.
     */
    private String name;

    /* 附件大小 */
    private Integer size;

    /**
     * The attach type.
     */
    private String attachType;

    /**
     * The meta data.
     */
    @CopyIgnore
    private String metaData;

    /**
     * The account id.
     */
    private Long accountId;

    /* 七牛云附件默认：未删除 */
    private Boolean isDel = false;

    /**
     * @return the base domain
     * @see FormInterface#convert2Domain()
     */
    @SuppressWarnings("unchecked")
    @Override
    public AttachDomain convert2Domain() {
        AttachDomain target = new AttachDomain();
        BeanUtil.copyPropertiesWithIgnores(this, target);
        if (StringUtil.isNotEmpty(metaData)) {
            target.setMetaData(
                    (Map<String, String>) JsonConverterUtil.deserialize(metaData, new TypeReference<Map<String, String>>() {
                    }));
        }
        target.setAttachType(AttachTypeEnum.getEnumByCode(attachType));
        return target;
    }

    /**
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url the new url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Gets the attach type.
     *
     * @return the attach type
     */
    public String getAttachType() {
        return attachType;
    }

    /**
     * Sets the attach type.
     *
     * @param attachType the new attach type
     */
    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }

    /**
     * Gets the meta data.
     *
     * @return the meta data
     */
    public String getMetaData() {
        return metaData;
    }

    /**
     * Sets the meta data.
     *
     * @param metaData the new meta data
     */
    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    /**
     * Gets the account id.
     *
     * @return the account id
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Sets the account id.
     *
     * @param accountId the new account id
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }
}
