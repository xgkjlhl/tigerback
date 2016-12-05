/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.common.dal.query.OrderQuery;
import tiger.core.base.PageResult;
import tiger.core.domain.PaymentOrderDomain;
import tiger.core.domain.PaymentRefundDomain;

import java.util.List;

/**
 * @author mi.li
 * @version : v 0.1 2015年10月19日 下午7:36 mi.li Exp $
 */
public interface PaymentService {
    /**
     * 新增一笔订单
     *
     * @param order
     * @return
     */
    PaymentOrderDomain addOrder(PaymentOrderDomain order);

    /**
     * 获取当前用户的订单列表
     *
     * @param userId
     * @param status
     * @return
     */
    List<PaymentOrderDomain> allOrders(Long userId, String status);

    /**
     * 分页获取当前用户的订单列表
     *
     * @param query
     * @return
     */
    PageResult<List<PaymentOrderDomain>> listOrders(OrderQuery query);

    /**
     * 根据id获取订单
     *
     * @param id
     * @return
     */
    PaymentOrderDomain getOrderById(Long id, Long accountId);

    /**
     * 更新一笔订单
     *
     * @param domain
     * @return
     */
    int updateOrder(PaymentOrderDomain domain);

    /**
     * 删除一笔订单
     *
     * @param id
     * @param accountId
     * @return
     */
    int deleteOrder(long id, Long accountId);


    /**
     * 支付一笔订单
     *
     * @param orderId
     * @param payMethod
     * @return
     */
    boolean payOrder(Long orderId, String payMethod);

    /**
     * 新增退款
     *
     * @param domain
     * @return
     */
    Long addRefund(PaymentRefundDomain domain, Long accountId);

    /**
     * 申请退款
     *
     * @param domain
     * @return
     */
    boolean requestRefund(PaymentRefundDomain domain);

    /**
     * 获取一笔订单下的所有退款
     *
     * @param orderId
     * @return
     */
    List<PaymentRefundDomain> getRefundByOrderId(Long orderId, Long accountId);

    /**
     * 根据id获取退款
     *
     * @param id
     * @return
     */
    PaymentRefundDomain getRefundById(Long id, Long accountId);

    /**
     * 更新退款
     *
     * @param domain
     * @param accountId
     * @return
     */
    int updateRefund(PaymentRefundDomain domain, Long accountId);

}
