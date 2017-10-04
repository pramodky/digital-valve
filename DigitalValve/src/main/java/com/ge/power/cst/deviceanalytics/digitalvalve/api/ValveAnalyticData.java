/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics.digitalvalve.api;

import com.ge.power.cst.deviceanalytics.api.Alert;
import com.ge.power.cst.deviceanalytics.api.IAlert;
import com.ge.power.cst.deviceanalytics.api.IAnalyticData;
import com.ge.power.cst.deviceanalytics.api.Util.DataType;
import com.ge.power.cst.deviceanalytics.api.Util.SeverityType;

/**
 * 
 * @author 103019287
 */
public class ValveAnalyticData extends ValveAnalyticHstData
        implements IAnalyticData
{

    private double actionLimit;
    private double alarmLimit;
    private double baseLine;
    private String measurementUnitBaseLine;
    private long   projectedFailureDate;
    private IAlert alert           = new Alert();
    private double calcValue;

    private double actionLimitBias = 0D;
    private double alarmLimitBias  = 0D;

    /**
     * default constructor
     */
    public ValveAnalyticData()
    {
        this.setDataType(DataType.DOUBLE);
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalLimit#getActionLimit()
     */
    @Override
    public double getActionLimit()
    {
        return this.actionLimit;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalLimit#setActionLimit(double)
     */
    @Override
    public void setActionLimit(double actionLimit)
    {
        this.actionLimit = actionLimit;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalLimit#getAlarmLimit()
     */
    @Override
    public double getAlarmLimit()
    {
        return this.alarmLimit;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.ISignalLimit#setAlarmLimit(double)
     */
    @Override
    public void setAlarmLimit(double alarmLimit)
    {
        this.alarmLimit = alarmLimit;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#getBaseLine()
     */
    @Override
    public double getBaseLine()
    {
        return this.baseLine;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#setBaseLine(double)
     */
    @Override
    public void setBaseLine(double baseLine)
    {
        this.baseLine = baseLine;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#getMeasurementUnitBaseLine()
     */
    @Override
    public String getMeasurementUnitBaseLine()
    {
        return this.measurementUnitBaseLine;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#setMeasurementUnitBaseLine(java.lang.String)
     */
    @Override
    public void setMeasurementUnitBaseLine(String measurementUnit)
    {
        this.measurementUnitBaseLine = measurementUnit;
    }
    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#getProjectedFailureDate()
     */
    @Override
    public long getProjectedFailureDate()
    {
        return this.projectedFailureDate;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#setProjectedFailureDate(long)
     */
    @Override
    public void setProjectedFailureDate(long projectedFailureDate)
    {
        this.projectedFailureDate = projectedFailureDate;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#getAlertRecommendations()
     */
    @Override
    public IAlert getAlert()
    {
        return this.alert;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalyticData#setAlertRecommendations(java.util.List)
     */
    @Override
    public void setAlert(IAlert alert)
    {
        this.alert = alert;
    }

    @Override
    public void setValue(Object value)
    {
        this.setValue((double) value);
    }

    /**
     * @param value current value...
     *            <li>alert's severity is set after comparing this value with action and alarm limits
     */
    public void setValue(double value)
    {
        super.setValue(value);

        SeverityType severityType = SeverityType.INFORMATION;

        if ( !Double.isNaN(this.alarmLimit) && value >= this.alarmLimit + this.alarmLimitBias )
        {
            severityType = SeverityType.IMPORTANT;
        }
        else if ( !Double.isNaN(this.actionLimit) && value >= this.actionLimit + this.actionLimitBias )
        {
            severityType = SeverityType.WARNING;

        }
        this.getAlert().setSeverity(severityType);
    }

    /**
     * @return the current calculated value
     */
    public double getCalcValue()
    {
        return this.calcValue;
    }

    /**
     * @param calcValue the current calculated value...
     *            <li>this value gets assigned to baseline/reference if this is first reading
     *            <li>deviation gets calculated and assigned to value property
     * @param isDeviationRequired specify if deviation calculation is required
     *            <li>if true - deviation gets calculated and assigned to value property
     *            <li>if false - calcValue is assigned to value property
     * 
     */
    public void setCalcValue(double calcValue, boolean isDeviationRequired)
    {
        this.calcValue = calcValue;

        if ( Double.isNaN(calcValue) ) return;
        if ( Double.isNaN(this.getBaseLine()) )
        {
            // First reading... assign baseline
            this.setBaseLine(calcValue);
        }

        if ( isDeviationRequired )
        {
            // Calculate deviation
            double deviation = 0D;
            if ( !Double.isNaN(this.getBaseLine()) && this.getBaseLine() > 0D )
            {
                deviation = 100 * (calcValue - this.getBaseLine()) / this.getBaseLine();
            }

            this.setValue(deviation);
        }
        else
        {
            this.setValue(calcValue);
        }
    }

    /**
     * @return the bias that gets added to action limit while comparing value with threshold
     *         <br>
     *         if(value>=actionLimit + actionLimitBias) raise alert
     *         <br>
     *         defaulted to 0
     */
    public double getActionLimitBias()
    {
        return this.actionLimitBias;
    }

    /**
     * @param actionLimitBias the bias that gets added to action limit while comparing value with threshold
     *            <br>
     *            if(value>=actionLimit + actionLimitBias) raise alert
     *            <br>
     *            defaulted to 0
     */
    public void setActionLimitBias(double actionLimitBias)
    {
        this.actionLimitBias = actionLimitBias;
    }

    /**
     * @return the bias that gets added to alarm limit while comparing value with threshold
     *         <br>
     *         if(value>=alarmLimit + alarmLimitBias) raise alert
     *         <br>
     *         defaulted to 0
     */
    public double getAlarmLimitBias()
    {
        return this.alarmLimitBias;
    }

    /**
     * @param alarmLimitBias the bias that gets added to alarm limit while comparing value with threshold
     *            <br>
     *            if(value>=alarmLimit + alarmLimitBias) raise alert
     *            <br>
     *            defaulted to 0
     */
    public void setAlarmLimitBias(double alarmLimitBias)
    {
        this.alarmLimitBias = alarmLimitBias;
    }



}
