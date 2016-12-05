/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.loan;

import tiger.core.domain.LoanRecordDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 单笔还款
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:12 PM yiliang.gyl Exp $
 */
public class LoanSinglePayForm extends BaseForm implements FormInterface {

    /**
     * 本次收款总共还的钱
     */
    @NotNull(message = "还款总额不能为空")
    private Double sum;

    /**
     * 实际还款日期
     */
    @NotNull(message = "实际还款日期不能为空")
    private Date actualDate;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名算法
     */
    private String signType;


    private String remark;

    /**
     * 还款科目列表
     */
    private List<LoanSinglePayItemForm> items;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public List<LoanSinglePayItemForm> getItems() {
        return items;
    }

    public void setItems(List<LoanSinglePayItemForm> items) {
        this.items = items;
    }

    /**
     * 转换成为还款记录对象
     *
     * @return
     */
    @Override
    public LoanRecordDomain convert2Domain() {
        LoanRecordDomain loanRecordDomain = new LoanRecordDomain();
        loanRecordDomain.setActualAmount(this.sum);
        loanRecordDomain.setActualDate(this.actualDate);
        for (LoanSinglePayItemForm itemForm : this.items) {
            loanRecordDomain.getRecordItemDomains().add(itemForm.convert2Domain());
        }
        loanRecordDomain.setRemark(this.remark);
        return loanRecordDomain;
    }
}
