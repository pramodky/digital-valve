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

import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;

/**
 * 
 * @author 103019287
 */
public class HealthStatus
        implements IHealthStatus
{

    private int          numberOfActiveAlerts;
    private SeverityType maxSeverity = SeverityType.INFORMATION;

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IHealthStatus#getNumberOfActiveAlerts()
     */
    @Override
    public int getNumberOfActiveAlerts()
    {
        return this.numberOfActiveAlerts;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IHealthStatus#setNumberOfActiveAlerts(int)
     */
    @Override
    public void setNumberOfActiveAlerts(int numberOfActiveAlerts)
    {
        this.numberOfActiveAlerts = numberOfActiveAlerts;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IHealthStatus#getMaxSeverity()
     */
    @Override
    public SeverityType getMaxSeverity()
    {
        return this.maxSeverity;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IHealthStatus#setMaxSeverity(com.ge.power.cst.deviceanalytics.api.Util.SeverityType)
     */
    @Override
    public void setMaxSeverity(SeverityType maxSeverity)
    {
        this.maxSeverity = maxSeverity;
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

        HealthStatus healthStatus = (HealthStatus) o;

        return new EqualsBuilder().append(this.numberOfActiveAlerts, healthStatus.numberOfActiveAlerts)
                .append(this.maxSeverity, healthStatus.maxSeverity)

                .isEquals();

    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.numberOfActiveAlerts).append(this.maxSeverity).toHashCode();
    }
}
