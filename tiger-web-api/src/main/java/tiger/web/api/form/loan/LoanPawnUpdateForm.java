/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.loan;

import org.springframework.util.CollectionUtils;
import tiger.common.dal.enums.LoanPawnTypeEnum;
import tiger.common.util.BeanUtil;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.domain.LoanPawnDomain;
import tiger.core.domain.LoanPawnParamDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 贷款抵押物创建表单
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 24, 2015 3:16:38 PM yiliang.gyl Exp $
 */
public class LoanPawnUpdateForm extends BaseForm implements FormInterface {

    @NotNull(message = "贷款抵押物类型不能为空")
    @CopyIgnore
    private String pawnType;

    @Size(max = 1024, message = "备注字段过长,应小于1024")
    private String remark;

    private Integer order = 0;

    @CopyIgnore
    private List<LoanPawnItemForm> loanPawnItemForms;

    /**
     * Getter method for property <tt>pawnType</tt>.
     *
     * @return property value of pawnType
     */
    public String getPawnType() {
        return pawnType;
    }

    /**
     * Setter method for property <tt>pawnType</tt>.
     *
     * @param pawnType value to be assigned to property pawnType
     */
    public void setPawnType(String pawnType) {
        this.pawnType = pawnType;
    }

    /**
     * Getter method for property <tt>order</tt>.
     *
     * @return property value of order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Setter method for property <tt>order</tt>.
     *
     * @param order value to be assigned to property order
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    /**
     * Getter method for property <tt>loanPawnItemForms</tt>.
     *
     * @return property value of loanPawnItemForms
     */
    public List<LoanPawnItemForm> getLoanPawnItemForms() {
        return loanPawnItemForms;
    }

    /**
     * Setter method for property <tt>loanPawnItemForms</tt>.
     *
     * @param loanPawnItemForms value to be assigned to property loanPawnItemForms
     */
    public void setLoanPawnItemForms(List<LoanPawnItemForm> loanPawnItemForms) {
        this.loanPawnItemForms = loanPawnItemForms;
    }

    /**
     * Getter method for property <tt>remark</tt>.
     *
     * @return property value of remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Setter method for property <tt>remark</tt>.
     *
     * @param remark value to be assigned to property remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Convert to domain.
     *
     * @return the loan pawn domain
     */
    @Override
    public LoanPawnDomain convert2Domain() {
        LoanPawnDomain domain = new LoanPawnDomain();
        BeanUtil.copyPropertiesWithIgnores(this, domain);
        domain.setLoanPawnTypeEnum(LoanPawnTypeEnum.getEnumByCode(this.pawnType));
        if (!CollectionUtils.isEmpty(this.loanPawnItemForms)) {
            List<LoanPawnParamDomain> paramDomains = new ArrayList<>(this.loanPawnItemForms.size());
            this.loanPawnItemForms.forEach(loanPawnItemForm -> paramDomains.add(loanPawnItemForm.convert2Domain()));
            domain.setLoanPawnParamDomains(paramDomains);
        }
        return domain;
    }
}
