package org.grantharper.elastic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IndexInteraction
{
  private static final Logger logger = LogManager.getLogger(IndexInteraction.class);

  private RestHighLevelClient elasticClient;

  @Autowired
  public IndexInteraction(RestHighLevelClient restHighLevelClient){
    this.elasticClient = restHighLevelClient;
  }

  public void createAndDeleteIndex(){
    try {

      CreateIndexRequest createIndexRequest = new CreateIndexRequest("test");
      CreateIndexResponse createIndexResponse = elasticClient.indices().create(createIndexRequest);
      boolean acknowledged = createIndexResponse.isAcknowledged();
      if (acknowledged) logger.info("index created");
      else logger.error("index creation failed");

      logger.info("Index exists: deleting");
      DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("test");
      DeleteIndexResponse deleteIndexResponse = elasticClient.indices().delete(deleteIndexRequest);
      if(deleteIndexResponse.isAcknowledged()) logger.info("index deleted");
      else logger.error("index deletion failed");

    } catch (IOException e) {
      logger.error("error manipulating index", e);
    }
  }
}
