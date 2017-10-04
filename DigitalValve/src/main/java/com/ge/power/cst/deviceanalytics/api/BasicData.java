/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.ge.power.cst.deviceanalytics.api.Util.DataType;

/**
 * 
 * @author 103019287
 */
public class BasicData
        implements IBasicData
{
    private String   id;
    private String   name;
    private String   measurementUnit;
    private DataType dataType;
    private Object   value;

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IBasicData#getId()
     */
    @Override
    public String getId()
    {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IBasicData#setId(java.lang.String)
     */
    @Override
    public void setId(String id)
    {
        this.id = id;

    }

    /**
     * default construction
     */
    public BasicData()
    {

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignal#getName()
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignal#setName(java.lang.String)
     */
    @Override
    public void setName(String name)
    {
        this.name = name;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignal#getMeasurementUnit()
     */
    @Override
    public String getMeasurementUnit()
    {
        return this.measurementUnit;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignal#setMeasurementUnit(java.lang.String)
     */
    @Override
    public void setMeasurementUnit(String measurementUnit)
    {
        this.measurementUnit = measurementUnit;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IBasicData#getDataType()
     */
    @Override
    public DataType getDataType()
    {
        return this.dataType;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IBasicData#setDataType(java.lang.String)
     */
    @Override
    public void setDataType(DataType dataType)
    {
        this.dataType = dataType;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignal#getValue()
     */
    @Override
    public Object getValue()
    {
        return this.value;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignal#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object value)
    {
        this.value = value;

    }

    @Override
    public boolean equals(Object o)
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        BasicData basicData = (BasicData) o;

        return new EqualsBuilder().append(this.name, basicData.name)
                .append(this.measurementUnit, basicData.measurementUnit).append(this.value, basicData.value).isEquals();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.name).append(this.measurementUnit).append(this.value)
                .toHashCode();
    }

}
