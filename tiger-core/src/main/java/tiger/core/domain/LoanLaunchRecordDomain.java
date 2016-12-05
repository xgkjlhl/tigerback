package tiger.core.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by liuhailong on 16/12/1.
 */
public class LoanLaunchRecordDomain {
    private Double totalMoney;
    private String process;
    private String diedLine;
    private List<LoanLaunchRecordItemDomain> itemDomainList;
    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDiedLine() {
        return diedLine;
    }

    public void setDiedLine(String diedLine) {
        this.diedLine = diedLine;
    }

    public List<LoanLaunchRecordItemDomain> getItemDomainList() {
        return itemDomainList;
    }

    public void setItemDomainList(List<LoanLaunchRecordItemDomain> itemDomainList) {
        this.itemDomainList = itemDomainList;
    }


}
