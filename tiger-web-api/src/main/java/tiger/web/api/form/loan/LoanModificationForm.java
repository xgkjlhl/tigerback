/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.loan;

import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 贷款修正数据表单
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 8:22 PM yiliang.gyl Exp $
 */
public class LoanModificationForm extends BaseForm implements FormInterface {

    @NotNull(message = "请选择款项发生日期")
    private Date payDate;

    @NotNull(message = "摘要不能为空")
    @Size(min = 1, message = "摘要不能为空")
    private String type;

    @NotNull(message = "款项类别不能为空")
    @Size(min = 1, message = "款项类别不能为空")
    private String payMode;

    @NotNull(message = "金额不能为空")
    private Double amount;

    private String remark;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @Override
    public LoanRecordDomain convert2Domain() {
        LoanRecordDomain loanRecordDomain = new LoanRecordDomain();

        if (payMode.equals(LoanPayItemModeEnum.OUTCOME.getCode())) {
            setAmount(-amount);
        }
        loanRecordDomain.setAmount(amount);
        loanRecordDomain.setActualDate(payDate); //日期要支持输入
        loanRecordDomain.setLoanPayItemModeEnum(LoanPayItemModeEnum.getEnumByCode(payMode));
        loanRecordDomain.setRemark(remark);

        //设置成为修正
        loanRecordDomain.setType(LoanActionEnum.MODIFICATION);

        LoanRecordItemDomain recordItemDomain = new LoanRecordItemDomain();
        recordItemDomain.setItemType(LoanPayItemEnum.getEnumByCode(type));
        recordItemDomain.setAmount(Math.abs(amount));
        recordItemDomain.setItemModel(LoanPayItemModeEnum.getEnumByCode(payMode));

        loanRecordDomain.getRecordItemDomains().add(recordItemDomain);

        return loanRecordDomain;
    }
}
