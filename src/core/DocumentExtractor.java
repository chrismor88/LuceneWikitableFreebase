package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import bean.WikipediaTable;
import core.Message;
import util.NormalizerTableRows;

public class DocumentExtractor extends Thread{

	private String pathContentFile;

	private BlockingQueue<WikipediaTable> bufferTables;
	private BlockingQueue<String> responseBufferConsumers;


	public DocumentExtractor(String pathInputFile, BlockingQueue<WikipediaTable> bufferTables, BlockingQueue<String> responseBufferConsumers) {
		this.pathContentFile = pathInputFile;
		this.bufferTables = bufferTables;
		this.responseBufferConsumers = responseBufferConsumers;
	}

	@Override
	public void run() {
		super.run();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(pathContentFile)));
			String currentLine = "";
			WikipediaTable wikiTable = null;
			while((currentLine = reader.readLine())!=null){
				if(currentLine.startsWith("<doc")){
					String[] fieldsTag = currentLine.split("\t");
					int numberOfSignificantColumns = Integer.parseInt(fieldsTag[6].replaceAll("num_significant_columns=",""));
					if(numberOfSignificantColumns<=4){
						
						wikiTable = new WikipediaTable();
						wikiTable.setIdTable(fieldsTag[1].replace("idPage=","")+"-"+fieldsTag[2].replace("idTable=",""));
						wikiTable.setWikid(fieldsTag[3].replaceAll("wikid=", ""));
						wikiTable.setNumberOriginalRows(Integer.parseInt(fieldsTag[4].replaceAll("num_original_row=","")));
						wikiTable.setNumberOriginalColumns(Integer.parseInt(fieldsTag[5].replaceAll("num_original_col=","")));
						wikiTable.setNumberSignificantColumns(numberOfSignificantColumns);
						wikiTable.setRowsWithMultipleMentions(Integer.parseInt(fieldsTag[7].replaceAll("nrows_with_multiple_mentions=", "")));

					}
				}
				else{
					List<String> tableRows = new LinkedList<String>();
					while(!currentLine.equals("</doc>") && currentLine!=null){
						tableRows.addAll(NormalizerTableRows.normalizesTableRows(currentLine));
						currentLine = reader.readLine();
					}
					bufferTables.put(wikiTable);
					wikiTable.addRow(currentLine);
				}
			}
			wikiTable = new WikipediaTable();
			wikiTable.setIdTable(Message.FINISHED_PRODUCER);
			bufferTables.put(wikiTable);

			int counterCompletedConsumers = 0;
			int number_of_threads = Runtime.getRuntime().availableProcessors();
			while(counterCompletedConsumers <  number_of_threads){
				String message = responseBufferConsumers.take();
				if(message.equals(Message.FINISHED_CONSUMER))
					counterCompletedConsumers++;
			}
			
			this.interrupt();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
