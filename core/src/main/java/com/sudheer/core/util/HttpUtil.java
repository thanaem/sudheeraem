package com.sudheer.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtil<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);
    
    private final Class<T> givenClass;

    private HttpUtil(Class<T> givenClass){
        this.givenClass = givenClass;
    }

    public static <T> HttpUtil<T> getInstance(Class<T> givenClass){
        return new HttpUtil<>(givenClass);
    }

    public List<T> get(String uri){
    	List<T> responseObject = null;
        HttpGet get = new HttpGet(uri);
        HttpClient client = HttpClientBuilder.create().build();
        try {
        	HttpResponse response = client.execute(get);
        	StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			LOGGER.info("Response status: {}", statusCode);
			HttpEntity entity = response.getEntity();
			responseObject = getResponse(responseObject, entity);
		} catch (ClientProtocolException clientProtocolException) {
			LOGGER.warn("Http#clientProtocolException - Failed to execute", clientProtocolException);
		} catch (IOException ioException) {
			LOGGER.warn("Http#IOException - Failed to execute", ioException);
		}
        return responseObject;
    }

	private  List<T> getResponse(List<T> responseObject, HttpEntity entity) {
		ContentType contentType = ContentType.get(entity);
		if(contentType != null) {
		    try {
		        InputStream inputStream = entity.getContent();
		        ObjectMapper mapper = new ObjectMapper();
		        responseObject = mapper.readValue(inputStream, new TypeReference<List<T>>(){});
		    } catch (IOException ioException) {
		        LOGGER.warn("Http#handleResponse - Failed to retrieve response entity", ioException);
		    }
		}
		return responseObject;
	}


}