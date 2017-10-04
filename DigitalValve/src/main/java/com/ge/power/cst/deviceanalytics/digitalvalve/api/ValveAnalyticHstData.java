/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
 
package com.ge.power.cst.deviceanalytics.digitalvalve.api;

import com.ge.power.cst.deviceanalytics.api.BasicData;
import com.ge.power.cst.deviceanalytics.api.IHstData;
import com.ge.power.cst.deviceanalytics.api.Util.AggregationType;

/**
 * 
 * @author 103019287
 */
public class ValveAnalyticHstData extends BasicData
        implements IHstData
{

 
    private AggregationType aggregationType;
    @Override
    public AggregationType getAggregationType()
    {
        return this.aggregationType;
    }

    /* (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IHstData#setAggregationType(com.ge.power.cst.deviceanalytics.api.Util.AggregationType)
     */
    @Override
    public void setAggregationType(AggregationType aggregationType)
    {
        this.aggregationType=aggregationType;
    }

}
