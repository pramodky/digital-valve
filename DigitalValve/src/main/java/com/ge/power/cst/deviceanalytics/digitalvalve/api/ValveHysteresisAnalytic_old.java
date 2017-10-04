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
import com.ge.power.cst.deviceanalytics.api.LinearRegression;
import com.ge.power.cst.deviceanalytics.api.LinearRegression.Coefficients;

/**
 * 
 * @author 103019287
 */
public class ValveHysteresisAnalytic_old
{

    // collected inputs:
    private List<ILinearData> hysteresisDataCe  = new ArrayList<ILinearData>();
    private List<ILinearData> hysteresisDataOe  = new ArrayList<ILinearData>();

    private double            inletPressure_gt  = 50;

    // slopes:
    private double            slopeCe;
    private double            slopeOe;

    // baseline/reference values:
    private double            pupCeMinRef;
    private double            pupOeMaxRef;
    private double            pdpRef;

    // calculated pressure values
    private double            pupCeMin;
    private double            pupOeMax;
    private double            pdp;

    // calculated pressure values right after calibration
    private double            pupCeMinCold      = Double.NaN;
    private double            pupOeMaxCold      = Double.NaN;
    private double            pupCeMinHot       = Double.NaN;
    private double            pupOeMaxHot       = Double.NaN;
    private double            inletPressureHot  = Double.NaN;

    // calculate pressure deviations
    private ValveAnalyticData      pupCeMinDeviation = new ValveAnalyticData();
    private ValveAnalyticData      pupOeMaxDeviation = new ValveAnalyticData();
    private ValveAnalyticData      pdpDeviation      = new ValveAnalyticData();

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
    }

    /**
     * @return the slope in opening direction
     */
    public double getSlopeOe()
    {
        return this.slopeOe;
    }

    /**
     * @param slopeOe the slope slope in opening direction
     */
    public void setSlopeOe(double slopeOe)
    {
        this.slopeOe = slopeOe;
    }

    /**
     * @return minimum pup in closing direction (pup @ 0 position) - reference value
     */
    public double getPupCeMinRef()
    {
        return this.pupCeMinRef;
    }

    /**
     * @param pupCeMinRef minimum pup in closing direction (pup @ 0 position) - reference value
     */
    public void setPupCeMinRef(double pupCeMinRef)
    {
        this.pupCeMinRef = pupCeMinRef;
    }

    /**
     * @return maximum pup in opening direction (pup @ 100 position) - reference value
     */
    public double getPupOeMaxRef()
    {
        return this.pupOeMaxRef;
    }

    /**
     * @param pupOeMaxRef maximum pup in opening direction (pup @ 100 position)- reference value
     */
    public void setPupOeMaxRef(double pupOeMaxRef)
    {
        this.pupOeMaxRef = pupOeMaxRef;
    }

    /**
     * @return pressure difference - reference value
     */
    public double getPdpRef()
    {
        return this.pdpRef;
    }

    /**
     * @param pdpRef pressure difference - reference value
     */
    public void setPdpRef(double pdpRef)
    {
        this.pdpRef = pdpRef;
    }

    /**
     * @return minimum pup in closing direction (pup @ position 0)
     */
    public double getPupCeMin()
    {
        return this.pupCeMin;
    }

    /**
     * @param pupCeMin minimum pup in closing direction (pup @ position 0)
     */
    public void setPupCeMin(double pupCeMin)
    {
        this.pupCeMin = pupCeMin;
        // Check if baseline is captured
        if ( Double.isNaN(this.pupCeMinRef) )
        {
            this.pupCeMinRef = pupCeMin;
        }
    }

    /**
     * @return maximum pup in opening direction (pup @ position 100)
     */
    public double getPupOeMax()
    {
        return this.pupOeMax;
    }

    /**
     * @param pupOeMax maximum pup in opening direction (pup @ position 100)
     */
    public void setPupOeMax(double pupOeMax)
    {
        this.pupOeMax = pupOeMax;
        // Check if baseline is captured
        if ( Double.isNaN(this.pupOeMaxRef) )
        {
            this.pupOeMaxRef = pupOeMax;
        }

    }

    /**
     * @return pressure difference
     */
    public double getPdp()
    {
        return this.pdp;
    }

    /**
     * @param pdp pressure difference
     */
    public void setPdp(double pdp)
    {
        this.pdp = pdp;
        // Check if baseline is captured
        if ( Double.isNaN(this.pdpRef) )
        {
            this.pdpRef = pdp;
        }

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
     * run valve hysteresis analytic
     */
    public void runAnalytic()
    {
        if ( this.hysteresisDataCe.size() < 3 || this.hysteresisDataOe.size() < 3 ) return;

        Coefficients pupCeLimits = getPupLimits(this.hysteresisDataCe);
        Coefficients pupOeLimits = getPupLimits(this.hysteresisDataOe);

        double pupCeMinLimit = pupCeLimits.coefficient1; // pup @ position 0
        double pupOeMaxLimit = pupOeLimits.coefficient2; // pup @ position 100
        double inletPressure = getAverageInletPressure(); // average inlet pressure during closing and opening directions

        // check if slope calculation is required
        if ( Double.isNaN(this.slopeCe) || Double.isNaN(this.slopeOe) )
        {
            // slope calculation pending
            if ( inletPressure > this.inletPressure_gt )
            {
                // hot run, stem pressure present... assign slopes during hot run
                this.pupCeMinHot = pupCeMinLimit;
                this.pupOeMaxHot = pupOeMaxLimit;
                this.inletPressureHot = inletPressure;
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
                this.slopeCe = (this.pupCeMinHot - this.pupCeMinCold) / this.inletPressureHot;
                this.slopeOe = (this.pupOeMaxHot - this.pupOeMaxCold) / this.inletPressureHot;
            }
        }
        else
        {
            // Slopes available...
            // Calculate pdp
            this.setPdp(pupOeLimits.coefficient2 - pupCeLimits.coefficient2); // difference of pup @ position 100

            // Apply correction to closing and opening pressures
            this.setPupCeMin(pupCeMinLimit - this.slopeCe * inletPressure);
            this.setPupOeMax(pupOeMaxLimit - this.slopeOe * inletPressure);

            // calculate deviations
            if ( !Double.isNaN(this.pupCeMinRef) )
            {
                this.pupCeMinDeviation.setValue(100 * (this.pupCeMin - this.pupCeMinRef) / this.pupCeMinRef);
            }
            if ( !Double.isNaN(this.pupOeMaxRef) )
            {
                this.pupOeMaxDeviation.setValue(100 * (this.pupOeMax - this.pupOeMaxRef) / this.pupOeMaxRef);
            }
            if ( !Double.isNaN(this.pdpRef) )
            {
                this.pdpDeviation.setValue(100 * (this.pdp - this.pdpRef) / this.pdpRef);
            }
        }

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
