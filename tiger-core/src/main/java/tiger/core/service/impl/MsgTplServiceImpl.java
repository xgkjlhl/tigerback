/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tiger.common.dal.dataobject.MsgTplDO;
import tiger.common.dal.dataobject.example.MsgTplExample;
import tiger.common.dal.persistence.MsgTplMapper;
import tiger.common.util.BeanUtil;
import tiger.core.domain.MsgTplDomain;
import tiger.core.service.MsgTplService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangbin
 * @version v0.1 2015/10/5 17:29
 */
@Service
public class MsgTplServiceImpl implements MsgTplService {

    @Autowired
    private MsgTplMapper msgTplMapper;

    /**
     * 获取所有短信模板
     *
     * @return
     */
    public Map<Long, List<MsgTplDomain>> getMsgTpl() {

        List<MsgTplDO> msgTplDOs = msgTplMapper.getAllMsgTpl();
        Map<Long, List<MsgTplDomain>> longListHashMap = new HashMap<>();
        for (MsgTplDO msgTplDO : msgTplDOs) {
            MsgTplDomain domain = new MsgTplDomain();
            BeanUtil.copyPropertiesWithIgnores(msgTplDO, domain);

            if (longListHashMap.get(msgTplDO.getCatgId()) == null) {
                List<MsgTplDomain> msgTplDomains = new ArrayList<>();
                msgTplDomains.add(domain);
                longListHashMap.put(msgTplDO.getCatgId(), msgTplDomains);
            } else {
                longListHashMap.get(msgTplDO.getCatgId()).add(domain);
            }
        }
        return longListHashMap;
    }

    /**
     * 根据模板id获取模板内容
     *
     * @param msgTplId
     * @return
     */
    @Override
    public String getMsgTemplateById(Long msgTplId) {
        MsgTplExample example = new MsgTplExample();
        MsgTplExample.Criteria criteria = example.createCriteria();
        criteria.andTplIdEqualTo(msgTplId);

        List<MsgTplDO> list = msgTplMapper.selectByExample(example);
        if (list.size() <= 0) {
            return null;
        }
        return list.get(0).getContent();
    }
}
