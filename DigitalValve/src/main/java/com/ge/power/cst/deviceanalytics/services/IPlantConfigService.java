/*
 * Copyright (c) 2017 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics.services;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.ge.power.cst.deviceanalytics.api.Plant;

/**
 * 
 * @author 103019287
 */
public interface IPlantConfigService
{
    /**
     * Retrieves entire Plant info configured for this app.
     * 
     * @return Plant instance configured for this app.
     * @throws JAXBException JAXBException
     */
    Plant getPlant()
            throws JAXBException;

    /**
     * @return plant health status info in json string
     *         <br>
     *         sample string - {"healthStatus":{"numberOfActiveAlerts":10,"maxSeverity":"IMPORTANT"}}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getPlantHealthStatus()
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @return block health status info in json string
     *         <br>
     *         sample string - {"healthStatus":{"numberOfActiveAlerts":5,"maxSeverity":"IMPORTANT"}}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getBlockHealthStatus(String blockId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @return unit health status info in json string
     *         <br>
     *         sample string - {"healthStatus":{"numberOfActiveAlerts":2,"maxSeverity":"WARNING"}}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getUnitHealthStatus(String blockId, String unitId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @param systemId unique identifier of the system
     * @return unit health status info in json string
     *         <br>
     *         sample string - {"healthStatus":{"numberOfActiveAlerts":0,"maxSeverity":"INFORMATION"}}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getSystemHealthStatus(String blockId, String unitId, String systemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @param systemId unique identifier of the system
     * @param subSystemId unique identifier of the sub system
     * @return unit health status info in json string
     *         <br>
     *         sample string - {"healthStatus":{"numberOfActiveAlerts":0,"maxSeverity":"INFORMATION"}}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getSubSystemHealthStatus(String blockId, String unitId, String systemId, String subSystemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @param systemId unique identifier of the system
     * @param subSystemId unique identifier of the sub system
     * @param deviceId unique identifier of the device
     * @return unit health status info in json string
     *         <br>
     *         sample string - {"healthStatus":{"numberOfActiveAlerts":1,"maxSeverity":"IMPORTANT"}}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getDeviceHealthStatus(String blockId, String unitId, String systemId, String subSystemId, String deviceId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @return Plant information plus block ids and block name
     *         <br>
     *         sample string - {"name":"PLANT Number - 1","description":"Description plant","blocks":[{"id":"BLOCK1","name":"BLOCK - 1"},{"id":"BLOCK2","name":
     *         "BLOCK - 2"},{"id":"BLOCK3","name":"BLOCK - 3"},{"id":"BLOCK4","name":"BLOCK - 4"},{"id":"BLOCK5","name":"BLOCK - 5"}]}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getPlantInfo()
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @return Plant information plus block ids and block name
     *         <br>
     *         sample string - {"name":"PLANT Number - 1","description":"Description plant","blocks":[{"id":"BLOCK1","name":"BLOCK - 1"},{"id":"BLOCK2","name":
     *         "BLOCK - 2"},{"id":"BLOCK3","name":"BLOCK - 3"},{"id":"BLOCK4","name":"BLOCK - 4"},{"id":"BLOCK5","name":"BLOCK - 5"}]}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getBlockInfo(String blockId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @return Plant information plus block ids and block name
     *         <br>
     *         sample string - {"name":"PLANT Number - 1","description":"Description plant","blocks":[{"id":"BLOCK1","name":"BLOCK - 1"},{"id":"BLOCK2","name":
     *         "BLOCK - 2"},{"id":"BLOCK3","name":"BLOCK - 3"},{"id":"BLOCK4","name":"BLOCK - 4"},{"id":"BLOCK5","name":"BLOCK - 5"}]}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getUnitInfo(String blockId, String unitId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @param systemId unique identifier of the system
     * @return Plant information plus block ids and block name
     *         <br>
     *         sample string - {"name":"PLANT Number - 1","description":"Description plant","blocks":[{"id":"BLOCK1","name":"BLOCK - 1"},{"id":"BLOCK2","name":
     *         "BLOCK - 2"},{"id":"BLOCK3","name":"BLOCK - 3"},{"id":"BLOCK4","name":"BLOCK - 4"},{"id":"BLOCK5","name":"BLOCK - 5"}]}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getSystemInfo(String blockId, String unitId, String systemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @param systemId unique identifier of the system
     * @param subSystemId unique identifier of the sub system
     * @return Plant information plus block ids and block name
     *         <br>
     *         sample string - {"name":"PLANT Number - 1","description":"Description plant","blocks":[{"id":"BLOCK1","name":"BLOCK - 1"},{"id":"BLOCK2","name":
     *         "BLOCK - 2"},{"id":"BLOCK3","name":"BLOCK - 3"},{"id":"BLOCK4","name":"BLOCK - 4"},{"id":"BLOCK5","name":"BLOCK - 5"}]}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getSubSystemInfo(String blockId, String unitId, String systemId, String subSystemId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

    /**
     * @param blockId unique identifier of the block
     * @param unitId unique identifier of the unit - GT1, GT2, ST1, ST2,...
     * @param systemId unique identifier of the system
     * @param subSystemId unique identifier of the sub system
     * @param deviceId unique identifier of the device
     * @return Plant information plus block ids and block name
     *         <br>
     *         sample string - {"name":"PLANT Number - 1","description":"Description plant","blocks":[{"id":"BLOCK1","name":"BLOCK - 1"},{"id":"BLOCK2","name":
     *         "BLOCK - 2"},{"id":"BLOCK3","name":"BLOCK - 3"},{"id":"BLOCK4","name":"BLOCK - 4"},{"id":"BLOCK5","name":"BLOCK - 5"}]}
     * @throws JAXBException JAXBException
     * @throws IOException io exception
     * @throws JsonMappingException json mapping exception
     * @throws JsonGenerationException json generation exception
     */
    String getDeviceInfo(String blockId, String unitId, String systemId, String subSystemId, String deviceId)
            throws JAXBException, JsonGenerationException, JsonMappingException, IOException;

}
