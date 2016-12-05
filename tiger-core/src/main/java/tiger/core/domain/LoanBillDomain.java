/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 项目还款账单
 * ~ 由用户选择的还款列表计算而得到
 *
 * @author yiliang.gyl
 * @version v 0.1 Oct 1, 2015 9:04:01 PM yiliang.gyl Exp $
 */
public class LoanBillDomain implements Serializable {

    /**  */
    private static final long serialVersionUID = -4941095564215365164L;

    private List<LoanRecordDomain> recordDomains = new ArrayList<>();

    private Double amount;

    private boolean isEnd = false;

    private Date operateDate;

    /**
     * Getter method for property <tt>recordDomains</tt>.
     *
     * @return property value of recordDomains
     */
    public List<LoanRecordDomain> getRecordDomains() {
        return recordDomains;
    }

    /**
     * Setter method for property <tt>recordDomains</tt>.
     *
     * @param recordDomains value to be assigned to property recordDomains
     */
    public void setRecordDomains(List<LoanRecordDomain> recordDomains) {
        this.recordDomains = recordDomains;
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
     * Getter method for property <tt>isEnd</tt>.
     *
     * @return property value of isEnd
     */
    public boolean isEnd() {
        return isEnd;
    }

    /**
     * Setter method for property <tt>isEnd</tt>.
     *
     * @param isEnd value to be assigned to property isEnd
     */
    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * Getter method for property <tt>operateDate</tt>.
     *
     * @return property value of operateDate
     */
    public Date getOperateDate() {
        return operateDate;
    }

    /**
     * Setter method for property <tt>operateDate</tt>.
     *
     * @param operateDate value to be assigned to property operateDate
     */
    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

}
