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

import java.util.ArrayList;
import java.util.List;

import com.ge.power.cst.deviceanalytics.api.ILinearData;

/**
 * 
 * @author 103019287
 */
public class StrokeTestAnalytics_old
{
    private ClosingTimeAnalytic_old     servoClosingTimeAnalytic = new ClosingTimeAnalytic_old();
    private ClosingTimeAnalytic_old     fasvClosingTimeAnalytic  = new ClosingTimeAnalytic_old();
    private ValveHysteresisAnalytic_old valveHysteresisAnalytic_old  = new ValveHysteresisAnalytic_old();

    // closing time input fields:
    private boolean                 isStrokeTestStarted      = false;
    private boolean                 isFasvCommand            = false;
    private boolean                 isValveClosed            = false;
    private boolean                 isValveOpened            = false;
    private boolean                 isStrokeTestCompleted    = false;
    private double                  speed;
    // valve hysteresis inputs:
    private double                  position;
    private double                  pup;
    private double                  inletPressure;

    // data collection filters
    private double                  speed_gt                 = 500;
    private double                  inletPressure_gt         = 50;

    // local variables
    private boolean                 isStrokeTestInProgress   = false;
    private double                  previousPosition         = (Double.NaN);
    private List<ILinearData>       hysteresisDataCurrrent;

    // stroke test output fields
    private long                    lastStrokeTestDoneOn;

    /**
     * 
     */
    public StrokeTestAnalytics_old()
    {

    }

    /**
     * @return the servoClosingTimeAnalytic
     */
    public ClosingTimeAnalytic_old getServoClosingTimeAnalytic()
    {
        return this.servoClosingTimeAnalytic;
    }

    /**
     * @param servoClosingTimeAnalytic the servoClosingTimeAnalytic to set
     */
    public void setServoClosingTimeAnalytic(ClosingTimeAnalytic_old servoClosingTimeAnalytic)
    {
        this.servoClosingTimeAnalytic = servoClosingTimeAnalytic;
    }

    /**
     * @return the servoClosingTimeAnalytic
     */
    public ClosingTimeAnalytic_old getFasvClosingTimeAnalytic()
    {
        return this.fasvClosingTimeAnalytic;
    }

    /**
     * @param fasvClosingTimeAnalytic the servoClosingTimeAnalytic to set
     */
    public void setFasvClosingTimeAnalytic(ClosingTimeAnalytic_old fasvClosingTimeAnalytic)
    {
        this.fasvClosingTimeAnalytic = fasvClosingTimeAnalytic;
    }

    /**
     * @return the valveHysteresisAnalytic
     */
    public ValveHysteresisAnalytic_old getValveHysteresisAnalytic()
    {
        return this.valveHysteresisAnalytic_old;
    }

