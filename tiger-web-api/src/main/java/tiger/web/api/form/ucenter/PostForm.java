/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.ucenter;

import tiger.common.util.BeanUtil;
import tiger.core.domain.UcenterPostDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 主题帖
 *
 * @author mi.li
 * @version v 0.1 2015年9月27日 上午10:40:57 mi.li Exp $
 */
public class PostForm extends BaseForm implements FormInterface {

    /**
     * 帖子主题
     */
    @NotNull(message = "帖子主题不能为空")
    @Size(min = 1, max = 256, message = "帖子主题长度应在1到256之间")
    private String title;

    /**
     * 帖子内容
     */
    @NotNull(message = "帖子内容不能为空")
    private String content;

    private Boolean isSticky;

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

    public Boolean getIsSticky() {
        return isSticky;
    }

    public void setIsSticky(Boolean isSticky) {
        this.isSticky = isSticky;
    }

    /**
     * @see FormInterface#convert2Domain()
     */
    @Override
    public UcenterPostDomain convert2Domain() {
        UcenterPostDomain domain = new UcenterPostDomain();
        BeanUtil.copyPropertiesWithIgnores(this, domain);

        return domain;
    }

}
