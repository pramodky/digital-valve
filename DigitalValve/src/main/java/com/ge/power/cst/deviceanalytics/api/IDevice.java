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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.ge.power.cst.deviceanalytics.api.Util.DeviceType;
import com.ge.power.cst.deviceanalytics.digitalvalve.api.ValveSet;

/**
 * 
 * @author 103019287
 */
@XmlJavaTypeAdapter(ValveSet.XmlAdaptValveSet.class)
public interface IDevice
{
    /**
     * @return Unique Identifier for this device 
     */
    public String getId();

    /**
     * @param id Unique Identifier for this device 
     */
    public void setId(String id);

    /**
     * @return Name of this device 
     */
    public String getName();

    /**
     * @param name Name of this device 
     */
    public void setName(String name);

    /**
     * @return Description of this device 
     */
    public String getDescription();

    /**
     * @param description device description
     */
    public void setDescription(String description);

    /**
     * @return Type of this Device 
     * 
     */
    public DeviceType getType();

    /**
     * @param type Type of this devic 
     */
    public void setType(DeviceType type);
    
    /**
     * @return active alerts associated with this device
     */
    public List<IAlert> getActiveAlerts();
    
    /**
     * @return health status of this device
     */
    public IHealthStatus getHealthStatus();
}
