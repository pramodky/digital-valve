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

import java.util.List;

import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;

/**
 * 
 * @author 103019287
 */
public interface IAlert
{
    /**
     * @return alert name
     */
    public String getName();

    /**
     * @param name alert name
     */
    public void setName(String name);

    /**
     * @return alert description
     */
    public String getDescription();

    /**
     * @param description alert description
     */
    public void setDescription(String description);

    /**
     * @return list of alert recommendations
     */
    public List<String> getAlertRecommendations();

    /**
     * @param alertRecommendations list of alert recommendations
     */
    public void setAlertRecommendations(List<String> alertRecommendations);

    /**
     * @return severity level
     */
    public SeverityType getSeverity();

    /**
     * @param severity severity level
     */
    public void setSeverity(SeverityType severity);

    /**
     * @return if this is an active alert
     */
    public boolean isActive();
}
