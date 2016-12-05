/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.LoanSettingDO;
import tiger.common.dal.dataobject.example.LoanSettingExample;
import tiger.common.dal.enums.LoanSettingTypeEnum;
import tiger.common.dal.persistence.LoanSettingMapper;
import tiger.common.util.ByteUtil;
import tiger.core.domain.LoanSettingDomain;
import tiger.core.domain.convert.LoanSettingConvert;
import tiger.core.service.LoanSettingService;

import java.util.List;

/**
 * 贷款设置服务类
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 8:16 PM yiliang.gyl Exp $
 */
@Service
public class LoanSettingServiceImpl implements LoanSettingService {

    @Autowired
    private LoanSettingMapper loanSettingMapper;


    /**
     * @see LoanSettingService#insert(LoanSettingDomain)
     */
    @Override
    public LoanSettingDomain insert(LoanSettingDomain loanSettingDomain) {

        LoanSettingDO loanSettingDO = LoanSettingConvert.convert2Do(loanSettingDomain);
        boolean result = loanSettingMapper.
                insert(loanSettingDO) > 0;
        if (result) {
            return LoanSettingConvert.convert2Domain(loanSettingDO);
        } else {
            return null;
        }
    }

    /**
     * @see LoanSettingService#delete(Long)
     */
    @Override
    public Boolean delete(Long settingId) {
        return loanSettingMapper.deleteByPrimaryKey(settingId) > 0;
    }

    /**
     * @see LoanSettingService#update(LoanSettingDomain)
     */
    @Override
    public LoanSettingDomain update(LoanSettingDomain loanSettingDomain) {
        LoanSettingDO loanSettingDO = LoanSettingConvert.convert2Do(loanSettingDomain);
        if (loanSettingDO.getId() == null) {
            return null;
        }
        if (loanSettingMapper.updateByPrimaryKeySelective(loanSettingDO) > 0) {
            return read(loanSettingDO.getId());
        } else {
            return null;
        }

    }

    /**
     * @see LoanSettingService#read(Long)
     */
    @Override
    public LoanSettingDomain read(Long settingId) {
        LoanSettingDO loanSettingDO = loanSettingMapper.selectByPrimaryKey(settingId);
        if (loanSettingDO != null) {
            return LoanSettingConvert.convert2Domain(loanSettingDO);
        } else {
            return null;
        }
    }

    /**
     * @see LoanSettingService#findByTypeAndLoanId(LoanSettingTypeEnum, Long)
     */
    @Override
    public List<LoanSettingDomain> findByTypeAndLoanId(LoanSettingTypeEnum typeEnum, Long loanId) {
        LoanSettingExample example = new LoanSettingExample();
        example.createCriteria().andSettingTypeEqualTo(typeEnum.getCode()).andLoanIdEqualTo(loanId);
        return LoanSettingConvert.convert2Domains(loanSettingMapper.selectByExample(example));
    }

    /**
     * @see LoanSettingService#findByTypeAndStatus(LoanSettingTypeEnum, Boolean)
     */
    @Override
    public List<LoanSettingDomain> findByTypeAndStatus(LoanSettingTypeEnum typeEnum, Boolean isActive) {
        LoanSettingExample example = new LoanSettingExample();
        example.createCriteria().andSettingTypeEqualTo(typeEnum.getCode()).andIsActiveEqualTo(ByteUtil.booleanToByte(isActive));
        return LoanSettingConvert.convert2Domains(loanSettingMapper.selectByExample(example));
    }
}
