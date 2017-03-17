package br.frfroes.srcheng.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import br.com.frfroes.srcheng.bo.IndexingHandler;
import br.com.frfroes.srcheng.bo.SearchingHandler;
import br.com.frfroes.srcheng.model.Entity;

public class EgnineTests {
	
	SearchingHandler searchHandler;
	@Before
	public void setup() throws IOException{
		IndexingHandler indexingHandler = new IndexingHandler();
		Entity entity = new Entity("Title", "Type");
		indexingHandler.indexEntity(entity);
		searchHandler = new SearchingHandler();
	}
	@Test
	public void isReturnigArrayWithExpectedSizeOneParameter() throws IOException {
		assertEquals(3 , searchHandler.searchEntities("Title",null).size());
	}
	@Test
	public void isReturnigArrayWithExpectedSizeTwoParameters() throws IOException {
		assertEquals(1 , searchHandler.searchEntities("Title","Type").size());
	}
	@Test
	public void isReturnigExpectedObjectDataTwoParameters() throws IOException {
		Entity expectedEntity = new Entity("Title","Type");
		Entity actualEntity = searchHandler.searchEntities("Title","Type").get(0);
		assertEquals(expectedEntity.getTitle() ,actualEntity.getTitle());
		assertEquals(expectedEntity.getType() ,actualEntity.getType());
	}
	@Test
	public void isReturnigExpectedObjectDataOneParameters() throws IOException {
		Entity expectedEntity = new Entity("Title","Type");
		Entity actualEntity = searchHandler.searchEntities("Title","Type").get(0);
		assertEquals(expectedEntity.getTitle() ,actualEntity.getTitle());
		assertEquals(expectedEntity.getType() ,actualEntity.getType());
	}


}
