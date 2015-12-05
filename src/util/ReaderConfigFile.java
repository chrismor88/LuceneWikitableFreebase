package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class ReaderConfigFile {
	
	private final String propFileName = "config.properties";
	private final Map<String,String> keyToValue;
	
	public ReaderConfigFile() throws FileNotFoundException {
		keyToValue = new HashMap<String,String>();
		populateMap();
	}
	
	
	private void populateMap() throws FileNotFoundException {
		InputStream inputStream = null;
		Properties prop = new Properties();
		
		inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
		if (inputStream != null) {
			try {
				prop.load(inputStream);
				
				for(Entry<Object, Object> entry : prop.entrySet()){
					keyToValue.put((String)entry.getKey(),(String)entry.getValue());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
		
	}


	public synchronized String getValueFor(String key){
		String result = "";
		if(keyToValue.containsKey(key))
			result = keyToValue.get(key);
		return result;
	}
	
	
	public String getContentFile(){
		InputStream inputStream = null;

		Properties prop = new Properties();

		try {
//			inputStream = new FileInputStream(new File(propFileName));
			inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		String content_file = prop.getProperty("content_file_input");


		return content_file;
	}

	
	public String getPathStatisticsFile(){
		InputStream inputStream = null;

		Properties prop = new Properties();

		try {
//			inputStream = new FileInputStream(new File(propFileName));
			inputStream = this.getClass().getClassLoader().getResourceAsStream(propFileName);
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		String statistics_file = prop.getProperty("output_file_statistics");


		return statistics_file;
	}


	public String getIndexKBPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
