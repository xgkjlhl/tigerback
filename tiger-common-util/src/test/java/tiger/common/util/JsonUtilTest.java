/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.common.util;

import org.junit.Test;
import tiger.common.util.jsonUtilModel.FloatTestModel;
import tiger.common.util.jsonUtilModel.LongTestModel;

/**
 * @author yiliang.gyl
 * @version $ID: v 0.1 11:57 PM yiliang.gyl Exp $
 */
public class JsonUtilTest {


    @Test
    public void testFromJsonForLongList() {
        LongTestModel longTestModel = new LongTestModel();
        longTestModel.getIds().add(1l);
        longTestModel.getIds().add(2l);
        String longTestStr = JsonUtil.toJson(longTestModel);

        System.out.println("转换后的字符串:" + longTestStr);

        LongTestModel toModel = JsonUtil.fromJson(longTestStr, LongTestModel.class);

        for(Long num : toModel.getIds()){
            System.out.println("to:" + num);
        }
    }


    @Test
    public void testFromJsonForFloatList() {
        FloatTestModel longTestModel = new FloatTestModel();
        longTestModel.getIds().add(1f);
        longTestModel.getIds().add(1f);
        String longTestStr = JsonUtil.toJson(longTestModel);

        System.out.println("转换后的字符串:" + longTestStr);

        FloatTestModel toModel = JsonUtil.fromJson(longTestStr, FloatTestModel.class);

        for(Float num : toModel.getIds()){
            System.out.println("to:" + num);
        }
    }


}


