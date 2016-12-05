/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.customer;

import tiger.core.base.BaseDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/10/24 22:39
 */
public class CustomerListForm extends BaseForm implements FormInterface {

    /**
     * id 数组
     */
    @NotNull
    List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    @Override
    public BaseDomain convert2Domain() {
        return null;
    }
}
