package br.com.frfroes.srcheng.bo;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import br.com.frfroes.srcheng.dao.MemoryIndex;
import br.com.frfroes.srcheng.enumeration.FieldNamesEnum;
import br.com.frfroes.srcheng.model.Entity;
/**Class that handles indexing in the MemoryIndex Class.
 * Contains a method who thats an Entity object as a parameter,
 * creates a Lucene Document based on it and index this Document 
 * in the MemoryIndex.   
 * @author frfroes
 * @see MemoryIndex
 */
public class IndexingHandler {
	/**
	 * Private Lucene IndexWriter attribute who can index Lucene Documents in
	 * a Lucene Directory
	 */
	private IndexWriter indexWriter;
	/**
	 * Public IndexingHandler constructor who instantiate a Lucene StandardAnalyzer
	 * object, pass it as a constructor parameter for instantiate a Lucene 
	 * IndexWriteConfig object, and then passes the config alongside the Lucene Directory in the
	 * MemotyIndex index to instantiate the indexWriter.
	 * @throws IOException - may throw and IOEXpception
	 */
	public IndexingHandler() throws IOException {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		this.indexWriter = new IndexWriter(MemoryIndex.getIndex(), config);
	}
	/**
	 * A method who is responsible for taking an Entity and indexing its content 
	 * to the MemoryIndex index. It instantiate a Lucene Document and adds to it a Lucene
	 * TextFild object constructed with a FieldNameEnum.TITLE getName method, a Entity getTitle 
	 * method, and a Lucene YES constant who tells that the field should be stored. Then it does the 
	 * same thing only now with the FieldNameEnum.TYPE and the Entity getType. After it
	 * index the doc with the addDocument method of the indexWriter attribute and 
	 * calls the closeIndexWriter method.
	 * @param entity - Entity object parameter
	 * @throws IOException - may throw and IOEXpception
	 * @see Entity
	 * @see MemoryIndex
	 * @see FieldNamesEnum
	 */
	public void indexEntity(Entity entity) throws IOException {
		Document doc = new Document();
		doc.add(new TextField(FieldNamesEnum.TITLE.getName(), entity.getTitle(), Field.Store.YES));
		doc.add(new TextField(FieldNamesEnum.TYPE.getName(), entity.getType(), Field.Store.YES));
		indexWriter.addDocument(doc);
		
		closeIndexWriter();
	}
	/**
	 * Private method who calls the close method in the indexWriter attribute.
	 * @throws IOException
	 */
	private void closeIndexWriter() throws IOException {
		this.indexWriter.close();
	}

}
