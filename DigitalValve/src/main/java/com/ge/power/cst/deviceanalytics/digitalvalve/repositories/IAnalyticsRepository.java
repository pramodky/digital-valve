/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics.digitalvalve.repositories;

import java.util.List;

import com.ge.power.cst.commons.repository.IJooqRepository;
import com.ge.power.cst.deviceanalytics.api.IAnalytic;

/**
 * 
 * @author 103019287
 */
public interface IAnalyticsRepository
        extends IJooqRepository
{
    /**
     * @return analytics configured in the database
     */
    public List<IAnalytic> getAnalytics();
}
