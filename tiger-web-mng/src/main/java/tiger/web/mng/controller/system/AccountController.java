package tiger.web.mng.controller.system;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.mng.account.support.MngAccountManager;
import tiger.common.dal.enums.MessageTypeEnum;
import tiger.common.dal.enums.NotificationKeyEnum;
import tiger.common.dal.query.AccountQuery;
import tiger.common.util.Paginator;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.Notification.NotificationBasicDomain;
import tiger.core.domain.mng.MngAccountDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.AccountService;
import tiger.core.service.component.KafkaService;
import tiger.web.mng.controller.base.BaseController;
import tiger.web.mng.form.system.AccountUpdateForm;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiresAuthentication
public class AccountController extends BaseController {

    private Logger logger = Logger.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private MngAccountManager accountManager;
    @Autowired
    private KafkaService kafkaService;

    /**
     * 用户管理页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String account(@ModelAttribute("model") ModelMap model,
                          @Valid AccountQuery query,
                          BindingResult bindingResult) {
        PageResult<List<AccountDomain>> pageResult = accountService.query(query);

        model.addAttribute("userList", accountManager.accountLoanCounts(pageResult.getData()));
        Paginator paginator = pageResult.getPaginator();
        model.addAttribute("paginator", paginator);
        model.addAttribute("slider", paginator.getSlider());
        return "/systemConfig/accounts";
    }

    /**
     * 更新用户id
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.POST)
    @ResponseBody
    public boolean update(@PathVariable("id") Long id,
                          @Valid AccountUpdateForm form,
                          BindingResult bindingResult) {
        AccountDomain accountDomain = form.convert2Domain();
        accountDomain.setId(id);
        return accountService.updateAccount(accountDomain);
    }

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public String getAccountProfile(@PathVariable("id") Long id,
                                    @ModelAttribute("model") ModelMap model) {
        model.put("account", accountService.read(id));
        return "/systemConfig/modal/accountEdit";
    }

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean delete(@PathVariable("id") Long id) {
        if (logger.isInfoEnabled()) {
            logger.info("管理员 [" + currentAccount() + "] 删除用户 [" + id + "]");
        }
        return accountService.delete(id);
    }


    /**
     * 修改账户的状态
     *
     * @return
     */
    @RequestMapping(value = "/changeAccountStatus/{id}", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Integer> changeAccountStatus(@PathVariable("id") Long accountId, @RequestParam String status,@RequestParam Long workspaceId) {
        //如果传递过来的账户ID是null,不予更新
        if(accountId==null||workspaceId==null){
            return new BaseResult<>(ErrorCodeEnum.PARAMETERS_IS_NULL, 0);
        }
        //status 为要更新的状态，如果是0则是禁用，1则是启用

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("id",accountId);

        if(AccountService.ACCOUNT_STATUS.equals(status)){
            //将该账户禁用  之后该账户就不能登录小贷管家
            map.put("status",status);
        }else{
            //将该账户启用，可以登录
            map.put("status",null);
        }
        int result=accountService.changeAccountStatus(map);
//        if(result!=0){
//            // 发送消息到kafka
//            kafkaService.sendOneToKafka(NotificationKeyEnum.CUSTOMER, new NotificationBasicDomain(workspaceId, accountId,accountId,  MessageTypeEnum.ACCOUNT_NO_LOGIN, null));
//        }
        return new BaseResult<>(result);
    }

}
