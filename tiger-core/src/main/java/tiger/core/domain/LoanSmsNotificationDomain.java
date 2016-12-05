/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:30 PM yiliang.gyl Exp $
 */
public class LoanSmsNotificationDomain extends BaseDomain {

    private Long loanId;

    private Long day;

    private Long customerId;

    private String customerName;

    private String customerMobile;

    private LoanDomain loanDomain;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public LoanDomain getLoanDomain() {
        return loanDomain;
    }

    public void setLoanDomain(LoanDomain loanDomain) {
        this.loanDomain = loanDomain;
    }

}
