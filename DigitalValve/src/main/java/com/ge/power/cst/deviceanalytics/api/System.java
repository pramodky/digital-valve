package com.ge.power.cst.deviceanalytics.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

import com.ge.power.cst.deviceanalytics.api.Util.SystemType;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Entity Class to store information about a System - Fuel System, Purge
 * System...
 * 
 * @author 103019287
 *
 */
@XmlType(propOrder =
{
        "name", "description", "type", "subSystems"
})
@JsonFilter("filterSystem")
public class System
{

    private String          id;
    private String          name;
    private String          description;
    private SystemType      type;
    private List<SubSystem> subSystems = new ArrayList<SubSystem>();

    /**
     * default constructor
     */
    public System()
    {

    }

    /**
     * @param id system id
     * @param name system name
     * @param description system description
     * @param subSystems subsystems belonging to this system
     */
    public System(String id, String name, String description, List<SubSystem> subSystems)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subSystems = subSystems;
    }

    /**
     * 
     * @param systemId unique identifier of the system
     */
    public void setId(String systemId)
    {
        this.id = systemId;
    }

    /**
     * 
     * @return unique identifier of the system
     */
    @XmlTransient
    // @XmlAttribute
    public String getId()
    {
        return this.id;
    }

    /**
     * 
     * @param systemName name of the system
     * 
     */
    public void setName(String systemName)
    {
        this.name = systemName;
    }

    /**
     * 
     * @return name of the system
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * @param description system description
     * 
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * 
     * @return system description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @return the system type
     */
    public SystemType getType()
    {
        return this.type;
    }

    /**
     * @param type system type
     */
    public void setType(SystemType type)
    {
        this.type = type;
    }

    /**
     * @return the sub systems belonging to this system
     */
    @XmlElement(name = "subSystem")
    public List<SubSystem> getSubSystems()
    {
        return this.subSystems;
    }

    /**
     * @param subSystems sub systems belonging to this system
     */
    public void setSubSystems(List<SubSystem> subSystems)
    {
        this.subSystems = subSystems;
    }

    /**
     * @return active alerts associated with this system
     */
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for (SubSystem subSystem : this.getSubSystems())
        {
            activeAlerts.addAll(subSystem.getActiveAlerts());
        }
        return activeAlerts;
    }

    /**
     * @return health status of this system
     */
    public IHealthStatus getHealthStatus()
    {
        // TODO: Pass this info to UI
        IHealthStatus healthStatus = new HealthStatus();

        // Iterate through the health status of all the sub systems
        // find the health status with max severity
        // assign the max severity
        // And sum up the number of active alerts
        for (SubSystem subSystem : this.getSubSystems())
        {
            IHealthStatus valveHealthStatus = subSystem.getHealthStatus();
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

        System system = (System) o;

        return new EqualsBuilder().append(this.id, system.id).append(this.name, system.name)
                .append(this.description, system.description)
                .append(this.getActiveAlerts(), system.getActiveAlerts())
                //.append(this.getHealthStatus(), system.getHealthStatus())
                .append(this.subSystems, system.subSystems)
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.description)
                .append(this.subSystems).toHashCode();
    }
}
