package br.com.frfroes.srcheng.dao;

import javax.inject.Singleton;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**Class responsible for store the the indexed Lucene Documents and in a Lucene
 * static Directory and make them available for searching.
 * It is also a Singleton that will persist in the memory as long as the 
 * application is running.     
 * @author frfroes
 */
@Singleton
public class MemoryIndex {
	/**
	 * Static private Lucene Directory attribute witch Lucene Documents can be
	 * indexed into and searched from. It is instantiated with a Lucene RAMDirecory object.
	 */
	static private Directory index = new RAMDirectory();
	
	/**
	 * A getter for the index attribute
	 * @return the index attribute
	 */
	public static Directory getIndex(){
		return index;
	}

}
