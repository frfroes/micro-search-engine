package br.com.frfroes.srcheng.ws;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.frfroes.srcheng.bo.IndexingHandler;
import br.com.frfroes.srcheng.bo.SearchingHandler;
import br.com.frfroes.srcheng.model.Entity;
/**
 * Class wich is a Jersey WebService, it maintains the RESTful endpoints 
 * that the application uses to post for and get to the client.
 * It has a method who handles HTTP get requests from the client and returns a 
 * JSON as response. And it also has a method who handles HTTP post request 
 * with a JSON from the client.
 * @author frfroes
 */
@Path("entities")
public class EntityWS {
	
	/**
	 * Private Gson attribute variable object who converts POJOs to JSONs and vice-versa
	 */
	private Gson gson;
	
	/**
	 * Public EntityWS constructor who instantiate the gson attribute
	 */
	public EntityWS() {
		super();
		this.gson = new GsonBuilder().create();
	}
	/**
	 * Method responsible for handling the get request from the client.
	 * It takes the 'q' and the 'entity_type' parameters from the URL ,
	 * passes it to a SearchHandler, converts the search result to a JSON using 
	 * the gson attribute, and return it as a response.
	 * @param search - q parameter passed by the URL query
	 * @param type - entity_type parameter passed by the URL query 
	 * @return A converted Array of Entity in JSON String format
	 * @see SearchingHandler
	 * @see Entity
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String doGet(@QueryParam("q") String search, @QueryParam("entity_type") String type){
		List<Entity> searchResults = null;
		try {
			SearchingHandler srchHandler = new SearchingHandler();
			searchResults = srchHandler.searchEntities(search, type);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gson.toJson(searchResults);
	}
	
	/**
	 * Method responsible for handling the post request from the client.
	 * It takes a JSON String, converts it to a Entity object using 
	 * the gson attribute, and passes it to a IndexHandler. 
	 * It does not return anything.
	 * @param entityJson - JSON passed by the client post request
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void doPost(String entityJson){
		Entity newEntity = gson.fromJson(entityJson, Entity.class);	
		try {
			IndexingHandler idxHandler = new  IndexingHandler();
			idxHandler.indexEntity(newEntity);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
