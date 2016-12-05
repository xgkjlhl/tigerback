/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.ucenter;

import tiger.common.util.BeanUtil;
import tiger.core.domain.UcenterHelpDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author mi.li
 * @version v 0.1 2015年9月28日 下午8:30:26 mi.li Exp $
 */
public class HelpForm extends BaseForm implements FormInterface {

    private Long catalogId;

    @NotNull(message = "标题不能为空")
    @Size(min = 1, max = 128, message = "标题长度应在1到128之间")
    private String title;

    @NotNull(message = "内容不能为空")
    private String content;

    private Boolean isCommon;

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }

    /**
     * @see FormInterface#convert2Domain()
     */
    @Override
    public UcenterHelpDomain convert2Domain() {
        UcenterHelpDomain domain = new UcenterHelpDomain();
        BeanUtil.copyPropertiesWithIgnores(this, domain);

        return domain;
    }

}
