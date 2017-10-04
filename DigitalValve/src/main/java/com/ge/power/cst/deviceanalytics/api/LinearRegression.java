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

/**
 * This class computes the coefficients (slope - a & y intercept - b) of linear expression (y = ax + b) that
 * best fits x & y data sets
 * 
 * @author 103019287
 */
public class LinearRegression
{
    /**
     * Default constructor
     */
    public LinearRegression()
    {

    }

    /**
     * 
     * @author 103019287
     */
    public class Coefficients
    {
        /**
         * first coefficient
         */
        public double coefficient1;
        /**
         * second coefficient
         */
        public double coefficient2;
    }

    /**
     * Compute the coefficients (slope - <b>m</b> & y intercept - <b>b</b>) of linear expression (<i>y = <b>m</b>x + <b>b</b></i>) that
     * best fits the data set
     * 
     * @param data data used for computation
     * @return coefficients (coefficient1 - slope <b>m</b>; coefficient2 - y intercept <b>b</b>) of linear expression (<i>y = <b>m</b>x + <b>b</b></i>)
     */
    public Coefficients compute(List<ILinearData> data)
    {
        Coefficients coefficients = new Coefficients();
        // throws exception if regression can not be performed
        if ( data.size() < 3 )
        {
            throw new IllegalArgumentException("Must have more than two data points"); //$NON-NLS-1$
        }

        // Get the mean of the data sets
        Coefficients mean = mean(data);
        double x_mean = mean.coefficient1;
        double y_mean = mean.coefficient2;

        // covariance - SUM((Xi - Xmean) * (Yi - Ymean))/n
        double covariance = 0;

        // Variance - SUM((Xi-Xmean)*(Xi-Xmean))/n
        double variance = 0;

        // Loop through the data sets and calculate covariance & variance
        for (ILinearData dataPoint : data)
        {
            // deviations
            double xDeviation = dataPoint.getValueX() - x_mean;
            double yDeviation = dataPoint.getValueY() - y_mean;

            covariance += xDeviation * yDeviation;
            variance += Math.pow(xDeviation, 2);
        }

        covariance /= data.size() - 2;
        variance /= data.size() - 2;

        // get the value of the gradient/slope using the formula b = cov[x,y] / var[x]
        coefficients.coefficient1 = covariance / variance;

        // get the value of the y-intercept using the formula b = ybar + a *
        // xbar
        coefficients.coefficient2 = y_mean - coefficients.coefficient1 * x_mean;

        return coefficients;
    }

    /**
     * Calculate the mean of two data sets
     * 
     * @param data data points
     * @return An array of length 2 with mean values of the two data sets
     */
    public Coefficients mean(List<ILinearData> data)
    {

        double sum1 = 0;
        double sum2 = 0;
        for (ILinearData dataPoint : data)
        {
            sum1 += dataPoint.getValueX();
            sum2 += dataPoint.getValueY();
        }
        sum1 /= data.size();
        sum2 /= data.size();

        Coefficients coefficients = new Coefficients();
        coefficients.coefficient1 = sum1;
        coefficients.coefficient2 = sum2;

        return coefficients;
    }
}
