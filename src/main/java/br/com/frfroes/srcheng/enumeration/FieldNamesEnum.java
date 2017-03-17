package br.com.frfroes.srcheng.enumeration;
/**
 * Enum who has the names witch the application uses for creates its
 * Lucene Fields.
 * @author frfroes
 */
public enum FieldNamesEnum {
	TITLE("title"), TYPE("type");
	
	private String name;
	
	FieldNamesEnum(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
