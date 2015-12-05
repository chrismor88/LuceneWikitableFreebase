package util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class ReaderAccessFile {

	private Map<String,String> properties;
	
	public ReaderAccessFile(String accessFile) {
		Properties prop = new Properties();
		FileReader fileReader;
		try {
			fileReader = new FileReader(accessFile);
			prop.load(fileReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		properties.put("accessKey",prop.getProperty("accessKey"));
		properties.put("secretKey",prop.getProperty("secretKey"));
	}
	
	
	public String getAccessKey(){
		return properties.get("accessKey");		
	}
	
	public String getSecretKey(){
		return properties.get("secretKey");		
	}
}
