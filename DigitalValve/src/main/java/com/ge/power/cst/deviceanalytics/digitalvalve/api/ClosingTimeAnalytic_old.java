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

import com.ge.power.cst.deviceanalytics.api.IAnalyticData;

/**
 * 
 * @author 103019287
 */
public class ClosingTimeAnalytic_old
{
    private double        closingTimeRef;

    private long          startTimeStamp;
    private long          endTimeStamp;

    private double        closingTime;
    private IAnalyticData closingTimeDeviation = new ValveAnalyticData();

    private boolean       isCalibrationInProgress;
    private boolean       isClosingTimeRefChanged;

    /**
     * @return the closingTimeRef
     */
    public double getClosingTimeRef()
    {
        return this.closingTimeRef;
    }

    /**
     * @param closingTimeRef the closingTimeRef to set
     */
    public void setClosingTimeRef(double closingTimeRef)
    {
        this.isClosingTimeRefChanged = (this.closingTimeRef != closingTimeRef);
        this.closingTimeRef = closingTimeRef;
    }

    /**
     * @return the startTimeStamp
     */
    public long getStartTimeStamp()
    {
        return this.startTimeStamp;
    }

    /**
     * @param startTimeStamp the startTimeStamp to set
     */
    public void setStartTimeStamp(long startTimeStamp)
    {
        this.startTimeStamp = startTimeStamp;
    }

    /**
     * @return the endTimeStamp
     */
    public long getEndTimeStamp()
    {
        return this.endTimeStamp;
    }

    /**
     * @param endTimeStamp the endTimeStamp to set
     */
    public void setEndTimeStamp(long endTimeStamp)
    {
        this.endTimeStamp = endTimeStamp;
    }

    /**
     * @return the closingTime
     */
    public double getClosingTime()
    {
        return this.closingTime;
    }

    /**
     * @param closingTime the closingTime to set
     */
    public void setClosingTime(double closingTime)
    {
        this.closingTime = closingTime;
        if ( this.isCalibrationInProgress || Double.isNaN(this.getClosingTimeRef()) || this.getClosingTimeRef() == 0D )
        {
            // First reading... make this as reference time
            this.setClosingTimeRef(closingTime);
        }
    }

    /**
     * @return the closingTimeDeviation
     */
    public IAnalyticData getClosingTimeDeviation()
    {
        return this.closingTimeDeviation;
    }

    /**
     * @param closingTimeDeviation the closingTimeDeviation to set
     */
    public void setClosingTimeDeviation(IAnalyticData closingTimeDeviation)
    {
        this.closingTimeDeviation = closingTimeDeviation;
    }

    /**
     * @return is calibration in progress
     */
    public boolean isCalibrationInProgress()
    {
        return this.isCalibrationInProgress;
    }

    /**
     * @param isCalibrationInProgress is calibration in progress
     */
    public void setCalibrationInProgress(boolean isCalibrationInProgress)
    {
        this.isCalibrationInProgress = isCalibrationInProgress;
    }

    /**
     * @return the isClosingTimeRefChanged
     */
    public boolean isClosingTimeRefChanged()
    {
        return this.isClosingTimeRefChanged;
    }

    /**
     * @param isClosingTimeRefChanged the isClosingTimeRefChanged to set
     */
    public void setClosingTimeRefChanged(boolean isClosingTimeRefChanged)
    {
        this.isClosingTimeRefChanged = isClosingTimeRefChanged;
    }

    /**
     * Calculates closing time
     * 
     * @return closing time
     */
    public double computeClosingTime()
    {

        this.setClosingTime(this.getEndTimeStamp() - this.getStartTimeStamp());
        return this.getClosingTime();
    }

    /**
     * Calculates closing time deviation
     */
    public void computeClosingTimeDeviation()
    {
        double deviation = 0D;
        if ( !Double.isNaN(this.closingTimeRef) )
        {
            if ( this.getClosingTimeRef() != 0D && this.getClosingTime() > 0 )
            {
                deviation = 100 * (this.getClosingTime() - this.getClosingTimeRef()) / this.getClosingTimeRef();
            }
        }
        this.getClosingTimeDeviation().setValue(deviation);

        
    }
    
    /**
     * computes valve closing time and calculates closing time deviation
     */
    public void runAnalytic()
    {
        this.isClosingTimeRefChanged = false;
        this.computeClosingTime();
        this.computeClosingTimeDeviation();
    }
}
