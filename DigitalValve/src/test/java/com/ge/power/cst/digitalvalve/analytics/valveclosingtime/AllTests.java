/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.digitalvalve.analytics.valveclosingtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ge.power.cst.deviceanalytics.api.Alert;
import com.ge.power.cst.deviceanalytics.api.BasicData;
import com.ge.power.cst.deviceanalytics.api.IAlert;
import com.ge.power.cst.deviceanalytics.api.IAnalytic;
import com.ge.power.cst.deviceanalytics.api.IAnalyticData;
import com.ge.power.cst.deviceanalytics.api.IBasicData;
import com.ge.power.cst.deviceanalytics.api.IHealthStatus;
import com.ge.power.cst.deviceanalytics.api.Util.AggregationType;
import com.ge.power.cst.deviceanalytics.api.Util.DataType;
import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveClosingTimeAnalytic;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.Valve;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveAnalyticData;

/**
 * 
 * @author 103019287
 */
public class AllTests
{
    private Valve                    valve;
    private ValveClosingTimeAnalytic closingTimeAnalytic;

    private long                     strokeTestStartTime;
    private long                     fasvStartTime;
    private long                     valveClosedTime;
    private long                     valveOpenedTime;

    /**
     * Test preparation
     */
    @Before
    public void before()
    {
        this.valve = new Valve();
        List<IAnalytic> analytics = new ArrayList<IAnalytic>();
        this.valve.setAnalytics(analytics);

        this.closingTimeAnalytic = new ValveClosingTimeAnalytic();
        analytics.add(this.closingTimeAnalytic);

        // prepare analytic data set
        List<IAnalyticData> analyticDataSet = new ArrayList<IAnalyticData>();
        // prepare analytic data for servo
        analyticDataSet.add(prepareAnalyticData("SERVO")); //$NON-NLS-1$
        // prepare analytic data for FASV
        analyticDataSet.add(prepareAnalyticData("FASV")); //$NON-NLS-1$
        this.closingTimeAnalytic.setAnalyticDataSet(analyticDataSet);

        // prepare data filter criterion
        this.closingTimeAnalytic.setDataFilterCriteria(prepareFilterCriteria());

    }

