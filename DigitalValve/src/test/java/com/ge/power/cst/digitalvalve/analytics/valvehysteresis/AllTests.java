/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.digitalvalve.analytics.valvehysteresis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ge.power.cst.deviceanalytics.api.Alert;
import com.ge.power.cst.deviceanalytics.api.BasicData;
import com.ge.power.cst.deviceanalytics.api.IAlert;
import com.ge.power.cst.deviceanalytics.api.IAnalytic;
import com.ge.power.cst.deviceanalytics.api.IAnalyticData;
import com.ge.power.cst.deviceanalytics.api.IBasicData;
import com.ge.power.cst.deviceanalytics.api.IHealthStatus;
import com.ge.power.cst.deviceanalytics.api.ILinearData;
import com.ge.power.cst.deviceanalytics.api.Util.AggregationType;
import com.ge.power.cst.deviceanalytics.api.Util.DataType;
import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.Valve;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveAnalyticData;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveHysteresisAnalytic;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveHysteresisData;

/**
 * 
 * @author 103019287
 */
public class AllTests
{
    private Valve             valve = new Valve();
    private ValveHysteresisAnalytic valveHysteresisAnalytic;

    /**
     * Test preparation
     */
    @Before
    public void before()
    {
        this.valve = new Valve();
        this.valve = new Valve();
        List<IAnalytic> analytics = new ArrayList<IAnalytic>();
        this.valve.setAnalytics(analytics);
        
        this.valveHysteresisAnalytic = new ValveHysteresisAnalytic();
        analytics.add(this.valveHysteresisAnalytic);
        
        // prepare analytic data set
        List<IAnalyticData> analyticDataSet = new ArrayList<IAnalyticData>();
        //prepare analytic data for pup Deviation in closing direction
        analyticDataSet.add(prepareAnalyticData("PUP_CLOSING")); //$NON-NLS-1$
        //prepare analytic data for pup Deviation in opening direction
        analyticDataSet.add(prepareAnalyticData("PUP_OPENING")); //$NON-NLS-1$
        //prepare analytic data for pdp Deviation
        analyticDataSet.add(prepareAnalyticData("PDP")); //$NON-NLS-1$
        this.valveHysteresisAnalytic.setAnalyticDataSet(analyticDataSet);

        //prepare other conditions
        this.valveHysteresisAnalytic.setOtherSettings(prepareOtherSettings());

    }

