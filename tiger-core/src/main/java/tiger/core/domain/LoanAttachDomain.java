/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 10:57 AM yiliang.gyl Exp $
 */
public class LoanAttachDomain extends BaseDomain {

    private Long loanId;

    private Long attachId;

    private String type;

    private AttachDomain attachDomain;

    public AttachDomain getAttachDomain() {
        return attachDomain;
    }

    public void setAttachDomain(AttachDomain attachDomain) {
        this.attachDomain = attachDomain;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
