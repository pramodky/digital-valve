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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

import com.ge.power.cst.deviceanalytics.api.HealthStatus;
import com.ge.power.cst.deviceanalytics.api.IHealthStatus;
import com.ge.power.cst.deviceanalytics.api.IAlert;
import com.ge.power.cst.deviceanalytics.api.IAnalytic;
import com.ge.power.cst.deviceanalytics.api.Util.DeviceStatus;

/**
 * 
 * @author 103019287
 */
@XmlType(propOrder =
{
        "name", "description", "controlValve"
})
@JsonFilter("filterValve")
public class Valve
{
    private String               id;
    private String               name;
    private String               description;
    private boolean              isControlValve;
    private DeviceStatus         deviceStatus            = DeviceStatus.DEVICE_ENABLED;
    private boolean              isCalibrationInProgress = false;

    private List<IAnalytic>      analytics               = new ArrayList<IAnalytic>();

    private LinkedList<String>   statusLog               = new LinkedList<String>();

    

    /**
     * @param valveId Unique Identifier for this Valve
     */
    public void setId(String valveId)
    {
        this.id = valveId;

    }

    /**
     * @return Unique Identifier for this Valve
     */
    @XmlAttribute
    public String getId()
    {
        return this.id;
    }

    /**
     * @param name Name of the Valve
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Name of the Valve
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param description Description of the Valve
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return Description of the Valve
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @return the isControlValve
     */
    public boolean isControlValve()
    {
        return this.isControlValve;
    }

    /**
     * @param isControlValve the isControlValve to set
     */
    public void setControlValve(boolean isControlValve)
    {
        this.isControlValve = isControlValve;
    }

    /**
     * @return Enabled/Disabled status to indicate whether the valve is
     *         enabled to control
     */
    @XmlTransient
    public DeviceStatus getDeviceStatus()
    {
        return this.deviceStatus;
    }

    /**
     * @param deviceStatus Enabled/Disabled status to indicate whether the valve is
     *            enabled to control
     */
    public void setDeviceStatus(DeviceStatus deviceStatus)
    {
        this.deviceStatus = deviceStatus;

    }


    /**
     * @return the analytics
     */
    @XmlTransient
    public List<IAnalytic> getAnalytics()
    {
        return this.analytics;
    }

    /**
     * @param analytics the analytics to set
     */
    public void setAnalytics(List<IAnalytic> analytics)
    {
        this.analytics = analytics;
    }

    /**
     * @return the active alerts associated with this valve
     */
    @XmlTransient
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for(IAnalytic analytic: this.analytics)
        {
            activeAlerts.addAll(analytic.getActiveAlerts());
        }
        return activeAlerts;
    }

    /**
     * @return health status of this valve
     */
    public IHealthStatus getHealthStatus()
    {
        // TODO: Pass this info to UI
        IHealthStatus healthStatus = new HealthStatus();
        List<IAlert> activeAlerts = this.getActiveAlerts();
        // Iterate through all the active alerts
        // find the alert with max severity level and
        // assign the max alert value
        for (IAlert activeAlert : activeAlerts)
        {
            healthStatus.setMaxSeverity(

                    (activeAlert.getSeverity().getValue() <

                    healthStatus.getMaxSeverity().getValue()) ?

                            activeAlert.getSeverity() : healthStatus.getMaxSeverity());
        }
        // Assign number of active alerts
        healthStatus.setNumberOfActiveAlerts(activeAlerts.size());
        return healthStatus;
    }



    /**
     * @return the status
     */
    @XmlTransient
    public String getStatusLog()
    {
        return this.statusLog.toString().replace("[", "").replace("]", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                                                                                   // //$NON-NLS-5$
                                                                                   // //$NON-NLS-6$
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

        Valve valve = (Valve) o;

        return new EqualsBuilder().append(this.id, valve.id).append(this.name, valve.name)
                .append(this.description, valve.description).append(this.isControlValve, valve.isControlValve)
                .append(this.deviceStatus, valve.deviceStatus)
                .append(this.isCalibrationInProgress, valve.isCalibrationInProgress)
                .append(this.getActiveAlerts(), valve.getActiveAlerts())
                .append(this.getHealthStatus(), valve.getHealthStatus()).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.description)
                .append(this.isControlValve).append(this.deviceStatus).append(this.isCalibrationInProgress)
                .toHashCode();
    }


}
