/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.form.loan;

import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;
import tiger.core.domain.LoanRecordItemDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.web.api.form.BaseForm;
import tiger.web.api.form.FormInterface;

import javax.validation.constraints.NotNull;

/**
 * 单笔还款科目
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 3:15 PM yiliang.gyl Exp $
 */
public class LoanSinglePayItemForm extends BaseForm implements FormInterface {

    @NotNull(message = "请填写科目")
    private String itemType;

    @NotNull(message = "请输入款项类型")
    private String itemMode;

    @NotNull(message = "请输入金额")
    private Double amount;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getItemMode() {
        return itemMode;
    }

    public void setItemMode(String itemMode) {
        this.itemMode = itemMode;
    }

    /**
     * 获取还款详情模型
     *
     * @return
     */
    @Override
    public LoanRecordItemDomain convert2Domain() {
        LoanRecordItemDomain recordItemDomain = new LoanRecordItemDomain();
        recordItemDomain.setAmount(this.amount);
        recordItemDomain.setItemType(LoanPayItemEnum.getEnumByCode(this.itemType));
        recordItemDomain.setItemModel(LoanPayItemModeEnum.getEnumByCode(this.itemMode));

        if (recordItemDomain.getItemType() == null || recordItemDomain.getItemModel() == null) {
            throw new TigerException(ErrorCodeEnum.BLANK_PARAMETER_VALUE, "有不完整的参数");
        }
        return recordItemDomain;
    }
}
