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

import com.ge.power.cst.deviceanalytics.api.IAlert;
import com.ge.power.cst.deviceanalytics.api.IAnalytic;
import com.ge.power.cst.deviceanalytics.api.IAnalyticData;
import com.ge.power.cst.deviceanalytics.api.IBasicData;
import com.ge.power.cst.deviceanalytics.api.Util.AnalyticId;

/**
 * 
 * @author 103019287
 */
public class ValveAnalytic
        implements IAnalytic
{
    private AnalyticId          id;
    private String              name;
    private long                lastExecutedOn;
    private List<IAnalyticData> analyticDataSet    = new ArrayList<IAnalyticData>();
    private List<IBasicData>    dataFilterCriteria = new ArrayList<IBasicData>();
    private List<IBasicData>    otherSettings      = new ArrayList<IBasicData>();

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getId()
     */
    @Override
    public AnalyticId getId()
    {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setId(com.ge.power.cst.deviceanalytics.api.Util.AnalyticId)
     */
    @Override
    public void setId(AnalyticId id)
    {
        this.id = id;

    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getName()
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setName(java.lang.String)
     */
    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getLastExecutedOn()
     */
    @Override
    public long getLastExecutedOn()
    {
        return this.lastExecutedOn;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setLastExecutedOn(long)
     */
    @Override
    public void setLastExecutedOn(long lastExecutedOn)
    {
        this.lastExecutedOn = lastExecutedOn;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getAnalyticDataSet()
     */
    @Override
    public List<IAnalyticData> getAnalyticDataSet()
    {
        return this.analyticDataSet;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setAnalyticDataSet(java.util.List)
     */
    @Override
    public void setAnalyticDataSet(List<IAnalyticData> analyticDataSet)
    {
        this.analyticDataSet = analyticDataSet;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getDataFilterCriteria()
     */
    @Override
    public List<IBasicData> getDataFilterCriteria()
    {
        return this.dataFilterCriteria;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setDataFilterCriteria(java.util.List)
     */
    @Override
    public void setDataFilterCriteria(List<IBasicData> dataFilterCriteria)
    {
        this.dataFilterCriteria = dataFilterCriteria;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getOtherSettings()
     */
    @Override
    public List<IBasicData> getOtherSettings()
    {
        return this.otherSettings;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setOtherSettings(java.util.List)
     */
    @Override
    public void setOtherSettings(List<IBasicData> otherSettings)
    {
        this.otherSettings = otherSettings;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#run()
     */
    @Override
    public void run()
    {
        // TODO 1. update AnalyticData.value in ANALYTIC_DATA db table
        // TODO 2. add AnalyticData.value to HST_DATA table
        // TODO 3. prepare chart data - fetch hst data, get projections, identify projected failure date
        // TODO 4. publish AnalyticDataSet and chart data to Web Socket
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getActiveAlerts()
     */
    @Override
    public List<IAlert> getActiveAlerts()
    {
        List<IAlert> activeAlerts = new ArrayList<IAlert>();
        for (IAnalyticData analyticData : this.analyticDataSet)
        {
            IAlert alert = analyticData.getAlert();
            if ( alert.isActive() )
            {
                activeAlerts.add(alert);
            }

        }
        return activeAlerts;
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#getProjectedFailureDate()
     */
    @Override
    public long getProjectedFailureDate()
    {
        long projectedFailureDate = -1;
        for (IAnalyticData analyticData : this.analyticDataSet)
        {
            long date = analyticData.getProjectedFailureDate();
            //TODO: check this condition
            projectedFailureDate = (date > projectedFailureDate) ? date : projectedFailureDate;
        }
        return projectedFailureDate;
    }

}
