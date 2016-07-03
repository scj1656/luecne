package com.solar.protocol;

import java.util.HashMap;
import java.util.Map;

public class SearchQuery {
	
	private Map<String,String> query = new HashMap<String,String>();;

	public Map<String, String> getQuery() {
		return query;
	}

	public void setQuery(Map<String, String> query) {
		this.query = query;
	}
}
