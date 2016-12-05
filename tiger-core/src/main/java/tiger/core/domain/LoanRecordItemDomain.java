/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */

package tiger.core.domain;

import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.common.util.annotation.CopyIgnore;
import tiger.core.base.BaseDomain;
import tiger.core.domain.extraDomain.ExtraField;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 贷款还款资金细节
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 27, 2015 11:07:11 AM yiliang.gyl Exp $
 */
public class LoanRecordItemDomain extends BaseDomain {

    private Date updateTime;

    private Long loanRecordId;

    private Double amount;

    private LoanPayItemEnum itemType;

    private Integer order;

    private LoanPayItemModeEnum itemModel;

    private Boolean happened;

    /**
     * 结存,财务显示需要使用
     */
    @CopyIgnore
    private Double balance = 0.0;

    private String keyId;

    private Long loanId;

    @CopyIgnore
    private Map<String, String> extParams = new HashMap<>();

    /**
     * 实例化一个项目
     *
     * @param amount   the amount
     * @param itemEnum the item enum
     *
     * @return the loan record item domain
     */
    public static LoanRecordItemDomain instance(Double amount, LoanPayItemEnum itemEnum,
                                                LoanPayItemModeEnum itemModeEnum) {
        LoanRecordItemDomain instance = new LoanRecordItemDomain();
        instance.setAmount(amount);
        instance.setItemType(itemEnum);
        instance.setItemModel(itemModeEnum);
        return instance;

    }

    public static LoanRecordItemDomain instance(Double amount,
                                                LoanPayItemEnum itemEnum,
                                                LoanPayItemModeEnum itemModeEnum,
                                                ExtraField extraField) {
        LoanRecordItemDomain instance = instance(amount, itemEnum, itemModeEnum);

        Map<String, String> extraFieldMap = new HashMap<>();
        if (extraField.getName() != null) {
            extraFieldMap.put("name", extraField.getName());
        }
        if (extraField.getValue() != null) {
            extraFieldMap.put("value", extraField.getValue().toString());
        }
        if (extraField.getDescription() != null) {
            extraFieldMap.put("description", extraField.getDescription());
        }
        instance.setExtParams(extraFieldMap);

        return instance;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Boolean getIsHappened() {
        return happened;
    }

    public void setHappened(Boolean happened) {
        this.happened = happened;
    }

    /**
     * Getter method for property <tt>loanRecordId</tt>.
     *
     * @return property value of loanRecordId
     */
    public Long getLoanRecordId() {
        return loanRecordId;
    }

    /**
     * Setter method for property <tt>loanRecordId</tt>.
     *
     * @param loanRecordId value to be assigned to property loanRecordId
     */
    public void setLoanRecordId(Long loanRecordId) {
        this.loanRecordId = loanRecordId;
    }

    /**
     * Getter method for property <tt>amount</tt>.
     *
     * @return property value of amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter method for property <tt>amount</tt>.
     *
     * @param amount value to be assigned to property amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter method for property <tt>itemType</tt>.
     *
     * @return property value of itemType
     */
    public LoanPayItemEnum getItemType() {
        return itemType;
    }

    /**
     * Setter method for property <tt>itemType</tt>.
     *
     * @param itemType value to be assigned to property itemType
     */
    public void setItemType(LoanPayItemEnum itemType) {
        this.itemType = itemType;
    }

    /**
     * Getter method for property <tt>itemModel</tt>.
     *
     * @return property value of itemModel
     */
    public LoanPayItemModeEnum getItemModel() {
        return itemModel;
    }

    /**
     * Setter method for property <tt>itemModel</tt>.
     *
     * @param itemModel value to be assigned to property itemModel
     */
    public void setItemModel(LoanPayItemModeEnum itemModel) {
        this.itemModel = itemModel;
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

    public Map<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }
}
