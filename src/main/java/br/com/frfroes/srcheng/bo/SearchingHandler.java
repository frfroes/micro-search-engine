package br.com.frfroes.srcheng.bo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;

import br.com.frfroes.srcheng.dao.MemoryIndex;
import br.com.frfroes.srcheng.enumeration.FieldNamesEnum;
import br.com.frfroes.srcheng.model.Entity;

/**Class that handles searches in the MemoryIndex Class.
 * It contains the methods necessary to search in the MemoryIndex based on
 * a String search parameter alone, or a String search parameter combined with
 * a String type parameter.
 * @see MemoryIndex     
 * @author frfroes
 */
public class SearchingHandler {
	
	/**
	 * Private Lucene IndexSearcher attribute variable who searches 
	 * Lucene TopDocs in a Lucene Directory
	 * Private Lucene IndexReader attribute variable who reads 
	 * Lucene Documents in a Lucene Directory
	 */
	private IndexSearcher indexSearcher;
	private IndexReader indexReader;
	/**
	 * Private final int attribute variable who behaves as the number of TopDocs constant
	 */
	private final int NUM_HITS = 10;
	/**
	 * Public SearchingHandler constructor who instantiates the indexReader attribute
	 * passing a Lucene Directory from the MemoryIndex in the Lucene DirectoryReader open method.
	 * It also instantiate the indexSearcher with passer the indexReader as a 
	 * constructor parameter.
	 * @throws IOException - may throw and IOEXpception
	 * @see MemoryIndex
	 */
	public SearchingHandler() throws IOException{
		this.indexReader  = DirectoryReader.open(MemoryIndex.getIndex());
		this.indexSearcher = new IndexSearcher(indexReader);
	}
/**
 * This method is responsible for taking a String parameter and make it a a
 * Lucene interpretable Query.
 * A overloaded method who takes a search String along with the FieldsNameEnum.TITLE
 * and sets it as a constructor parameter for a Lucene Term, which is a parameter 
 * for a Lucene WildcardQuery constructor who is returned.  
 * @param search - A String search parameter
 * @return A Lucene WildcardQuery object instantiated by Lucene Term object.
 * @see FieldNamesEnum
 */
private Query setSearchQuery(String search){
	search = search.toLowerCase();
	return new WildcardQuery(
			new Term(FieldNamesEnum.TITLE.getName(),"*"+search+"*"));
}
/**
 * This method is responsible for taking two Strings parameter and make it a a
 * Lucene interpretable Query.
 * A overloaded method who takes a search String along with a FieldsNameEnum.TITLE
 * and sets it as a constructor parameter for a Lucene Term, which is a parameter 
 * for a Lucene WildcardQuery constructor who is passed to a Lucene Query Variable.
 * It aslo takes a takes a type String along with a FieldsNameEnum.TYPE
 * and sets it as a constructor parameter for a Lucene Term, which is a parameter 
 * for a Lucene TermQuery constructor who is passed to a Lucene Query Variable.
 * Then it instantiates a Lucene BooleanQuery and adds the searchQuery and the typeQuery
 * to it alongside a Lucene must constant. This operation combines the two previous queries
 * into a new query with a AND behavior between them. It return the new combined query.
 * @param search - A String search parameter
 * @param type - A String type parameter
 * @return A Lucene BooleanQuery wich is the AND combination of the queries
 * who contains the search and the type String parameter.
 * @see FieldNamesEnum
 */
private Query setSearchQuery(String search, String type){
	search = search.toLowerCase();
	Query searchQuery = new WildcardQuery(
			new Term(FieldNamesEnum.TITLE.getName(),"*"+search+"*"));
	type = type.toLowerCase();
	Query typeQuery = new TermQuery(
			new Term(FieldNamesEnum.TYPE.getName(),type));
	
	BooleanQuery.Builder resultQuery = new BooleanQuery.Builder();
	resultQuery.add(searchQuery,  BooleanClause.Occur.MUST);
	resultQuery.add(typeQuery,  BooleanClause.Occur.MUST);
	
	return resultQuery.build();
}
/**
 * A method who is responsible for taking a search and a type String parameter and search
 * for them in the MemoryIndex and convert the results in a Collection of Entity objects.
 * It creates a Lucene Query variable and instantiates it with the overloaded setSearchQuery 
 * method, based on if the type String parameter is empty or not. Then it uses the indexSearcher
 * attribute search method, passing the searchQuery and at the NUM_HITS constant, and pass the 
 * results to a Lucene TopDocs variable. The docs scoreDocs attribute is passed to a Lucene 
 * ScoreDoc array. A new List of Entity is instantiated by a ArrayList. Then a for-each loop
 * runs the hits array and for each of the arrays elements it does the fallowing:
 * Uses the indexSearcher doc method to search for the Lucene Document based on the doc attribute
 * of each element, and store the found document in a new Lucene Document variable. Still in the
 * loop it takes the founded Lucene Document object and uses its Lucene Fields to instantiate 
 * a Entity object who is added to the searchResult ArrayList.
 * When the loop is done, the closeIndexReader method is called and the searchResult is returned.
 * @param search - A String search parameter
 * @param type - A String type parameter
 * @return A ArrayList of Entity instantiated based on the found Lucene Documents.
 * @throws IOException - may throw and IOEXpception
 * @see MemoryIndex
 * @see Entity
 * @see FieldNamesEnum
 */
public List<Entity> searchEntities(String search, String type) throws IOException{
	Query searchQuery;
	if(type == null) searchQuery = setSearchQuery(search);
	else searchQuery = setSearchQuery(search, type);
	
	TopDocs docs = indexSearcher.search(searchQuery,NUM_HITS);
	ScoreDoc[] hits = docs.scoreDocs;
	
	List<Entity> searchResult = new ArrayList<>();
	for (ScoreDoc hit : hits) {
		Document doc = indexSearcher.doc(hit.doc);
		searchResult.add(new Entity(doc.get(
						FieldNamesEnum.TITLE.getName()),doc.get(FieldNamesEnum.TYPE.getName())));	
	}
	
	closeIndexReader();
	return searchResult;
}
/**
 * Private method who calls the close method in the indexReader attribute.
 * @throws IOException - may throw and IOEXpception
 */
private void closeIndexReader() throws IOException{
	this.indexReader.close();
}
	

}
