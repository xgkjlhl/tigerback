/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

/**
 * 贷款抵押物模型
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 3:47:37 PM yiliang.gyl Exp $
 */
public class LoanPawnParamDomain extends BaseDomain {

    private Long loanPawnId;

    private String param;

    private String value;

    private String description;

    /**
     * Getter method for property <tt>loanPawnId</tt>.
     *
     * @return property value of loanPawnId
     */
    public Long getLoanPawnId() {
        return loanPawnId;
    }

    /**
     * Setter method for property <tt>loanPawnId</tt>.
     *
     * @param loanPawnId value to be assigned to property loanPawnId
     */
    public void setLoanPawnId(Long loanPawnId) {
        this.loanPawnId = loanPawnId;
    }

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

}
