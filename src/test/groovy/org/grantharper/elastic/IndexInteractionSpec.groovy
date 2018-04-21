package org.grantharper.elastic

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import spock.lang.Specification

class IndexInteractionSpec extends Specification
{

  RestHighLevelClient elasticsearchClient;

  def setup()
  {
    elasticsearchClient = new RestHighLevelClient(
            RestClient.builder(new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));
  }

  def cleanup()
  {
    elasticsearchClient.close()
  }

  def "create an elasticsearch index"()
  {
    given: "dataload instantiation"
    IndexInteraction dataLoad = new IndexInteraction()
    dataLoad.setElasticClient(elasticsearchClient)

    when: "index is created"
    dataLoad.createAndDeleteIndex()

    then: "something"
    true == true
  }

}