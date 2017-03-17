package br.com.frfroes.srcheng.model;

/**
 * Class that contains the entity structure indexed by the application
 * @author frfroes
 */
public class Entity {
	
	private String title;
	private String type; 
	
	public Entity (){
		super();
	}
	public Entity(String title, String type) {
		super();
		this.title = title;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
	

}
