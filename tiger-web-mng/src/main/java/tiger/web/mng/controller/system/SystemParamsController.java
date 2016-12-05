package tiger.web.mng.controller.system;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.common.dal.enums.SystemParamTypeEnum;
import tiger.common.dal.query.SystemParamsQuery;
import tiger.core.domain.SystemParamDomain;
import tiger.core.service.SystemParamService;
import tiger.web.mng.controller.base.BaseController;
import tiger.web.mng.form.system.SystemParamForm;

import javax.validation.Valid;
import java.util.Arrays;


/**
 * Created by tiny on 15/12/26.
 */
@Controller
@RequiresAuthentication
public class SystemParamsController extends BaseController {
    public static final String MODAL_SYSTEM_PARAMS_EDIT = "/systemConfig/modal/systemParamsEdit";

    private Logger logger = Logger.getLogger(SystemParamsController.class);

    @Autowired
    SystemParamService systemParamService;

    /**
     * 查询系统参数
     *
     * @param query
     * @return
     */
    @RequestMapping(value = "/systemParams", method = RequestMethod.GET)
    public String querySystemParams(@ModelAttribute("model") ModelMap model,
                                    SystemParamsQuery query) {
        model.addAttribute("systemParams", systemParamService.query(query));
        model.addAttribute("enumList", Arrays.asList(SystemParamTypeEnum.values()));

        return "/systemConfig/systemParams";
    }

    /**
     * 获取新建系统参数页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/systemParam", method = RequestMethod.GET)
    public String getCreateForm(@ModelAttribute("model") ModelMap model) {
        model.addAttribute("modalTitle", "新建系统参数");
        model.addAttribute("systemParam", new SystemParamDomain());
        model.addAttribute("enumList", Arrays.asList(SystemParamTypeEnum.values()));

        return MODAL_SYSTEM_PARAMS_EDIT;
    }

    /**
     * 新建系统参数
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/systemParam", method = RequestMethod.POST)
    @ResponseBody
    public boolean create(@Valid SystemParamForm form,
                          BindingResult bindingResult) {
        SystemParamDomain systemParamDomain = form.convert2Domain();

        return systemParamService.add(systemParamDomain);
    }

    /**
     * 更新系统参数id
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/systemParam/{id}", method = RequestMethod.POST)
    @ResponseBody
    public boolean update(@PathVariable("id") Long id,
                          @Valid SystemParamForm form,
                          BindingResult bindingResult) {
        SystemParamDomain systemParamDomain = form.convert2Domain();
        systemParamDomain.setId(id);

        return systemParamService.update(systemParamDomain);

    }

    /**
     * 根据id获取系统参数信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/systemParam/{id}", method = RequestMethod.GET)
    public String select(@PathVariable("id") Long id,
                         @ModelAttribute("model") ModelMap model) {
        model.addAttribute("modalTitle", "新建系统参数");
        model.addAttribute("systemParam", systemParamService.read(id));
        model.addAttribute("enumList", Arrays.asList(SystemParamTypeEnum.values()));

        return MODAL_SYSTEM_PARAMS_EDIT;
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/systemParam/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean delete(@PathVariable("id") Long id) {
        if (logger.isInfoEnabled()) {
            logger.info("管理员 [" + currentAccount() + "] 删除用户 [" + id + "]");
        }

        return systemParamService.delete(id);
    }

}
