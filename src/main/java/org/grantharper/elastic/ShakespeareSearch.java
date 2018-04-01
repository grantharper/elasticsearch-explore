package org.grantharper.elastic;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ShakespeareSearch {

	private static final Logger logger = LogManager.getLogger(ShakespeareSearch.class);

	private static final String INDEX = "shakespeare";

	RestHighLevelClient client;

	private void openTransportClient() {
		logger.info("Opening rest client");

		client = new RestHighLevelClient(
				RestClient.builder(new HttpHost("localhost", 9200, "http"), new HttpHost("localhost", 9201, "http")));
		System.out.println("Opened rest client");
	}

	public void search() {
		try {

			openTransportClient();
			performOverallSearch();
			performSpecificSearch();

		} finally {
			if (client != null) {
				logger.info("Closing rest client");
				try {
					client.close();
				} catch (IOException e) {
					logger.error("Error closing rest client", e);
				}
			}
		}
	}
	
	SearchResponse performSearch(SearchRequest searchRequest) {
		SearchResponse response;
		try {
			response = client.search(searchRequest);
			return response;
		} catch (IOException e) {
			logger.error("search error", e);
			return null;
		}
		
	}

	void performOverallSearch() {
		SearchRequest searchRequest = new SearchRequest(INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);

		SearchResponse response = performSearch(searchRequest);
		logger.info("overall search result= " + response);
	}

	void performSpecificSearch() {
		
		SearchRequest searchRequest = new SearchRequest(INDEX);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("play_name", "Hamlet"))
						.must(QueryBuilders.termQuery("speaker", "HAMLET")));
		SearchResponse searchResponse = performSearch(searchRequest);
		SearchHits hits = searchResponse.getHits();

		logger.info("specific search result");
		logger.info("hit count=" + hits.getTotalHits());

		for (SearchHit hit : hits.getHits()) {
			String source = hit.getSourceAsString();
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			String speechLine = (String) sourceAsMap.get("text_entry");

			logger.info(speechLine);
		}

	}

}