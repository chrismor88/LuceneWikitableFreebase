package lucene;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import util.ReaderConfigFile;
import util.CorrectorEntity;


public class SearcherWikidToMid {

	private String indexPath;
	private final String Field = "title";
	private Analyzer analyzer;
	private IndexReader reader;
	private IndexSearcher searcher;
	private QueryParser parser;

	public SearcherWikidToMid(ReaderConfigFile readerFileConfig) throws IOException {
		indexPath = readerFileConfig.getValueFor("index_mapping");
		reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
		searcher = new IndexSearcher(reader);
		analyzer = new KeywordAnalyzer();
		parser = new QueryParser(Version.LUCENE_47, Field, analyzer);
	}


	public synchronized String findMidFor(String wikid) throws IOException, UnsupportedEncodingException {

		int maxHits = 3;
		Document d = null;
		String mid = null;


		try {
			CorrectorEntity corrector = new CorrectorEntity();
			
			wikid = corrector.correctSpecialCharacters(wikid);

			Query query = parser.parse(wikid);

			TopDocs results = searcher.search(query, 5 * maxHits);
			ScoreDoc[] hits = results.scoreDocs;

			if(hits.length > 0){
				int docId = hits[0].doc;
				d = searcher.doc(docId);
				mid = d.get("mid");
			}

		} catch (ParseException e) {
			System.err.println("Incorrect Query in SearcherWikidToMid for string: "+wikid);
		}

		return mid;
	}

}