package tiger.core.domain;

import tiger.common.util.annotation.CopyIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��Ŀ��������б�����¼ģ��
 * <p>
 * Created by Jaric Liao on 2016/1/7.
 */
public class LoanFiscalSummaryItemDomain {

    private List<LoanFiscalSummaryItemDetailDomain> list;

    private String keyId;

    private Long loanId;

    private Double amountIncome;

    private Double amountOutcome;



    public LoanFiscalSummaryItemDomain(LoanRecordItemDomain loanRecordItem) {
        this.keyId = loanRecordItem.getKeyId();
        this.loanId = loanRecordItem.getLoanId();
        this.list = new ArrayList<LoanFiscalSummaryItemDetailDomain>();
        amountIncome = 0.0;
        amountOutcome = 0.0;
        this.addNewRecord(loanRecordItem);
    }

    public List<LoanFiscalSummaryItemDetailDomain> getList() {
        return list;
    }

    public void setList(List<LoanFiscalSummaryItemDetailDomain> list) {
        this.list = list;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Double getAmountIncome() {
        return amountIncome;
    }

    public void setAmountIncome(Double amountIncome) {
        this.amountIncome = amountIncome;
    }

    public Double getAmountOutcome() {
        return amountOutcome;
    }

    public void setAmountOutcome(Double amountOutcome) {
        this.amountOutcome = amountOutcome;
    }


    public void addNewRecord(LoanRecordItemDomain loanRecordItem) {
        // flag ָʾδ�ҵ������Ŀ
        boolean flag = false;
        for (LoanFiscalSummaryItemDetailDomain summaryItemDetail : list) {
            if (summaryItemDetail.getItemType().equals(loanRecordItem.getItemType())) {
                summaryItemDetail.addNewRecord(loanRecordItem);
                flag = true;
                break;
            }
        }
        // �½���������
        if (!flag) {
            LoanFiscalSummaryItemDetailDomain summaryItemDetail = new LoanFiscalSummaryItemDetailDomain(loanRecordItem);
            list.add(summaryItemDetail);
        }
    }

    public void calculate() {
        for (LoanFiscalSummaryItemDetailDomain summaryItemDetail : list) {
            amountIncome += summaryItemDetail.getAmountIncome();
            amountOutcome += summaryItemDetail.getAmountOutcome();
        }
    }
}
