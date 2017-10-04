package com.ge.power.cst.deviceanalytics.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Entity Class to store information about a Block
 * 
 * @author 103019287
 *
 */
@XmlType(propOrder =
{
        "name", "description", "units"
})
@JsonFilter("filterBlock")
public class Block
{
    private String     id;
    private String     name;
    private String     description;
    private List<Unit> units = new ArrayList<Unit>();

    /**
     * Default constructor for Block class
     */
    public Block()
    {

    }

    /**
     * @param id block id
     * @param name block name
     * @param description block description
     * @param units units belonging to this block
     */
    public Block(String id, String name, String description, List<Unit> units)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.units = units;
    }

    /**
     * 
     * @param blockId unique Identifier of the Block
     */
    public void setId(String blockId)
    {
        this.id = blockId;
    }

    /**
     * 
     * @return unique Identifier of the Block
     */
    @XmlTransient
    public String getId()
    {
        return this.id;
    }

    /**
     * 
     * @param blockName name of the block
     * 
     */
    public void setName(String blockName)
    {
        this.name = blockName;
    }

    /**
     * 
     * @return name of the block
     */

    public String getName()
    {
        return this.name;
    }

    /**
     * 
     * @param description block description
     * 
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * 
     * @return block description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * 
     * @param units units belonging to this block
     */
    public void setUnits(List<Unit> units)
    {
        this.units = units;
    }

    /**
     * 
     * @return units belonging to this block
     */
    @XmlElement(name = "unit")
    public List<Unit> getUnits()
    {
        return this.units;
    }

    /**
     * @return active alerts associated with this block
     */
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for (Unit unit : this.getUnits())
        {
            activeAlerts.addAll(unit.getActiveAlerts());
        }

        return activeAlerts;
    }

    /**
     * @return health status of this block
     */
    public IHealthStatus getHealthStatus()
    {
        // TODO: Pass this info to UI
        IHealthStatus healthStatus = new HealthStatus();

        // Iterate through the health status of all the units
        // find the health status with max severity
        // assign the max severity
        // And sum up the number of active alerts
        for (Unit unit : this.getUnits())
        {
            IHealthStatus valveHealthStatus = unit.getHealthStatus();
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

        Block block = (Block) o;

        return new EqualsBuilder().append(this.id, block.id).append(this.name, block.name)
                .append(this.description, block.description)
                .append(this.getActiveAlerts(), block.getActiveAlerts())
                //.append(this.getHealthStatus(), block.getHealthStatus())
                .append(this.units, block.units).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.name).append(this.description).append(this.units)
                .toHashCode();
    }
}
