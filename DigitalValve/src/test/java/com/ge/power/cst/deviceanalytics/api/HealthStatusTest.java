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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.ge.power.cst.deviceanalytics.api.Block;
import com.ge.power.cst.deviceanalytics.api.IHealthStatus;
import com.ge.power.cst.deviceanalytics.api.IAlert;
import com.ge.power.cst.deviceanalytics.api.Plant;
import com.ge.power.cst.deviceanalytics.api.SubSystem;
import com.ge.power.cst.deviceanalytics.api.System;
import com.ge.power.cst.deviceanalytics.api.Unit;
import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.Valve;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveAnalytic;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveAnalyticData;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveSet;

/**
 * 
 * @author 103019287
 */
public class HealthStatusTest
{
    private ValveSet  valveSetWithNoActiveAlerts;
    private ValveSet  valveSetWithSeverityImp;
    private ValveSet  valveSetWithSeverityWarn;

    private SubSystem subSystemWithNoActiveAlerts;
    private SubSystem subSystemWithSeverityWarn;
    private SubSystem subSystemWithSeverityImp;

    private System    systemWithNoActiveAlerts;
    private System    systemWithSeverityWarn;
    private System    systemWithSeverityImp;

    private Unit      unitWithNoActiveAlerts;
    private Unit      unitWithSeverityWarn;
    private Unit      unitWithSeverityImp;

    private Block     blockWithNoActiveAlerts;
    private Block     blockWithSeverityWarn;
    private Block     blockWithSeverityImp;

    private Plant     plantWithNoActiveAlerts;
    private Plant     plantWithSeverityWarn;
    private Plant     plantWithSeverityImp;

