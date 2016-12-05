/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.core.base.BaseDomain;

/**
 * 记录一个贷款的所有操作记录
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:48 PM yiliang.gyl Exp $
 */
public class LoanLogDomain extends BaseDomain {

    private Long loanId;

    private Long operatorId;

    private String operationType;

    private String operationDesc;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationDesc() {
        return operationDesc;
    }

    public void setOperationDesc(String operationDesc) {
        this.operationDesc = operationDesc;
    }
}
