package tiger.core.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author liuhailong
 * @version V 0.1
 * @since 16/11/22 上午11:30.
 */
public class StatisticsDomain {
    //总的小计
    private String feeType;
    //费用的详情列表
    private List<StatisticsDetailDomain> list=new ArrayList<StatisticsDetailDomain>();
    //收入
    private Double inCome=0d;
    //支出
    private Double outCome=0d;
    //小计  收入和支出之和
    private Double result=0d;

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public List<StatisticsDetailDomain> getList() {
        return list;
    }

    public void setList(List<StatisticsDetailDomain> list) {
        this.list = list;
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
