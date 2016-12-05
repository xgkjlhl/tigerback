/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.LoanRefineDO;
import tiger.common.dal.dataobject.example.LoanRefineExample;
import tiger.common.dal.persistence.LoanRefineMapper;
import tiger.core.domain.LoanRefineDomain;
import tiger.core.domain.convert.LoanRefineConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.LoanRefineService;

import java.util.List;

/**
 * @author alfred_yuan
 * @version ${ID}: v 0.1 20:30 alfred_yuan Exp $
 */
@Service
public class LoanRefineServiceImpl implements LoanRefineService {

    private final Logger logger = Logger.getLogger(LoanRefineServiceImpl.class);

    @Autowired
    private LoanRefineMapper loanRefineMapper;

    /**
     * @see LoanRefineService#insert(LoanRefineDomain)
     */
    @Override
    public LoanRefineDomain insert(LoanRefineDomain domain) {
        if (logger.isInfoEnabled()) {
            logger.info("开始保存合同调整信息, 收到的参数为: [" + domain + "]");
        }
        if (domain == null) {
            return null;
        }
        LoanRefineDO refineDO = LoanRefineConvert.convertToDO(domain);
        if (loanRefineMapper.insert(refineDO) <= 0) {
            logger.error("保存合同调整信息失败, 参数为: [" + domain + "]");
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        }
        if (logger.isInfoEnabled()) {
            logger.info("成功保存合同调整信息, 收到的参数为: [" + domain + "]");
        }
        return LoanRefineConvert.convertToDomain(refineDO);
    }

    /**
     * @see LoanRefineService#insert(LoanRefineDomain)
     */
    @Override
    public LoanRefineDomain read(Long id) {
        if (logger.isInfoEnabled()) {
            logger.info("开始读取id为" + id + "的合同调整信息");
        }
        LoanRefineDomain refineDomain = LoanRefineConvert.convertToDomain(loanRefineMapper.selectByPrimaryKey(id));
        if (refineDomain == null) {
            logger.error("读取id为" + id + "的合同调整信息失败");
        }
        return refineDomain;
    }

    /**
     * @see LoanRefineService#insert(LoanRefineDomain)
     */
    @Override
    public List<LoanRefineDomain> getLoanRefineByLoanId(Long loanId) {
        LoanRefineExample selectExample = new LoanRefineExample();
        selectExample.createCriteria().andLoanIdEqualTo(loanId);

        return LoanRefineConvert.convertToDomains(loanRefineMapper.selectByExample(selectExample));
    }

}
