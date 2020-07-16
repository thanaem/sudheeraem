package com.sudheer.core.services.impl;

import java.text.MessageFormat;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sudheer.core.models.Quote;
import com.sudheer.core.services.QuoteService;
import com.sudheer.core.util.HttpUtil;

@Component(service = QuoteService.class)
@Designate(ocd = QuoteServiceImpl.Config.class)
public class QuoteServiceImpl implements QuoteService{
	private static final Logger LOGGER = LoggerFactory.getLogger(QuoteServiceImpl.class);

	String quoteAPI;
	
    @ObjectClassDefinition(name = "Quote Service API endpoint")
    @interface Config {
        @AttributeDefinition(name = "Quote API")
        String QuoteAPI() default "https://localhost:8080/quotes?qcount={0}";
    }
    
    @Activate
    protected void activate(Config config){
    	quoteAPI = config.QuoteAPI();
    }

	@Override
	public List<Quote> getQuote(Integer count) {

		return HttpUtil.getInstance(Quote.class).get(MessageFormat.format(quoteAPI, count));
	}

}
