package tiger.core.domain;

/**
 * Description
 *
 * @author liuhailong
 * @version V 0.1
 * @since 16/11/22 上午11:30.
 */
public class StatisticsDetailDomain {
    private String code;
    //费用类型
    private String feeType;
    //收入
    private Double inCome=0d;
    //支出
    private Double outCome=0d;
    //小计  收入和支出之和
    private Double result=0d;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public Double getInCome() {
        return inCome;
    }

    public void setInCome(Double inCome) {
        this.inCome = inCome;
    }

    public Double getOutCome() {
        return outCome;
    }

    public void setOutCome(Double outCome) {
        this.outCome = outCome;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }
}
