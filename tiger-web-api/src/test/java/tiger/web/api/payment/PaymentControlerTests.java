/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.web.api.payment;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import tiger.common.dal.enums.ObjectTypeEnum;
import tiger.common.dal.enums.OrderStatusEnum;
import tiger.common.util.JsonUtil;
import tiger.web.api.base.AbstractIntegrationTests;
import tiger.web.api.form.payment.ObjectForm;
import tiger.web.api.form.payment.OrderForm;
import tiger.web.api.form.payment.RefundForm;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mi.li
 * @version v 0.1 15/12/5 下午8:50 mi.li Exp $
 */
public class PaymentControlerTests extends AbstractIntegrationTests {
    @Test
    public void testOrder() throws Exception {
        // ~ 增
        logger.info("开始测试新增一个订单");

        OrderForm orderForm = new OrderForm();
        orderForm.setTotalFee(10998.0);
        orderForm.setDiscount(2.0);
        orderForm.setNotes("优惠2元");

        List<ObjectForm> objects = new ArrayList<>();
        //商品1
        ObjectForm object1 = new ObjectForm();
        object1.setType(ObjectTypeEnum.PACKAGE.getCode());
        object1.setIdInType(Long.valueOf(1));
        object1.setSubject("白金会员1个月");
        object1.setPrice(500.0);
        object1.setShowUrl("www.huiyuan.com");
        object1.setNotes("活动价");
        object1.setQuantity(12);
        objects.add(object1);

        //商品2
        ObjectForm object2 = new ObjectForm();
        object2.setType(ObjectTypeEnum.ADDON.getCode());
        object2.setIdInType(Long.valueOf(2));
        object2.setSubject("短信包");
        object2.setPrice(50.0);
        object2.setShowUrl("www.duanxinbao.com");
        object2.setNotes("多买优惠");
        object2.setQuantity(100);
        objects.add(object2);

        orderForm.setObjects(objects);

        MvcResult result = mockMvc.perform(post("/payment/order")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(orderForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.totalFee", is(10998.0)))
                .andExpect(jsonPath("$.data.discount", is(2.0)))
                .andExpect(jsonPath("$.data.notes", is("优惠2元")))
                .andReturn();

        logger.info("结束测试新增一个订单");

        //获取订单id
        Integer orderId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        logger.info("新增订单的id为" + orderId);

        // ~ 查
        logger.info("开始测试获取一个订单");
        mockMvc.perform(get("/payment/order/" + orderId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", is(orderId)));
        logger.info("结束测试获取一个订单");

        // ~ 改
        logger.info("开始测试更新一个订单");
        orderForm.setStatus(OrderStatusEnum.CLOSED.getCode());
        orderForm.setTotalFee(10.99);
        orderForm.setDiscount(2.5);
        mockMvc.perform(put("/payment/order/" + orderId)
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(orderForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试更新一个订单");

        //支付一笔订单
        logger.info("开始测试支付一笔订单");
        mockMvc.perform(put("/payment/order/" + orderId + "/pay").param("payMethod", "ALIPAY")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试支付一笔订单");

        // ~ 删
        logger.info("开始测试删除一个订单");
        mockMvc.perform(delete("/payment/order/" + orderId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试删除一个订单");
    }

    @Test
    public void testOrders() throws Exception {
        logger.info("开始测试获取当前用户的订单列表");

        MvcResult result = mockMvc.perform(get("/payment/orders").param("scope", "all")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andReturn();
        logger.info(result.getResponse().getContentAsString());

        mockMvc.perform(get("/payment/orders").param("scope", "list")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));

        mockMvc.perform(get("/payment/orders").param("scope", "list").param("status", OrderStatusEnum.UNPAID.getCode())
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));

        logger.info("结束测试获取当前用户的订单列表");
    }

    @Test
    public void testRefund() throws Exception {
        OrderForm orderForm = new OrderForm();
        orderForm.setTotalFee(10998.0);
        orderForm.setDiscount(2.0);
        orderForm.setNotes("优惠2元");

        List<ObjectForm> objects = new ArrayList<>();
        //商品1
        ObjectForm object1 = new ObjectForm();
        object1.setType(ObjectTypeEnum.PACKAGE.getCode());
        object1.setIdInType(Long.valueOf(1));
        object1.setSubject("白金会员1个月");
        object1.setPrice(500.0);
        object1.setShowUrl("www.huiyuan.com");
        object1.setNotes("活动价");
        object1.setQuantity(12);
        objects.add(object1);

        //商品2
        ObjectForm object2 = new ObjectForm();
        object2.setType(ObjectTypeEnum.ADDON.getCode());
        object2.setIdInType(Long.valueOf(2));
        object2.setSubject("短信包");
        object2.setPrice(50.0);
        object2.setShowUrl("www.duanxinbao.com");
        object2.setNotes("多买优惠");
        object2.setQuantity(100);
        objects.add(object2);

        orderForm.setObjects(objects);

        MvcResult result = mockMvc.perform(post("/payment/order")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(orderForm)))
                .andReturn();

        //获取订单id
        Integer orderId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        logger.info("新增订单的id为" + orderId);

        // ~ 增
        logger.info("开始测试对一笔订单进行退款");
        RefundForm refundForm = new RefundForm();
        refundForm.setTotalFee(5.0);
        refundForm.setReason("不喜欢");
        refundForm.setNotes("退全款");
        result = mockMvc.perform(post("/payment/order/" + orderId + "/refund")
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(refundForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.totalFee", is(5.0)))
                .andExpect(jsonPath("$.data.reason", is("不喜欢")))
                .andExpect(jsonPath("$.data.notes", is("退全款")))
                .andReturn();
        logger.info("结束测试对一笔订单进行退款");

        //获取退款id
        Integer refundId = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
        logger.info("新增退款的id为" + refundId);

        //获取某一订单下的退款
        logger.info("开始测试获取某一订单下的退款");
        mockMvc.perform(get("/payment/order/" + orderId + "/refunds")
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")));
        logger.info("结束测试获取某一订单下的退款");

        // ~ 查
        logger.info("开始测试获取一笔退款");
        mockMvc.perform(get("/payment/refund/" + refundId)
                .header(TOKEN_KEY, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data.id", is(refundId)));
        logger.info("结束测试获取一笔退款");

        // ~ 改
        logger.info("开始测试更新一笔退款");
        refundForm.setStatus("CLOSED");
        mockMvc.perform(put("/payment/refund/" + refundId)
                .header(TOKEN_KEY, token)
                .contentType("application/json")
                .content(JsonUtil.toJson(refundForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("200")))
                .andExpect(jsonPath("$.data", is(true)));
        logger.info("结束测试更新一笔退款");
    }
}
