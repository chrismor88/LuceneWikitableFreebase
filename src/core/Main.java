package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import util.ReaderAccessFile;
import util.ReaderConfigFile;
import bean.WikipediaTable;
import statisticsKB.StatisticalAnalyzerWikitable;
import statisticsKB.WriterStatisticalTable;


public class Main {


	public static void main(String[] args) throws FileNotFoundException {
		
		
		ReaderConfigFile readerFileConfig = new ReaderConfigFile();
		
		BlockingQueue<WikipediaTable> bufferTables = new LinkedBlockingQueue<>(1000);
		BlockingQueue<String> responseBufferConsumers = new LinkedBlockingQueue<>(4);

		try {
			String pathFileStatistics = readerFileConfig.getPathStatisticsFile();
			String contentFilePath = readerFileConfig.getContentFile();
			File timestamp = new File(readerFileConfig.getValueFor("file_timestamp"));
			FileWriter writerTimestamp = new FileWriter(timestamp);
			
			
			BufferedWriter fileWriterStatistics = new BufferedWriter(new FileWriter(new File(pathFileStatistics),true));
			String headerFileStatistics ="\t\t\t\t\t\t\t\t\t\t\t\t1ST PAIRS OF COLUMNS\t\t\t2ND PAIRS OF COLUMNS\t\t\t3RD PAIRS OF COLUMNS\n" 
					+ "ID\tWIKID\tURL\tORIGINAL ROWS\tORIGINAL COLUMNS\tEFFECTIVE ROWS\tSIGNIFICANT COLUMNS\tINDEX COLUMN KEY\t"
					+ "MID FOUND ON COLUMN 1\tMID FOUND ON COLUMN 2\tMID FOUND ON COLUMN 3\tMID FOUND ON COLUMN 4\t"
					+ "PAIRS OF ENTITIES\tRELATIONS FOUND\tNUMBER OF PAIRS WITH AT LEAST ONE RELATION\tAVERAGE NUMBER OF RELATIONS\t"
					+ "PAIRS OF ENTITIES\tRELATIONS FOUND\tNUMBER OF PAIRS WITH AT LEAST ONE RELATION\tAVERAGE NUMBER OF RELATIONS\t"
					+ "PAIRS OF ENTITIES\tRELATIONS FOUND\tNUMBER OF PAIRS WITH AT LEAST ONE RELATION\tAVERAGE NUMBER OF RELATIONS\t"
					+ "NUM. ROWS WITH MULTIPLE MENTIONS\n";
			fileWriterStatistics.write(headerFileStatistics);
			DocumentExtractor docExtractor = new DocumentExtractor(contentFilePath, bufferTables,responseBufferConsumers);
			
			
			StatisticalAnalyzerWikitable searcherForRel = new StatisticalAnalyzerWikitable(readerFileConfig);
			WriterStatisticalTable statisticalTable = new WriterStatisticalTable(fileWriterStatistics);

			int cores = Runtime.getRuntime().availableProcessors();
			
			Consumer[] consumers = new Consumer[cores];
			docExtractor.start();
			
			Date start = new Date();

			for(Consumer consumer : consumers){
				consumer = new Consumer(bufferTables, responseBufferConsumers, statisticalTable, searcherForRel);
				consumer.start();
			}


			docExtractor.join();
			
			Date end = new Date();
			double totalTimeInMilliSecs = ((double)end.getTime() - (double)end.getTime());
			double totalTime = totalTimeInMilliSecs / 1000;
			
			writerTimestamp.write("Start at: "+start.toString()+"\n");
			writerTimestamp.write("End at: "+end.toString()+"\n");
			writerTimestamp.write("Total in milli secs: "+totalTimeInMilliSecs);
			writerTimestamp.write("Total in secs: "+totalTime);
			
			System.out.println("Start at: "+start.toString());
			System.out.println("End at: "+end.toString());
			System.out.println("Total in milli secs: "+totalTimeInMilliSecs);
			System.out.println("Total in secs: "+totalTime);

			writerTimestamp.close();
			
			synchronized (fileWriterStatistics) {
				fileWriterStatistics.flush();
				fileWriterStatistics.close();
			}

		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

}
