/*
 * Copyright (c) 2016 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */

package com.ge.power.cst.deviceanalytics.webservices;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ge.power.cst.commons.web.Responses;
import com.ge.power.cst.deviceanalytics.api.Block;

import aQute.bnd.annotation.component.Component;

/**
 * Interface to register web-services. Contains root path for REST calls.
 * 
 * @author 103019287
 */
interface IactuatorAnalyticsWebServer {
	/**
	 * Main path for web services
	 */
	public static final String PATH = "/actuatorAnalytics"; //$NON-NLS-1$
}

/**
 * This class provides REST methods to access -
 * <ol>
 * <li>master data - Blocks and Units; Systems, Sub Systems and portion of
 * Device (Actuator); Full Actuator info</li>
 * </ol>
 * 
 * @author 103019287
 */
@Component
@Path(IactuatorAnalyticsWebServer.PATH)
public class ActuatorAnalyticsWebService implements IactuatorAnalyticsWebServer {
	/**
	 * Logger Service
	 */
	private static final Logger _logger = LoggerFactory.getLogger(ActuatorAnalyticsWebService.class);
	
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


	/**
	 * REST method to return a block instance along with Units configured for
	 * this project.
	 * 
	 * @return Block instance in Json format
	 */
	@GET
	@Path("/getBlock")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBlock()
	{
		// Get the block instance
		Block block = null; //this.blockService.getBlock();

		ObjectMapper jsonParser = new ObjectMapper();

		// Exclude field from serialization - unit.systems...
		String[] ignorableFieldNamesUnit = { "systems" }; //$NON-NLS-1$
		// and use it to create filters
		FilterProvider filters = new SimpleFilterProvider().addFilter("filterUnit", //$NON-NLS-1$
				SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNamesUnit));
		// Create writer object with the filters
		ObjectWriter writer = jsonParser.writer(filters);

		String jsonString;
		try {
			// Parse the block instance to Json string
			jsonString = writer.writeValueAsString(block);
			// Return parsed Json string
			return Responses.addCrossOriginHeaders(Response.ok(jsonString)).build();
		} catch (JsonGenerationException e) {
			_logger.error("Unable to send Block data.", e); //$NON-NLS-1$
			return Response.serverError().build();
		} catch (JsonMappingException e) {
			_logger.error("Unable to send Block data.", e); //$NON-NLS-1$
			return Response.serverError().build();
		} catch (IOException e) {
			_logger.error("Unable to send Block data.", e); //$NON-NLS-1$
			return Response.serverError().build();
		}

	}


	

}
