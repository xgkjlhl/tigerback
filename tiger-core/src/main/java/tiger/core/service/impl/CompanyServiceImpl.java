/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.CompanyDO;
import tiger.common.dal.dataobject.example.CompanyExample;
import tiger.common.dal.persistence.CompanyMapper;
import tiger.common.dal.query.CompanyQuery;
import tiger.common.util.BeanUtil;
import tiger.common.util.StringUtil;
import tiger.core.domain.CompanyDomain;
import tiger.core.domain.convert.CompanyConvert;
import tiger.core.enums.ErrorCodeEnum;
import tiger.core.exception.TigerException;
import tiger.core.service.CompanyService;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/9/19 9:44
 */
@Service
public class CompanyServiceImpl implements CompanyService {
    private static final String DEFAULT_VALUE = "无";
    private static Logger logger = Logger.getLogger(CompanyServiceImpl.class);

    @Autowired
    CompanyMapper companyMapper;

    /**
     * 根据ID获取当前用户的公司信息。
     *
     * @param id
     * @return
     */
    @Override
    public CompanyDomain getCompanyById(Long id) {
        if (id == null) {
            return null;
        }
        CompanyDomain companyDomain = CompanyConvert.convertToDomain(companyMapper.selectByPrimaryKey(id));
        if (companyDomain == null) {
            logger.error("获取id为" + id + "的公司信息失败");
            return null;
        }
        return companyDomain;
    }

    /**
     * 添加公司
     *
     * @param company
     * @return
     */
    @Override
    public boolean addCompany(CompanyDomain company) {
        CompanyDO companyDO = new CompanyDO();
        BeanUtil.copyPropertiesWithIgnores(company, companyDO);

        int count = companyMapper.insertSelective(companyDO);
        if (count > 0) {
            // 将新增的companyId返回到原来的companyDomain中
            company.setId(companyDO.getId());
            return true;
        }
        logger.error("添加公司信息失败, 参数为[" + company + "]");
        throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
    }

    /**
     * 更新公司
     *
     * @param company
     * @return
     */
    @Override
    public boolean updateCompany(CompanyDomain company) {
        CompanyDO companyDO = new CompanyDO();
        BeanUtil.copyPropertiesWithIgnores(company, companyDO);

        int count = companyMapper.updateByPrimaryKeySelective(companyDO);
        if (count < 0) {
            logger.error("更新公司信息失败, 参数为[" + company + "]");
            throw new TigerException(ErrorCodeEnum.DB_EXCEPTION);
        }

        return true;
    }

    @Override
    public List<CompanyDomain> query(CompanyQuery query) {
        if (query == null) {
            return null;
        }

        CompanyExample example = new CompanyExample();
        CompanyExample.Criteria exampleCriteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(query.getIds())) {
            exampleCriteria.andIdIn(query.getIds());
        }

        return CompanyConvert.convertToDomains(companyMapper.selectByExample(example));
    }


    // ~ Private method
    @Deprecated
    private void setDefaultCompany(CompanyDomain domain) {
        Class<?> companyClass = domain.getClass();
        Field[] fields = companyClass.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getType().equals(String.class)) {
                    field.setAccessible(true);
                    String value = (String) field.get(domain);
                    if (null != value) {
                        field.set(domain, StringUtil.defaultIfBlank(value, DEFAULT_VALUE));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
