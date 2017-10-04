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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * 
 * @author 103019287
 */
@XmlRootElement
@XmlType(propOrder =
{
        "name", "description", "blocks"
})
@JsonFilter("filterPlant")
public class Plant
{
    private String      name;
    private String      description;
    private List<Block> blocks;

    /**
     * default constructor for Plant class
     */
    public Plant()
    {
        this.blocks = new ArrayList<Block>();
    }

    /**
     * @return name of the plant
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param name name of the plant
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return plant description
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     * @param description plant description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return blocks belonging to this plant
     */
    @XmlElement(name = "block")
    public List<Block> getBlocks()
    {
        return this.blocks;
    }

    /**
     * @param blocks blocks belonging to this plant
     */
    public void setBlocks(List<Block> blocks)
    {
        this.blocks = blocks;
    }

    /**
     * @return active alerts associated with this block
     */
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for (Block block : this.getBlocks())
        {
            activeAlerts.addAll(block.getActiveAlerts());
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

        // Iterate through the health status of all the blocks
        // find the health status with max severity
        // assign the max severity
        // And sum up the number of active alerts
        for (Block block : this.getBlocks())
        {
            IHealthStatus valveHealthStatus = block.getHealthStatus();
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

        Plant plant = (Plant) o;

        return new EqualsBuilder().append(this.name, plant.name).append(this.description, plant.description)
                .append(this.getActiveAlerts(), plant.getActiveAlerts())
                //.append(this.getHealthStatus(), plant.getHealthStatus())
                .append(this.blocks, plant.blocks).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37).append(this.name).append(this.description).append(this.blocks).toHashCode();
    }
}
