/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.mng.controller.communication;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tiger.biz.mng.feedback.support.FeedbackManager;
import tiger.common.dal.query.FeedbackQuery;
import tiger.common.util.Paginator;
import tiger.core.base.PageResult;
import tiger.core.domain.listDomain.FeedbackListDomain;
import tiger.web.mng.controller.base.BaseController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author alfred_yuan
 * @version v 0.1 22:08 alfred_yuan Exp $
 */
@Controller
@RequiresAuthentication
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackManager feedbackManager;

    /**
     * 用户页面反馈列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/feedbacks", method = RequestMethod.GET)
    public String feedbackListPage(@ModelAttribute("model") ModelMap model,
                                   @Valid FeedbackQuery query,
                                   BindingResult bindingResult) {
        PageResult<List<FeedbackListDomain>> pageResult = feedbackManager.getFeedbackWithAccount(query);
        model.addAttribute("feedbackList", pageResult.getData());
        Paginator paginator = pageResult.getPaginator();
        model.addAttribute("paginator", paginator);
        model.addAttribute("slider", paginator.getSlider());
        return "/communication/feedbacks";
    }

    @RequestMapping(value = "/feedback/{id}", method = RequestMethod.GET)
    public String feedbackModal(@ModelAttribute("model") ModelMap model,
                                @PathVariable("id") Long id) {
        model.addAttribute("feedback", feedbackManager.getFeedbackById(id));

        return "/communication/feedbackDetail";
    }
}
