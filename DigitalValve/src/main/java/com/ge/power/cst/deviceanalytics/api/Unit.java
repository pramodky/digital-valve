package com.ge.power.cst.deviceanalytics.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

import com.ge.power.cst.deviceanalytics.api.Util.FrameSize;
import com.ge.power.cst.deviceanalytics.api.Util.UnitType;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * Entity Class to store information about an Unit - GT1, GT2, ST1, ST2,...
 * 
 * @author 103019287
 *
 */
@XmlRootElement
// @JsonFilter("filterUnit")
@XmlType(propOrder =
{
        "name", "frameSize", "unitType", "systems"
})
@JsonFilter("filterUnit")
public class Unit
{
    private String       id;
    private String       name;
    private FrameSize    frameSize;
    private UnitType     unitType;
    private List<System> systems = new ArrayList<System>();

    /**
     * Default constructor for Unit class
     */
    public Unit()
    {
        this.systems = new ArrayList<System>();
    }

    /**
     * @param id unit id
     * @param name unit name
     * @param frameSize unit frame
     * @param unitType unit type
     * @param systems systems belonging to this unit
     */
    public Unit(String id, String name, FrameSize frameSize, UnitType unitType, List<System> systems)
    {
        this.id = id;
        this.name = name;
        this.frameSize = frameSize;
        this.unitType = unitType;
        this.systems = systems;
    }

    /**
     * 
     * @param unitId unique identifier for the unit - GT1, GT2, ST1, ST2,...
     */
    public void setId(String unitId)
    {
        this.id = unitId;
    }

    /**
     * 
     * @return unique identifier of the unit - GT1, GT2, ST1, ST2,...
     */
    @XmlAttribute
    public String getId()
    {
        return this.id;
    }

    /**
     * SET
     * 
     * @param unitName name of the unit- Gas Turbine 1, Gas Turbine 2, Steam Turbine 1,
     *            Steam Turbine 2,...
     * 
     */
    public void setName(String unitName)
    {
        this.name = unitName;
    }

    /**
     * 
     * @return name of the unit- Gas Turbine 1, Gas Turbine 2, Steam Turbine 1,
     *         Steam Turbine 2,...
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * @param frameSize frame size of the unit
     */
    public void setFrameSize(FrameSize frameSize)
    {
        this.frameSize = frameSize;
    }

    /**
     * 
     * @return frameSize frame size of the unit
     */
    public FrameSize getFrameSize()
    {
        return this.frameSize;
    }

    /**
     * 
     * @param unitType type of this unit
     */
    public void setUnitType(UnitType unitType)
    {
        this.unitType = unitType;
    }

    /**
     * 
     * @return type of this unit
     */
    public UnitType getUnitType()
    {
        return this.unitType;
    }

    /**
     * 
     * @param systems systems belonging to this unit
     */
    public void setSystems(List<System> systems)
    {
        this.systems = systems;
    }

    /**
     * 
     * @return systems belonging to this unit
     */
    @XmlElement(name = "system")
    public List<System> getSystems()
    {
        return this.systems;
    }

    /**
     * @return active alerts associated with this unit
     */
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for (System system : this.getSystems())
        {
            activeAlerts.addAll(system.getActiveAlerts());
        }

        return activeAlerts;
    }

    /**
     * @return health status of this unit
     */
    public IHealthStatus getHealthStatus()
    {
        // TODO: Pass this info to UI
        IHealthStatus healthStatus = new HealthStatus();

        // Iterate through the health status of all the systems
        // find the health status with max severity
        // assign the max severity
        // And sum up the number of active alerts
        for (System system : this.getSystems())
        {
            IHealthStatus valveHealthStatus = system.getHealthStatus();
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

        Unit unit = (Unit) o;

        return new EqualsBuilder().append(this.id, unit.id).append(this.name, unit.name)
                .append(this.frameSize, unit.frameSize).append(this.unitType, unit.unitType)
                .append(this.getActiveAlerts(), unit.getActiveAlerts())
                //.append(this.getHealthStatus(), unit.getHealthStatus())
                .append(this.systems, unit.systems).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.frameSize)
                .append(this.unitType).append(this.systems).toHashCode();
    }
}
