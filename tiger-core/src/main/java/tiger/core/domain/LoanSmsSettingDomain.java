/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 9:58 PM yiliang.gyl Exp $
 */
public class LoanSmsSettingDomain extends BaseDomain {

    private Boolean open;

    private Integer day;

    private Long loanId;

    public LoanSmsSettingDomain() {

    }

    public LoanSmsSettingDomain(boolean open, int day, long loanId) {
        this.open = open;
        this.day = day;
        this.loanId = loanId;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

}
