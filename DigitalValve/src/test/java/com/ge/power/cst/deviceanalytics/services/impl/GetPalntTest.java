/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics.services.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ge.power.cst.deviceanalytics.api.Block;
import com.ge.power.cst.deviceanalytics.api.Plant;
import com.ge.power.cst.deviceanalytics.api.SubSystem;
import com.ge.power.cst.deviceanalytics.api.System;
import com.ge.power.cst.deviceanalytics.api.Unit;
import com.ge.power.cst.deviceanalytics.api.Util.DeviceType;
import com.ge.power.cst.deviceanalytics.api.Util.FrameSize;
import com.ge.power.cst.deviceanalytics.api.Util.SubSystemType;
import com.ge.power.cst.deviceanalytics.api.Util.SystemType;
import com.ge.power.cst.deviceanalytics.api.Util.UnitType;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.Valve;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveSet;
import com.ge.power.cst.deviceanalytics.services.IPlantConfigService;
import com.ge.power.cst.deviceanalytics.services.PlantConfigServiceImpl;

/**
 * 
 * @author 103019287
 */
public class GetPalntTest
{
    private IPlantConfigService plantConfigService;
    private Plant               expectedPlant;

    /**
     * Test preparation
     */
    @Before
    public void before()
    {
        this.plantConfigService = new PlantConfigServiceImpl();

        this.expectedPlant = createTestPlant();

        try
        {
            JAXBContext context = JAXBContext.newInstance(Plant.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            FileOutputStream fos = new FileOutputStream("test-plant.xml"); //$NON-NLS-1$

            m.marshal(this.expectedPlant, fos);
            fos.close();

        }
        catch (JAXBException | FileNotFoundException e)
        {
            Assert.fail("Exception " + e); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            Assert.fail("Exception " + e); //$NON-NLS-1$
        }

    }

    private Plant createTestPlant()
    {
        Plant plant = new Plant();
        plant.setName("PLANT Number - 1"); //$NON-NLS-1$
        plant.setDescription("Description plant"); //$NON-NLS-1$
        for (int i = 0; i < 5; i++)
        {
            plant.getBlocks().add(createTestBlock(i + 1));
        }

        return plant;
    }

    private int gtUnitId = 1;
    private int stUnitId = 1;

    private Block createTestBlock(int index)
    {
        Block block = new Block();
        block.setId("BLOCK" + index); //$NON-NLS-1$
        block.setName("BLOCK - " + index); //$NON-NLS-1$
        block.setDescription("Description block - " + index); //$NON-NLS-1$

        block.getUnits().add(createTestUnit(this.gtUnitId++, UnitType.GT));
        block.getUnits().add(createTestUnit(this.gtUnitId++, UnitType.GT));
        block.getUnits().add(createTestUnit(this.stUnitId++, UnitType.ST));

        return block;
    }

    private Unit createTestUnit(int unitId, UnitType unitType)
    {
        Unit unit = new Unit();
        unit.setId(unitType.toString() + unitId);
        unit.setUnitType(unitType);

        if ( unitType == UnitType.GT )
        {
            unit.setName("Gas Turbine " + unitId); //$NON-NLS-1$
            unit.setFrameSize(FrameSize.GT_7F_05);
            unit.getSystems().add(createTestSystem(unit.getId(), SystemType.GT_CS, "Compressor Systems")); //$NON-NLS-1$
            unit.getSystems().add(createTestSystem(unit.getId(), SystemType.GT_FS, "Fuel System")); //$NON-NLS-1$
        }
        else if ( unitType == UnitType.ST )
        {

            unit.setName("Steam Turbine " + unitId); //$NON-NLS-1$
            unit.setFrameSize(FrameSize.ST_D14);
            unit.getSystems().add(createTestSystem(unit.getId(), SystemType.ST_HP, "HP System")); //$NON-NLS-1$
            unit.getSystems().add(createTestSystem(unit.getId(), SystemType.ST_IP, "IP System")); //$NON-NLS-1$
            unit.getSystems().add(createTestSystem(unit.getId(), SystemType.ST_LP, "LP System")); //$NON-NLS-1$

        }

        return unit;
    }

    private System createTestSystem(String unitId, SystemType systemType, String name)
    {

        System system = new System();

        system.setName(name);
        system.setType(systemType);
        system.setDescription("Description - " + name); //$NON-NLS-1$

        system.setId(unitId + "_" + systemType.toString()); //$NON-NLS-1$
        SubSystem subSystem = new SubSystem();

        switch (systemType)
        {
            case GT_CS:
            {
                subSystem = createSubSystem(unitId, SubSystemType.GT_CS_GV, "Guided Vanes"); //$NON-NLS-1$
                break;
            }
            case GT_FS:
            {
                subSystem = createSubSystem(unitId, SubSystemType.GT_FS_GF, "Gas Fuel"); //$NON-NLS-1$
                break;
            }
            case ST_HP:
            {
                subSystem = createSubSystem(unitId, SubSystemType.ST_HP_HP, ""); //$NON-NLS-1$
                break;
            }
            case ST_IP:
            {
                subSystem = createSubSystem(unitId, SubSystemType.ST_IP_IP, ""); //$NON-NLS-1$
                break;
            }
            case ST_LP:
            {
                subSystem = createSubSystem(unitId, SubSystemType.ST_LP_LP, ""); //$NON-NLS-1$
                break;
            }
            default:
            {
                break;
            }
        }

        system.getSubSystems().add(subSystem);

        return system;

    }

    private SubSystem createSubSystem(String unitId, SubSystemType subSystemType, String name)
    {
        SubSystem subSystem = new SubSystem();
        String subSystemId = unitId + "_" + subSystemType.toString(); //$NON-NLS-1$
        subSystem.setId(subSystemId);
        subSystem.setName(name);
        subSystem.setDescription("Description - " + name); //$NON-NLS-1$
        subSystem.setType(subSystemType);

        switch (subSystemType)
        {
            case ST_HP_HP:
            {
                subSystem.getDevices().add(createValveSet(subSystemId, 1, DeviceType.MSCV, "MSCV #1")); //$NON-NLS-1$
                subSystem.getDevices().add(createValveSet(subSystemId, 2, DeviceType.MSCV, "MSCV #2")); //$NON-NLS-1$
                break;
            }
            case ST_IP_IP:
            {
                subSystem.getDevices().add(createValveSet(subSystemId, 1, DeviceType.CRV, "CRV #1")); //$NON-NLS-1$
                subSystem.getDevices().add(createValveSet(subSystemId, 2, DeviceType.CRV, "CRV #2")); //$NON-NLS-1$
                break;
            }
            case GT_CS_GV:
            {
                subSystem.getDevices().add(createValveSet(subSystemId, 1, DeviceType.UNKNOWN, "")); //$NON-NLS-1$
                break;
            }
            case GT_FS_GF:
            {
                subSystem.getDevices().add(createValveSet(subSystemId, 1, DeviceType.UNKNOWN, "")); //$NON-NLS-1$
                break;
            }
            case ST_LP_LP:
            {
                subSystem.getDevices().add(createValveSet(subSystemId, 1, DeviceType.UNKNOWN, "")); //$NON-NLS-1$
                break;
            }
            default:
                break;
        }

        return subSystem;
    }

    private ValveSet createValveSet(String subSystemId, int itemNum, DeviceType actuatorSetType, String name)
    {
        ValveSet valveSet = new ValveSet();
        String valveSetId = subSystemId + "_" + actuatorSetType.toString() + itemNum; //$NON-NLS-1$
        valveSet.setId(valveSetId);

        valveSet.setName(name);
        valveSet.setDescription("Description - " + name); //$NON-NLS-1$
        valveSet.setType(actuatorSetType);

        switch (actuatorSetType)
        {
            case MSCV:
            {
                valveSet.getValves().add(createValve(valveSetId + "_CV", name + " CV", true)); //$NON-NLS-1$ //$NON-NLS-2$
                valveSet.getValves().add(createValve(valveSetId + "_MSV", name + " MSV", false)); //$NON-NLS-1$ //$NON-NLS-2$
                break;
            }
            case CRV:
            {
                valveSet.getValves().add(createValve(valveSetId + "_IV", name + " IV", true)); //$NON-NLS-1$ //$NON-NLS-2$
                valveSet.getValves().add(createValve(valveSetId + "_RSV", name + " RSV", false)); //$NON-NLS-1$ //$NON-NLS-2$
                break;
            }
            default:
                break;

        }
        return valveSet;
    }

    private Valve createValve(String id, String name, boolean isControlValve)
    {
        Valve valve = new Valve();
        valve.setId(id);
        valve.setName(name);
        valve.setDescription("Description - " + name); //$NON-NLS-1$
        valve.setControlValve(isControlValve);

        return valve;
    }

    /**
     * Test case to test getPlant method
     */
    @Test
    public void getPlant()
    {
        try
        {
            Plant plant = this.plantConfigService.getPlant();
            printPlant(plant);

            assertThat(plant).isEqualTo(this.expectedPlant);
        }
        catch (JAXBException e)
        {
            Assert.fail("Exception " + e); //$NON-NLS-1$
        }
    }

    private void printPlant(Plant plant)
    {

        try
        {
            JAXBContext context;
            context = JAXBContext.newInstance(Plant.class);

            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            m.marshal(plant, java.lang.System.out);
        }
        catch (JAXBException e)
        {
            Assert.fail("Exception " + e); //$NON-NLS-1$
        }

    }

    /**
     * Test case to check health status of plant - no active alerts
     */
    @Test
    public void getPlantHealthStatus()
    {
        try
        {
             String expectedPlantHealthStatus = "{\"healthStatus\":{\"numberOfActiveAlerts\":0,\"maxSeverity\":\"INFORMATION\"}}"; //$NON-NLS-1$
            
            String actualPlantHealthStatus = this.plantConfigService.getPlantHealthStatus();
            java.lang.System.out.println(actualPlantHealthStatus);
            assertThat(actualPlantHealthStatus).isEqualTo(expectedPlantHealthStatus);
        }
        catch (JAXBException | IOException e)
        {
            Assert.fail("Exception " + e); //$NON-NLS-1$
        }
    }
    
    /**
     * Test case to check health status of block - no active alerts
     */
    @Test
    public void getBlockHealthStatus()
    {
        try
        {
             String expectedBlockHealthStatus = "{\"healthStatus\":{\"numberOfActiveAlerts\":0,\"maxSeverity\":\"INFORMATION\"}}"; //$NON-NLS-1$
            
            String actualBlockHealthStatus = this.plantConfigService.getBlockHealthStatus("BLOCK1"); //$NON-NLS-1$
            java.lang.System.out.println(actualBlockHealthStatus);
            assertThat(actualBlockHealthStatus).isEqualTo(expectedBlockHealthStatus);
        }
        catch (JAXBException | IOException e)
        {
            Assert.fail("Exception " + e); //$NON-NLS-1$
        }
    }

    /**
     * 
     */
    @Test
    public void getPlantInfo()
    {
        try
        {
            String expectedPlantInfo = "{\"name\":\"PLANT Number - 1\",\"description\":\"Description plant\"," //$NON-NLS-1$
                    + "\"blocks\":[{\"id\":\"BLOCK1\",\"name\":\"BLOCK - 1\"}," //$NON-NLS-1$
                    + "{\"id\":\"BLOCK2\",\"name\":\"BLOCK - 2\"}," + "{\"id\":\"BLOCK3\",\"name\":\"BLOCK - 3\"}," //$NON-NLS-1$ //$NON-NLS-2$
                    + "{\"id\":\"BLOCK4\",\"name\":\"BLOCK - 4\"}," + "{\"id\":\"BLOCK5\",\"name\":\"BLOCK - 5\"}]}"; //$NON-NLS-1$ //$NON-NLS-2$
            String actualPlantInfo = this.plantConfigService.getPlantInfo();
            java.lang.System.out.println(actualPlantInfo);
            assertThat(actualPlantInfo).isEqualTo(expectedPlantInfo);
        }
        catch (JAXBException | IOException e)
        {
            Assert.fail("Exception " + e); //$NON-NLS-1$
        }
    }
}
