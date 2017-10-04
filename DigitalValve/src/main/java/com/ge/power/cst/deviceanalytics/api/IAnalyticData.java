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
public interface IAnalyticData
        extends IHstData
{
    /**
     * @return action limit value (H)
     */
    public double getActionLimit();

    /**
     * @param actionLimit action limit value (H)
     */
    public void setActionLimit(double actionLimit);

    /**
     * @return alarm limit value (HH)
     */
    public double getAlarmLimit();

    /**
     * @param alarmLimit alarm limit value (HH)
     */
    public void setAlarmLimit(double alarmLimit);

    /**
     * @return baseline/reference value - first reading after device calibration (newly serviced device)
     */
    public double getBaseLine();

    /**
     * @param baseLine baseline/reference value - first reading after device calibration (newly serviced device)
     */
    public void setBaseLine(double baseLine);
    
    /**
     * @return measurement unit of baseline value
     */
    public String getMeasurementUnitBaseLine();

    /**
     * @param measurementUnit measurement unit of baseline value
     */
    public void setMeasurementUnitBaseLine(String measurementUnit);
    
    /**
     * @return projected failure date
     */
    public long getProjectedFailureDate();
    
    /**
     * @param projectedFailureDate projected failure date
     */
    public void setProjectedFailureDate(long projectedFailureDate);

    /**
     * @return list of alert recommendation
     */
    public IAlert getAlert();

    /**
     * @param alert list of alert recommendation
     */
    public void setAlert(IAlert alert);

}