    /**
     * Test preparation
     */
    @Before
    public void before()
    {
        // valve sets
        this.valveSetWithNoActiveAlerts = new ValveSet();
        this.valveSetWithNoActiveAlerts.getValves().add(createValve());
        this.valveSetWithNoActiveAlerts.getValves().add(createValve());
        this.valveSetWithNoActiveAlerts.getValves().add(createValve());

        this.valveSetWithSeverityWarn = new ValveSet();
        this.valveSetWithSeverityWarn.getValves().add(createValve());
        this.valveSetWithSeverityWarn.getValves().add(createValve());
        this.valveSetWithSeverityWarn.getValves().add(createValve());
        setSeverityWarn(this.valveSetWithSeverityWarn.getValves().get(0)); // active alerts = 2
        setSeverityWarn(this.valveSetWithSeverityWarn.getValves().get(1)); // active alerts = 2

        this.valveSetWithSeverityImp = new ValveSet();
        this.valveSetWithSeverityImp.getValves().add(createValve());
        this.valveSetWithSeverityImp.getValves().add(createValve());
        this.valveSetWithSeverityImp.getValves().add(createValve());
        setSeverityImp(this.valveSetWithSeverityImp.getValves().get(0)); // active alerts = 3
        setSeverityWarn(this.valveSetWithSeverityImp.getValves().get(1)); // active alerts = 2

        // Sub systems
        this.subSystemWithNoActiveAlerts = new SubSystem();
        this.subSystemWithNoActiveAlerts.getDevices().add(this.valveSetWithNoActiveAlerts);

        this.subSystemWithSeverityWarn = new SubSystem();
        this.subSystemWithSeverityWarn.getDevices().add(this.valveSetWithNoActiveAlerts);
        this.subSystemWithSeverityWarn.getDevices().add(this.valveSetWithSeverityWarn); // active alerts = 4

        this.subSystemWithSeverityImp = new SubSystem();
        this.subSystemWithSeverityImp.getDevices().add(this.valveSetWithNoActiveAlerts);
        this.subSystemWithSeverityImp.getDevices().add(this.valveSetWithSeverityWarn); // active alerts = 4
        this.subSystemWithSeverityImp.getDevices().add(this.valveSetWithSeverityImp); // active alerts = 5

        // system
        this.systemWithNoActiveAlerts = new System();
        this.systemWithNoActiveAlerts.getSubSystems().add(this.subSystemWithNoActiveAlerts);

        this.systemWithSeverityWarn = new System();
        this.systemWithSeverityWarn.getSubSystems().add(this.subSystemWithNoActiveAlerts);
        this.systemWithSeverityWarn.getSubSystems().add(this.subSystemWithSeverityWarn); // active alerts = 4

        this.systemWithSeverityImp = new System();
        this.systemWithSeverityImp.getSubSystems().add(this.subSystemWithNoActiveAlerts);
        this.systemWithSeverityImp.getSubSystems().add(this.subSystemWithSeverityWarn); // active alerts = 4
        this.systemWithSeverityImp.getSubSystems().add(this.subSystemWithSeverityImp); // active alerts = 9

        // Unit
        this.unitWithNoActiveAlerts = new Unit();
        this.unitWithNoActiveAlerts.getSystems().add(this.systemWithNoActiveAlerts);

        this.unitWithSeverityWarn = new Unit();
        this.unitWithSeverityWarn.getSystems().add(this.systemWithNoActiveAlerts);
        this.unitWithSeverityWarn.getSystems().add(this.systemWithSeverityWarn); // active alerts = 4

        this.unitWithSeverityImp = new Unit();
        this.unitWithSeverityImp.getSystems().add(this.systemWithNoActiveAlerts);
        this.unitWithSeverityImp.getSystems().add(this.systemWithSeverityWarn); // active alerts = 4
        this.unitWithSeverityImp.getSystems().add(this.systemWithSeverityImp); // active alerts = 13

        // Block
        this.blockWithNoActiveAlerts = new Block();
        this.blockWithNoActiveAlerts.getUnits().add(this.unitWithNoActiveAlerts);

        this.blockWithSeverityWarn = new Block();
        this.blockWithSeverityWarn.getUnits().add(this.unitWithNoActiveAlerts);
        this.blockWithSeverityWarn.getUnits().add(this.unitWithSeverityWarn); // active alerts = 4

        this.blockWithSeverityImp = new Block();
        this.blockWithSeverityImp.getUnits().add(this.unitWithNoActiveAlerts);
        this.blockWithSeverityImp.getUnits().add(this.unitWithSeverityWarn); // active alerts = 4
        this.blockWithSeverityImp.getUnits().add(this.unitWithSeverityImp); // active alerts = 17

        // plant
        this.plantWithNoActiveAlerts = new Plant();
        this.plantWithNoActiveAlerts.getBlocks().add(this.blockWithNoActiveAlerts);

        this.plantWithSeverityWarn = new Plant();
        this.plantWithSeverityWarn.getBlocks().add(this.blockWithNoActiveAlerts);
        this.plantWithSeverityWarn.getBlocks().add(this.blockWithSeverityWarn); // active alerts = 4

        this.plantWithSeverityImp = new Plant();
        this.plantWithSeverityImp.getBlocks().add(this.blockWithNoActiveAlerts);
        this.plantWithSeverityImp.getBlocks().add(this.blockWithSeverityWarn); // active alerts = 4
        this.plantWithSeverityImp.getBlocks().add(this.blockWithSeverityImp); // active alerts = 21

    }

    private Valve createValve()
    {
        Valve valve = new Valve();
        IAnalytic analytic = new ValveAnalytic();
        valve.getAnalytics().add(analytic);

        for (int i = 1; i <= 5; i++)
        {
            IAnalyticData analyticData = new ValveAnalyticData();
            IAlert alert = analyticData.getAlert();
            alert.setName("alert_" + i); //$NON-NLS-1$
            alert.setSeverity(SeverityType.INFORMATION);
            analytic.getAnalyticDataSet().add(analyticData);
        }
        return valve;
    }

    private void setSeverityImp(Valve valve)
    {
        for (IAnalyticData analyticData: valve.getAnalytics().get(0).getAnalyticDataSet())
        {
            IAlert alert=analyticData.getAlert();
            switch (alert.getName())
            {
                case "alert_1": //$NON-NLS-1$
                case "alert_2": //$NON-NLS-1$
                {
                    alert.setSeverity(SeverityType.IMPORTANT);
                    break;
                }
                case "alert_3": //$NON-NLS-1$
                {
                    // alert.setValue(true);
                    alert.setSeverity(SeverityType.WARNING);
                    break;
                }
                default:
                {
                    // alert.setValue(false);
                    alert.setSeverity(SeverityType.INFORMATION);
                    break;
                }
            }
        }
    }

