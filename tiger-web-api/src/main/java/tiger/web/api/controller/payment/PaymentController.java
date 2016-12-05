/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.controller.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tiger.biz.payment.support.PaymentManager;
import tiger.common.dal.annotation.LoginRequired;
import tiger.common.dal.enums.OrderStatusEnum;
import tiger.common.dal.enums.RefundStatusEnum;
import tiger.common.dal.query.OrderQuery;
import tiger.common.util.StringUtil;
import tiger.core.base.BaseResult;
import tiger.core.base.PageResult;
import tiger.core.domain.PaymentOrderDomain;
import tiger.core.domain.PaymentRefundDomain;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.service.PaymentService;
import tiger.web.api.controller.base.BaseController;
import tiger.web.api.form.payment.OrderForm;
import tiger.web.api.form.payment.OrderUpdateForm;
import tiger.web.api.form.payment.RefundForm;
import tiger.web.api.form.payment.RefundUpdateForm;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 该方法废弃
 *
 * @author mi.li
 * @version v 0.1 2015年10月19日 下午7:34 mi.li Exp $
 */
//@RestController
//@ResponseBody
public class PaymentController extends BaseController {

    private static final int DEFAULT_PAGE_SIZE = 10;

    @Autowired
    PaymentManager paymentManager;

    @Autowired
    PaymentService paymentService;

