package tiger.web.mng.controller.staff;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tiger.biz.mng.staff.support.StaffManager;
import tiger.common.dal.query.StaffQuery;
import tiger.core.domain.StaffDomain;
import tiger.web.mng.controller.base.BaseController;
import tiger.web.mng.form.staff.SimpleResetPasswordForm;
import tiger.web.mng.form.staff.StaffCreateForm;

import javax.validation.Valid;

@Controller
@RequiresAuthentication
public class StaffController extends BaseController {

    public static final String DEFAULT_PASSWORD = "123456";
    @Autowired
    private StaffManager staffManager;

    /**
     * 获取管理员列表
     *
     * @return
     */
    @RequestMapping(value = "/staffs", method = RequestMethod.GET)
    public String viewAllStaffs(@ModelAttribute("model") ModelMap model,
                                @Valid StaffQuery query,
                                BindingResult bindingResult) {
        model.addAttribute("staffs", staffManager.query(query));

        return "/staff/staffs";
    }

    /**
     * 获取修改密码页面
     *
     * @return
     */
    @RequestMapping(value = "/staff/password", method = RequestMethod.GET)
    public String getPasswordUpdatePage() {
        return "/staff/passwordUpdate";
    }

    /**
     * 重置密码操作
     */
    @RequestMapping(value = "/staff/password", method = RequestMethod.POST)
    @ResponseBody
    public boolean updatePassword(@Valid SimpleResetPasswordForm form,
                                  BindingResult bindingResult) {
        StaffDomain staffDomain = form.convert2Domain();
        staffDomain.setId(currentAccount().getId());

        return staffManager.updatePassword(staffDomain);
    }

    @RequestMapping(value = "/staff", method = RequestMethod.GET)
    public String getCreatePage() {
        return "/staff/createStaff";
    }

    @RequestMapping(value = "/staff", method = RequestMethod.POST)
    @ResponseBody
    public boolean createStaff(@Valid StaffCreateForm form,
                               BindingResult bindingResult) {
        StaffDomain staff = form.convert2Domain();
        staff.setPassword(DEFAULT_PASSWORD);

        return staffManager.create(staff);
    }
}
