/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import java.io.Serializable;

/**
 * 客户项目数模型
 *
 * @author HuPeng
 * @version v 0.1 2015年10月21日 下午4:45:47 HuPeng Exp $
 */
public class CustomerBizCountDomain implements Serializable {

    /**  */
    private static final long serialVersionUID = 3121166721767344385L;

    /**
     * 客户作为贷款人的项目数量
     */
    private Integer loanerCount;

    /**
     * 客户作为贷款人的项目数量
     */
    private Integer holderCount;

    /**
     * 客户作为投资人的项目数量
     */
    private Integer investorCount;

    /**
     * Getter method for property <tt>loanerCount</tt>.
     *
     * @return property value of loanerCount
     */
    public Integer getLoanerCount() {
        return loanerCount;
    }

    /**
     * Setter method for property <tt>loanerCount</tt>.
     *
     * @param loanerCount value to be assigned to property loanerCount
     */
    public void setLoanerCount(Integer loanerCount) {
        this.loanerCount = loanerCount;
    }

    /**
     * Getter method for property <tt>holderCount</tt>.
     *
     * @return property value of holderCount
     */
    public Integer getHolderCount() {
        return holderCount;
    }

    /**
     * Setter method for property <tt>holderCount</tt>.
     *
     * @param holderCount value to be assigned to property holderCount
     */
    public void setHolderCount(Integer holderCount) {
        this.holderCount = holderCount;
    }

    /**
     * Getter method for property <tt>investorCount</tt>.
     *
     * @return property value of investorCount
     */
    public Integer getInvestorCount() {
        return investorCount;
    }

    /**
     * Setter method for property <tt>investorCount</tt>.
     *
     * @param investorCount value to be assigned to property investorCount
     */
    public void setInvestorCount(Integer investorCount) {
        this.investorCount = investorCount;
    }

}
