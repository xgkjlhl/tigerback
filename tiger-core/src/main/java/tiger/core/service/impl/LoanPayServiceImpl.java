/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tiger.common.dal.dataobject.LoanDO;
import tiger.common.dal.dataobject.LoanRecordDO;
import tiger.common.dal.dataobject.LoanRecordItemsDO;
import tiger.common.dal.dataobject.example.LoanRecordItemsExample;
import tiger.common.dal.enums.LoanActionEnum;
import tiger.common.dal.enums.LoanStatusEnum;
import tiger.common.dal.persistence.LoanMapper;
import tiger.common.dal.persistence.LoanRecordItemsMapper;
import tiger.common.dal.persistence.LoanRecordMapper;
import tiger.core.domain.LoanDomain;
import tiger.core.domain.LoanMergedBillDomain;
import tiger.core.domain.LoanRecordDomain;
import tiger.core.domain.LoanRecordItemDomain;
import tiger.core.domain.convert.LoanRecordConvert;
import tiger.core.domain.convert.LoanRecordItemConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.loan.calculator.CalculatorFactory;
import tiger.core.service.LoanPayService;
import tiger.core.service.LoanRecordService;
import tiger.core.service.LoanService;

import java.util.ArrayList;
import java.util.List;

/**
 * 贷款收付服务实现
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 30, 2015 4:14:03 PM yiliang.gyl Exp $
 */
@Service
public class LoanPayServiceImpl implements LoanPayService {

    private static Logger logger = Logger.getLogger(LoanPayServiceImpl.class);

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRecordMapper loanRecordMapper;

    @Autowired
    private LoanRecordItemsMapper loanRecordItemsMapper;

    @Autowired
    private LoanRecordService loanRecordService;

    /**
     * 项目放款
     *
     * @see LoanPayService#launch(LoanRecordDomain, List)
     */
    @Override
    @Transactional
    public boolean launch(LoanRecordDomain loanRecordDomain, List<LoanRecordDomain> payBackItems) {
        if (loanRecordDomain == null) {
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL);
        }
        int rc = 1;
        //1. 更新放款参数
        if (loanRecordDomain.getId() != null) {
            LoanRecordDO loanRecordDO = new LoanRecordDO();
            loanRecordDO.setActualAmount(loanRecordDomain.getAmount());
            loanRecordDO.setActualDate(loanRecordDomain.getActualDate());
            loanRecordDO.setActualInterest(loanRecordDomain.getActualInterest());
            loanRecordDO.setId(loanRecordDomain.getId());
            loanRecordDO.setState(LoanStatusEnum.FINISH.getCode());
            rc = loanRecordMapper.updateByPrimaryKeySelective(loanRecordDO);
        }
        //2. 计算并存储所有的还款列表
        for (LoanRecordDomain domain : payBackItems) {
            saveLoanRecordDomain(domain);
        }

        //3. 把项目状态改成还款中
        LoanDO loanDO = new LoanDO();
        loanDO.setId(loanRecordDomain.getLoanId());
        loanDO.setState(LoanStatusEnum.PAY_PROCESS.getCode());
        loanDO.setLaunchDate(loanRecordDomain.getActualDate());
        int statusRc = loanMapper.updateByPrimaryKeySelective(loanDO);

