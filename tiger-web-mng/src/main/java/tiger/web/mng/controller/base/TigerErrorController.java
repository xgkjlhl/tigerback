/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.web.mng.controller.base;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author alfred_yuan
 * @version v 0.1 01:45 alfred_yuan Exp $
 */
@Controller
public class TigerErrorController implements ErrorController {
    private static final String PATH = "/error";

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = PATH)
    public String error(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);

        return "base/error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