    private void setSeverityWarn(Valve valve)
    {
        for (IAnalyticData analyticData: valve.getAnalytics().get(0).getAnalyticDataSet())
        {
            IAlert alert=analyticData.getAlert();
            switch (alert.getName())
            {
                case "alert_2": //$NON-NLS-1$
                case "alert_4": //$NON-NLS-1$
                {
                    // alert.setValue(true);
                    alert.setSeverity(SeverityType.WARNING);
                    break;
                }
                default:
                {
                    // alert.setValue(false);
                    alert.setSeverity(SeverityType.INFORMATION);
                    break;
                }
            }
        }
    }

    /**
     * valves with no active alerts
     */
    @Test
    public void testHealthStatusInfo()
    {
        IHealthStatus hs = this.valveSetWithNoActiveAlerts.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    /**
     * valves with active alerts having max severity of WARNING
     */
    @Test
    public void testHealthStatusWarn()
    {
        IHealthStatus hs = this.valveSetWithSeverityWarn.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(4);
    }

    /**
     * valves with active alerts having max severity of IMPORTANT
     */
    @Test
    public void testHealthStatusImp()
    {
        IHealthStatus hs = this.valveSetWithSeverityImp.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(5);
    }

    /**
     * sub system with no active alerts
     */
    @Test
    public void testSubSystemWithStatusInfo()
    {
        IHealthStatus hs = this.subSystemWithNoActiveAlerts.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    /**
     * sub system with active alerts having max severity of WARNING
     */
    @Test
    public void testSubSystemWithStatusWarn()
    {
        IHealthStatus hs = this.subSystemWithSeverityWarn.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(4);
    }

    /**
     * sub system with active alerts having max severity of IMPORTANT
     */
    @Test
    public void testSubSystemWithStatusImp()
    {
        IHealthStatus hs = this.subSystemWithSeverityImp.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(9);
    }

    /**
     * system with no active alerts
     */
    @Test
    public void testSystemWithStatusInfo()
    {
        IHealthStatus hs = this.systemWithNoActiveAlerts.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    /**
     * system with active alerts having max severity of WARNING
     */
    @Test
    public void testSystemWithStatusWarn()
    {
        IHealthStatus hs = this.systemWithSeverityWarn.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(4);
    }

    /**
     * system with active alerts having max severity of IMPORTANT
     */
    @Test
    public void testSystemWithStatusImp()
    {
        IHealthStatus hs = this.systemWithSeverityImp.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(13);
    }

    /**
     * unit with no active alerts
     */
    @Test
    public void testUnitWithStatusInfo()
    {
        IHealthStatus hs = this.unitWithNoActiveAlerts.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    /**
     * unit with active alerts having max severity of WARNING
     */
    @Test
    public void testUnitWithStatusWarn()
    {
        IHealthStatus hs = this.unitWithSeverityWarn.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(4);
    }

    /**
     * unit with active alerts having max severity of IMPORTANT
     */
    @Test
    public void testUnitWithStatusImp()
    {
        IHealthStatus hs = this.unitWithSeverityImp.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(17);
    }

    /**
     * block with no active alerts
     */
    @Test
    public void testBlockWithStatusInfo()
    {
        IHealthStatus hs = this.blockWithNoActiveAlerts.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    /**
     * block with active alerts having max severity of WARNING
     */
    @Test
    public void testBlockWithStatusWarn()
    {
        IHealthStatus hs = this.blockWithSeverityWarn.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(4);
    }

    /**
     * block with active alerts having max severity of IMPORTANT
     */
    @Test
    public void testBlockWithStatusImp()
    {
        IHealthStatus hs = this.blockWithSeverityImp.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(21);
    }

    /**
     * plant with no active alerts
     */
    @Test
    public void testPlantWithStatusInfo()
    {
        IHealthStatus hs = this.plantWithNoActiveAlerts.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    /**
     * plant with active alerts having max severity of WARNING
     */
    @Test
    public void testPlantWithStatusWarn()
    {
        IHealthStatus hs = this.plantWithSeverityWarn.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(4);
    }

    /**
     * plant with active alerts having max severity of IMPORTANT
     */
    @Test
    public void testPlantWithStatusImp()
    {
        IHealthStatus hs = this.plantWithSeverityImp.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(25);
    }

}
