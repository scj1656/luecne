package com.solar.handler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.solar.annotation.SolarHandler;
import com.solar.common.config.AnalyzerFactory;
import com.solar.common.config.SolarConfig;
import com.solar.common.constants.Relation;
import com.solar.lucene.LuceneSearch;
import com.solar.lucene.SolarSearch;
import com.solar.model.ByteValue.ValueType;
import com.solar.protocol.SearchRequest;
import com.solar.protocol.SearchResponse;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;

@SolarHandler
public class SearchHandler extends RequestHandler {

    private static final Logger logger         = LoggerFactory.getLogger(SearchHandler.class);

    private static final String SEARCH_NANDLER = "search";

    private SolarSearch         solarSearch;

    public String getName() {
        return SEARCH_NANDLER;
    }

    @Override
    public void handler(SolarRequest solarRequest, SolarResponse solarResponse) {
        SearchRequest searchRequest = (SearchRequest) solarRequest;
        Directory directory = null;
        try {
            directory = FSDirectory.open(new File(SolarConfig.DEFAULT.getIndexDir()));
        } catch (IOException e) {
            logger.error("", e);
        }
        solarSearch = new LuceneSearch(directory);
        TopDocs docs = solarSearch.search(buildQuery(searchRequest), 10000);
        ScoreDoc[] scoreDoces = docs.scoreDocs;
        List<ScoreDoc> scoreDocsTList = new ArrayList<ScoreDoc>();
        for (ScoreDoc scoreDoc : scoreDoces) {
            scoreDocsTList.add(scoreDoc);
        }

        docs = new TopDocs(scoreDocsTList.size(), scoreDocsTList.toArray(new ScoreDoc[] {}),
            docs.getMaxScore());
        SearchResponse searchResponse = (SearchResponse) solarResponse;
        searchResponse.setTopDocs(docs);
    }

    private Query buildQuery(SearchRequest searchRequest) {
        BooleanQuery booleanQuery = new BooleanQuery();
        Analyzer analyzer = AnalyzerFactory.getInstance()
            .getAnalyzer(searchRequest.getAnalyzerName());
        Map<String, String> queryMap = searchRequest.getQuery();
        for (Entry<String, String> entry : queryMap.entrySet()) {
            String value = entry.getValue();
            Relation relation = Relation.valueOf(value);
            Occur occur = null;
            switch (relation) {
                case MUST:
                    occur = Occur.MUST;
                    break;
                case MUST_NOT:
                    occur = Occur.MUST_NOT;
                    break;
                case SHOULD:
                    occur = Occur.SHOULD;
                    break;
                default:
                    break;
            }

            String fieldQuery = entry.getKey();
            String[] queryParams = fieldQuery.split(":");
            if (queryParams.length != 2) {
                //报错
            }

            String field = queryParams[0];
            String fieldValue = queryParams[1];
            boolean isRange = false;

            if (fieldValue.startsWith("[") && fieldValue.endsWith("]")) {
                fieldValue = fieldValue.substring(1, fieldValue.length() - 1);
                isRange = true;
            }

            Query query = null;

            if (isRange) {
                String[] values = field.split("-");
                String min = values[0];
                String max = values[1];
                ValueType valueType = getValueType(min);
                switch (valueType) {
                    case DOUBLE:
                        query = NumericRangeQuery.newDoubleRange(field, Double.parseDouble(min),
                            Double.parseDouble(max), true, true);
                        break;
                    case LONG:
                        query = NumericRangeQuery.newLongRange(field, Long.parseLong(min),
                            Long.parseLong(max), true, true);
                        break;
                    default:
                        break;
                }
            } else {
                ValueType valueType = getValueType(fieldValue);
                switch (valueType) {
                    case DOUBLE:
                        query = NumericRangeQuery.newDoubleRange(field,
                            Double.parseDouble(fieldValue), Double.parseDouble(fieldValue), true,
                            true);
                        break;
                    case LONG:
                        query = NumericRangeQuery.newLongRange(field, Long.parseLong(fieldValue),
                            Long.parseLong(fieldValue), true, true);
                        break;
                    case STRING:
                        if (field.contains(",")) {
                            query = buildStringQuery(field.split(","), fieldValue, analyzer);
                        } else {
                            query = buildStringQuery(field, fieldValue, analyzer);
                        }
                        break;
                    default:
                        break;
                }
            }
            booleanQuery.add(query, occur);
        }
        return booleanQuery;
    }

    /**
     * 默认都分词
     * @author xuzhu
     * 2016年8月3日 下午5:03:36
     * @param field
     * @param fieldValue
     * @param analyzer
     * @return
     */
    private Query buildStringQuery(String field, String fieldValue, Analyzer analyzer) {
        Query query = null;
        QueryParser queryParser = new AnalyzingQueryParser(Version.LUCENE_46, field, analyzer);
        queryParser.enable_tracing();
        queryParser.setDefaultOperator(QueryParser.Operator.OR);
        try {
            query = queryParser.parse(fieldValue);
        } catch (ParseException e) {
            logger.error("", e);
        }
        return query;
    }

    private Query buildStringQuery(String[] fields, String fieldValue, Analyzer analyzer) {
        Query query = null;

        String[] values = new String[fields.length];
        Arrays.fill(values, fieldValue);
        Occur[] occurs = new Occur[fields.length];
        Arrays.fill(occurs, Occur.SHOULD);
        try {
            query = MultiFieldQueryParser.parse(Version.LUCENE_46, values, fields, analyzer);
        } catch (ParseException e) {
            logger.error("", e);
        }
        return query;
    }

    private static ValueType getValueType(String value) {
        ValueType valueType = ValueType.STRING;
        Pattern decimalPattern = Pattern.compile("^\\d+\\.\\d+$");
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher isDoubleNum = decimalPattern.matcher(value);
        Matcher isLongNum = pattern.matcher(value);
        if (isDoubleNum.matches()) {
            valueType = ValueType.DOUBLE;
        } else if (isLongNum.matches()) {
            valueType = ValueType.LONG;
        }
        return valueType;
    }

    public static void main(String[] args) {
        ValueType value = SearchHandler.getValueType("sdcsdc");
        System.out.println(value.name());
    }

}
