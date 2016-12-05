/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.account;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.common.dal.annotation.LoginRequired;
import tiger.core.base.BaseResult;
import tiger.core.domain.AccountDomain;
import tiger.core.domain.CompanyDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.CompanyService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.company.CompanyForm;

import javax.validation.Valid;

/**
 * 用户公司信息管理接口
 *
 * @author zhangbin
 * @version v0.1 2015/10/1 15:51
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/account")
public class CompanyController extends BaseController {
    private static Logger logger = Logger.getLogger(CompanyController.class);

    @Autowired
    CompanyService companyService;

    /**
     * 登录用户查看自己的公司信息.
     *
     * @return
     */
    @RequestMapping(value = "/company", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> getUserCompany() {
        AccountDomain accountDomain = this.currentAccount();
        checkAccountAndCompany(accountDomain);
        CompanyDomain domain = companyService.getCompanyById(accountDomain.getCompanyId());
        if (domain == null) {
            return new BaseResult<>(ErrorCodeEnum.NOT_FOUND.getCode(), "没有找到任何公司信息");
        } else {
            return new BaseResult<>(domain);
        }
    }

    /**
     * 更新当前用户的公司信息。
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/company", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> updateUserCompany(
            @RequestBody @Valid CompanyForm form,
            BindingResult bindingResult) {
        AccountDomain accountDomain = this.currentAccount();
        checkAccountAndCompany(accountDomain);

        CompanyDomain companyDomain = form.convert2Domain();
        companyDomain.setId(accountDomain.getCompanyId());

        return new BaseResult<>(companyService.updateCompany(companyDomain));
    }

    // ~ Private methods
    private void checkAccountAndCompany(AccountDomain accountDomain) {
        if (accountDomain == null) {
            logger.error("获取登陆用户失败");
            throw new TigerException(ErrorCodeEnum.REQUIRED_LOGIN.getCode(), "需要重新登录");
        }
        if (accountDomain.getCompanyId() < 1L) {
            logger.error("用户[\" + accountDomain + \"]单位信息有误");
            throw new TigerException(ErrorCodeEnum.NOT_FOUND.getCode(), "用户单位信息有误");
        }
    }
}
