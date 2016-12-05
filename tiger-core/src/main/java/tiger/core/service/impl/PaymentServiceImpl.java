/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.PaymentObjectDO;
import tiger.common.dal.dataobject.PaymentOrderDO;
import tiger.common.dal.dataobject.PaymentOrderObjectDO;
import tiger.common.dal.dataobject.PaymentRefundDO;
import tiger.common.dal.dataobject.example.PaymentObjectExample;
import tiger.common.dal.dataobject.example.PaymentOrderExample;
import tiger.common.dal.dataobject.example.PaymentOrderObjectExample;
import tiger.common.dal.dataobject.example.PaymentRefundExample;
import tiger.common.dal.enums.OrderStatusEnum;
import tiger.common.dal.enums.PayMethodEnum;
import tiger.common.dal.persistence.PaymentObjectMapper;
import tiger.common.dal.persistence.PaymentOrderMapper;
import tiger.common.dal.persistence.PaymentOrderObjectMapper;
import tiger.common.dal.persistence.PaymentRefundMapper;
import tiger.common.dal.query.OrderQuery;
import tiger.common.util.Paginator;
import tiger.common.util.component.payment.Base.Refund;
import tiger.core.base.PageResult;
import tiger.core.domain.PaymentObjectDomain;
import tiger.core.domain.PaymentOrderDomain;
import tiger.core.domain.PaymentRefundDomain;
import tiger.core.domain.convert.PaymentObjectConvert;
import tiger.core.domain.convert.PaymentOrderConvert;
import tiger.core.domain.convert.PaymentRefundConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.PaymentService;
import tiger.core.util.PaymentFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mi.li
 * @version : v 0.1 2015年10月19日 下午7:37 mi.li Exp $
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentOrderMapper paymentOrderMapper;

    @Autowired
    PaymentObjectMapper paymentObjectMapper;

    @Autowired
    PaymentOrderObjectMapper paymentOrderObjectMapper;

    @Autowired
    PaymentRefundMapper paymentRefundMapper;


    /**
     * 新增一笔订单
     *
     * @see PaymentService#addOrder(PaymentOrderDomain)
     */
    @Override
    public PaymentOrderDomain addOrder(PaymentOrderDomain orderDomain) {
        int rc;
        //插入order
        PaymentOrderDO orderDO = PaymentOrderConvert.convertDomainToDO(orderDomain);
        rc = paymentOrderMapper.insert(orderDO);
        checkReturnCodeThrowException(rc);

        //插入object
        Map<PaymentObjectDO, Integer> paymentObjectMap = new HashMap<PaymentObjectDO, Integer>();
        for (PaymentObjectDomain domain : orderDomain.getObjects()) {
            PaymentObjectDO objectDO = PaymentObjectConvert.convertDomainToDO(domain);
            Integer quantity = domain.getQuantity();
            //TODO: 性能优化
            rc = paymentObjectMapper.insert(objectDO);
            checkReturnCodeThrowException(rc);
            paymentObjectMap.put(objectDO, quantity);
        }

        //插入order-object关系表
        List<PaymentOrderObjectDO> orderObjectDOs = new ArrayList<PaymentOrderObjectDO>();
        for (Map.Entry<PaymentObjectDO, Integer> entry : paymentObjectMap.entrySet()) {
            PaymentOrderObjectDO orderObjectDO = new PaymentOrderObjectDO();
            orderObjectDO.setOrderId(orderDO.getId());
            orderObjectDO.setObjectId(entry.getKey().getId());
            orderObjectDO.setQuantity(entry.getValue());

            orderObjectDOs.add(orderObjectDO);
        }
        rc = paymentOrderObjectMapper.batchInsert(orderObjectDOs);
        checkReturnCodeThrowException(rc);
        return getOrderById(orderDO.getId(), orderDO.getAccountId());
    }

    /**
     * 获取当前用户的订单列表
     *
     * @see PaymentService#allOrders(Long, String)
     */
    @Override
    public List<PaymentOrderDomain> allOrders(Long userId, String status) {
        PaymentOrderExample orderExample = new PaymentOrderExample();
        PaymentOrderExample.Criteria criteria = orderExample.createCriteria();
        if (null != userId)
            criteria.andAccountIdEqualTo(userId);
        if (null != status)
            criteria.andStatusEqualTo(status);
        criteria.andIsDeletedEqualTo(false);
        List<PaymentOrderDO> orderDOs = paymentOrderMapper.selectByExample(orderExample);

        List<PaymentOrderDomain> resultList = getOrderDomainByOrderDO(orderDOs);
        return resultList;
    }

    /**
     * 分页获取当前用户的订单列表
     *
     * @see PaymentService#listOrders(OrderQuery)
     */
    @Override
    public PageResult<List<PaymentOrderDomain>> listOrders(OrderQuery query) {
        PaymentOrderExample example = new PaymentOrderExample();
        PaymentOrderExample.Criteria criteria = example.createCriteria();
        if (null != query.getUserId())
            criteria.andAccountIdEqualTo(query.getUserId());
        if (null != query.getStatus())
            criteria.andStatusEqualTo(query.getStatus());
        criteria.andIsDeletedEqualTo(false);
        int totalItems = paymentOrderMapper.countByExample(example);

        PageResult<List<PaymentOrderDomain>> results = new PageResult<>();

        Paginator paginator = new Paginator();
        paginator.setItems(totalItems);
        paginator.setItemsPerPage(query.getPageSize());
        paginator.setPage(query.getPageNum());

        RowBounds rowBounds = new RowBounds(paginator.getBeginIndex() - 1, paginator.getItemsPerPage());

        List<PaymentOrderDO> orderDOs = paymentOrderMapper.selectByExampleAndPage(example, rowBounds);

        List<PaymentOrderDomain> resultList = getOrderDomainByOrderDO(orderDOs);

        results.setData(resultList);
        results.setPaginator(paginator);

        return results;
    }

    /**
     * 根据id获取订单
     *
     * @see PaymentService#getOrderById(Long, Long)
     */
    @Override
    public PaymentOrderDomain getOrderById(Long orderId, Long accountId) {
        PaymentOrderDO paymentOrderDO = paymentOrderMapper.selectByPrimaryKey(orderId);
        if (null != paymentOrderDO) {
            if (!accountId.equals(paymentOrderDO.getAccountId()))
                throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
            if (!paymentOrderDO.getIsDeleted()) {
                PaymentOrderDomain orderDomain = PaymentOrderConvert.convertDOToDomain(paymentOrderDO);

                orderDomain.setObjects(getObjectsDomainByOrderId(orderId));

                return orderDomain;
            }
        }
        throw new TigerException(ErrorCodeEnum.NOT_FOUND);
    }

    /**
     * 更新一笔订单
     *
     * @see PaymentService#updateOrder(PaymentOrderDomain)
     */
    @Override
    public int updateOrder(PaymentOrderDomain domain) {
        PaymentOrderDO orderDO = PaymentOrderConvert.convertDomainToDO(domain);
        return paymentOrderMapper.updateByPrimaryKeySelective(orderDO);
    }

    /**
     * 删除一笔订单(假删除)
     *
     * @see PaymentService#deleteOrder(long, Long)
     */
    @Override
    public int deleteOrder(long id, Long accountId) {
        PaymentOrderDO paymentOrderDO = paymentOrderMapper.selectByPrimaryKey(id);
        if (null == paymentOrderDO || paymentOrderDO.getIsDeleted())
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        if (accountId != paymentOrderDO.getAccountId())
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
        paymentOrderDO.setIsDeleted(true);
        return paymentOrderMapper.updateByPrimaryKeySelective(paymentOrderDO);
    }

    /**
     * 支付一笔订单
     *
     * @see PaymentService#payOrder(Long, String)
     */
    @Override
    public boolean payOrder(Long orderId, String payMethod) {
        PaymentOrderDO orderDO = paymentOrderMapper.selectByPrimaryKey(orderId);
        orderDO.setStatus(OrderStatusEnum.PAID.getCode());
        orderDO.setPayMethod(payMethod);
        int rc = paymentOrderMapper.updateByPrimaryKey(orderDO);
        return checkReturnCode(rc);
    }

    /**
     * 新增退款
     *
     * @see PaymentService#addRefund(PaymentRefundDomain, Long)
     */
    @Override
    public Long addRefund(PaymentRefundDomain domain, Long accountId) {
        //检查order是否存在
        getOrderById(domain.getOrderId(), accountId);
        PaymentRefundDO paymentRefundDO = PaymentRefundConvert.convertDomainToDO(domain);
        int rc = paymentRefundMapper.insertSelective(paymentRefundDO);
        return paymentRefundDO.getId();
    }

    /**
     * 申请退款
     *
     * @see PaymentService#requestRefund(PaymentRefundDomain)
     */
    @Override
    public boolean requestRefund(PaymentRefundDomain domain) {
        PaymentOrderDO orderDO = paymentOrderMapper.selectByPrimaryKey(domain.getOrderId());
        Refund refund = PaymentFactory.createRefund(PayMethodEnum.ALIPAY.getCode());
        refund.setBatchNo(domain.getBatchNo());
        refund.setBatchNum(1);
        refund.setDetailData("" + orderDO.getTradeNo() + "^" + domain.getTotalFee() + "^" + domain.getReason());
        refund.setRefundId(domain.getId());
        //refund.refund();
        return true;
    }

    /**
     * 获取一笔订单下的所有退款
     *
     * @see PaymentService#getRefundByOrderId(Long, Long)
     */
    @Override
    public List<PaymentRefundDomain> getRefundByOrderId(Long orderId, Long accountId) {
        PaymentOrderDO paymentOrderDO = paymentOrderMapper.selectByPrimaryKey(orderId);
        if (null != paymentOrderDO) {
            if (accountId != paymentOrderDO.getAccountId())
                throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
            if (!paymentOrderDO.getIsDeleted()) {
                PaymentRefundExample example = new PaymentRefundExample();
                example.createCriteria().andOrderIdEqualTo(orderId);
                List<PaymentRefundDO> refundDOs = paymentRefundMapper.selectByExample(example);

                List<PaymentRefundDomain> refundDomains = PaymentRefundConvert.convertDOsToDomains(refundDOs);

                return refundDomains;
            }
        }
        throw new TigerException(ErrorCodeEnum.NOT_FOUND);
    }

    /**
     * 根据id获取退款
     *
     * @see PaymentService#getRefundById(Long, Long)
     */
    @Override
    public PaymentRefundDomain getRefundById(Long id, Long accountId) {
        PaymentRefundDO refundDO = paymentRefundMapper.selectByPrimaryKey(id);
        if (null != refundDO) {
            checkRefundAccount(refundDO, accountId);
            PaymentRefundDomain domain = PaymentRefundConvert.convertDOToDomain(refundDO);
            return domain;
        } else
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
    }

    /**
     * 更新退款
     *
     * @see PaymentService#updateRefund(PaymentRefundDomain, Long)
     */
    @Override
    public int updateRefund(PaymentRefundDomain domain, Long accountId) {
        PaymentRefundDO refundDO = paymentRefundMapper.selectByPrimaryKey(domain.getId());
        if (null == refundDO)
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);

        checkRefundAccount(refundDO, accountId);

        if (null != domain.getStatus())
            refundDO.setStatus(domain.getStatus().getCode());
        if (null != domain.getTotalFee())
            refundDO.setTotalFee(domain.getTotalFee());
        if (null != domain.getNotes())
            refundDO.setNotes(domain.getNotes());
        if (null != domain.getReason())
            refundDO.setReason(domain.getReason());
        return paymentRefundMapper.updateByPrimaryKey(refundDO);
    }

    // ~ private methods

    private void checkReturnCodeThrowException(int rc) {
        if (!checkReturnCode(rc)) throw new TigerException(ErrorCodeEnum.BIZ_FAIL);
    }

    /**
     * Check return code.
     *
     * @param rc the rc
     * @return true, if successful
     */
    private boolean checkReturnCode(int rc) {
        if (rc > 0) {
            return true;
        } else {
            return false;
        }
    }

    private List<PaymentOrderDomain> getOrderDomainByOrderDO(List<PaymentOrderDO> orderDOs) {
        List<PaymentOrderDomain> resultList = new ArrayList<PaymentOrderDomain>();

        //对每一个订单
        for (PaymentOrderDO orderDO : orderDOs) {

            PaymentOrderDomain orderDomain = new PaymentOrderDomain();
            orderDomain = PaymentOrderConvert.convertDOToDomain(orderDO);

            Long orderId = orderDO.getId();

            orderDomain.setObjects(getObjectsDomainByOrderId(orderId));

            resultList.add(orderDomain);
        }
        return resultList;
    }

    private List<PaymentObjectDomain> getObjectsDomainByOrderId(Long orderId) {
        List<PaymentObjectDomain> objectDomains = new ArrayList<PaymentObjectDomain>();

        //获取关系表
        PaymentOrderObjectExample orderObjectExample = new PaymentOrderObjectExample();
        orderObjectExample.createCriteria().andOrderIdEqualTo(orderId);

        List<PaymentOrderObjectDO> orderObjectList = paymentOrderObjectMapper.selectByExample(orderObjectExample);

        //对一个订单内的每一个商品
        for (PaymentOrderObjectDO orderObjectDO : orderObjectList) {
            PaymentObjectExample objectExample = new PaymentObjectExample();
            objectExample.createCriteria().andIdEqualTo(orderObjectDO.getObjectId());
            List<PaymentObjectDO> objectDOs = paymentObjectMapper.selectByExample(objectExample);
            PaymentObjectDO objectDO = objectDOs.get(0);

            PaymentObjectDomain objectDomain = PaymentObjectConvert.convertDOToDomain(objectDO);
            objectDomain.setQuantity(orderObjectDO.getQuantity());

            objectDomains.add(objectDomain);
        }
        return objectDomains;
    }

    /**
     * 检查退款对应的order是否是本人的
     *
     * @param refundDO
     * @param accountId
     */
    private void checkRefundAccount(PaymentRefundDO refundDO, Long accountId) {
        PaymentOrderDO orderDO = paymentOrderMapper.selectByPrimaryKey(refundDO.getOrderId());
        if (accountId != orderDO.getAccountId())
            throw new TigerException(ErrorCodeEnum.UNAUTHORIZED);
    }

}