        if (rc > 0 && statusRc > 0) {
            return true;
        } else {
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        }

    }

    public boolean manualCreateLoanToLaunch(LoanRecordDomain loanRecordDomain,List<LoanRecordDO> loanRocordDos){
        if (loanRecordDomain == null) {
            throw new TigerException(ErrorCodeEnum.BIZ_FAIL);
        }
        int rc = 1;
        //1. 更新放款参数
        if (loanRecordDomain.getId() != null) {
            LoanRecordDO loanRecordDO = new LoanRecordDO();
            loanRecordDO.setActualAmount(loanRecordDomain.getAmount());
            loanRecordDO.setActualDate(loanRecordDomain.getActualDate());
            loanRecordDO.setActualInterest(loanRecordDomain.getActualInterest());
            loanRecordDO.setId(loanRecordDomain.getId());
            loanRecordDO.setState(LoanStatusEnum.FINISH.getCode());
            rc = loanRecordMapper.updateByPrimaryKeySelective(loanRecordDO);
        }
        // 2:更新还款明细表将所有的状态有WAIT_LANCH改为PAY_PROCESS
        for(int i=1;i<loanRocordDos.size();i++){
            LoanRecordDO payBack=loanRocordDos.get(i);
            LoanRecordDO loanRecordDo=new LoanRecordDO();
            loanRecordDo.setId(payBack.getId());
            loanRecordDo.setState(LoanStatusEnum.PAY_PROCESS.getCode());
            loanRecordMapper.updateByPrimaryKeySelective(loanRecordDo);
        }
        //3. 把项目状态改成还款中
        LoanDO loanDO = new LoanDO();
        loanDO.setId(loanRecordDomain.getLoanId());
        loanDO.setState(LoanStatusEnum.PAY_PROCESS.getCode());
        loanDO.setLaunchDate(loanRecordDomain.getActualDate());
        int statusRc = loanMapper.updateByPrimaryKeySelective(loanDO);

        if (rc > 0 && statusRc > 0) {
            return true;
        } else {
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        }

    }



    /**
     * @see tiger.core.service.LoanPayService#calLaunchBill(LoanDomain)
     */
    @Override
    public LoanRecordDomain calLaunchBill(LoanDomain loanDomain) {
        CalculatorFactory calculatorFactory = new CalculatorFactory(loanDomain);
        return calculatorFactory.getLaunchItem();
    }

    /**
     * @see tiger.core.service.LoanPayService#calProcessBills(tiger.core.domain.LoanDomain)
     */
    @Override
    public List<LoanRecordDomain> calProcessBills(LoanDomain loanDomain) {
        CalculatorFactory calculatorFactory = new CalculatorFactory(loanDomain);
        // 1. 计算放款时还款计划
        List<LoanRecordDomain> domains = calculatorFactory.getProcessItems();
        domains.add(calculatorFactory.getEndItems());
        // 1. 对order从2开始重排列
        int order = 2;
        for (LoanRecordDomain domain : domains) {
            domain.setOrder(order);
            order++;
        }
        return domains;
    }

    /**
     * @see tiger.core.service.LoanPayService#saveLoanRecordDomain(tiger.core.domain.LoanRecordDomain)
     */
    @Override
    @Transactional
    public LoanRecordDomain saveLoanRecordDomain(LoanRecordDomain loanRecordDomain) {
        LoanRecordDO loanRecordDO = LoanRecordConvert.convert2DO(loanRecordDomain);
        boolean result = loanRecordMapper.insert(loanRecordDO) > 0;
        if (loanRecordDO.getId() > 0) {
            //插入成功
            if (loanRecordDomain.getRecordItemDomains().size() > 0) {
                //存储详情
                int rc = loanRecordItemsMapper.batchInsertItems(LoanRecordItemConvert
                        .convert2Dos(loanRecordDomain.getRecordItemDomains(), loanRecordDO.getId()));
                result = result & (rc > 0);
            }
        }
        if (result) {
            return LoanRecordConvert.convert2Domain(loanRecordDO);
        } else {
            return null;
        }
    }

    /**
     * @see LoanPayService#updateLoanRecordDomain(LoanRecordDomain)
     */
    @Override
    @Transactional
    public LoanRecordDomain updateLoanRecordDomain(LoanRecordDomain loanRecordDomain) {
        LoanRecordDO loanRecordDO = LoanRecordConvert.convert2DO(loanRecordDomain);
        if (loanRecordDO.getId() == null) {
            logger.error("无还款记录主键，无法更新数据 [" + loanRecordDO + "]");
            return null;
        }
        //1. 更新主记录
        boolean result = loanRecordMapper.updateByPrimaryKeySelective(loanRecordDO) > 0;

        //2. 插入新的详情
        if (loanRecordDomain.getRecordItemDomains() != null &&
                loanRecordDomain.getRecordItemDomains().size() > 0) {
            //1. 删除原有详情
            result = result && deleteLoanRecordItems(loanRecordDO.getId());

            //2. 存储详情
            int rc = loanRecordItemsMapper.batchInsertItems(LoanRecordItemConvert
                    .convert2Dos(loanRecordDomain.getRecordItemDomains(), loanRecordDO.getId()));
            result = result && (rc > 0);
        }
        if (result) {
            return loanRecordService.getLoanRecordById(loanRecordDO.getId());
        } else {
            return null;
        }
    }

    /**
     * @see LoanPayService#deleteLoanRecordDomain(Long)
     */
    @Override
    @Transactional
    public boolean deleteLoanRecordDomain(Long loanRecordId) {
        boolean result = deleteLoanRecordItems(loanRecordId);
        return result && (loanRecordMapper.deleteByPrimaryKey(loanRecordId) > 0);
    }


    /**
     * 计算实际还款账单
     *
     * @param loanRecordDomain ~必须存在实际还款日期
     * @see LoanPayService#calPayBill(LoanRecordDomain)
     */
    @Override
    public LoanRecordDomain calPayBill(LoanRecordDomain loanRecordDomain) {
        LoanDomain loanDomain = loanService.read(loanRecordDomain.getLoanId());
        if (loanDomain == null) {
            throw new TigerException(ErrorCodeEnum.NOT_FOUND);
        }
        CalculatorFactory factory = new CalculatorFactory(loanDomain);
        return factory.recalculatePayback(loanRecordDomain);
    }

    /**
     * @see LoanPayService#payLoanRecord(LoanRecordDomain)
     */
    @Override
    @Transactional
    public boolean payLoanRecord(LoanRecordDomain loanRecordDomain) {
        //1. 更新基础信息
        LoanRecordDO loanRecordDO = new LoanRecordDO();
        loanRecordDO.setId(loanRecordDomain.getId());
        loanRecordDO.setAmount(loanRecordDomain.getActualAmount());
        loanRecordDO.setActualAmount(loanRecordDomain.getActualAmount());
        loanRecordDO.setActualDate(loanRecordDomain.getActualDate());
        loanRecordDO.setState(LoanStatusEnum.FINISH.getCode());
        loanRecordDO.setRemark(loanRecordDomain.getRemark());

        int rc = loanRecordMapper.updateByPrimaryKeySelective(loanRecordDO);
        //2. 更新还款详情信息
        if (rc > 0 && loanRecordDomain.getRecordItemDomains().size() > 0) {
            List<LoanRecordItemsDO> itemsDOs = LoanRecordItemConvert.convert2Dos(
                    loanRecordDomain.getRecordItemDomains());
            int order = 1;
            for (LoanRecordItemsDO itemsDO : itemsDOs) {
                if (itemsDO.getId() != null) {
                    itemsDO.setId(null);
                }
                itemsDO.setLoanRecordId(loanRecordDomain.getId());
                itemsDO.setOrder(order);
                order++;
            }
            //2.1 批量删除之前的详情字段先
            if (!deleteRecordItemsByRecordId(loanRecordDomain.getId())) {
                throw new TigerException(ErrorCodeEnum.DB_EXCEPTION, "数据库异常");
            }
            //2.2 批量插入
            rc = loanRecordItemsMapper.batchInsertItems(itemsDOs);
            if (rc <= 0) {
                throw new TigerException(ErrorCodeEnum.DB_EXCEPTION, "数据库异常");
            }
        }

        return rc > 0;
    }

    /**
     * @see tiger.core.service.LoanPayService#payLoanRecords(LoanMergedBillDomain)
     */
    @Override
    @Transactional
    public boolean payLoanRecords(LoanMergedBillDomain loanMergedBillDomain) {
        boolean result = false;

        List<LoanRecordDomain> recordDomains = loanMergedBillDomain.getBills();

        if (CollectionUtils.isEmpty(recordDomains)) {
            logger.error("接受到一个空的 recordDomains");
            return false;
        }
        List<LoanRecordDO> recordDOs = LoanRecordConvert.convert2PayDOs(recordDomains);

        List<Long> originItemIds = new ArrayList<>();
        List<LoanRecordItemDomain> itemDomains = new ArrayList<>();
        for (LoanRecordDomain recordDomain : recordDomains) {
            for (LoanRecordItemDomain item : recordDomain.getRecordItemDomains()) {
                if (item.getId() != null) {
                    originItemIds.add(item.getId());
                } else {
                    item.setLoanId(loanMergedBillDomain.getLoanId());
                }
                itemDomains.add(item);
            }
        }
        try {
            //1. 更新DO
            for (LoanRecordDO insertDO : recordDOs) {
                insertDO.setState(LoanStatusEnum.FINISH.getCode());
                loanRecordMapper.updateByPrimaryKeySelective(insertDO);
            }
            //2. 插入新的详情
            if (CollectionUtils.isNotEmpty(itemDomains)) {
                //2.1 批量删除之前的详情字段先
                int dRc = loanRecordItemsMapper.deleteByIds(originItemIds);

                //2.2 批量插入
                int rc = loanRecordItemsMapper.batchInsertItems(LoanRecordItemConvert
                        .convert2Dos(itemDomains, null));
                if (rc > 0 && dRc > 0) {
                    result = true;
                } else {
                    throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
                }
            }
        } catch (Exception e) {
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        }
        return result;
    }

    //~ private methods

    private boolean deleteLoanRecordItems(Long loanRecordId) {
        LoanRecordItemsExample recordItemsExample = new LoanRecordItemsExample();
        recordItemsExample.createCriteria().andLoanRecordIdEqualTo(loanRecordId);
        return loanRecordItemsMapper.deleteByExample(recordItemsExample) > 0;
    }

    //删除一个loanId下的items
    private boolean deleteRecordItemsByRecordId(Long recordId) {
        LoanRecordItemsExample example = new LoanRecordItemsExample();
        example.createCriteria().andLoanRecordIdEqualTo(recordId);
        return loanRecordItemsMapper.deleteByExample(example) > 0;
    }
}
