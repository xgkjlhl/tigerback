package tiger.web.api.form.fee;

/**
 * Created by zhangpeiyuan on 16/11/18.
 */
public class FeeForm {
    //费用名称
    private String feeName;
    //费用钱数
    private double feeValue;
    //收款方式  1：一次性收取  2：分期收取
    private String receiptType;


    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public double getFeeValue() {
        return feeValue;
    }

    public void setFeeValue(double feeValue) {
        this.feeValue = feeValue;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

}
