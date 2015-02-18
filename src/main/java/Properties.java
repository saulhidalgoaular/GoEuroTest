import java.io.InputStream;

/**
 * Properties class.
 * 
 * It contains a basic mapping which is being read from a config file   
 */
public class Properties {
	public static Properties instance = new Properties();
	private java.util.Properties configProperties;

	private Properties(){}

	public static Properties getInstance(){
		return instance;
	}

	/**
	 * It loads the properties config file.
	 * @param fileName Path of the file
	 * @throws Exception It throws an exception when cannot read the file.
	 */
	public void loadProperties(String fileName) throws Exception {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

		if ( inputStream != null ){
			configProperties = new java.util.Properties();
			configProperties.load(inputStream);

			inputStream.close();
		}else{
			throw new Exception("Property file " + fileName + " not found");
		}
	}


	/**
	 * Returns the value to which the specified key is mapped,
	 * or null if this map contains no mapping for the key.
	 * @param name the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or
	 *         null if this map contains no mapping for the key
	 */
	public String getProperty(String name){
		return configProperties.getProperty(name);
	}
}
