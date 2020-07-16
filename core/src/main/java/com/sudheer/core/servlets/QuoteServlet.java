/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.sudheer.core.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sudheer.core.models.Quote;
import com.sudheer.core.services.QuoteService;
/*
 * This can be accessed using either http://<aemhost:port>>/content/sudheer/public/services/quote.json?count= 
 * or http://<aemhost:port>>/bin/get/quote?count=
 */
@Component(service=Servlet.class,
           property={
                   Constants.SERVICE_DESCRIPTION + "=Quote Servlet",
                   "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                   "sling.servlet.resourceTypes="+ "/apps/sudheer/components/content/quote",
                   "sling.servlet.extensions=" + "json",
                   "sling.servlet.paths=" + "/bin/get/quote"
           })
public class QuoteServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteServlet.class);

    private static final String ERROR_RESPONSE = "{\"status\": \"Error occurred while processing the request. Please try again.\"}";
	private static final String COUNT = "count";
    
    @Reference
    transient QuoteService quoteService;

    @Override
    protected void doGet(final SlingHttpServletRequest request,
            final SlingHttpServletResponse response) throws ServletException, IOException {
        String countParam = request.getParameter(COUNT);
        String quoteResponse = ERROR_RESPONSE;
        Integer count = 0;
        if(StringUtils.isNotBlank(countParam)) {
        	try {
        		count = Integer.valueOf(countParam);
            }catch(NumberFormatException numberFormatException) {
            	LOGGER.warn("Invalid count:: NumberFormatException occurred.");
            }
        }
        List<Quote> Quotes = quoteService.getQuote(count);
        
        ObjectMapper objectMapper = new ObjectMapper();
        if(Quotes != null)
        	quoteResponse = objectMapper.writeValueAsString(Quotes);
       
        response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        response.getWriter().write(quoteResponse);
    }
}
