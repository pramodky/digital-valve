package com.ge.power.cst.deviceanalytics.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

import com.ge.power.cst.deviceanalytics.api.Util.SubSystemType;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Entity Class to store information about a Sub-System - Gas Fuel, Liquid Fuel,
 * Gas Fuel Purge...
 * 
 * @author 103019287
 *
 */
@XmlType(propOrder =
{
        "name", "description", "type", "devices"
})
@JsonFilter("filterSubSystem")
public class SubSystem
{
    private String        id;
    private String        name;
    private String        description;
    private SubSystemType type;
    private List<IDevice> devices = new ArrayList<IDevice>();

    /**
     * default constructor
     */
    public SubSystem()
    {

    }

    /**
     * @param id - subsystem id
     * @param name - subsystem name
     * @param description - subsystem description
     * @param devices - devices of subsystem
     */
    public SubSystem(String id, String name, String description, List<IDevice> devices)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.devices = devices;
    }

    /**
     * 
     * @return unique identifier of the sub-System
     */
    @XmlTransient
    // @XmlAttribute
    public String getId()
    {
        return this.id;
    }

    /**
     * 
     * @param subSystemId unique identifier of the sub-System
     * 
     */
    public void setId(String subSystemId)
    {
        this.id = subSystemId;
    }

    /**
     * 
     * @param subSystemName name of the sub-System
     * 
     */
    public void setName(String subSystemName)
    {
        this.name = subSystemName;
    }

    /**
     * 
     * @return name of the sub-System
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * @param description sub-system description
     * 
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * 
     * @return sub-system description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @return the type of sub-system
     */
    public SubSystemType getType()
    {
        return this.type;
    }

    /**
     * @param type type of sub-system
     */
    public void setType(SubSystemType type)
    {
        this.type = type;
    }

    /**
     * @return the devices belonging to this sub-system
     */
    @XmlElement(name = "device")
    public List<IDevice> getDevices()
    {
        return this.devices;
    }

    /**
     * @param devices devices belonging to this sub-system
     */
    public void setDevices(List<IDevice> devices)
    {
        this.devices = devices;
    }

    /**
     * @return active alerts associated with this sub-system
     */
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for (IDevice device : this.getDevices())
        {
            activeAlerts.addAll(device.getActiveAlerts());
        }

        return activeAlerts;
    }

    /**
     * @return health status of this sub-system
     */
    public IHealthStatus getHealthStatus()
    {
        // TODO: Pass this info to UI
        IHealthStatus healthStatus = new HealthStatus();

        // Iterate through the health status of all the devices
        // find the health status with max severity
        // assign the max severity
        // And sum up the number of active alerts
        for (IDevice device : this.getDevices())
        {
            IHealthStatus valveHealthStatus = device.getHealthStatus();
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

        SubSystem subSystem = (SubSystem) o;

        return new EqualsBuilder().append(this.id, subSystem.id).append(this.name, subSystem.name)
                .append(this.description, subSystem.description)
                .append(this.getActiveAlerts(), subSystem.getActiveAlerts())
                //.append(this.getHealthStatus(), subSystem.getHealthStatus())
                .append(this.devices, subSystem.devices)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.description)
                .append(this.devices).toHashCode();
    }
}
