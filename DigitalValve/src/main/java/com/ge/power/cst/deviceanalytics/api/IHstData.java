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

import com.ge.power.cst.deviceanalytics.api.Util.AggregationType;

/**
 * 
 * @author 103019287
 */
public interface IHstData
        extends IBasicData
{
    /**
     * @return aggregation type to compress data in database.
     *         NONE if this signal value is not required to be persisted in database.
     */
    public AggregationType getAggregationType();

    /**
     * @param aggregationType aggregation type to compress data in database.
     *            NONE if this signal value is not required to be persisted in database.
     */
    public void setAggregationType(AggregationType aggregationType);
}
