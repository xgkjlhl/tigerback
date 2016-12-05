/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.loan;

import tiger.common.util.BeanUtil;
import tiger.core.domain.LoanPawnParamDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 3:27:11 PM yiliang.gyl Exp $
 */
public class LoanPawnItemForm extends BaseForm implements FormInterface {

    @NotNull(message = "有缺失的参数")
    @Size(min = 1, max = 32, message = "非法的参数名")
    private String param;

    @NotNull(message = "缺失参数值")
    @Size(min = 1, max = 32, message = "非法的参数值")
    private String value;

    @Size(max = 128, message = "描述字段过长,应小于128")
    private String description;

    /**
     * Getter method for property <tt>param</tt>.
     *
     * @return property value of param
     */
    public String getParam() {
        return param;
    }

    /**
     * Setter method for property <tt>param</tt>.
     *
     * @param param value to be assigned to property param
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * Getter method for property <tt>value</tt>.
     *
     * @return property value of value
     */
    public String getValue() {
        return value;
    }

    /**
     * Setter method for property <tt>value</tt>.
     *
     * @param value value to be assigned to property value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Getter method for property <tt>description</tt>.
     *
     * @return property value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for property <tt>description</tt>.
     *
     * @param description value to be assigned to property description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Convert param to domain.
     *
     * @return the loan pawn param domain
     */
    @Override
    public LoanPawnParamDomain convert2Domain() {
        LoanPawnParamDomain paramDomain = new LoanPawnParamDomain();
        BeanUtil.copyPropertiesWithIgnores(this, paramDomain);
        return paramDomain;
    }
}
