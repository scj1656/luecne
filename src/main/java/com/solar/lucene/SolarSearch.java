package com.solar.lucene;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

public interface SolarSearch {

    TopDocs search(Query query, int maxHitsPerSearch);

    TopDocs search(Query query, Filter filter, int maxHitsPerSearch);
}
