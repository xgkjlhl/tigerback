/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.customer;

import tiger.web.api.form.BaseForm;

import javax.validation.constraints.NotNull;

/**
 * @author mi.li
 * @version v 0.1 16/3/28 下午8:16 mi.li Exp $
 */
public class CustomerAssignmentForm extends BaseForm {

    @NotNull(message = "请指定目标用户")
    private Long targetId;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
