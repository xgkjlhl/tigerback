/**
 * 404 Studio Inc.
 * Copyright (c) 2014-2016 All Rights Reserved.
 */

package tiger.core.domain.extraDomain;

/**
 * 额外字段对象
 *
 * @author alfred_yuan
 * @version v 0.1 21:15 alfred_yuan Exp $
 */
public class ExtraField {
    private String name;
    private Double value;
    private String description;

    public ExtraField() {
    }

    public ExtraField(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public ExtraField(String name, Double value, String description) {
        this.name = name;
        this.value = value;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
