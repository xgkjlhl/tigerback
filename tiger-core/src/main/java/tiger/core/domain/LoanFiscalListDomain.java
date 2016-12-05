/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain;

import tiger.common.util.Paginator;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目财务列表模型
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 9:48 PM yiliang.gyl Exp $
 */
public class LoanFiscalListDomain {

    private Double amount;

    private List<LoanRecordItemDomain> list = new ArrayList<>();

    private Paginator paginator;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public List<LoanRecordItemDomain> getList() {
        return list;
    }

    public void setList(List<LoanRecordItemDomain> list) {
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