    /**
     * 新增一笔订单
     *
     * @param orderForm
     * @param bingdingResult
     * @return
     */
    @RequestMapping(value = "/payment/order", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<PaymentOrderDomain> addOrder(@Valid @RequestBody OrderForm orderForm, BindingResult bingdingResult) {
        PaymentOrderDomain domain = orderForm.convert2Domain();
        domain.setAccountId(currentAccount().getId());

        return paymentManager.addOrder(domain);
    }

    /**
     * 获取当前用户的订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/payment/orders", method = RequestMethod.GET, params = "scope=all")
    @ResponseBody
    @LoginRequired
    public BaseResult<List<PaymentOrderDomain>> allOrders(HttpServletRequest request) {
        String status = null;
        if (StringUtil.isNotBlank(request.getParameter("status")))
            status = request.getParameter("status");

        List<PaymentOrderDomain> list = paymentService.allOrders(currentAccount().getId(), status);
        return new BaseResult<>(list);
    }

    /**
     * 分页获取当前用户的订单列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "payment/orders", method = RequestMethod.GET, params = "scope=list")
    @ResponseBody
    @LoginRequired
    public PageResult<List<PaymentOrderDomain>> listOrders(HttpServletRequest request) {
        OrderQuery oq = new OrderQuery();
        oq.setUserId(currentAccount().getId());
        if (StringUtil.isNotBlank(request.getParameter("status")))
            oq.setStatus(request.getParameter("status"));
        if (StringUtil.isNotBlank(request.getParameter("pageSize")))
            oq.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
        else
            oq.setPageSize(DEFAULT_PAGE_SIZE);

        if (StringUtil.isNotBlank(request.getParameter("pageNum")))
            oq.setPageNum(Integer.parseInt(request.getParameter("pageNum")));

        return paymentService.listOrders(oq);
    }

    /**
     * 获取一个订单
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "payment/order/{id}", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<PaymentOrderDomain> getOrderById(@PathVariable("id") long id) {
        PaymentOrderDomain domain = paymentService.getOrderById(id, currentAccount().getId());

        return new BaseResult<>(domain);
    }

    /**
     * 更新一笔订单
     *
     * @param form
     * @param bingdingResult
     * @param id
     * @return
     */
    @RequestMapping(value = "payment/order/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> updateOrderById(@RequestBody @Valid OrderUpdateForm form,
                                              BindingResult bingdingResult,
                                              @PathVariable("id") long id) {
        PaymentOrderDomain domain = new PaymentOrderDomain();
        domain.setId(id);
        domain.setStatus(OrderStatusEnum.getEnumByCode(form.getStatus()));
        domain.setTotalFee(form.getTotalFee());
        domain.setDiscount(form.getDiscount());

        return paymentManager.updateOrder(domain, currentAccount().getId());
    }

    /**
     * 删除一笔订单 - 假删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "payment/order/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> deleteOrderById(@PathVariable("id") long id) {
        int rc = paymentService.deleteOrder(id, currentAccount().getId());
        return checkReturnCode(rc);
    }

    /**
     * 支付一笔订单
     *
     * @param request
     * @param orderId
     * @return
     */
    @RequestMapping(value = "payment/order/{id}/pay", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> payOrder(HttpServletRequest request, @PathVariable("id") long orderId) {
        String payMethod = null;
        if (StringUtil.isNotBlank(request.getParameter("payMethod"))) {
            payMethod = request.getParameter("payMethod");
            return paymentManager.payOrder(currentAccount().getId(), orderId, payMethod);
        }
        return new BaseResult<>(ErrorCodeEnum.ILLEGAL_PARAMETER);
    }

    /**
     * @param request
     * @param orderId
     */
    @RequestMapping(value = "payment/order/{id}/pay/return", method = RequestMethod.GET)
    @ResponseBody
    public void payReturn(HttpServletRequest request, @PathVariable("id") long orderId) {
        paymentManager.afterPay(request, "RETURN", currentAccount().getId());
    }

    /**
     * @param request
     * @param orderId
     */
    @RequestMapping(value = "payment/order/{id}/pay/notify", method = RequestMethod.GET)
    @ResponseBody
    public void payNotify(HttpServletRequest request, @PathVariable("id") long orderId) {
        paymentManager.afterPay(request, "NOTIFY", currentAccount().getId());
    }

    /**
     * 对一笔订单进行退款
     *
     * @param refundForm
     * @param bingdingResult
     * @param id
     * @return
     */
    @RequestMapping(value = "payment/order/{id}/refund", method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public BaseResult<PaymentRefundDomain> addRefund(@Valid @RequestBody RefundForm refundForm, BindingResult bingdingResult, @PathVariable("id") long id) {
        PaymentRefundDomain domain = refundForm.convert2Domain();
        domain.setOrderId(id);
        domain.setStatus(RefundStatusEnum.APPLY);

        return paymentManager.addRefund(domain, currentAccount().getId());
    }

    @RequestMapping(value = "payment/refund/{id}/notify", method = RequestMethod.GET)
    @ResponseBody
    public void refundNotify(HttpServletRequest request, @PathVariable("id") long refundId) {
        paymentManager.afterRefund(request, refundId, currentAccount().getId());
    }

    /**
     * 获取一笔订单下的所有退款
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "payment/order/{id}/refunds", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<List<PaymentRefundDomain>> getRefundByOrderId(@PathVariable("id") long id) {
        List<PaymentRefundDomain> domains = paymentService.getRefundByOrderId(id, currentAccount().getId());

        return new BaseResult<>(domains);
    }

    /**
     * 获取一笔退款
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "payment/refund/{id}", method = RequestMethod.GET)
    @ResponseBody
    @LoginRequired
    public BaseResult<PaymentRefundDomain> getRefundById(@PathVariable("id") long id) {
        PaymentRefundDomain domain = paymentService.getRefundById(id, currentAccount().getId());
        return new BaseResult<>(domain);
    }

    /**
     * 更新一笔退款
     *
     * @param form
     * @param bingdingResult
     * @param id
     * @return
     */
    @RequestMapping(value = "payment/refund/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @LoginRequired
    public BaseResult<Object> updateRefundById(@RequestBody @Valid RefundUpdateForm form,
                                               BindingResult bingdingResult,
                                               @PathVariable("id") long id) {
        PaymentRefundDomain domain = new PaymentRefundDomain();

        domain.setId(id);
        domain.setNotes(form.getNotes());
        domain.setReason(form.getReason());
        domain.setStatus(RefundStatusEnum.getEnumByCode(form.getStatus()));
        domain.setTotalFee(form.getTotalFee());

        int rc = paymentService.updateRefund(domain, currentAccount().getId());
        return checkReturnCode(rc);
    }

    // ~ private methods

    private BaseResult<Object> checkReturnCode(int rc) {
        if (rc > 0) {
            return new BaseResult<>(true);
        } else {
            return new BaseResult<>(ErrorCodeEnum.BIZ_FAIL, false);
        }
    }
}