    private IAnalyticData prepareAnalyticData(String type)
    {
        IAnalyticData analyticData = new ValveAnalyticData();
        //PUP_CLOSING_DEVIATION, PUP_OPENING_DEVIATION, PDP_DEVIATION
        analyticData.setId(type + "_DEVIATION"); //$NON-NLS-1$
        analyticData.setName(type + " Deviation"); //$NON-NLS-1$

        analyticData.setActionLimit(10D);
        analyticData.setAlarmLimit(15D);

        analyticData.setBaseLine(Double.NaN);
        analyticData.setMeasurementUnit("pct"); //$NON-NLS-1$
        if(type.equals("PDP")) //$NON-NLS-1$
        {
            analyticData.setMeasurementUnit("psi"); //$NON-NLS-1$
        }
        else
        {
            analyticData.setMeasurementUnit("psig"); //$NON-NLS-1$
        }
        analyticData.setAggregationType(AggregationType.LINEAR);

        IAlert alert = new Alert();
        alert.setName("ST1_MASV1_CV_" + type + "_DEVIATION_ALERT"); //$NON-NLS-1$ //$NON-NLS-2$
        alert.setDescription(type + " closing time deviation exceeds threshold limits"); //$NON-NLS-1$
        List<String> alertRecommendations = new ArrayList<String>();
        alertRecommendations.add("Recommendation 1 for " + type + " deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alertRecommendations.add("Recommendation 2 for " + type + " deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alertRecommendations.add("Recommendation 3 for " + type + " deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alertRecommendations.add("Recommendation 4 for " + type + " deviation alert"); //$NON-NLS-1$ //$NON-NLS-2$
        alert.setAlertRecommendations(alertRecommendations);
        analyticData.setAlert(alert);
        return analyticData;
    }
    
    private List<IBasicData> prepareOtherSettings()
    {
        List<IBasicData> otherSettings = new ArrayList<IBasicData>();
        IBasicData otherSetting = new BasicData();
        otherSetting.setId("SLOPE_CE"); //$NON-NLS-1$
        otherSetting.setName("Slope in closing direction"); //$NON-NLS-1$
        otherSetting.setDataType(DataType.DOUBLE);
        otherSetting.setValue(Double.NaN);
        otherSetting.setMeasurementUnit(""); //$NON-NLS-1$
        otherSettings.add(otherSetting);

        otherSetting = new BasicData();
        otherSetting.setId("SLOPE_OE"); //$NON-NLS-1$
        otherSetting.setName("Slope in opening direction"); //$NON-NLS-1$
        otherSetting.setDataType(DataType.DOUBLE);
        otherSetting.setValue(Double.NaN);
        otherSetting.setMeasurementUnit(""); //$NON-NLS-1$
        otherSettings.add(otherSetting);
        
        otherSetting = new BasicData();
        otherSetting.setId("INLET_PRESS_HOT_GT"); //$NON-NLS-1$
        otherSetting.setName("Inlet pressure (hot/warm conditions) >"); //$NON-NLS-1$
        otherSetting.setDataType(DataType.DOUBLE);
        otherSetting.setValue(50D);
        otherSetting.setMeasurementUnit("psi"); //$NON-NLS-1$
        otherSettings.add(otherSetting);

        return otherSettings;
    }
    
    private void strokeTestRun(double inletPressure, double offset)
    {
        this.valveHysteresisAnalytic.setInletPressure(inletPressure);
        long strokeTestStartTime = System.currentTimeMillis();
        
        this.valveHysteresisAnalytic.setStrokeTestStarted(true);
     // generate pup vs. pos data in closing direction
        // 1. drop in pup; no change in pos
        // 2. drop in pup; drop in pos
        // 3. drop in pup; no change in pos

        // 1. drop in pup (1000 to 900 step -10) no change in pos (100)
        this.valveHysteresisAnalytic.setPosition(100D);
        for (int pup = 1000; pup >= 900; pup -= 10)
        {
            this.valveHysteresisAnalytic.setPup(pup);
            this.valveHysteresisAnalytic.collectHysteresisData();
        }
        
     // 2. drop in pup; drop in pos - pos = 0.125pup-12.5
        for (int pup = 890; pup >= 100; pup -= 10)
        {
            this.valveHysteresisAnalytic.setPup(pup);
            this.valveHysteresisAnalytic.setPosition(0.125 * pup - (12.5+offset));
            this.valveHysteresisAnalytic.collectHysteresisData();
        }
        
     // 3. drop in pup (90 to 50 step -10); no change in pos (0.0)
        for (int pup = 90; pup >= 50; pup -= 10)
        {
            this.valveHysteresisAnalytic.setPup(pup);
            this.valveHysteresisAnalytic.collectHysteresisData();
        }
        
        this.valveHysteresisAnalytic.setValveClosed(true);
     // generate pup vs. pos data in opening direction
        // 1. increase in pup; no change in pos
        // 2. increase in pup; increase in pos
        // 3. increase in pup; no change in pos

        // 1. increase in pup (60 to 150 step +10); no change in pos (0.0)
        for (int pup = 50; pup <= 150; pup += 10)
        {
            this.valveHysteresisAnalytic.setPup(pup);
            this.valveHysteresisAnalytic.collectHysteresisData();
        }
        
     // 2. increase in pup; increase in pos - pos = 0.125pup -18.75
        for (int pup = 160; pup <= 950; pup += 10)
        {
            this.valveHysteresisAnalytic.setPup(pup);
            this.valveHysteresisAnalytic.setPosition(0.125 * pup - (18.75+offset*2));
            this.valveHysteresisAnalytic.collectHysteresisData();
        }

        // 3. increase in pup; no change in pos
        for (int pup = 960; pup <= 1000; pup += 10)
        {
            this.valveHysteresisAnalytic.setPup(pup);
            this.valveHysteresisAnalytic.collectHysteresisData();
        }
        this.valveHysteresisAnalytic.setValveOpened(true, strokeTestStartTime + 1200);
        
        System.out.println("======================="); //$NON-NLS-1$
        for(ILinearData data:this.valveHysteresisAnalytic.getHysteresisDataCe())
        {
            System.out.println(data.getValueX() + "\t" + data.getValueY() + "\t" + ((ValveHysteresisData) data).getInletPressure()); //$NON-NLS-1$ //$NON-NLS-2$
        }
        for(ILinearData data:this.valveHysteresisAnalytic.getHysteresisDataOe())
        {
            System.out.println(data.getValueX() + "\t" + data.getValueY() + "\t" + ((ValveHysteresisData) data).getInletPressure()); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
    /**
     * 
     */
    @Test
    public void coldRun()
    {
        // simulate cold run condition
        strokeTestRun(0.0, 0.0);
        //pressures during cold run should have been calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run not calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(Double.NaN);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(Double.NaN);
        //Slope not calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(Double.NaN);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(Double.NaN);
        //Reference values not calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(Double.NaN);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(Double.NaN);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(Double.NaN);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(0);
    }
    /**
     * 
     */
    @Test
    public void hotRun()
    {
        // simulate cold run condition
        strokeTestRun(0.0, 0.0);
        // simulate hot run condition
        strokeTestRun(700.0, 0.25);
        
        //pressures during cold run should have been calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run should have been calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(102.0);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(954.0);
        assertThat(this.valveHysteresisAnalytic.getInletPressureHot()).isEqualTo(700.0);
        //Slopes should have been calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(0.002857142857142857);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(0.005714285714285714);
        //Reference values not 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(Double.NaN);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(Double.NaN);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(Double.NaN);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(0);

    }
    /**
     * 
     */
    @Test
    public void baseline()
    {
        // simulate cold run condition
        strokeTestRun(0.0, 0.0);
        // simulate hot run condition
        strokeTestRun(700.0, 0.25);
        //simulate 1st run after calibration
        strokeTestRun(750.0, 0.50);
        
        //pressures during cold run calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(102.0);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(954.0);
        assertThat(this.valveHysteresisAnalytic.getInletPressureHot()).isEqualTo(700.0);
        //Slopes calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(0.002857142857142857);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(0.005714285714285714);
        //pressures during 1st run after calibration calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getCalcValue()).isEqualTo(101.85714285714286);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getCalcValue()).isEqualTo(953.7142857142857);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getCalcValue()).isEqualTo(54.0);
        //Reference values - same as calculated during 1st run after calibration 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(101.85714285714286);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(953.7142857142857);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(54.0);
        //deviations are all zeros
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getValue()).isEqualTo(0D);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getValue()).isEqualTo(0D);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getValue()).isEqualTo(0D);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(0);
    }
    
    /**
     * 
     */
    @Test
    public void deviation()
    {
        // simulate cold run condition
        strokeTestRun(0.0, 0.0);
        // simulate hot run condition
        strokeTestRun(700.0, 0.25);
        //simulate 1st run after calibration
        strokeTestRun(750.0, 0.50);
      //simulate normal run 
        strokeTestRun(800.0, 0.75);
        
        //pressures during cold run calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run not calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(102.0);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(954.0);
        assertThat(this.valveHysteresisAnalytic.getInletPressureHot()).isEqualTo(700.0);
        //Slope not calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(0.002857142857142857);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(0.005714285714285714);
        
        //pressures during 1st run after calibration calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getCalcValue()).isEqualTo(103.71428571428571);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getCalcValue()).isEqualTo(957.4285714285714);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getCalcValue()).isEqualTo(56.0);
        //Reference values - same as calculated during 1st run after calibration 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(101.85714285714286);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(953.7142857142857);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(54.0);
        //deviations 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getValue()).isEqualTo(1.8232819074333702);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getValue()).isEqualTo(0.38945476333134293);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getValue()).isEqualTo(3.7037037037037037);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(0);
        
    }
    
    /**
     * 
     */
    @Test
    public void deviation2()
    {
        // simulate cold run condition
        strokeTestRun(0.0, 0.0);
        // simulate hot run condition
        strokeTestRun(700.0, 0.25);
        //simulate 1st run after calibration
        strokeTestRun(750.0, 0.50);
      //simulate normal run 
        strokeTestRun(800.0, 0.75);
      //simulate normal run 
        strokeTestRun(800.0, 1.00);
        
        
        //pressures during cold run calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run not calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(102.0);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(954.0);
        assertThat(this.valveHysteresisAnalytic.getInletPressureHot()).isEqualTo(700.0);
        //Slope not calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(0.002857142857142857);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(0.005714285714285714);
        
        //pressures during 1st run after calibration calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getCalcValue()).isEqualTo(105.71428571428571);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getCalcValue()).isEqualTo(961.4285714285714);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getCalcValue()).isEqualTo(58.0);
        //Reference values - same as calculated during 1st run after calibration 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(101.85714285714286);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(953.7142857142857);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(54.0);
        //deviations are all zeros
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getValue()).isEqualTo(3.7868162692847025);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getValue()).isEqualTo(0.8088675853804742);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getValue()).isEqualTo(7.407407407407407);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(0);
        IHealthStatus hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.INFORMATION);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(0);
    }
    
    @Test
    public void activeAlerts()
    {
        // simulate cold run condition
        strokeTestRun(0.0, 0.0);
        // simulate hot run condition
        strokeTestRun(700.0, 0.25);
        //simulate 1st run after calibration
        strokeTestRun(750.0, 0.50);
      //simulate normal run 
        strokeTestRun(800.0, 1.5);
      
        
        
        //pressures during cold run calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run not calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(102.0);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(954.0);
        assertThat(this.valveHysteresisAnalytic.getInletPressureHot()).isEqualTo(700.0);
        //Slope not calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(0.002857142857142857);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(0.005714285714285714);
        
        //pressures during 1st run after calibration calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getCalcValue()).isEqualTo(109.71428571428571);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getCalcValue()).isEqualTo(969.4285714285714);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getCalcValue()).isEqualTo(62.0);
        //Reference values - same as calculated during 1st run after calibration 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(101.85714285714286);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(953.7142857142857);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(54.0);
        //deviations are all zeros
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getValue()).isEqualTo(7.713884992987366);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getValue()).isEqualTo(1.6476932294787368);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getValue()).isEqualTo(14.814814814814815);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(1);
        IHealthStatus hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.WARNING);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(1);
        
      //simulate normal run with pdp alert
        strokeTestRun(800.0, 1.6);
      //pressures during cold run calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run not calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(102.0);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(954.0);
        assertThat(this.valveHysteresisAnalytic.getInletPressureHot()).isEqualTo(700.0);
        //Slope not calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(0.002857142857142857);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(0.005714285714285714);
        
        //pressures during 1st run after calibration calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getCalcValue()).isEqualTo(110.51428571428461);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getCalcValue()).isEqualTo(971.0285714285711);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getCalcValue()).isEqualTo(62.79999999999973);
        //Reference values - same as calculated during 1st run after calibration 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(101.85714285714286);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(953.7142857142857);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(54.0);
        //deviations are all zeros
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getValue()).isEqualTo(8.499298737726823);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getValue()).isEqualTo(1.815458358298356);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getValue()).isEqualTo(16.29629629629579);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(1);
        hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(1);
        
      //simulate normal run with pdp & pup ce alert
        strokeTestRun(800.0, 2.0);
      //pressures during cold run calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinCold()).isEqualTo(100.00);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxCold()).isEqualTo(950.00);
        //pressures during hot run not calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinHot()).isEqualTo(102.0);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxHot()).isEqualTo(954.0);
        assertThat(this.valveHysteresisAnalytic.getInletPressureHot()).isEqualTo(700.0);
        //Slope not calculated
        assertThat(this.valveHysteresisAnalytic.getSlopeCe()).isEqualTo(0.002857142857142857);
        assertThat(this.valveHysteresisAnalytic.getSlopeOe()).isEqualTo(0.005714285714285714);
        
        //pressures during 1st run after calibration calculated
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getCalcValue()).isEqualTo(113.71428571428571);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getCalcValue()).isEqualTo(977.4285714285714);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getCalcValue()).isEqualTo(66.0);
        //Reference values - same as calculated during 1st run after calibration 
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getBaseLine()).isEqualTo(101.85714285714286);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getBaseLine()).isEqualTo(953.7142857142857);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getBaseLine()).isEqualTo(54.0);
        //deviations are all zeros
        assertThat(this.valveHysteresisAnalytic.getPupCeMinDeviation().getValue()).isEqualTo(11.640953716690031);
        assertThat(this.valveHysteresisAnalytic.getPupOeMaxDeviation().getValue()).isEqualTo(2.486518873576999);
        assertThat(this.valveHysteresisAnalytic.getPdpDeviation().getValue()).isEqualTo(22.22222222222222);
        
        assertThat(this.valveHysteresisAnalytic.getActiveAlerts().size()).isEqualTo(2);
        hs = this.valve.getHealthStatus();
        assertThat(hs.getMaxSeverity()).isEqualTo(SeverityType.IMPORTANT);
        assertThat(hs.getNumberOfActiveAlerts()).isEqualTo(2);
    }

}
