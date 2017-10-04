/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics;

import java.io.IOException;
import java.util.Dictionary;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aQute.bnd.annotation.component.Reference;

import com.ge.dspmicro.machinegateway.api.IMachineGateway;
import com.ge.power.cst.commons.machinegateway.MachineAdapterDataSource;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.ConfigurationPolicy;
import aQute.bnd.annotation.component.Deactivate;

/**
 * Main entry level class for Actuator Analytics app.
 * 
 * @author 103019287
 */
@Component(immediate = true, name = DeviceAnalytics.SERVICE_PID, configurationPolicy = ConfigurationPolicy.require)
public class DeviceAnalytics
{
    @SuppressWarnings("javadoc")
    /**
     * Service PID
     */
    public static final String                       SERVICE_PID              = "com.ge.power.cst.diviceanalytics";                                                                                                                                             //$NON-NLS-1$

    /**
     * Logger Service
     */
    static final Logger                              _logger                  = LoggerFactory
            .getLogger(DeviceAnalytics.class);
 
    /**
     * Main scheduler to execute task that runs with primary sampling rate (1 sec)
     */
    ScheduledExecutorService                         scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor();
    /**
     * Secondary scheduler to execute task that runs with secondary sampling rate (40 ms)
     */
    // ScheduledExecutorService scheduledExecutorServiceSec = Executors.newSingleThreadScheduledExecutor();

    private IMachineGateway                          machineGateway;

    /**
     * Data source connected to adapter sampling data with primary rate (1 sec)
     */
    MachineAdapterDataSource                         dataSource;
    /**
     * Data source connected to adapter sampling data with secondary rate (40 ms)
     */
    MachineAdapterDataSource                         dataSourceSecondary;


    /**
     * @param ctx
     *            provided by Predix Machine OSGi container
     */
    @Activate
    public void activate(ComponentContext ctx)
    {
        _logger.info("Actuator Analytics App activated."); //$NON-NLS-1$
        Dictionary<?, ?> props = ctx.getProperties();

        DeviceAnalyticsConfiguration.populateConfiguration(props);


        _logger.info("Started Actuator Analytics successfully"); //$NON-NLS-1$
    }


    /**
     * @param ctx
     *            provided by Predix Machine OSGi container
     */
    @Deactivate
    public void deactivate(ComponentContext ctx)
    {
        this.scheduledExecutorService.shutdown();
        try
        {
            this.scheduledExecutorService.awaitTermination(30, TimeUnit.SECONDS);
            // this.scheduledExecutorServiceSec.awaitTermination(30, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            _logger.warn("Exception thrown while waiting for scheduled tasks to terminate.", e); //$NON-NLS-1$
        }
        
    }


    /**
     * Inject reference to Machine Gateway, called automatically by OSGI
     * 
     * @param gateway
     *            provided by Predix Machine OSGi container
     */
    @Reference
    public void setMachineGatewayService(IMachineGateway gateway)
    {
        // Inject reference to Machine Gateway
        this.machineGateway = gateway;
    }

    private class ActuatorAnalyticsRunnable
            implements Runnable
    {

        /**
         * 
         */
        public ActuatorAnalyticsRunnable()
        {

        }

        @Override
        public void run()
        {

            
        }
    }

}
