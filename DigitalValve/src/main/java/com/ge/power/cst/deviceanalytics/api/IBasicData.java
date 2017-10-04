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

import com.ge.power.cst.deviceanalytics.api.Util.DataType;

/**
 * 
 * @author 103019287
 */
public interface IBasicData
{
    /**
     * @return unique identifier of this data
     */
    public String getId();

    /**
     * @param id unique identifier of this data
     */
    public void setId(String id);

    /**
     * @return Name of this data
     */
    public String getName();

    /**
     * @param name Name of this data
     */
    public void setName(String name);

    /**
     * @return measurement unit of this data
     */
    public String getMeasurementUnit();

    /**
     * @param measurementUnit measurement unit of this data
     */
    public void setMeasurementUnit(String measurementUnit);
    
    /**
     * @return data type of this data
     */
    public DataType getDataType();
    
    /**
     * @param dataType data type of this data
     */
    public void setDataType(DataType dataType);

    /**
     * @return data value
     */
    public Object getValue();

    /**
     * @param value data value
     */
    public void setValue(Object value);
}
