package tiger.core.domain;

import java.util.Date;

/**
 * Created by zhangpeiyuan on 16/11/28.
 */
public class ModifyLoanRecordDomain {
    //修改后的本金
    private Double amount;
    //修改后的利息
    private Double interest;
    //修改后的分期费用的值
    private Double costSchedule;
    //履约保证金
    private Double bondPerformMoney;
    //暂收费用
    private Double tempMoney;
    //收款日期
    private String diedLine;


    public Double getAmount() {
        if(amount!=null){
            return amount;
        }else{
            return 0.00;
        }
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getInterest() {
        if(interest!=null){
            return interest;
        }else{
            return 0.00;
        }
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Double getCostSchedule() {
        if(costSchedule!=null){
            return costSchedule;
        }else{
            return 0.00;
        }

    }

    public void setCostSchedule(Double costSchedule) {
        this.costSchedule = costSchedule;
    }

    public Double getBondPerformMoney() {
        if(bondPerformMoney!=null){
            return bondPerformMoney;
        }else{
            return 0.00;
        }
    }

    public void setBondPerformMoney(Double bondPerformMoney) {
        this.bondPerformMoney = bondPerformMoney;
    }

    public Double getTempMoney() {
        if(tempMoney!=null){
            return tempMoney;
        }else{
            return 0.00;
        }
    }

    public void setTempMoney(Double tempMoney) {
        this.tempMoney = tempMoney;
    }

    public String getDiedLine() {
        return diedLine;
    }

    public void setDiedLine(String diedLine) {
        this.diedLine = diedLine;
    }
}
