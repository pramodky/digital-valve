/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics.services;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import com.ge.power.cst.deviceanalytics.api.System;
import com.ge.power.cst.deviceanalytics.api.Unit;
import com.ge.power.cst.deviceanalytics.api.SubSystem;
import com.ge.power.cst.deviceanalytics.api.Block;
import com.ge.power.cst.deviceanalytics.api.IDevice;
import com.ge.power.cst.deviceanalytics.api.Plant;

/**
 * 
 * @author 103019287
 */
public class PlantConfigServiceImpl
        implements IPlantConfigService
{

    private Plant      cachedPlant;
    private final Lock lock;

    /**
     * Service constructor<br>
     * This service populates api with plant info configured in ActuatorAnalyticsPlant.xml file
     */
    public PlantConfigServiceImpl()
    {
        this.lock = new ReentrantLock();
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.actuatoranalytics.plant.services.IPlantConfigService#getPlant()
     */
    @Override
    public Plant getPlant()
            throws JAXBException
    {
        if ( this.cachedPlant == null )
        {
            this.lock.lock();
            try
            {
                this.cachedPlant = (this.cachedPlant != null) ? this.cachedPlant : loadPlant();
            }
            finally
            {
                this.lock.unlock();
            }

        }
        return this.cachedPlant;
    }

    private Plant loadPlant()
            throws JAXBException
    {
        File file = new File("test-plant.xml"); //$NON-NLS-1$
        JAXBContext jaxbContext;

        jaxbContext = JAXBContext.newInstance(Plant.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        Plant plant = (Plant) jaxbUnmarshaller.unmarshal(file);

        //Generate Ids
        for (Block block : plant.getBlocks())
        {
            generateId(block);
        }

        return plant;
    }

    private int blockIdSeed = 0;

    private void generateId(Block block)
    {
        // Generate and set Id
        this.blockIdSeed++;
        block.setId("BLOCK" + this.blockIdSeed); //$NON-NLS-1$

        for (Unit unit : block.getUnits())
        {
            generateUnitId(unit);
        }
    }

    private void generateUnitId(Unit unit)
    {
        // Generate and set Unit Id -- Not required... supplied through XML

        // Generate Id for Systems
        for (System system : unit.getSystems())
        {
            generateSystemId(unit.getId(), system);
        }
    }

    private void generateSystemId(String unitId, System system)
    {
        // Generate and set Id
        system.setId(unitId + "_" + system.getType().toString()); //$NON-NLS-1$

        // Generate Id for Sub Systems
        for (SubSystem subSystem : system.getSubSystems())
        {
            generateSubSystemId(unitId, subSystem);
        }

    }

    private void generateSubSystemId(String unitId, SubSystem subSystem)
    {
        // Generate and set Id
        String subSystemId = unitId + "_" + subSystem.getType().toString(); //$NON-NLS-1$
        subSystem.setId(subSystemId);

        int itemNum = 1;
        // Generate Id for devices
        for (IDevice device : subSystem.getDevices())
        {
            generateDeviceId(subSystemId, itemNum++, device);
        }
    }

    private void generateDeviceId(String subSystemId, int itemNum, IDevice device)
    {
        String deviceId = subSystemId + "_" + device.getType() + itemNum; //$NON-NLS-1$
        device.setId(deviceId);

        // Generate Id for Valve -- Not required... supplied through XML
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getPlant()
     */
    @Override
    public String getPlantHealthStatus()
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        Plant plant = getPlant();
        ObjectMapper jsonParser = new ObjectMapper();

        // Exclude all field except health status from serialization...
        String[] ignorableFieldNamesPlant =
        {
                "name", "description", "blocks", "activeAlerts" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        };

        // and use it to create filters
        FilterProvider filters = new SimpleFilterProvider().addFilter("filterPlant", //$NON-NLS-1$
                SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNamesPlant));

        // Create writer object with the filters
        ObjectWriter writer = jsonParser.writer(filters);

        // Parse the plant instance to Json string
        String jsonString = writer.writeValueAsString(plant);

        return jsonString;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getBlockHealthStatus(java.lang.String)
     */
    @Override
    public String getBlockHealthStatus(String blockId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        Block identifiedBlock = null;
        for (Block block : getPlant().getBlocks())
        {
            if ( block.getId().equals(blockId) )
            {
                identifiedBlock = block;
                break;
            }
        }

        if ( identifiedBlock == null ) return null;

        ObjectMapper jsonParser = new ObjectMapper();

        // Exclude all field except health status from serialization...
        String[] ignorableFieldNamesBlock =
        {
                "id", "name", "description", "units", "activeAlerts" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        };

        // and use it to create filters
        FilterProvider filters = new SimpleFilterProvider().addFilter("filterBlock", //$NON-NLS-1$
                SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNamesBlock));

        // Create writer object with the filters
        ObjectWriter writer = jsonParser.writer(filters);

        // Parse the plant instance to Json string
        String jsonString = writer.writeValueAsString(identifiedBlock);

        return jsonString;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getUnitHealthStatus()
     */
    @Override
    public String getUnitHealthStatus(String blockId, String unitId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getSystemHealthStatus(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getSystemHealthStatus(String blockId, String unitId, String systemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getSubSystemHealthStatus(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public String getSubSystemHealthStatus(String blockId, String unitId, String systemId, String subSystemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getDeviceHealthStatus(java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public String getDeviceHealthStatus(String blockId, String unitId, String systemId, String subSystemId,
            String deviceId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getPlantInfo()
     */
    @Override
    public String getPlantInfo()
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        Plant plant = getPlant();
        ObjectMapper jsonParser = new ObjectMapper();

        // Exclude health status and active alerts from serialization...
        String[] ignorableFieldNamesPlant =
        {
                "healthStatus", "activeAlerts" //$NON-NLS-1$ //$NON-NLS-2$
        };

        // Exclude all field except id and name from serialization...
        String[] ignorableFieldNamesBlock =
        {
                "description", "units", "healthStatus", "activeAlerts" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        };

        // and use it to create filters
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("filterPlant", //$NON-NLS-1$
                        SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNamesPlant))
                .addFilter("filterBlock", SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNamesBlock)); //$NON-NLS-1$

        // Create writer object with the filters
        ObjectWriter writer = jsonParser.writer(filters);

        // Parse the plant instance to Json string
        String jsonString = writer.writeValueAsString(plant);

        return jsonString;
    }

    /* (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getBlockInfo(java.lang.String)
     */
    @Override
    public String getBlockInfo(String blockId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getUnitInfo(java.lang.String, java.lang.String)
     */
    @Override
    public String getUnitInfo(String blockId, String unitId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getSystemInfo(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getSystemInfo(String blockId, String unitId, String systemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getSubSystemInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getSubSystemInfo(String blockId, String unitId, String systemId, String subSystemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.services.IPlantConfigService#getDeviceInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String getDeviceInfo(String blockId, String unitId, String systemId, String subSystemId, String deviceId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException
    {
        // TODO Auto-generated method stub
        return null;
    }
}
