package lucene;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


import util.ReaderConfigFile;
import util.CorrectorEntity;



public class SearcherRedirectToWikid {
	
	private static final String Field = "redirect";
	private String indexPath;
	private Analyzer analyzer;
	private IndexReader reader;
	private IndexSearcher searcher;
	private QueryParser parser;



	public SearcherRedirectToWikid(ReaderConfigFile readerConfigFile) throws IOException {
		indexPath = readerConfigFile.getValueFor("index_redirect");
		reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
		searcher = new IndexSearcher(reader);
		analyzer = new KeywordAnalyzer();
		parser = new QueryParser(Version.LUCENE_47,Field, analyzer);
	}
	

	public synchronized String findWikidFor(String redirect) throws IOException, UnsupportedEncodingException {
		
		int maxHits = 5;

		String wikid = null;

		try {
			CorrectorEntity corrector = new CorrectorEntity();
			
			redirect = corrector.correctSpecialCharacters(redirect);
			
			Query query = parser.parse(redirect);

			TopDocs results = searcher.search(query, 5 * maxHits);
			ScoreDoc[] hits = results.scoreDocs;

			if(hits.length > 0) {
				int docId = hits[0].doc;
				Document d = searcher.doc(docId);
				wikid = d.get("wikid");
			}

		} catch (ParseException e) {
			System.err.println("Incorrect Query in SearcherWikiRedirect for string: "+redirect);
		}
		
		return wikid;
	}

}
