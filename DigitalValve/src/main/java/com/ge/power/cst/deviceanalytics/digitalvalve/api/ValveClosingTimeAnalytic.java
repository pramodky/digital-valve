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

import java.util.List;

import com.ge.power.cst.deviceanalytics.api.IAnalyticData;
import com.ge.power.cst.deviceanalytics.api.IBasicData;

/**
 * 
 * @author 103019287
 */
public class ValveClosingTimeAnalytic extends ValveAnalytic
{
    // Inputs from controller (listener)
    private boolean           isStrokeTestStarted       = false;
    private boolean           isFasvCommand             = false;
    private boolean           isValveClosed             = false;
    private boolean           isValveOpened             = false;
    private boolean           isStrokeTestCompleted     = false;
    private double            speed;
    private double            inletPressure;

    // valve analytic data - closing time deviation for servo and FASV
    private ValveAnalyticData servoClosingTimeDeviation = new ValveAnalyticData();
    private ValveAnalyticData fasvClosingTimeDeviation  = new ValveAnalyticData();

    // data collection filters
    private double            speed_gt                  = 500;
    private double            inletPressure_gt          = 50;

    // local variables
    private boolean           isStrokeTestInProgress    = false;
    private long              servoStartTimeStamp;
    private long              fasvStartTimeStamp;
    private long              servoEndTimeStamp;
    private long              fasvEndTimeStamp;

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setAnalyticDataSet(java.util.List)
     */
    @Override
    public void setAnalyticDataSet(List<IAnalyticData> analyticDataSet)
    {
        super.setAnalyticDataSet(analyticDataSet);

        for (IAnalyticData analyticData : analyticDataSet)
        {
            switch (analyticData.getId())
            {
                case "SERVO_CLOSING_TIME_DEVIATION": //$NON-NLS-1$
                {
                    this.servoClosingTimeDeviation = (ValveAnalyticData) analyticData;
                    break;
                }
                case "FASV_CLOSING_TIME_DEVIATION": //$NON-NLS-1$
                {
                    this.fasvClosingTimeDeviation = (ValveAnalyticData) analyticData;
                    break;
                }
                default:
                {
                    break;
                }

            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setDataFilterCriteria(java.util.List)
     */
    @Override
    public void setDataFilterCriteria(List<IBasicData> dataFilterCriteria)
    {
        super.setDataFilterCriteria(dataFilterCriteria);
        for (IBasicData dataFilterCriterion : dataFilterCriteria)
        {
            switch (dataFilterCriterion.getId())
            {
                case "SPEED_GT": //$NON-NLS-1$
                {
                    this.speed_gt = (double) dataFilterCriterion.getValue();
                    break;
                }
                case "INLET_PRESSURE_GT": //$NON-NLS-1$
                {
                    this.inletPressure_gt = (double) dataFilterCriterion.getValue();
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * @return the isStrokeTestStarted
     */
    public boolean isStrokeTestStarted()
    {
        return this.isStrokeTestStarted;
    }

    /**
     * @param isStrokeTestStarted the isStrokeTestStarted to set
     * @param timeStamp time stamp when value changed
     */
    public void setStrokeTestStarted(boolean isStrokeTestStarted, long timeStamp)
    {
        this.isStrokeTestStarted = isStrokeTestStarted;

        if ( !isStrokeTestStarted ) return;
        if ( !this.isUnitOnline() ) return; // capture closing time data only if the unit is online

        this.isStrokeTestInProgress = true;

        this.servoStartTimeStamp = timeStamp;
        this.servoEndTimeStamp = 0;

        this.fasvStartTimeStamp = 0;
        this.fasvEndTimeStamp = 0;

    }

    /**
     * @return the isFasvCommand
     */
    public boolean isFasvCommand()
    {
        return this.isFasvCommand;
    }

    /**
     * @param isFasvCommand the isFasvCommand to set
     * @param timeStamp time stamp when value changed
     */
    public void setFasvCommand(boolean isFasvCommand, long timeStamp)
    {
        this.isFasvCommand = isFasvCommand;
        if ( !isFasvCommand ) return;
        if ( !this.isStrokeTestInProgress ) return;

        // FASV command true --> servo end time and FASV start time
        this.servoEndTimeStamp = timeStamp;
        this.fasvStartTimeStamp = timeStamp;
    }

    /**
     * @return the isValveClosed
     */
    public boolean isValveClosed()
    {
        return this.isValveClosed;
    }

    /**
     * @param isValveClosed the isValveClosed to set
     * @param timeStamp time stamp when value changed
     */
    public void setValveClosed(boolean isValveClosed, long timeStamp)
    {
        this.isValveClosed = isValveClosed;

        if ( !isValveClosed ) return;
        if ( !this.isStrokeTestInProgress ) return;

        // For valves with FASVs - valve closed time stamp is FASV stop time stamp
        // For valves without FASVs - valve closed time stamp is servo stop time stamp
        if ( this.servoEndTimeStamp == 0 )
        {
            // valve without FASV
            // in this case valve closed time stamp is servo stop time stamp
            this.servoEndTimeStamp = timeStamp;
        }
        else
        {
            // valve with FASV
            // in this case valve closed time stamp is FASV stop time stamp
            this.fasvEndTimeStamp = timeStamp;
        }
    }

    /**
     * @return the isValveOpened
     */
    public boolean isValveOpened()
    {
        return this.isValveOpened;
    }

    /**
     * @param isValveOpened the isValveOpened to set
     * @param timeStamp time stamp when value changed
     */
    public void setValveOpened(boolean isValveOpened, long timeStamp)
    {
        this.isValveOpened = isValveOpened;

        if ( !isValveOpened ) return;
        if ( !this.isStrokeTestInProgress ) return;

        // For partial stroke test the valve won't get completely closed
        // i.e., FASV command and valve closed command would never get set to true
        // in that case set servo and FASV closing time stamp to valve open time stamp
        if ( this.servoStartTimeStamp > 0 && this.servoEndTimeStamp == 0 )
        {
            this.servoEndTimeStamp = timeStamp;
        }
        if ( this.fasvStartTimeStamp > 0 && this.fasvEndTimeStamp == 0 )
        {
            this.fasvEndTimeStamp = timeStamp;
        }

        this.setStrokeTestCompleted(true, timeStamp);
    }

    /**
     * @return the isStrokeTestCompleted
     */
    public boolean isStrokeTestCompleted()
    {
        return this.isStrokeTestCompleted;
    }

    /**
     * @param isStrokeTestCompleted the isStrokeTestCompleted to set
     * @param timeStamp time stamp when value changed
     */
    public void setStrokeTestCompleted(boolean isStrokeTestCompleted, long timeStamp)
    {
        this.isStrokeTestCompleted = isStrokeTestCompleted;

        if ( !isStrokeTestCompleted ) return;
        if ( !this.isStrokeTestInProgress ) return;

        this.setLastExecutedOn(timeStamp);

        // run analytic
        this.run();

        this.isStrokeTestInProgress = false;
    }

    /**
     * @return the speed
     */
    public double getSpeed()
    {
        return this.speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    /**
     * @return the inletPressure
     */
    public double getInletPressure()
    {
        return this.inletPressure;
    }

    /**
     * @param inletPressure the inletPressure to set
     */
    public void setInletPressure(double inletPressure)
    {
        this.inletPressure = inletPressure;
    }

    /**
     * @return the servoClosingTimeDeviation
     */
    public ValveAnalyticData getServoClosingTimeDeviation()
    {
        return this.servoClosingTimeDeviation;
    }

    /**
     * @param servoClosingTimeDeviation the servoClosingTimeDeviation to set
     */
    public void setServoClosingTimeDeviation(ValveAnalyticData servoClosingTimeDeviation)
    {
        this.servoClosingTimeDeviation = servoClosingTimeDeviation;
    }

    /**
     * @return the fasvClosingTimeDeviation
     */
    public ValveAnalyticData getFasvClosingTimeDeviation()
    {
        return this.fasvClosingTimeDeviation;
    }

    /**
     * @param fasvClosingTimeDeviation the fasvClosingTimeDeviation to set
     */
    public void setFasvClosingTimeDeviation(ValveAnalyticData fasvClosingTimeDeviation)
    {
        this.fasvClosingTimeDeviation = fasvClosingTimeDeviation;
    }

    /*
     * check if data filer criterion is met
     */
    private boolean isUnitOnline()
    {
        return (this.speed > this.speed_gt && this.inletPressure > this.inletPressure_gt);
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#run()
     */
    @Override
    public void run()
    {
        // Calculate closing time
        if ( this.servoStartTimeStamp > 0 && this.servoEndTimeStamp > 0 )
        {
            this.servoClosingTimeDeviation.setCalcValue(this.servoEndTimeStamp - this.servoStartTimeStamp, true);
        }
        if ( this.fasvStartTimeStamp > 0 && this.fasvEndTimeStamp > 0 )
        {
            this.fasvClosingTimeDeviation.setCalcValue(this.fasvEndTimeStamp - this.fasvStartTimeStamp, true);
        }

        // 1. update AnalyticData.value in ANALYTIC_DATA db table
        // 2. add AnalyticData.value to HST_DATA table
        // 3. prepare chart data - fetch hst data, get projections, identify projected failure date
        // 4. publish AnalyticDataSet and chart data to Web Socket
        super.run();
    }
}
