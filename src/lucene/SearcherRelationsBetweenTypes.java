package lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
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

public class SearcherRelationsBetweenTypes {

	private final float SCORE_THRESHOLD = 0.5f;
	private final String[] fields = new String[2];
	private String indexPath;
	private  Analyzer analyzer;
	private  IndexReader reader;
	private  IndexSearcher searcher;
	private  MultiFieldQueryParser parser;
	private ReaderConfigFile readerFileConfig;


	public SearcherRelationsBetweenTypes(ReaderConfigFile readerFileConfig) throws IOException {
		indexPath = readerFileConfig.getValueFor("index_relations_between_types");
		reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
		searcher = new IndexSearcher(reader);
		analyzer = new KeywordAnalyzer();
		fields[0] = "types1";
		fields[1] = "types2";
		parser = new MultiFieldQueryParser(Version.LUCENE_47, fields, analyzer);

	}


	public Set<String> findRelationsBetweenTheseTypes(List<String> types1,List<String> types2) throws IOException, UnsupportedEncodingException {
		int hitsPerPage = 5;
		Set<String> relationsFound = new LinkedHashSet<>();

		for(String t1 : types1){
			for(String t2 : types2){
				Query query1 = new TermQuery(new Term("type1", t1));
				Query query2 = new TermQuery(new Term("type2",t2));

				BooleanQuery bq = new BooleanQuery();
				bq.add(query1, Occur.MUST);
				bq.add(query2, Occur.MUST);

				TopDocs results = searcher.search(bq, 5 * hitsPerPage);
				ScoreDoc[] hits = results.scoreDocs;


				for(int i=0;i<hits.length;++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					relationsFound.add(d.get("predicate"));

				}
			}
		}

		return relationsFound;
	}

/*
	public static void main(String[] args) {
		List<String> types1 = new ArrayList<>();
		List<String> types2 = new ArrayList<>();


		types1.add("music.recording");
		types1.add("music.single");

		types2.add("music.group_member");
		types2.add("film.director");
		types2.add("film.music_contributor");
		types2.add("film.person_or_entity_appearing_in_film");

		ReaderConfigFile readerFileConfig;
		try {
			readerFileConfig = new ReaderConfigFile();
			SearcherRelationsBetweenTypes searcher = new SearcherRelationsBetweenTypes(readerFileConfig);
			Set<String> relationsBetweenTypes = searcher.findRelationsBetweenTheseTypes(types1, types2);
			for(String rel : relationsBetweenTypes)
				System.out.println(rel);		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
*/
	
	
}
