/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.loan;

import org.springframework.format.annotation.DateTimeFormat;
import tiger.web.api.form.BaseForm;

import java.util.Date;

/**
 * 项目账单计算参数传递接口
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 8:40 PM yiliang.gyl Exp $
 */
public class LoanRecalForm extends BaseForm {


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualPayDate;

    /**
     * @return
     */
    public Date getActualPayDate() {
        return actualPayDate;
    }

    /**
     * @param actualPayDate
     */
    public void setActualPayDate(Date actualPayDate) {
        this.actualPayDate = actualPayDate;
    }

}
