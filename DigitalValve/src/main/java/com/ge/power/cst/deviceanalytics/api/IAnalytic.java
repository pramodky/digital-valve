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

import java.util.List;

import com.ge.power.cst.deviceanalytics.api.Util.AnalyticId;

/**
 * 
 * @author 103019287
 */
public interface IAnalytic
{

    /**
     * @return unique identifier of this analytic
     */
    public AnalyticId getId();

    /**
     * @param id unique identifier of this analytic
     */
    public void setId(AnalyticId id);

    /**
     * @return name of analytic
     */
    public String getName();

    /**
     * @param name name of analytic
     */
    public void setName(String name);

    /**
     * @return last execution date
     */
    public long getLastExecutedOn();

    /**
     * @param lastExecutedOn last execution date
     */
    public void setLastExecutedOn(long lastExecutedOn);

    /**
     * @return list of analytic data associated with this analytic
     */
    public List<IAnalyticData> getAnalyticDataSet();

    /**
     * @param analyticDataSet list of analytic data associated with this analytic
     */
    public void setAnalyticDataSet(List<IAnalyticData> analyticDataSet);

    /**
     * @return list of data filter criterion
     */
    public List<IBasicData> getDataFilterCriteria();

    /**
     * @param dataFilterCriteria list of data filter criterion
     */
    public void setDataFilterCriteria(List<IBasicData> dataFilterCriteria);

    /**
     * @return list of other settings
     */
    public List<IBasicData> getOtherSettings();

    /**
     * @param otherSettings list of other settings
     */
    public void setOtherSettings(List<IBasicData> otherSettings);

    /**
     * execute analytic
     */
    public void run();

    /**
     * @return projected failure date
     */
    public long getProjectedFailureDate();

    /**
     * @return active alerts for this analytic
     */
    public List<IAlert> getActiveAlerts();

}