    private IAnalyticData prepareAnalyticData(String type)
    {
        IAnalyticData analyticData = new ValveAnalyticData();

        analyticData.setId(type + "_CLOSING_TIME_DEVIATION"); //$NON-NLS-1$
        analyticData.setName(type + " Deviation"); //$NON-NLS-1$

        analyticData.setActionLimit(15D);
        analyticData.setAlarmLimit(20D);

        analyticData.setBaseLine(Double.NaN);
        analyticData.setMeasurementUnit("pct"); //$NON-NLS-1$
        analyticData.setMeasurementUnitBaseLine("ms"); //$NON-NLS-1$
        analyticData.setAggregationType(AggregationType.LINEAR);

        IAlert alert = new Alert();
        alert.setName("ST1_MASV1_CV_" + type + "_DEVIATION_ALERT"); //$NON-NLS-1$ //$NON-NLS-2$
        alert.setDescription(type + " closing time deviation exceeds threshold limits"); //$NON-NLS-1$
        List<String> alertRecommendations = new ArrayList<String>();
        alertRecommendations.add("Recommendation 1 for " + type + " closing time deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alertRecommendations.add("Recommendation 2 for " + type + " closing time deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alertRecommendations.add("Recommendation 3 for " + type + " closing time deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alertRecommendations.add("Recommendation 4 for " + type + " closing time deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alert.setAlertRecommendations(alertRecommendations);
        analyticData.setAlert(alert);

        return analyticData;
    }

    private List<IBasicData> prepareFilterCriteria()
    {
        List<IBasicData> dataFilterCriteria = new ArrayList<IBasicData>();
        IBasicData dataFilterCriterion = new BasicData();
        dataFilterCriterion.setId("SPEED_GT"); //$NON-NLS-1$
        dataFilterCriterion.setName("Speed >"); //$NON-NLS-1$
        dataFilterCriterion.setDataType(DataType.DOUBLE);
        dataFilterCriterion.setValue(500.00);
        dataFilterCriterion.setMeasurementUnit("rpm"); //$NON-NLS-1$
        dataFilterCriteria.add(dataFilterCriterion);

        dataFilterCriterion = new BasicData();
        dataFilterCriterion.setId("INLET_PRESSURE_GT"); //$NON-NLS-1$
        dataFilterCriterion.setName("and Inlet Pressure >"); //$NON-NLS-1$
        dataFilterCriterion.setDataType(DataType.DOUBLE);
        dataFilterCriterion.setValue(50.00);
        dataFilterCriterion.setMeasurementUnit("psi"); //$NON-NLS-1$
        dataFilterCriteria.add(dataFilterCriterion);

        return dataFilterCriteria;
    }

    private void setDataFilter(double speed, double inletPressure)
    {
        this.closingTimeAnalytic.setSpeed(speed);
        this.closingTimeAnalytic.setInletPressure(inletPressure);
    }

    private void simulateStrokeTest(boolean isRealTime, long servoClosingTime, long fasvClosingTime,
            long valveOpeningTime, boolean isFasvPresent, boolean isPartial)
    {
        this.strokeTestStartTime = System.currentTimeMillis();
        this.closingTimeAnalytic.setStrokeTestStarted(true, this.strokeTestStartTime);

        this.fasvStartTime = this.strokeTestStartTime + servoClosingTime;
        if ( isRealTime )
        {
            try
            {
                Thread.sleep(servoClosingTime);
            }
            catch (InterruptedException e)
            {
                Assert.fail("Exception " + e); //$NON-NLS-1$
            }
            this.fasvStartTime = System.currentTimeMillis();
        }
        if ( isFasvPresent )
        {
            this.closingTimeAnalytic.setFasvCommand(true, this.fasvStartTime);
        }

        this.valveClosedTime = this.fasvStartTime + fasvClosingTime;
        if ( isRealTime )
        {
            try
            {
                Thread.sleep(fasvClosingTime);
            }
            catch (InterruptedException e)
            {
                Assert.fail("Exception " + e); //$NON-NLS-1$
            }
            this.valveClosedTime = System.currentTimeMillis();
        }
        if ( !isPartial )
        {
            this.closingTimeAnalytic.setValveClosed(true, this.valveClosedTime);
        }

        this.valveOpenedTime = this.valveClosedTime + valveOpeningTime;
        if ( isRealTime )
        {
            try
            {
                Thread.sleep(valveOpeningTime);
            }
            catch (InterruptedException e)
            {
                Assert.fail("Exception " + e); //$NON-NLS-1$
            }
            this.valveOpenedTime = System.currentTimeMillis();
        }
        this.closingTimeAnalytic.setValveOpened(true, this.valveOpenedTime);

    }

    @Test
    public void testRealTime()
    {
        // data filter criteria not met 1
        this.setDataFilter(0D, 0D);
        simulateStrokeTest(true, 0, 0, 0, true, false);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(null);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(null);

        // data filter criteria not met 2
        this.setDataFilter(500D, 51D);
        simulateStrokeTest(true, 0, 0, 0, true, false);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(null);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(null);

        // data filter criteria met baseline calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(true, 1000, 100, 0, true, false);
        long servoBaseline = this.fasvStartTime - this.strokeTestStartTime;
        long fasvBaseline = this.valveClosedTime - this.fasvStartTime;
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);

        // data filter criteria met deviation calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(true, 1001, 101, 0, true, false);
        double servoClosingTime = this.fasvStartTime - this.strokeTestStartTime;
        double fasvClosingTime = this.valveClosedTime - this.fasvStartTime;
        double servoDeviation = 100 * ((servoClosingTime - servoBaseline) / servoBaseline);
        double fasvDeviation = 100 * ((fasvClosingTime - fasvBaseline) / fasvBaseline);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);

        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getCalcValue()).isEqualTo(servoClosingTime);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getCalcValue()).isEqualTo(fasvClosingTime);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(servoDeviation);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(fasvDeviation);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);
        IHealthStatus hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    @Test
    public void testNonRealTime()
    {
        // data filter criteria met baseline calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(false, 1000, 100, 0, true, false);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(1000);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(100);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);

        // data filter criteria met deviation calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(false, 1001, 101, 0, true, false);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getCalcValue()).isEqualTo(1001);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getCalcValue()).isEqualTo(101);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(0.1);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(1D);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);
        IHealthStatus hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);

        // data filter criteria met servo warning alert
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(false, 1150, 110, 0, true, false);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getCalcValue()).isEqualTo(1150);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getCalcValue()).isEqualTo(110);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(15.0);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(10.0);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(1);
        hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(1);

        // data filter criteria met servo warning alert and FASV error alert
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(false, 1150, 120, 10, true, false);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getCalcValue()).isEqualTo(1150);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getCalcValue()).isEqualTo(120);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(15.0);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(20.0);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(2);
        hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(2);
    }

    @Test
    public void testNoFasv()
    {

        // data filter criteria met baseline calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(true, 1000, 100, 0, false, false);
        double servoBaseline = this.valveClosedTime - this.strokeTestStartTime;
        double fasvBaseline = Double.NaN;
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(null);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);

        // data filter criteria met deviation calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(false, 1001, 101, 0, false, false);
        double servoClosingTime = this.valveClosedTime - this.strokeTestStartTime;
        double servoDeviation = 100 * (servoClosingTime - servoBaseline) / servoBaseline;
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);

        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getCalcValue()).isEqualTo(servoClosingTime);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getCalcValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(servoDeviation);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(null);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);
        IHealthStatus hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    @Test
    public void testPartialWithFasv()
    {

        // data filter criteria met baseline calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(true, 1000, 100, 100, true, true);
        double servoBaseline = this.fasvStartTime - this.strokeTestStartTime;
        double fasvBaseline = this.valveOpenedTime - this.fasvStartTime;
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);

        // data filter criteria met deviation calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(false, 1001, 101, 0, true, true);
        double servoClosingTime = this.fasvStartTime - this.strokeTestStartTime;
        double fasvClosingTime = this.valveOpenedTime - this.fasvStartTime;

        double servoDeviation = 100 * (servoClosingTime - servoBaseline) / servoBaseline;
        double fasvDeviation = 100 * (fasvClosingTime - fasvBaseline) / fasvBaseline;
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);

        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getCalcValue()).isEqualTo(servoClosingTime);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getCalcValue()).isEqualTo(fasvClosingTime);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(servoDeviation);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(fasvDeviation);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);
        IHealthStatus hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

    @Test
    public void testPartialWithNoFasv()
    {

        // data filter criteria met baseline calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(true, 1000, 100, 100, false, true);
        double servoBaseline = this.valveOpenedTime - this.strokeTestStartTime;
        double fasvBaseline = Double.NaN;
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(0D);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(null);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);

        // data filter criteria met deviation calculation
        this.setDataFilter(500.001, 50.0001);
        simulateStrokeTest(false, 1001, 101, 0, false, true);
        double servoClosingTime = this.valveOpenedTime - this.strokeTestStartTime;
        double fasvClosingTime = 0.0;

        double servoDeviation = 100 * (servoClosingTime - servoBaseline) / servoBaseline;
        double fasvDeviation = Double.NaN;
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getBaseLine()).isEqualTo(servoBaseline);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getBaseLine()).isEqualTo(fasvBaseline);

        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getCalcValue()).isEqualTo(servoClosingTime);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getCalcValue()).isEqualTo(fasvClosingTime);
        assertThat(this.closingTimeAnalytic.getServoClosingTimeDeviation().getValue()).isEqualTo(servoDeviation);
        assertThat(this.closingTimeAnalytic.getFasvClosingTimeDeviation().getValue()).isEqualTo(null);
        assertThat(this.closingTimeAnalytic.getActiveAlerts().size()).isEqualTo(0);
        IHealthStatus hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }

}
