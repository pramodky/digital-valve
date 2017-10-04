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

import com.ge.power.cst.deviceanalytics.api.ILinearData;

/**
 * 
 * @author 103019287
 */
public class ValveHysteresisData
        implements ILinearData
{

    private double position;
    private double pup;
    private double inletPressure;

    /**
     * @param position valve position
     * @param pup pressure under piston
     * @param inletPressure inlet pressure
     */
    public ValveHysteresisData(double position, double pup, double inletPressure)
    {
        this.position=position;
        this.pup=pup;
        this.inletPressure=inletPressure;
    }
    /**
     * x value - pressure under piston (pup)
     */
    @Override
    public double getValueX()
    {
        return this.pup;
    }

    /**
     * x value - pressure under piston (pup)
     */
    @Override
    public void setValueX(double pup)
    {
        this.pup = pup;
    }

    /**
     * y value - position
     */
    @Override
    public double getValueY()
    {
        return this.position;
    }

    /**
     * y value - position
     */
    @Override
    public void setValueY(double position)
    {
        this.position=position;

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

}
