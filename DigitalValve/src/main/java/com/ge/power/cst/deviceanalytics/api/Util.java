package com.ge.power.cst.deviceanalytics.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains utilities used within the api package - Enums
 * 
 * @author 103019287
 */
public class Util
{
    /**
     * Valid Unit Types
     * 
     * @author 103019287
     */
    public enum UnitType
    {
        /**
         * Gas Turbine
         */
        GT(0),
        /**
         * Steam Turbine
         */
        ST(1),
        /**
         * Heat Recovery Steam Generator
         */
        HRSG(2),
        /**
         * Balance of Plant
         */
        BOP(3),
        /**
         * Gas Turbine Generator
         */
        GEN_GT(4),
        /**
         * Steam Turbine Generator
         */
        GEN_ST(5);

        private static final Map<Integer, UnitType> typesByValue = new HashMap<Integer, UnitType>();

        static
        {
            for (UnitType type : UnitType.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private UnitType(int value)
        {
            this.value = value;
        }

        /**
         * @return Unit Type integer value - GT(0), ST(1), HRSG(2), BOP(3),
         *         GEN_GT(4), GEN_ST(5)
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value - GT(0), ST(1), HRSG(2), BOP(3), GEN_GT(4),
         *            GEN_ST(5)
         * @return UnitType
         */
        public static UnitType getValue(int value)
        {
            return typesByValue.get(value);
        }

    }

    /**
     * Valid Turbine Frame Sizes
     * 
     * @author 103019287
     */
    public enum FrameSize
    {

        /**
         * GT Frame 9HA
         */
        GT_9HA(0),
        /**
         * GT Frame 7F05
         */
        GT_7F_05(1),
        /**
         * GT Frame ST
         */
        ST_D14(2);

        private static final Map<Integer, FrameSize> typesByValue = new HashMap<Integer, FrameSize>();

        static
        {
            for (FrameSize type : FrameSize.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private FrameSize(int value)
        {
            this.value = value;
        }

        /**
         * @return Frame Size Integer value - GT_9HA(0), GT_7F_05(1), ST_XXX(2)
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value - GT_9HA(0), GT_7F_05(1), ST_XXX(2)
         * @return FrameSize
         */
        public static FrameSize getValue(int value)
        {
            return typesByValue.get(value);
        }

    }

    /**
     * 
     * @author 103019287
     */
    public enum SystemType
    {
        /**
         * GT - Compressor Systems
         */
        GT_CS,
        /**
         * GT - Fuel System
         */
        GT_FS,
        /**
         * ST - HP System
         */
        ST_HP,
        /**
         * ST - LP System
         */
        ST_LP,
        /**
         * ST - IP System
         */
        ST_IP;
    }

    /**
     * 
     * @author 103019287
     */
    public enum SubSystemType
    {
        /**
         * GT Compressor System - Guide Vanes
         */
        GT_CS_GV,
        /**
         * GT Fuel System - Gas Fuel
         */
        GT_FS_GF,
        /**
         * ST HP Subsystem
         */
        ST_HP_HP,
        /**
         * ST IP Subsystem
         */
        ST_IP_IP,
        /**
         * ST LP Subsystem
         */
        ST_LP_LP;
    }

    /**
     * Valid Device Statuses
     * 
     * @author 103019287
     */
    public enum DeviceStatus
    {
        // DISABLED, ENABLED;
        /**
         * Device Status DISABLED
         */
        DEVICE_DISABLED(0),
        /**
         * Device Status ENABLED
         */
        DEVICE_ENABLED(1);

        private static final Map<Integer, DeviceStatus> typesByValue = new HashMap<Integer, DeviceStatus>();

        static
        {
            for (DeviceStatus type : DeviceStatus.values())
            {
                typesByValue.put(type.value, type);
            }
        }

        private int value;

        private DeviceStatus(int value)
        {
            this.value = value;
        }

        /**
         * @return Device Status integer value - DISABLED(0), ENABLED(1)
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value - DISABLED(0), ENABLED(1)
         * @return DeviceStatus
         */
        public static DeviceStatus getValue(int value)
        {
            return typesByValue.get(value);
        }
    }

    /**
     * Valid Signal Classification Types
     * 
     * @author 103019287
     */
    public enum SignalType
    {
        /**
         * Position Command
         */
        COMMAND(0),
        /**
         * Position FeedBack
         */
        FEEDBACK(1),
        /**
         * Position Error
         */
        POSITION_ERROR(2),
        /**
         * Servo Current
         */
        SERVO_CURRENT(3),
        /**
         * Command in %
         */
        COMMAND_PCT(4),
        /**
         * Feedback in %
         */
        FEEDBACK_PCT(5),
        /**
         * Feedback A in %
         */
        FEEDBACK_PCT_A(6),
        /**
         * Feedback B in %
         */
        FEEDBACK_PCT_B(7),
        /**
         * Feedback C in %
         */
        FEEDBACK_PCT_C(8),
        /**
         * Alarm H
         */
        ALARM_H(9),
        /**
         * Action H
         */
        ACTION_H(10),
        /**
         * Pre Vote Current R
         */
        PRE_VOTE_CURRENT_R(11),
        /**
         * Pre Vote Current S
         */
        PRE_VOTE_CURRENT_S(12),
        /**
         * Pre Vote Current T
         */
        PRE_VOTE_CURRENT_T(13),
        /**
         * Device Status
         */
        DEVICE_STATUS(14),
        /**
         * IO Pack Status R
         */
        IO_PACK_STATUS_R(15),
        /**
         * IO Pack Status s
         */
        IO_PACK_STATUS_S(16),
        /**
         * IO Pack Status t
         */
        IO_PACK_STATUS_T(17),
        /**
         * Servo Suicide R Status
         */
        SERVO_SUICIDE_R(18),
        /**
         * Servo Suicide S Status
         */
        SERVO_SUICIDE_S(19),
        /**
         * Servo Suicide T Status
         */
        SERVO_SUICIDE_T(20),
        /**
         * Position Feedback Status
         */
        POSITION_FEEDBACK_STATUS(21),
        /**
         * High Position Error Alarm Status
         */
        HIGH_POSITION_ERROR_ALERT(22),
        /**
         * High Position Feedback Spread Status
         */
        HIGH_POSITION_FEEDBACK_SPREAD_ALERT(23),
        /**
         * High Servo Current Spread Alarm Status
         */
        HIGH_SERVO_CURRENT_SPREAD_ALERT(24),
        /**
         * IO Pack Diagnostic Alarm Status
         */
        IO_PACK_DIAGNOSTIC_ALERT(25),
        /**
         * Servo Suicide Alarm Status
         */
        SERVO_SUICIDED_ALERT(26),
        /**
         * Position Feedback Faulted Alarm Status
         */
        POSITION_FEEDBACK_FAULTED_ALERT(27),
        /**
         * Position Max
         */
        POSITION_MAX(28),
        /**
         * Position Max in %
         */
        POSITION_MAX_PCT(29),
        /**
         * Position Min
         */
        POSITION_MIN(30),
        /**
         * Position Min in %
         */
        POSITION_MIN_PCT(31),

        /**
         * Motor current for electric actuators
         */
        MOTOR_CURRENT(32),
        /**
         * Motor current minimum limit for electric actuators
         */
        MOTOR_CURRENT_MIN(33),
        /**
         * Motor current maximum limit for electric actuators
         */
        MOTOR_CURRENT_MAX(34),

        /**
         * POSITION ERROR DEVIATION
         */
        POSITION_ERROR_DEVIATION(35),

        STROKE_TEST_IN_PROGRESS(36), FASV_CLOSING_TIME(37), SERVO_CLOSING_TIME(38), FASV_CLOSING_TIME_DEVIATION(
                39), SERVO_CLOSING_TIME_DEVIATION(40), FASV_COMMAND(41), VALVE_CLOSED(42), CALIBRATION_IN_PROGRESS(
                        43), INLET_PRESSURE(44), SPEED(
                                45), PRESSURE_UNDER_PISTON(46), VALVE_OPENED(47), CTL_HYSTS_POS_DEVIATION(
                                        48), CTL_HYSTS_PRESS_DEVIATION(49), VLV_HYSTS_PUP_CLOSING_DEVIATION(
                                                50), VLV_HYSTS_PUP_OPENING_DEVIATION(51), VLV_HYSTS_PDP_DEVIATION(52);

        private static final Map<Integer, SignalType> typesByValue = new HashMap<Integer, SignalType>();

        static
        {
            for (SignalType type : SignalType.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private SignalType(int value)
        {
            this.value = value;
        }

        /**
         * @return SignalClassificationType Integer value
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value
         * @return SignalClassificationType
         */
        public static SignalType getValue(int value)
        {
            return typesByValue.get(value);
        }
    }

    /**
     * Valid IO Pack Statuses
     * 
     * @author 103019287
     */
    public enum IO_PackStatus
    {
        /**
         * IO Pack Status OK
         */
        OK(0),
        /**
         * IO Pack Status Diagnostic
         */
        DIAG(1);

        private static final Map<Integer, IO_PackStatus> typesByValue = new HashMap<Integer, IO_PackStatus>();

        static
        {
            for (IO_PackStatus type : IO_PackStatus.values())
            {
                typesByValue.put(type.value, type);
            }
        }

        private int value;

        private IO_PackStatus(int value)
        {
            this.value = value;
        }

        /**
         * @return IO Pack Status integer value - OK(0), DIAG(1)
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value - OK(0), DIAG(1)
         * @return IO_PackStatus
         */
        public static IO_PackStatus getValue(int value)
        {
            return typesByValue.get(value);
        }
    }

    /**
     * Valid Servo Suicide Status
     * 
     * @author 103019287
     */
    public enum ServoSuicideStatus
    {
        /**
         * Servo Suicide Status OK
         */
        OK(0),
        /**
         * Servo Suicide Status Suicided
         */
        SUIC(1);

        private static final Map<Integer, ServoSuicideStatus> typesByValue = new HashMap<Integer, ServoSuicideStatus>();

        static
        {
            for (ServoSuicideStatus type : ServoSuicideStatus.values())
            {
                typesByValue.put(type.value, type);
            }
        }

        private int value;

        private ServoSuicideStatus(int value)
        {
            this.value = value;
        }

        /**
         * @return Servo Suicide Status integer value - OK(0), SUIC(1)
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value - OK(0), SUIC(1)
         * @return ServoSuicideStatus
         */
        public static ServoSuicideStatus getValue(int value)
        {
            return typesByValue.get(value);
        }
    }

    /**
     * Valid Position Feedback Status
     * 
     * @author 103019287
     */
    public enum PositionFeedbackStatus
    {
        /**
         * Position Feedback Status - AVERAGE SENSORS
         */
        AVERAGE_SENSORS(4),
        /**
         * Position Feedback Status - USING SINGLE SENSOR
         */
        USING_SINGLE_SENSOR(5),
        /**
         * Position Feedback Status - MAX OF 2 SENSORS
         */
        MAX_OF_2_SENSORS(6),
        /**
         * Position Feedback Status - DEFAULT VALUE
         */
        DEFAULT_VALUE(7);

        private static final Map<Integer, PositionFeedbackStatus> typesByValue = new HashMap<Integer, PositionFeedbackStatus>();

        static
        {
            for (PositionFeedbackStatus type : PositionFeedbackStatus.values())
            {
                typesByValue.put(type.value, type);
            }
        }

        private int value;

        private PositionFeedbackStatus(int value)
        {
            this.value = value;
        }

        /**
         * @return Position Feedback Status integer value - AVERAGE_SENSORS(4),
         *         USING_SINGLE_SENSOR(5), MAX_OF_2_SENSORS(6), DEFAULT_VALUE(7)
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value - AVERAGE_SENSORS(4),
         *            USING_SINGLE_SENSOR(5), MAX_OF_2_SENSORS(6),
         *            DEFAULT_VALUE(7)
         * @return PositionFeedbackStatus
         */
        public static PositionFeedbackStatus getValue(int value)
        {
            return typesByValue.get(value);
        }
    }

    /**
     * Valid Actuator Set Type
     * <li>{@link #MSCV}
     * <li>{@link #CRV}
     * <li>{@link #UNKNOWN}
     * 
     * @author 212469291
     */
    public enum DeviceType
    {

        /**
         * DeviceType MSCV
         */
        MSCV(0),
        /**
         * DeviceType CRV
         */
        CRV(1),
        /**
         * DeviceType Unknown
         */
        UNKNOWN(-1);

        private static final Map<Integer, DeviceType> typesByValue = new HashMap<Integer, DeviceType>();

        static
        {
            for (DeviceType type : DeviceType.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private DeviceType(int value)
        {
            this.value = value;
        }

        /**
         * @return DeviceType Integer value - MSCV(0), CRV(1), UNKNOWN(-1)
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value
         *            Integer value - MSCV(0), CRV(1), UNKNOWN(-1)
         * @return DeviceType
         */
        public static DeviceType getValue(int value)
        {
            return typesByValue.get(value);
        }

    }

    /**
     * Valid Signal grouping
     * <li>{@link #PRIMARY}
     * <li>{@link #SECONDARY}
     * <li>{@link #BOTH}
     * <li>{@link #CALC}
     * <li>{@link #ALERT}
     * <li>{@link #UNKNOWN}
     * 
     * @author 103019287
     */
    public enum SignalGroupType
    {

        /**
         * Group signals that are collected at primary collection rate (default - 1 sec.)
         */
        PRIMARY(0),
        /**
         * Group signals that are collected at secondary collection rate (default - 40 ms.)
         */
        SECONDARY(1),

        /**
         * Group signals that are collected at both collection rates (primary and secondary)
         */
        BOTH(2),

        /**
         * Group signals that are computed by analytics
         */
        CALC(3),
        /**
         * Group signals that are used by the analytics as alerts
         */
        ALERT(4),
        /**
         * Unknown group
         */
        UNKNOWN(-1);

        private static final Map<Integer, SignalGroupType> typesByValue = new HashMap<Integer, SignalGroupType>();

        static
        {
            for (SignalGroupType type : SignalGroupType.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private SignalGroupType(int value)
        {
            this.value = value;
        }

        /**
         * @return Integer value of valid Signal grouping
         *         <li>{@link #PRIMARY}
         *         <li>{@link #SECONDARY}
         *         <li>{@link #BOTH}
         *         <li>{@link #CALC}
         *         <li>{@link #ALERT}
         *         <li>{@link #UNKNOWN}
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value Integer value of valid Signal grouping
         *            <li>{@link #PRIMARY}
         *            <li>{@link #SECONDARY}
         *            <li>{@link #BOTH}
         *            <li>{@link #CALC}
         *            <li>{@link #ALERT}
         *            <li>{@link #UNKNOWN}
         * 
         * @return SignalGroupType
         */
        public static SignalGroupType getValue(int value)
        {
            return typesByValue.get(value);
        }

    }
    
    /**
     * valid data types
     * @author 103019287
     */
    public enum DataType
    {

        LONG(1), DOUBLE(2), BOOLEAN(3);

        private static final Map<Integer, DataType> typesByValue = new HashMap<Integer, DataType>();

        static
        {
            for (DataType type : DataType.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private DataType(int value)
        {
            this.value = value;
        }

        /**
         * @return Integer value of valid DataType
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value Integer value of valid DataType
         * @return Severity
         */
        public static DataType getValue(int value)
        {
            return typesByValue.get(value);
        }

    }

    /**
     * @author 103019287
     */
    public enum SeverityType
    {

        IMPORTANT(1), WARNING(2), ERROR(3), INFORMATION(4), UNKNOWN(5);

        private static final Map<Integer, SeverityType> typesByValue = new HashMap<Integer, SeverityType>();

        static
        {
            for (SeverityType type : SeverityType.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private SeverityType(int value)
        {
            this.value = value;
        }

        /**
         * @return Integer value of valid SeverityType
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value Integer value of valid SeverityType
         * @return Severity
         */
        public static SeverityType getValue(int value)
        {
            return typesByValue.get(value);
        }

    }
    
    /**
     * aggregation type to compress data in database.
     * @author 103019287
     */
    public enum AggregationType
    {

        NONE(1), AVERAGE(2), MAX(3), MIN(4), LINEAR(5);

        private static final Map<Integer, AggregationType> typesByValue = new HashMap<Integer, AggregationType>();

        static
        {
            for (AggregationType type : AggregationType.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private AggregationType(int value)
        {
            this.value = value;
        }

        /**
         * @return Integer value of valid AggregationType
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value Integer value of valid AggregationType
         * @return Severity
         */
        public static AggregationType getValue(int value)
        {
            return typesByValue.get(value);
        }

    }
    
    /**
     * aggregation type to compress data in database.
     * @author 103019287
     */
    public enum AnalyticId
    {

        VALVE_CLOSING_ANALYTIC(1), VALVE_HYSTERESIS_ANALYTIC(2), CONTROL_HYSTERESIS_ANALYTIC(3);

        private static final Map<Integer, AnalyticId> typesByValue = new HashMap<Integer, AnalyticId>();

        static
        {
            for (AnalyticId type : AnalyticId.values())
            {
                typesByValue.put(type.value, type);
            }
        }
        private int value;

        private AnalyticId(int value)
        {
            this.value = value;
        }

        /**
         * @return Integer value of valid AnalyticId
         */
        public int getValue()
        {
            return this.value;
        }

        /**
         * @param value Integer value of valid AnalyticId
         * @return Severity
         */
        public static AnalyticId getValue(int value)
        {
            return typesByValue.get(value);
        }

    }
}
