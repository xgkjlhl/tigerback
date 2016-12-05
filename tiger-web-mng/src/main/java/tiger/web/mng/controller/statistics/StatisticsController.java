/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.mng.controller.statistics;

import mng.core.service.StatisticsService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tiger.common.dal.query.StatisticsQuery;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 16:01 alfred_yuan Exp $
 */
@Controller
@RequiresAuthentication
@RequestMapping(value = "/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/accountIncrease", method = RequestMethod.GET)
    public String accountIncrease(@ModelAttribute("model") ModelMap model,
                                  @Valid StatisticsQuery query,
                                  BindingResult bindingResult) {
        Date from = query.getFrom();
        model.addAttribute("sum", statisticsService.sumAccountBeforeDate(from));
        model.addAttribute("data", statisticsService.accountGrowthRangeStatistics(from, query.getTo(), query.getUnit()));
        fillModel(model, query);

        return "/statistics/accountIncrease";
    }

    @RequestMapping(value = "/loanStatistics", method = RequestMethod.GET)
    public String loanIncrease(@ModelAttribute("model") ModelMap model,
                               @Valid StatisticsQuery query,
                               BindingResult bindingResult) {
        Date from = query.getFrom();
        model.addAttribute("sum", statisticsService.sumLoanBeforeDate(from));
        model.addAttribute("data", statisticsService.loanGrowthRangeStatistics(from, query.getTo(), query.getUnit()));
        fillModel(model, query);

        return "/statistics/loanStatistics";
    }

    /**
     * 给model增加更多内容, 包括from, to, unit, months, years
     *
     * @param model
     * @param query
     */
    private void fillModel(@ModelAttribute("model") ModelMap model, @Valid StatisticsQuery query) {
        model.addAttribute("from", query.getFrom());
        model.addAttribute("to", query.getTo());
        model.addAttribute("unit", query.getUnit().getCode());
        model.addAttribute("months", getYearMonths());
        model.addAttribute("years", getLastFiveYears());
    }


    /**
     * 获取当前月份(N-1)至一月(0)的逆序列表
     *
     * @return
     */
    public List<Integer> getYearMonths() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

        List<Integer> months = new ArrayList<>(month + 1);
        while (month >= 0) {
            months.add(month);
            month--;
        }

        return months;
    }

    /**
     * 获取最近5年的年份列表
     *
     * @return
     */
    public List<Integer> getLastFiveYears() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        List<Integer> years = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            years.add(year);
            year--;
        }

        return years;
    }
}
