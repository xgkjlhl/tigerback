/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.CustomerMsgDO;
import tiger.common.dal.dataobject.example.CustomerMsgExample;
import tiger.common.dal.enums.CustomerMsgBizTypeEnum;
import tiger.common.dal.persistence.CustomerMsgMapper;
import tiger.core.domain.CustomerMsgDomain;
import tiger.core.domain.convert.CustomerMsgConverter;
import tiger.core.service.CustomerMsgService;

import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/10/16 18:10
 */
@Service
public class CustomerMsgServiceImpl implements CustomerMsgService {
    @Autowired
    private CustomerMsgMapper mapper;

    /**
     * @see CustomerMsgService#getCustomerMsgById(Long)
     */
    @Override
    public CustomerMsgDomain getCustomerMsgById(Long id) {

        CustomerMsgDO customerMsgDO = mapper.selectByPrimaryKey(id);
        return CustomerMsgConverter.convert2Domain(customerMsgDO);
    }

    /**
     * @see CustomerMsgService#updateMsgState(Long, String)
     */
    @Override
    public boolean updateMsgState(Long id, String state) {

        CustomerMsgDO customerMsgDO = new CustomerMsgDO();
        customerMsgDO.setStatus(state);
        customerMsgDO.setId(id);

        return mapper.updateByPrimaryKey(customerMsgDO) == 1;
    }

    /**
     * @see CustomerMsgService#insert(CustomerMsgDomain)
     */
    @Override
    public boolean insert(CustomerMsgDomain customerMsgDomain) {
        return mapper.insert(CustomerMsgConverter.convert2Do(customerMsgDomain)) > 0;
    }

    /**
     * @see CustomerMsgService#findByOutBizIdAndType(String, CustomerMsgBizTypeEnum)
     */
    @Override
    public List<CustomerMsgDomain> findByOutBizIdAndType(String outBizId, CustomerMsgBizTypeEnum bizTypeEnum) {
        CustomerMsgExample example = new CustomerMsgExample();
        example.createCriteria().andOutBizIdEqualTo(outBizId).andBizTypeEqualTo(bizTypeEnum.getCode());
        return CustomerMsgConverter.convert2Domain(mapper.selectByExample(example));
    }

}
