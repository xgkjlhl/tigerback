/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package tiger.core.domain.mng;

import org.apache.commons.beanutils.BeanUtils;
import tiger.core.domain.AccountDomain;

import java.lang.reflect.InvocationTargetException;

/**
 * @author alfred_yuan
 * @version v 0.1 14:03 alfred_yuan Exp $
 */
public class MngAccountDomain extends AccountDomain {

    private static final long serialVersionUID = 5794545208371845546L;


    public MngAccountDomain(AccountDomain account, int loanCount) {
        try {
            BeanUtils.copyProperties(this, account);
            this.loanCount = loanCount;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private int loanCount;

    /**
     * Getter for property 'loanCount'.
     *
     * @return Value for property 'loanCount'.
     */
    public int getLoanCount() {
        return loanCount;
    }

    /**
     * Setter for property 'loanCount'.
     *
     * @param loanCount Value to set for property 'loanCount'.
     */
    public void setLoanCount(int loanCount) {
        this.loanCount = loanCount;
    }
}
