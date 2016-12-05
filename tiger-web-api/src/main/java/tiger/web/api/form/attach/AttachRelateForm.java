/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.attach;

import tiger.core.domain.AttachRelateDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 8:49 PM yiliang.gyl Exp $
 */
public class AttachRelateForm extends BaseForm implements FormInterface {

    @NotNull(message = "附件attachId不能为空")
    private Long attachId;

    private String type;

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public AttachRelateDomain convert2Domain() {
        AttachRelateDomain target = new AttachRelateDomain();

        target.setAttachId(this.attachId);

        return target;
    }

}
