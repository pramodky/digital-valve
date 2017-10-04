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

import com.ge.power.cst.deviceanalytics.api.IAnalyticData;
import com.ge.power.cst.deviceanalytics.api.IBasicData;
import com.ge.power.cst.deviceanalytics.api.ILinearData;
import com.ge.power.cst.deviceanalytics.api.LinearRegression;
import com.ge.power.cst.deviceanalytics.api.LinearRegression.Coefficients;

/**
 * 
 * @author 103019287
 */
public class ValveHysteresisAnalytic extends ValveAnalytic
{
    // Inputs from controller (listener)
    private boolean           isStrokeTestStarted    = false;
    private boolean           isValveClosed          = false;
    private boolean           isValveOpened          = false;
    private boolean           isStrokeTestCompleted  = false;
    private double            inletPressure;
    private double            position;
    private double            pup;

    // collected inputs:
    private List<ILinearData> hysteresisDataCe       = new ArrayList<ILinearData>();
    private List<ILinearData> hysteresisDataOe       = new ArrayList<ILinearData>();
    private List<ILinearData> hysteresisDataCurrrent;

    // valve analytic data - pressure deviations
    private ValveAnalyticData pupCeMinDeviation      = new ValveAnalyticData();
    private ValveAnalyticData pupOeMaxDeviation      = new ValveAnalyticData();
    private ValveAnalyticData pdpDeviation           = new ValveAnalyticData();

    // slopes - populated through other settings
    private double            slopeCe;
    private double            slopeOe;
    // inlet pressure threshold to determine hot/warm condition - populated through other settings
    private double            inletPressureHot_gt    = 50;

    // local variables
    private boolean           isStrokeTestInProgress = false;
    private double            previousPosition       = (Double.NaN);
    // calculated pressure values right after calibration
    private double            pupCeMinCold           = Double.NaN;
    private double            pupOeMaxCold           = Double.NaN;
    private double            pupCeMinHot            = Double.NaN;
    private double            pupOeMaxHot            = Double.NaN;
    private double            inletPressureHot       = Double.NaN;

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
                case "PUP_CLOSING_DEVIATION": //$NON-NLS-1$
                {
                    this.pupCeMinDeviation = (ValveAnalyticData) analyticData;
                    break;
                }
                case "PUP_OPENING_DEVIATION": //$NON-NLS-1$
                {
                    this.pupOeMaxDeviation = (ValveAnalyticData) analyticData;
                    break;
                }
                case "PDP_DEVIATION": //$NON-NLS-1$
                {
                    this.pdpDeviation = (ValveAnalyticData) analyticData;
                    break;
                }

                default:
                    break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#setOtherSettings(java.util.List)
     */
    @Override
    public void setOtherSettings(List<IBasicData> otherSettings)
    {
        super.setOtherSettings(otherSettings);
        for (IBasicData setting : otherSettings)
        {
            switch (setting.getId())
            {
                case ("SLOPE_CE")://$NON-NLS-1$
                {
                    this.slopeCe = (double) setting.getValue();
                    break;
                }
                case ("SLOPE_OE")://$NON-NLS-1$
                {
                    this.slopeOe = (double) setting.getValue();
                    break;
                }
                case ("INLET_PRESS_HOT_GT")://$NON-NLS-1$
                {
                    this.inletPressureHot_gt = (double) setting.getValue();
                    break;
                }
                default:
                    break;

            }
        }
    }

    /**
     * @return the slope in closing direction
     */
    public double getSlopeCe()
    {
        return this.slopeCe;
    }

    /**
     * @param slopeCe the slope in closing direction
     */
    public void setSlopeCe(double slopeCe)
    {
        this.slopeCe = slopeCe;

        for (IBasicData setting : this.getOtherSettings())
        {
            if ( setting.getId().equals("SLOPE_CE") ) //$NON-NLS-1$
            {
                setting.setValue(slopeCe);
                break;
            }
        }
    }

    /**
     * @return the slope in opening direction
     */
    public double getSlopeOe()
    {
        return this.slopeOe;
    }

    /**
     * @param slopeOe the slope in opening direction
     */
    public void setSlopeOe(double slopeOe)
    {
        this.slopeOe = slopeOe;

        for (IBasicData setting : this.getOtherSettings())
        {
            if ( setting.getId().equals("SLOPE_OE") ) //$NON-NLS-1$
            {
                setting.setValue(slopeOe);
                break;
            }
        }
    }

