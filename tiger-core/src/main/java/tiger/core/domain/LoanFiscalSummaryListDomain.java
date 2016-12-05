package tiger.core.domain;

import tiger.common.util.Paginator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ��Ŀ��������б�ģ��
 * <p>
 * Created by Jaric Liao on 2016/1/7.
 */
public class LoanFiscalSummaryListDomain {

    private Double amount;

    private List<LoanFiscalSummaryItemDomain> list = new ArrayList<>();

    private Paginator paginator;

    public LoanFiscalSummaryListDomain() {
        this.amount = 0.0;
    }

    public LoanFiscalSummaryListDomain(Map<String, LoanFiscalSummaryItemDomain> collection) {
        list = new ArrayList<>();
        Double income = 0.0;
        Double outcome = 0.0;
        for (Map.Entry<String, LoanFiscalSummaryItemDomain> entry : collection.entrySet()) {
            LoanFiscalSummaryItemDomain summaryItem = entry.getValue();
            summaryItem.calculate();
            list.add(summaryItem);
            income += summaryItem.getAmountIncome();
            outcome += summaryItem.getAmountOutcome();
        }
        this.sumAmount(income, outcome);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<LoanFiscalSummaryItemDomain> getList() {
        return list;
    }

    public void setList(List<LoanFiscalSummaryItemDomain> list) {
        this.list = list;
    }

    public Paginator getPaginator() {
        return paginator;
    }

    public void setPaginator(Paginator paginator) {
        this.paginator = paginator;
    }

    public void sumAmount(Double income, Double outcome) {
        if (income == null) {
            income = 0.00;
        }
        if (outcome == null) {
            outcome = 0.00;
        }
        this.amount = income - outcome;
    }
}
