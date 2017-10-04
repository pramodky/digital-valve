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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.ge.power.cst.deviceanalytics.api.HealthStatus;
import com.ge.power.cst.deviceanalytics.api.IDevice;
import com.ge.power.cst.deviceanalytics.api.IHealthStatus;
import com.ge.power.cst.deviceanalytics.api.IAlert;
import com.ge.power.cst.deviceanalytics.api.Util.DeviceType;

/**
 * 
 * @author 103019287
 */
@XmlType(propOrder =
{
        "name", "description", "type", "valves"
})
//@JsonFilter("filterValveSet")
public class ValveSet
        implements IDevice
{

    private String      id;
    private String      name;
    private DeviceType  type;
    private String      description;
    private List<Valve> valves = new ArrayList<Valve>();

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#getId()
     */
    @Override
    @XmlTransient
    public String getId()
    {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#setId(java.lang.String)
     */
    @Override
    public void setId(String id)
    {
        this.id = id;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#getName()
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#setName(java.lang.String)
     */
    @Override
    public void setName(String name)
    {
        this.name = name;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#getDescription()
     */
    @Override
    public String getDescription()
    {
        return this.description;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description)
    {
        this.description = description;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#getType()
     */
    @Override
    public DeviceType getType()
    {
        return this.type;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#setType(com.ge.power.cst.deviceanalytics.api.Util.DeviceType)
     */
    @Override
    public void setType(DeviceType type)
    {
        this.type = type;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#getActiveAlerts()
     */
    @Override
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for (Valve valve : this.getValves())
        {
            activeAlerts.addAll(valve.getActiveAlerts());
        }
        return activeAlerts;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IDevice#getHealthStatus()
     */
    @Override
    public IHealthStatus getHealthStatus()
    {
        // TODO: Pass this info to UI
        IHealthStatus healthStatus = new HealthStatus();

        // Iterate through the health status of all the valves
        // find the health status with max severity
        // assign the max severity
        // And sum up the number of active alerts
        for (Valve valve : this.getValves())
        {
            IHealthStatus valveHealthStatus = valve.getHealthStatus();
            healthStatus.setMaxSeverity(

                    (valveHealthStatus.getMaxSeverity().getValue() <

                    healthStatus.getMaxSeverity().getValue()) ?

                            valveHealthStatus.getMaxSeverity() :

                            healthStatus.getMaxSeverity());

            healthStatus.setNumberOfActiveAlerts(
                    healthStatus.getNumberOfActiveAlerts() + valveHealthStatus.getNumberOfActiveAlerts());

        }
        return healthStatus;
    }

    /**
     * @return the valves belonging to this valve set
     */
    @XmlElement(name = "valve")
    public List<Valve> getValves()
    {
        return this.valves;
    }

    /**
     * @param valves the valves belonging to this valve set
     */
    public void setValves(List<Valve> valves)
    {
        this.valves = valves;
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

        ValveSet valveSet = (ValveSet) o;

        return new EqualsBuilder().append(this.id, valveSet.id).append(this.name, valveSet.name)
                .append(this.type, valveSet.type).append(this.description, valveSet.description)
                .append(this.getActiveAlerts(), valveSet.getActiveAlerts())
                //.append(this.getHealthStatus(), valveSet.getHealthStatus())
                .append(this.valves, valveSet.valves)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.type).append(this.description)
                .append(this.valves).toHashCode();
    }

    /**
     * 
     * @author 103019287
     */
    public static class XmlAdaptValveSet extends XmlAdapter<ValveSet, IDevice>
    {
        @Override
        public IDevice unmarshal(ValveSet v)
        {
            return v;
        }

        @Override
        public ValveSet marshal(IDevice v)
        {
            return (ValveSet) v;
        }
    }

}
