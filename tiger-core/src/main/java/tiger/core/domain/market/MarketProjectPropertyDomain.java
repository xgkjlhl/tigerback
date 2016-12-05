/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package tiger.core.domain.market;

/**
 *
 *
 * @author yiliang.gyl
 * @version $ID: v 0.1 10:26 PM yiliang.gyl Exp $
 */
public class MarketProjectPropertyDomain {

    private Boolean hasAwesome = false;

    private Integer awesomeCount = 0;

    private Boolean hasCollect = false;

    public Boolean getHasAwesome() {
        return hasAwesome;
    }

    public void setHasAwesome(Boolean hasAwesome) {
        this.hasAwesome = hasAwesome;
    }

    public Integer getAwesomeCount() {
        return awesomeCount;
    }

    public void setAwesomeCount(Integer awesomeCount) {
        this.awesomeCount = awesomeCount;
    }

    public Boolean getHasCollect() {
        return hasCollect;
    }

    public void setHasCollect(Boolean hasCollect) {
        this.hasCollect = hasCollect;
    }
}
