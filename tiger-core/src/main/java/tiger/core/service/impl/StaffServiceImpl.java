/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.persistence.StaffMapper;
import tiger.common.dal.query.StaffQuery;
import tiger.core.domain.StaffDomain;
import tiger.core.domain.convert.StaffConvert;
import tiger.core.service.StaffService;

import java.util.List;

/**
 * @author HuPeng
 * @version v 0.1 2015年10月19日 下午4:11:20 HuPeng Exp $
 */
@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffMapper staffMapper;

    /**
     * @see StaffService#query(StaffQuery)
     */
    @Override
    public List<StaffDomain> query(StaffQuery query) {
        return StaffConvert.convert2Domains(staffMapper.query(query));
    }

    /**
     * @see tiger.core.service.StaffService#read(long)
     */
    @Override
    public StaffDomain read(long id) {
        return StaffConvert.convert2Domain(staffMapper.read(id));
    }

    /**
     * @see tiger.core.service.StaffService#readByUsername(java.lang.String)
     */
    @Override
    public StaffDomain readByUsername(String username) {
        return StaffConvert.convert2Domain(staffMapper.readByUsername(username));
    }

    /**
     * @see StaffService#update(StaffDomain)
     */
    @Override
    public boolean update(StaffDomain staffDomain) {
        return staffMapper.updateByPrimaryKeySelective(StaffConvert.convert2DO(staffDomain)) > 0;
    }

    /**
     * @see StaffService#create(StaffDomain)
     */
    @Override
    public boolean create(StaffDomain staffDomain) {
        return staffMapper.create(StaffConvert.convert2DO(staffDomain)) > 0;
    }

}
