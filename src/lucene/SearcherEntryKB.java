
package lucene;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import bean.EntryKB;
import util.ReaderConfigFile;

public class SearcherEntryKB {


	private final float SCORE_THRESHOLD = 0.5f;
	private final String[] fields = new String[2];
	private String indexPath;
	private  Analyzer analyzer;
	private  IndexReader reader;
	private  IndexSearcher searcher;
	private  MultiFieldQueryParser parser;
	private ReaderConfigFile readerFileConfig;


	public SearcherEntryKB(ReaderConfigFile readerFileConfig) throws IOException {
		indexPath = readerFileConfig.getValueFor("index_KB");
		reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
		searcher = new IndexSearcher(reader);
		analyzer = new KeywordAnalyzer();
		fields[0] = "mid1";
		fields[1] = "mid2";
		parser = new MultiFieldQueryParser(Version.LUCENE_47, fields, analyzer);

	}

	public SearcherEntryKB() throws IOException {
		indexPath = "/home/chris88/Documenti/Tesi/componenti/indexes/index_KB";
		reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
		searcher = new IndexSearcher(reader);
		analyzer = new KeywordAnalyzer();
		fields[0] = "mid1";
		fields[1] = "mid2";
		parser = new MultiFieldQueryParser(Version.LUCENE_47, fields, analyzer);
	}



	public synchronized List<String> findRelationsBetween(String mid1,String mid2) throws IOException, UnsupportedEncodingException {
		int hitsPerPage = 5;

		Query query1 = new TermQuery(new Term("mid1", mid1));
		Query query2 = new TermQuery(new Term("mid2",mid2));

		BooleanQuery bq = new BooleanQuery();
		bq.add(query1, Occur.MUST);
		bq.add(query2, Occur.MUST);


		List<String> relationsFound = new ArrayList<>();

		TopDocs results = searcher.search(bq, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;


		for(int i=0;i<hits.length;++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			relationsFound.add(d.get("predicate"));

		}

		return relationsFound;
	}


	public synchronized List<String> findTypes1For(String mid1) throws IOException, UnsupportedEncodingException {
		int hitsPerPage = 5;

		Query query = new TermQuery(new Term("mid1", mid1));

		BooleanQuery bq = new BooleanQuery();
		bq.add(query, Occur.MUST);


		List<String> typesFound = new ArrayList<>();

		TopDocs results = searcher.search(bq, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;


		if(hits.length > 0) {
			Document d = searcher.doc(hits[0].doc);
			typesFound = Arrays.asList(d.get("types1").split(","));

		}

		return typesFound;
	}

	public synchronized List<String> findTypes2For(String mid2) throws IOException, UnsupportedEncodingException {
		int hitsPerPage = 5;

		Query query = new TermQuery(new Term("mid2", mid2));

		BooleanQuery bq = new BooleanQuery();
		bq.add(query, Occur.MUST);


		List<String> typesFound = new ArrayList<>();

		TopDocs results = searcher.search(bq, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;


		if(hits.length > 0) {
			Document d = searcher.doc(hits[0].doc);
			typesFound = Arrays.asList(d.get("types2").split(","));

		}

		return typesFound;
	}


	private static Predicate<String> filterTypes(){
		return p -> p.startsWith("m.");
	}

}


