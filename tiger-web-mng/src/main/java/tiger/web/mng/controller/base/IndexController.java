/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.mng.controller.base;

import mng.core.service.StatisticsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tiger.common.util.DateUtil;
import tiger.web.mng.form.LoginForm;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 9:59 PM yiliang.gyl Exp $
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresAuthentication
    public String index(@ModelAttribute("model") ModelMap model) {
        Date now = DateUtil.getDayEnd(new Date());
        Date monthBegin = DateUtil.getFirstDayOfMonth(now);
        Date dayBegin = DateUtil.getDayBegin(now);

        model.addAttribute("accountDailyGrowth", statisticsService.calAccountGrowthRate(dayBegin, now));
        model.addAttribute("accountMonthlyGrowth", statisticsService.calAccountGrowthRate(monthBegin, now));

        model.addAttribute("loanDailyGrowth", statisticsService.calLoanGrowthRate(dayBegin, now));
        model.addAttribute("loanMonthlyGrowth", statisticsService.calLoanGrowthRate(monthBegin, now));

        model.addAttribute("staff", currentAccount());

        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(HttpServletResponse response) {
        Subject currentUser = SecurityUtils.getSubject();

        if (currentUser.isAuthenticated()) {
            return "redirect:/index";
        }

        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String userLogin(ServletRequest req, ServletResponse res, LoginForm loginForm) {
        try {
            Subject currentUser = SecurityUtils.getSubject();
            currentUser.login(new UsernamePasswordToken(loginForm.getUsername(), loginForm.getPassword()));
            WebUtils.redirectToSavedRequest(req, res, "/index");
            return null; //redirect
        } catch (AuthenticationException | IOException e) {
            return "login";
        }
    }
}
