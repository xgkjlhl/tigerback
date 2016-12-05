/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util.loan;

import tiger.common.util.DateUtil;

import java.util.Date;

/**
 * 贷款时间计算的工具类
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 19, 2015 1:50:14 PM yiliang.gyl Exp $
 */
public class LoanDateUtil {

    /**
     * 获取贷款结束日期工具类
     *
     * @param beginDate
     * @param monthSpan
     * @return 计算出的结束日期
     */
    public static Date getLoanEndDate(Date beginDate, int monthSpan) {
        Date begin = DateUtil.getDayBegin(beginDate);
        return DateUtil.addMonths(begin, monthSpan);
    }

}
