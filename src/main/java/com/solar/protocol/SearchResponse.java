package com.solar.protocol;

import org.apache.lucene.search.TopDocs;

public class SearchResponse extends SolarResponse {

    private TopDocs topDocs;

    public TopDocs getTopDocs() {
        return topDocs;
    }

    public void setTopDocs(TopDocs topDocs) {
        this.topDocs = topDocs;
    }

}
