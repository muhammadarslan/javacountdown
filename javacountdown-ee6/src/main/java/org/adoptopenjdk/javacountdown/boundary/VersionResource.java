/*
 * Copyright [2013] Adopt OpenJDK Programme
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.adoptopenjdk.javacountdown.boundary;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.adoptopenjdk.javacountdown.control.DataProvider;
import org.adoptopenjdk.javacountdown.control.ResultCache;
import org.adoptopenjdk.javacountdown.control.VisitTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

/**
 * REST Web Service for the javacountdown website
 */
@Path("version")
@Stateless
public class VersionResource {

    private static final Logger logger = LoggerFactory.getLogger(VersionResource.class);
    
    @Inject
    private DataProvider dataProvider;

    @EJB
    ResultCache cache;

    /**
     * Retrieves visitor information from web client in JSON format
     *
     * @param content
     * @return
     */
    @POST
    @Consumes("application/json")
    public Response log(String content) {
        logger.debug("Client sent input: {}", content);

        VisitTransfer visitTransfer = null;
        Gson gson = new Gson();
        try {
            visitTransfer = gson.fromJson(content, VisitTransfer.class);
        } catch (JsonSyntaxException e) {
            logger.warn("Could not deserialize client input, message: {}", e.getMessage());
            throw new WebApplicationException(e, Response.status(Response.Status.BAD_REQUEST).build());
        }
        dataProvider.persistVisit(visitTransfer);
        return Response.noContent().build();
    }

    /**
     * Gets Data for the map
     *
     * @return JSON object with data for the map.
     */
    @GET
    @Produces("application/json")
    public Response getData() {
        return Response.ok(cache.getCountryData()).build();
    }
}
