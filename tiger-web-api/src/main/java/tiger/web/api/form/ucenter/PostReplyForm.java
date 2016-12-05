/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.ucenter;

import tiger.common.util.BeanUtil;
import tiger.core.domain.UcenterPostReplyDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 回复
 *
 * @author mi.li
 * @version v 0.1 2015年9月28日 下午6:24:15 mi.li Exp $
 */
public class PostReplyForm extends BaseForm implements FormInterface {

    /**
     * 回复内容
     */
    @NotNull(message = "回复内容不能为空")
    @Size(min = 1, max = 4096, message = "回复内容长度应在1到4096之间")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @see FormInterface#convert2Domain()
     */
    @Override
    public UcenterPostReplyDomain convert2Domain() {
        UcenterPostReplyDomain domain = new UcenterPostReplyDomain();
        BeanUtil.copyPropertiesWithIgnores(this, domain);

        return domain;
    }


}
