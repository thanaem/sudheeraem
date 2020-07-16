package com.sudheer.core.services;

import java.util.List;

import com.sudheer.core.models.Quote;

public interface QuoteService {
	public List<Quote> getQuote(Integer count);

}
