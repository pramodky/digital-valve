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

/**
 * 
 * @author 103019287
 */
public interface ILinearData
{
    /**
     * @return x value
     */
    public double getValueX();
    /**
     * @param valueX x value
     */
    public void setValueX(double valueX);

    /**
     * @return y value
     */
    public double getValueY();
    /**
     * @param valueY y value
     */
    public void setValueY(double valueY);

}
