package org.grantharper;

import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.grantharper.elastic.IndexInteraction;
import org.grantharper.elastic.ShakespeareSearch;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan
public class App {

  private static final Logger logger = LogManager.getLogger(App.class);

	public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
		ShakespeareSearch shakespeareSearch = context.getBean(ShakespeareSearch.class);
		shakespeareSearch.search();
    IndexInteraction indexInteraction = context.getBean(IndexInteraction.class);
    indexInteraction.createAndDeleteIndex();
		RestHighLevelClient client = context.getBean(RestHighLevelClient.class);
    if (client != null) {
      logger.info("Closing rest client");
      try {
        client.close();
      } catch (IOException e) {
        logger.error("Error closing rest client", e);
      }
    }
	}

	@Bean
  RestHighLevelClient restHighLevelClient(){
    return new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));
  }

}
