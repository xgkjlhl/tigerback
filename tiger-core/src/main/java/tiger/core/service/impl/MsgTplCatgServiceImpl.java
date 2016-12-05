/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.MsgTplCatgDO;
import tiger.common.dal.persistence.MsgTplCatgMapper;
import tiger.common.util.BeanUtil;
import tiger.core.domain.MsgTplCatgDomain;
import tiger.core.service.MsgTplCatgService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangbin
 * @version v0.1 2015/10/5 17:28
 */
@Service
public class MsgTplCatgServiceImpl implements MsgTplCatgService {

    @Autowired
    MsgTplCatgMapper msgTplCatgMapper;

    /**
     * @return
     */
    public List<MsgTplCatgDomain> getAll() {
        List<MsgTplCatgDO> msgTplCatgDOs = msgTplCatgMapper.getAllCategory();
        List<MsgTplCatgDomain> msgTplCatgDomains = new ArrayList<>();

        for (MsgTplCatgDO msgTplCatgDO : msgTplCatgDOs) {
            MsgTplCatgDomain domain = new MsgTplCatgDomain();
            BeanUtil.copyPropertiesWithIgnores(msgTplCatgDO, domain);

            msgTplCatgDomains.add(domain);
        }
        return msgTplCatgDomains;
    }
}
