/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 测试分页器
 *
 * @author yiliang.gyl
 * @version v 0.1 Sep 27, 2015 10:02:51 AM yiliang.gyl Exp $
 */
public class PaginatorTest {

    /**
     * 测试正常的分页情况
     */
    @Test
    public void testPaginatorNormal() {
        Paginator paginator = new Paginator(10);
        paginator.setItems(9);
        paginator.setPage(1);
        Assert.assertSame(paginator.getOffset(), 0);
        Assert.assertSame(paginator.getLastPage(), 1);
    }

}
