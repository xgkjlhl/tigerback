package tiger.web.mng.controller.loan;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tiger.biz.mng.loan.support.LoanMngManager;
import tiger.core.base.BaseResult;
import tiger.web.mng.controller.base.BaseController;

/**
 * Created by Jaric Liao on 2016/3/21.
 */
@Controller
@RequiresAuthentication
public class LoanController extends BaseController {

    private Logger logger = Logger.getLogger(LoanController.class);

    @Autowired
    private LoanMngManager loanMngManager;

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/loan/{id}/loaner", method = RequestMethod.PUT)
    @ResponseBody
    public BaseResult<Boolean> syncronizeLoanerSnapshot(@PathVariable("id") Long id) {
        return new BaseResult<>(loanMngManager.synchronizeSnapshot(id));
    }

    /**
     * @param
     * @return
     */
    @RequestMapping(value = "/loans/loaner", method = RequestMethod.PUT)
    @ResponseBody
    public BaseResult<Boolean> syncronizeAllLoanerSnapshots(@RequestParam(value = "startId") Long startId,
                                                            @RequestParam(value = "endId") Long endId) {
        return new BaseResult<>(loanMngManager.synchronizeAllSnapshots(startId, endId));
    }
}
