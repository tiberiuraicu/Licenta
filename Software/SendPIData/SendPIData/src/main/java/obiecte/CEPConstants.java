package obiecte;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class CEPConstants {
	static Properties prop = new Properties();
	public String EXCHANGE_NAME;
	public String EXCHANGE_TYPE;
	public String HOST;
	public String RABBITMQ_USERNAME;
	public String RABBITMQ_PASSWORD;
	public String ALARM_KEY;
	public String PROCESSOR_KEY;
	public String CONFIG_XML_PATH;
	public String SENSOR_DATA_CSV;

	public CEPConstants() {
		try {
			propLoader();

			this.EXCHANGE_NAME = prop.getProperty("EXCHANGE_NAME");
			this.EXCHANGE_TYPE = prop.getProperty("EXCHANGE_TYPE");
			this.HOST = prop.getProperty("HOST");
			this.RABBITMQ_USERNAME = prop.getProperty("RABBITMQ_USERNAME");
			this.RABBITMQ_PASSWORD = prop.getProperty("RABBITMQ_PASSWORD");
			this.ALARM_KEY = prop.getProperty("ALARM_KEY");
			this.PROCESSOR_KEY = prop.getProperty("PROCESSOR_KEY");
			this.CONFIG_XML_PATH = prop.getProperty("CONFIG_XML_PATH");
			this.SENSOR_DATA_CSV = prop.getProperty("SENSOR_DATA_CSV");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void propLoader() {
		try {
			FileReader reader = new FileReader(new File("D:\\licenta\\soft\\SendPIData\\SendPIData\\config.config"));
			prop.load(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
