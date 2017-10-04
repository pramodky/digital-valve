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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;

/**
 * 
 * @author 103019287
 */
public class Alert
        implements IAlert
{
    private String       name;
    private String       description;
    private List<String> alertRecommendations = new ArrayList<String>();
    private SeverityType severity=SeverityType.UNKNOWN;

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAlert#getName()
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAlert#setName(java.lang.String)
     */
    @Override
    public void setName(String name)
    {
        this.name = name;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAlert#getDescription()
     */
    @Override
    public String getDescription()
    {
        return this.description;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAlert#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description)
    {
        this.description = description;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalAlert#getAlertRecommendations()
     */
    @Override
    public List<String> getAlertRecommendations()
    {
        return this.alertRecommendations;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalAlert#setAlertRecommendations(java.util.List)
     */
    @Override
    public void setAlertRecommendations(List<String> alertRecommendations)
    {
        this.alertRecommendations = alertRecommendations;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalAlert#getSeverity()
     */
    @Override
    public SeverityType getSeverity()
    {
        return this.severity;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalAlert#setSeverity(com.ge.power.cst.deviceanalytics.api.Util.SeverityType)
     */
    @Override
    public void setSeverity(SeverityType severity)
    {
        this.severity = severity;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAlert#isActive()
     */
    @Override
    public boolean isActive()
    {
        return this.severity.getValue() < 4;
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
        Alert alert = (Alert) o;

        return // super.equals(signalAlert) &&
        new EqualsBuilder().append(this.alertRecommendations, alert.alertRecommendations)
                .append(this.severity, alert.severity).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.alertRecommendations).append(this.severity).toHashCode();
    }
}
