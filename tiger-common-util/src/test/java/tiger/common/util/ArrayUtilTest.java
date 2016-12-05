/**
 * 404 Team
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util;

import org.junit.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Liao Zhenyu
 * @version v 0.1 2015年9月13日 下午6:26:17 Jaric Liao Exp $
 */
public class ArrayUtilTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link tiger.common.util.ArrayUtil#isEmpty(java.lang.Object[])}.
     */
    @Test
    public void testIsEmptyObjectArray() {
        Object[] objectArray = null;
        assertThat(ArrayUtil.isEmpty(objectArray), equalTo(true));
        objectArray = new Object[]{};
        assertThat(ArrayUtil.isEmpty(objectArray), equalTo(true));
        objectArray = new Object[]{1};
        assertThat(ArrayUtil.isEmpty(objectArray), equalTo(false));
    }

    /**
     * Test method for {@link tiger.common.util.ArrayUtil#isNotEmpty(java.lang.Object[])}.
     */
    @Test
    public void testIsNotEmptyObjectArray() {
        Object[] objectArray = null;
        assertThat(ArrayUtil.isNotEmpty(objectArray), equalTo(false));
        objectArray = new Object[]{};
        assertThat(ArrayUtil.isNotEmpty(objectArray), equalTo(false));
        objectArray = new Object[]{1};
        assertThat(ArrayUtil.isNotEmpty(objectArray), equalTo(true));
    }

    /**
     * Test method for {@link tiger.common.util.ArrayUtil#isSameLength(java.lang.Object[], java.lang.Object[])}.
     */
    @Test
    public void testIsSameLengthObjectArrayObjectArray() {
        Object[] objectArray1 = null;
        Object[] objectArray2 = null;
        assertThat(ArrayUtil.isSameLength(objectArray1, objectArray2), equalTo(true));
        objectArray1 = new Object[]{1};
        assertThat(ArrayUtil.isSameLength(objectArray1, objectArray2), equalTo(false));
        objectArray2 = new Object[]{2};
        assertThat(ArrayUtil.isSameLength(objectArray1, objectArray2), equalTo(true));
    }

    /**
     * Test method for {@link tiger.common.util.ArrayUtil#indexOf(java.lang.Object[], java.lang.Object)}.
     */
    @Test
    public void testIndexOfObjectArrayObject() {
        Object[] objectArray = new Object[]{1, 2, 3, 4};
        assertThat(ArrayUtil.indexOf(objectArray, 1), equalTo(0));
        assertThat(ArrayUtil.indexOf(objectArray, 5), equalTo(-1));
    }

    /**
     * Test method for {@link tiger.common.util.ArrayUtil#contains(java.lang.Object[], java.lang.Object)}.
     */
    @Test
    public void testContainsObjectArrayObject() {
        Object[] objectArray = new Object[]{1, 2, 3, 4};
        assertThat(ArrayUtil.contains(objectArray, 1), equalTo(true));
        assertThat(ArrayUtil.contains(objectArray, 5), equalTo(false));
    }

}
