package core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import bean.StatisticalKB_Bean;
import bean.WikipediaTable;
import statisticsKB.StatisticalAnalyzerWikitable;
import statisticsKB.WriterStatisticalTable;


public class Consumer extends Thread {

	private BlockingQueue<WikipediaTable> tablesBuffer; //buffer in cui vengono trasmessi e prelevati le stringhe di tipo json
	private BlockingQueue<String> outputBuffer; //buffer per comunicare al produttore la terminazione dei consumatori
	private StatisticalAnalyzerWikitable statisticalAnalyzer;
	private WriterStatisticalTable writerStatisticalTable;

	

	public Consumer(BlockingQueue<WikipediaTable> bufferTables, BlockingQueue<String> responseBufferConsumers,
			WriterStatisticalTable writerStatisticalTable, StatisticalAnalyzerWikitable analyzerWikitable) {
		this.tablesBuffer = bufferTables;
		this.outputBuffer = responseBufferConsumers;
		this.statisticalAnalyzer = analyzerWikitable;
		this.writerStatisticalTable = writerStatisticalTable;
	}


	@Override
	public void run() {
		super.run();

		while(true){
			try {
				WikipediaTable wikiTable = tablesBuffer.take();
				if(!wikiTable.getIdTable().equals(Message.FINISHED_PRODUCER)){
					StatisticalKB_Bean tableStatisticalResult = this.statisticalAnalyzer.analyzeThisWikitable(wikiTable);
					this.writerStatisticalTable.writeStatisticsAbout(wikiTable, tableStatisticalResult);
				}
				else{
					tablesBuffer.put(wikiTable);
					outputBuffer.put(Message.FINISHED_CONSUMER);
					break;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.interrupt();
	}


}