    /**
     * @param valveHysteresisAnalytic_old the valveHysteresisAnalytic to set
     */
    public void setValveHysteresisAnalytic(ValveHysteresisAnalytic_old valveHysteresisAnalytic_old)
    {
        this.valveHysteresisAnalytic_old = valveHysteresisAnalytic_old;
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

        this.isStrokeTestInProgress = true;
        if ( this.isUnitOnline() )
        {
            // capture closing time data only if the unit is online
            this.servoClosingTimeAnalytic.setStartTimeStamp(timeStamp);
            this.servoClosingTimeAnalytic.setEndTimeStamp(0);
            this.servoClosingTimeAnalytic.setClosingTime(0);
            this.servoClosingTimeAnalytic.getClosingTimeDeviation().setValue(Double.NaN);

            this.fasvClosingTimeAnalytic.setStartTimeStamp(0);
            this.fasvClosingTimeAnalytic.setEndTimeStamp(0);
            this.fasvClosingTimeAnalytic.setClosingTime(0);
            this.fasvClosingTimeAnalytic.getClosingTimeDeviation().setValue(Double.NaN);
        }
        // Initialize valve hysteresis data in closing direction
        this.previousPosition = (Double.NaN);
        this.valveHysteresisAnalytic_old.setHysteresisDataCe(new ArrayList<ILinearData>());
        this.hysteresisDataCurrrent = this.valveHysteresisAnalytic_old.getHysteresisDataCe();
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
        if ( !this.isUnitOnline() ) return; // capture closing time data only if the unit is online

        this.servoClosingTimeAnalytic.setEndTimeStamp(timeStamp);

        this.fasvClosingTimeAnalytic.setStartTimeStamp(timeStamp);

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

        if ( this.isUnitOnline() )
        {
            // capture closing time data only if the unit is online

            // For valves without FASVs - valve closed time stamp is servo stop time stamp
            // For valves with FASVs - valve closed time stamp is FASV stop time stamp

            // Valves without FASVs won't have FASV command (isFasvCommand)
            // and hence servo stopTimeStamp will be 0
            if ( this.servoClosingTimeAnalytic.getEndTimeStamp() == 0D )
            {
                // in this case valve closed time stamp is servo stop time stamp
                this.servoClosingTimeAnalytic.setEndTimeStamp(timeStamp);
            }
            else
            {
                // in this case valve closed time stamp is FASV stop time stamp
                this.fasvClosingTimeAnalytic.setEndTimeStamp(timeStamp);
            }
        }
        // Initialize valve hysteresis data in opening direction
        this.previousPosition = (Double.NaN);
        this.valveHysteresisAnalytic_old.setHysteresisDataOe(new ArrayList<ILinearData>());
        this.hysteresisDataCurrrent = this.valveHysteresisAnalytic_old.getHysteresisDataOe();
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

        if ( this.isUnitOnline() )
        {
            // capture closing time data only if the unit is online

            // For partial stroke test the valve won't get completely closed
            // i.e., FASV command and valve closed command would never get set to true
            // in that case set servo and FASV closing time stamp to valve open time stamp
            if ( this.servoClosingTimeAnalytic.getStartTimeStamp() > 0D
                    && this.servoClosingTimeAnalytic.getEndTimeStamp() == 0D )
            {
                this.servoClosingTimeAnalytic.setEndTimeStamp(timeStamp);
            }
            if ( this.fasvClosingTimeAnalytic.getStartTimeStamp() > 0D
                    && this.fasvClosingTimeAnalytic.getEndTimeStamp() == 0D )
            {
                this.fasvClosingTimeAnalytic.setEndTimeStamp(timeStamp);
            }
        }
        this.setStrokeTestCompleted(isValveOpened, timeStamp);
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

        runAnalytics();

        this.setLastStrokeTestDoneOn(timeStamp);
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
     * @return the lastStrokeTestDoneOn
     */
    public long getLastStrokeTestDoneOn()
    {
        return this.lastStrokeTestDoneOn;
    }

    /**
     * @param lastStrokeTestDoneOn the lastStrokeTestDoneOn to set
     */
    public void setLastStrokeTestDoneOn(long lastStrokeTestDoneOn)
    {
        this.lastStrokeTestDoneOn = lastStrokeTestDoneOn;
    }

    /**
     * @return the position
     */
    public double getPosition()
    {
        return this.position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(double position)
    {
        this.position = position;
    }

    /**
     * @return the pup
     */
    public double getPup()
    {
        return this.pup;
    }

    /**
     * @param pup the pup to set
     */
    public void setPup(double pup)
    {
        this.pup = pup;
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
     * @return the speed_gt
     */
    public double getSpeed_gt()
    {
        return this.speed_gt;
    }

    /**
     * @param speed_gt the speed_gt to set
     */
    public void setSpeed_gt(double speed_gt)
    {
        this.speed_gt = speed_gt;
    }

    /**
     * @return the inletPressure_gt
     */
    public double getInletPressure_gt()
    {
        return this.inletPressure_gt;
    }

    /**
     * @param inletPressure_gt the inletPressure_gt to set
     */
    public void setInletPressure_gt(double inletPressure_gt)
    {
        this.inletPressure_gt = inletPressure_gt;
        this.valveHysteresisAnalytic_old.setInletPressure_gt(inletPressure_gt);
    }

    /**
     * add one set of valve hysteresis data
     */
    public void addHysteresisData()
    {
        if ( !Double.isNaN(this.previousPosition) && this.getPosition() != this.previousPosition )
        {
            this.hysteresisDataCurrrent
                    .add(new ValveHysteresisData(this.getPosition(), this.getPup(), this.getInletPressure()));
        }
        this.previousPosition = this.getPosition();
    }

    /**
     * run stroke test related analytics
     */
    public void runAnalytics()
    {
        if ( this.isUnitOnline() )
        {
            // run closing time analytics only if the unit is online
            this.servoClosingTimeAnalytic.runAnalytic();
            this.fasvClosingTimeAnalytic.runAnalytic();
        }
        this.valveHysteresisAnalytic_old.runAnalytic();
    }

    private boolean isUnitOnline()
    {
        return (this.speed > this.speed_gt && this.inletPressure > this.inletPressure_gt);
    }

}
