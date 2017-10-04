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

import java.util.Dictionary;

import com.ge.power.cst.commons.machinegateway.CommunicationProtocol;

import aQute.bnd.annotation.metatype.Configurable;
import aQute.bnd.annotation.metatype.Meta;

/**
 * This class provides methods to read properties from config file.
 * 
 * @author 103019287
 * 
 */
public class DeviceAnalyticsConfiguration
{
    /**
     * Default data source file tag when none is provided by the user
     */
    public static final String DEFAULT_FILE_SOURCE = "**NONE**"; //$NON-NLS-1$

    @SuppressWarnings("javadoc")
    @Meta.OCD(name = "%component.name", localization = "OSGI-INF/l10n/bundle")
    public interface Configuration
    {

        /**
         * @return the data sample rate to use in milliseconds or default
         */
        @Meta.AD(name = "%sampleRate.name", description = "%sampleRate.description", id = DeviceAnalytics.SERVICE_PID
                + ".sampleRate", required = false, deflt = "1000")
        int sampleRate();

        /**
         * @return the db collection rate for raw data (rate at which raw data
         *         shall get inserted in the DEVICE_DATA_RAW db table) in
         *         minutes or default
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".dbCollectionRate_Raw", required = false, deflt = "1")
        int dbCollectionRate_Raw();

        /**
         * @return the db Level 1 compression rate (rate at which
         *         raw data shall be compressed from DEVICE_DATA_RAW table and
         *         inserted in the DEVICE_DATA_COMPRESSION_1 db table) in
         *         minutes or default
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".dbCompressionRate_1", required = false, deflt = "1")
        int dbCompressionRate_1();

        /**
         * @return the db Level 2 compression rate (rate at which
         *         the data shall be compressed from DEVICE_DATA_COMPRESSION_1
         *         table and inserted in the DEVICE_DATA_COMPRESSION_2 db table)
         *         in minutes or default
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".dbCompressionRate_2", required = false, deflt = "5")
        int dbCompressionRate_2();

        /**
         * @return the db collection rate for Level 3 compression (rate at which
         *         the data shall be deleted from DEVICE_DATA_COMPRESSION_2
         *         table) in minutes or default
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".dbCollectionRate_Compression3", required = false, deflt = "5")
        int dbCollectionRate_Compression3();

        /**
         * @return the machine adapter to connect to source - OPCUA | ACTUATOR...
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".machineAdapter", required = false, deflt = "OPCUA")
        CommunicationProtocol machineAdapter();

        /**
         * @return if CSV reports are to be generated.
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".isGenerateCsvReports", required = false, deflt = "flase")
        boolean isGenerateCsvReports();

        /**
         * @return directory path where csv reports are to be created
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".csvReportFolder", required = false)
        String csvReportFolder();

        /**
         * @return if factory test is in progress.
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".isFactoryTest", required = false, deflt = "false")
        boolean isFactoryTest();
        
        /**
         * @return if calibration is in progress.
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".isCalibrationInProgress", required = false, deflt = "false")
        boolean isCalibrationInProgress();

        /**
         * @return if calibration is in progress.
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".isRestDataBeforeCalibration", required = false, deflt = "false")
        boolean isRestDataBeforeCalibration();
        
        /**
         * @return Servo Closing Time Deviation Alarm Limit - Used to predict failure date
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".servoClosingDeviationAlarmLimit", required = true, deflt = "20.0")
        double servoClosingDeviationAlarmLimit();
        
        /**
         * @return Servo Closing Time Deviation Action Limit - Used as warning
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".servoClosingDeviationActionLimit", required = true, deflt = "15.0")
        double servoClosingDeviationActionLimit();
        
        /**
         * @return FASV Closing Time Deviation Alarm Limit - Used to predict failure date
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".fasvClosingDeviationAlarmLimit", required = true, deflt = "20.0")
        double fasvClosingDeviationAlarmLimit();
        
        /**
         * @return FASV Closing Time Deviation Action Limit - Used as warning
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".fasvClosingDeviationActionLimit", required = true, deflt = "15.0")
        double fasvClosingDeviationActionLimit();
        
        /**
         * @return rate at which the data related to control hysteresis analytic has to be collected (in minutes)
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".ctlHystsCollectionRate", required = true, deflt = "3")
        int ctlHystsCollectionRate();

        
        /**
         * @return Control Hysteresis Position Deviation Alarm Limit - Used to predict failure date
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".ctlHystsPosDevAlarmLimit", required = true, deflt = "20.0")
        double ctlHystsPosDevAlarmLimit();
        
        /**
         * @return Control Hysteresis Position Deviation Alarm Limit - Used as warning
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".ctlHystsPosDevActionLimit", required = true, deflt = "15.0")
        double ctlHystsPosDevActionLimit();
        
        /**
         * @return Control Hysteresis Pressure Deviation Alarm Limit - Used to predict failure date
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".ctlHystsPressDevAlarmLimit", required = true, deflt = "20.0")
        double ctlHystsPressDevAlarmLimit();
        
        /**
         * @return Control Hysteresis Pressure Deviation Alarm Limit - Used as warning
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".ctlHystsPressDevActionLimit", required = true, deflt = "15.0")
        double ctlHystsPressDevActionLimit();
        
        /**
         * @return Position lower limit - start collecting control hysteresis data when Position is greater than this number
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".ctlHystsPosLimit_gt", required = true, deflt = "5.0")
        double ctlHystsPosLimit_gt();

        /**
         * @return Position upper limit - start collecting control hysteresis data when Position is less than this number
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".ctlHystsPosLimit_lt", required = true, deflt = "50.0")
        double ctlHystsPosLimit_lt();

        /**
         * @return Speed lower limit - start collecting control hysteresis data when Speed is greater than this number
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".ctlHystsSpeedLimit_gt", required = true, deflt = "2950.0")
        double ctlHystsSpeedLimit_gt();

        /**
         * @return Inlet Pressure lower limit - start collecting control hysteresis data when Inlet Pressure is greater than this number
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID + ".ctlHystsInletPressLimit_gt", required = true, deflt = "100.0")
        double ctlHystsInletPressLimit_gt();

        /**
         * @return Valve Hysteresis Pup Closing Deviation Alarm Limit - Used to predict failure date
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".vlvHystsPupClosingDevAlarmLimit", required = true, deflt = "20.0")
        double vlvHystsPupClosingDevAlarmLimit();
        
        /**
         * @return Valve Hysteresis Pup Closing Deviation Alarm Limit - Used as warning
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".vlvHystsPupClosingDevActionLimit", required = true, deflt = "15.0")
        double vlvHystsPupClosingDevActionLimit();
        
        
        /**
         * @return Valve Hysteresis Pup Opening Deviation Alarm Limit - Used to predict failure date
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".vlvHystsPupOpeningDevAlarmLimit", required = true, deflt = "20.0")
        double vlvHystsPupOpeningDevAlarmLimit();
        
        /**
         * @return Valve Hysteresis Pup Opening Deviation Alarm Limit - Used as warning
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".vlvHystsPupOpeningDevActionLimit", required = true, deflt = "15.0")
        double vlvHystsPupOpeningDevActionLimit();
        
        
        
        /**
         * @return Valve Hysteresis Pdp Deviation Alarm Limit - Used to predict failure date
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".vlvHystsPdpDevAlarmLimit", required = true, deflt = "20.0")
        double vlvHystsPdpDevAlarmLimit();
        
        /**
         * @return Valve Hysteresis Pdp Deviation Alarm Limit - Used as warning
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".vlvHystsPdpDevActionLimit", required = true, deflt = "15.0")
        double vlvHystsPdpDevActionLimit();
        
        /**
         * @return Inlet Pressure lower limit - start collecting valve hysteresis data for hot condition when Inlet Pressure is greater than this number
         */
        @Meta.AD(id = DeviceAnalytics.SERVICE_PID
                + ".vlvHystsInletPressLimitHot_gt", required = true, deflt = "600.0")
        double vlvHystsInletPressLimitHot_gt();

    }

    private static Configuration instance;

    /**
     * @return a singleton of the properties class
     */
    public static Configuration getConfiguration()
    {
        return instance;
    }

    /**
     * Populate (and overwrite) the current application properties
     * 
     * @param props
     *            properties to use
     */
    public static void populateConfiguration(Dictionary<?, ?> props)
    {
        instance = Configurable.createConfigurable(Configuration.class, props);
    }

}
