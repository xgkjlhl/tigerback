/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.service;

import tiger.core.domain.MsgTplDomain;

import java.util.List;
import java.util.Map;

/**
 * @author zhangbin
 * @version v0.1 2015/10/5 17:28
 */
public interface MsgTplService {

    Map<Long, List<MsgTplDomain>> getMsgTpl();

    String getMsgTemplateById(Long msgTplId);
}
