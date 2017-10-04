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

import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;

/**
 * 
 * @author 103019287
 */
public interface IHealthStatus
{
    /**
     * @return number of active alerts
     */
    public int getNumberOfActiveAlerts();

    /**
     * @param numberOfActiveAlerts number of active alerts
     */
    public void setNumberOfActiveAlerts(int numberOfActiveAlerts);

    /**
     * @return maximum severity
     */
    public SeverityType getMaxSeverity();

    /**
     * @param maxSeverity maximum severity
     */
    public void setMaxSeverity(SeverityType maxSeverity);
}
