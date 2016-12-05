/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.feedback;

import org.apache.commons.collections.CollectionUtils;
import tiger.core.domain.AttachRelateDomain;
import tiger.core.domain.FeedbackDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;
import tiger.web.api.form.attach.AttachRelateForm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 16:22 alfred_yuan Exp $
 */
public class FeedbackCreateForm extends BaseForm implements FormInterface {

    private String content;

    private List<AttachRelateForm> attachs;

    public List<AttachRelateForm> getAttachs() {
        return attachs;
    }

    public void setAttachs(List<AttachRelateForm> attachs) {
        this.attachs = attachs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public FeedbackDomain convert2Domain() {
        FeedbackDomain target = new FeedbackDomain();
        target.setContent(this.content);

        if (CollectionUtils.isNotEmpty(attachs)) {
            List<AttachRelateDomain> attachRelateDomains = new ArrayList<>(attachs.size());
            attachs.forEach(attachRelateForm -> attachRelateDomains.add(attachRelateForm.convert2Domain()));

            target.setAttachRelateDomains(attachRelateDomains);
        }

        return target;
    }
}