    /**
     * @return the inletPressure_gt
     */
    public double getInletPressure_gt()
    {
        return this.inletPressureHot_gt;
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
     */
    public void setStrokeTestStarted(boolean isStrokeTestStarted)
    {
        this.isStrokeTestStarted = isStrokeTestStarted;

        if ( !isStrokeTestStarted ) return;
        this.isStrokeTestInProgress = true;

        // closing direction... initialize hysteresis data CE and set this as current hysteresis data
        this.previousPosition = (Double.NaN);
        this.hysteresisDataCe = new ArrayList<ILinearData>();
        this.hysteresisDataCurrrent = this.hysteresisDataCe;
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
     */
    public void setValveClosed(boolean isValveClosed)
    {
        this.isValveClosed = isValveClosed;

        if ( !isValveClosed ) return;
        if ( !this.isStrokeTestInProgress ) return;

        // opening direction... initialize hysteresis data CE and set this as current hysteresis data
        this.previousPosition = (Double.NaN);
        this.hysteresisDataOe = new ArrayList<ILinearData>();
        this.hysteresisDataCurrrent = this.hysteresisDataOe;
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

        this.setLastExecutedOn(timeStamp);

        // run analytic
        this.run();

        this.isStrokeTestInProgress = false;
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
     * @return valve Hysteresis Data in closing direction
     */
    public List<ILinearData> getHysteresisDataCe()
    {
        return this.hysteresisDataCe;
    }

    /**
     * @param hysteresisDataCe valve Hysteresis Data in closing direction
     */
    public void setHysteresisDataCe(List<ILinearData> hysteresisDataCe)
    {
        this.hysteresisDataCe = hysteresisDataCe;
    }

    /**
     * @return valve Hysteresis Data in closing direction
     */
    public List<ILinearData> getHysteresisDataOe()
    {
        return this.hysteresisDataOe;
    }

    /**
     * @param hysteresisDataOe valve Hysteresis Data in opening direction
     */
    public void setHysteresisDataOe(List<ILinearData> hysteresisDataOe)
    {
        this.hysteresisDataOe = hysteresisDataOe;
    }

    /**
     * @return minimum pressure in closing direction - deviation
     */
    public ValveAnalyticData getPupCeMinDeviation()
    {
        return this.pupCeMinDeviation;
    }

    /**
     * @param pupCeMinDeviation minimum pressure in closing direction - deviation
     */
    public void setPupCeMinDeviation(ValveAnalyticData pupCeMinDeviation)
    {
        this.pupCeMinDeviation = pupCeMinDeviation;
    }
    
    /**
     * @return maximum pressure in opening direction - deviation
     */
    public ValveAnalyticData getPupOeMaxDeviation()
    {
        return this.pupOeMaxDeviation;
    }

    /**
     * @param pupOeMaxDeviation maximum pressure in opening direction - deviation
     */
    public void setPupOeMaxDeviation(ValveAnalyticData pupOeMaxDeviation)
    {
        this.pupOeMaxDeviation = pupOeMaxDeviation;
    }

    /**
     * @return pressure difference - deviation
     */
    public ValveAnalyticData getPdpDeviation()
    {
        return this.pdpDeviation;
    }

    /**
     * @param pdpDeviation pressure difference - deviation
     */
    public void setPdpDeviation(ValveAnalyticData pdpDeviation)
    {
        this.pdpDeviation = pdpDeviation;
    }
    /**
     * @return minimum pup in closing direction (pup @ position 0)
     *         recorded after calibration during cold run (no steam)
     * 
     */
    public double getPupCeMinCold()
    {
        return this.pupCeMinCold;
    }

    /**
     * @return maximum pup in opening direction (pup @ position 100)
     *         recorded after calibration during cold run (no steam)
     */
    public double getPupOeMaxCold()
    {
        return this.pupOeMaxCold;
    }

    /**
     * @return minimum pup in closing direction (pup @ position 0)
     *         recorded after calibration during hot run (with steam)
     * 
     */
    public double getPupCeMinHot()
    {
        return this.pupCeMinHot;
    }

    /**
     * @return maximum pup in opening direction (pup @ position 100)
     *         recorded after calibration during hot run (with steam)
     */
    public double getPupOeMaxHot()
    {
        return this.pupOeMaxHot;
    }

    /**
     * @return average inlet pressure in both closing and opening directions
     *         recorded after calibration during hot run (with steam)
     */
    public double getInletPressureHot()
    {
        return this.inletPressureHot;
    }

    /**
     * add one set of valve hysteresis data... called from listener
     */
    public void collectHysteresisData()
    {
        if ( !Double.isNaN(this.previousPosition) && this.getPosition() != this.previousPosition )
        {
            this.hysteresisDataCurrrent
                    .add(new ValveHysteresisData(this.getPosition(), this.getPup(), this.getInletPressure()));
        }
        this.previousPosition = this.getPosition();
    }

    /*
     * (non-Javadoc)
     * @see com.ge.power.cst.deviceanalytics.api.IAnalytic#run()
     */
    @Override
    public void run()
    {
        if ( this.hysteresisDataCe.size() < 3 || this.hysteresisDataOe.size() < 3 ) return;

        Coefficients pupCeLimits = getPupLimits(this.hysteresisDataCe);
        Coefficients pupOeLimits = getPupLimits(this.hysteresisDataOe);

        double pupCeMinLimit = pupCeLimits.coefficient1; // pup @ position 0
        double pupOeMaxLimit = pupOeLimits.coefficient2; // pup @ position 100
        double inletPressureAvg = getAverageInletPressure(); // average inlet pressure during closing and opening directions

        // check if slope calculation is required
        if ( Double.isNaN(this.slopeCe) || Double.isNaN(this.slopeOe) )
        {
            // slope calculation pending
            if ( inletPressureAvg > this.inletPressureHot_gt )
            {
                // hot run, stem pressure present... assign slopes during hot run
                this.pupCeMinHot = pupCeMinLimit;
                this.pupOeMaxHot = pupOeMaxLimit;
                this.inletPressureHot = inletPressureAvg;
            }
            else
            {
                // cold run, no steam pressure... assign slopes during cold run
                this.pupCeMinCold = pupCeMinLimit;
                this.pupOeMaxCold = pupOeMaxLimit;
            }

            // Check if all pressure values during both cold and hot runs are captured... and calculate slopes
            if ( !Double.isNaN(this.pupCeMinCold) && !Double.isNaN(this.pupOeMaxCold) && !Double.isNaN(this.pupCeMinHot)
                    && !Double.isNaN(this.pupOeMaxHot) )
            {
                this.setSlopeCe((this.pupCeMinHot - this.pupCeMinCold) / this.inletPressureHot);
                this.setSlopeOe((this.pupOeMaxHot - this.pupOeMaxCold) / this.inletPressureHot);
            }
        }
        else
        {
            // Slopes available...
            // Calculate pdp
            double pdp = pupOeLimits.coefficient2 - pupCeLimits.coefficient2; // difference of pup @ position 100
            this.pdpDeviation.setCalcValue(pdp, true);

            // Apply correction to closing and opening pressures
            double pupCeMin = pupCeMinLimit - this.slopeCe * inletPressureAvg;
            double pupOeMax = pupOeMaxLimit - this.slopeOe * inletPressureAvg;
            this.pupCeMinDeviation.setCalcValue(pupCeMin, true);
            this.pupOeMaxDeviation.setCalcValue(pupOeMax, true);

        }

        // 1. update AnalyticData.value in ANALYTIC_DATA db table
        // 2. add AnalyticData.value to HST_DATA table
        // 3. prepare chart data - fetch hst data, get projections, identify projected failure date
        // 4. publish AnalyticDataSet and chart data to Web Socket
        super.run();
    }

    private Coefficients getPupLimits(List<ILinearData> hysteresisData)
    {
        LinearRegression lr = new LinearRegression();
        // Compute linear equation - position = coeff_m * pup + coeff_b
        Coefficients coefficients = lr.compute(hysteresisData);
        // get pup value when position equals the supplied parameter

        double pupAt0 = (0 - coefficients.coefficient2) / coefficients.coefficient1;
        double pupAt100 = (100 - coefficients.coefficient2) / coefficients.coefficient1;

        Coefficients pups = lr.new Coefficients();
        pups.coefficient1 = pupAt0;
        pups.coefficient2 = pupAt100;
        return pups;
    }

    private double getAverageInletPressure()
    {
        double sum = 0;
        double count = this.hysteresisDataCe.size() + this.hysteresisDataOe.size();
        for (ILinearData hysteresisData : this.hysteresisDataCe)
        {
            sum += ((ValveHysteresisData) hysteresisData).getInletPressure();
        }
        for (ILinearData hysteresisData : this.hysteresisDataOe)
        {
            sum += ((ValveHysteresisData) hysteresisData).getInletPressure();
        }

        sum /= count;

        return sum;
    }
}
