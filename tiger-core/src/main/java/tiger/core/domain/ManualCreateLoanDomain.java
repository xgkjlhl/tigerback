package tiger.core.domain;

import java.util.Date;

/**
 * Created by zhangpeiyuan on 16/11/28.
 */
public class ManualCreateLoanDomain {
    private Long loanId;
    private Long recordId;
    private Long workSpaceId;
    private Date lanchDate;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getWorkSpaceId() {
        return workSpaceId;
    }

    public void setWorkSpaceId(Long workSpaceId) {
        this.workSpaceId = workSpaceId;
    }

    public Date getLanchDate() {
        return lanchDate;
    }

    public void setLanchDate(Date lanchDate) {
        this.lanchDate = lanchDate;
    }
}
