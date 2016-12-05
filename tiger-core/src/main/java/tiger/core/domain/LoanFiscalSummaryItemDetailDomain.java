package tiger.core.domain;

import tiger.common.dal.enums.LoanPayItemEnum;
import tiger.common.dal.enums.LoanPayItemModeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaric Liao on 2016/1/7.
 */
public class LoanFiscalSummaryItemDetailDomain {

    private String keyId;

    private Long loanId;

    private Double amountIncome;

    private Double amountOutcome;

    private LoanPayItemEnum itemType;

    private Map<String, String> extParams = new HashMap<>();

    public LoanFiscalSummaryItemDetailDomain(LoanRecordItemDomain loanRecordItem) {
        this.keyId = loanRecordItem.getKeyId();
        this.loanId = loanRecordItem.getLoanId();
        this.itemType = loanRecordItem.getItemType();
        this.amountIncome = 0.0;
        this.amountOutcome = 0.0;
        this.extParams=loanRecordItem.getExtParams();
//        if(!this.extParams.isEmpty()){
//          String name= this.extParams.get("name");
//            itemType.setValue(name);
//        }
        if (loanRecordItem.getItemModel().equals(LoanPayItemModeEnum.INCOME))
            this.amountIncome = loanRecordItem.getAmount();
        else this.amountOutcome = loanRecordItem.getAmount();
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

    public LoanPayItemEnum getItemType() {
        return itemType;
    }

    public void setItemType(LoanPayItemEnum itemType) {
        this.itemType = itemType;
    }

    public Map<String, String> getExtParams() {
        return extParams;
    }

    public void setExtParams(Map<String, String> extParams) {
        this.extParams = extParams;
    }

    public void addNewRecord(LoanRecordItemDomain loanRecordItem) {
        if (loanRecordItem.getItemModel().equals(LoanPayItemModeEnum.INCOME))
            this.amountIncome += loanRecordItem.getAmount();
        else this.amountOutcome += loanRecordItem.getAmount();
    }
}
