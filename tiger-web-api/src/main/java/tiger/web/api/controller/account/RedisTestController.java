/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tiger.core.base.BaseResult;
import tiger.core.domain.AccountDomain;
import tiger.core.service.AccountService;
import tiger.web.api.controller.base.BaseController;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 7:30 PM yiliang.gyl Exp $
 */
@RestController
public class RedisTestController extends BaseController {

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/test-redis", method = RequestMethod.GET)
    @ResponseBody
    public BaseResult<AccountDomain> read(@RequestParam("id") Long id) {
        return new BaseResult<>(accountService.readWithPermissions(1000l));
    }
}
