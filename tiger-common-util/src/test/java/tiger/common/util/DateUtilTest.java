/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util;

import org.junit.Test;

import java.util.Date;

/**
 * @author zhangbin
 * @version v0.1 2015/10/24 23:10
 */
public class DateUtilTest {
    @Test
    public void testGetDiffMonth() {
        System.out.println(DateUtil.getDiffMonths(new Date(), DateUtil.parseDateWebFormat("2015-11-24")));
    }
}
