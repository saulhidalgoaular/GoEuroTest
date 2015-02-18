import java.util.HashMap;

/**
 * Constants class.
 * 
 * It must be used for values that are not going to change
 * very often. Use the config.properties file for rest of the
 * values
 * 
 * It is a classic singleton implementation. 
 */
public class Constants {
	// Singleton
	private static Constants instance = new Constants();

	private Constants(){
		mapper = new HashMap<String, String>();
		mapper.put("configFile", "config.properties");
	}

	public static Constants getInstance(){
		return instance;
	}

	// We are saving the configurations in a HashMap.
	private HashMap<String, String> mapper;

	/**
	 * Returns the value to which the specified key is mapped,
	 * or null if this map contains no mapping for the key.
	 * @param key the key whose associated value is to be returned
	 * @return the value to which the specified key is mapped, or
	 *         null if this map contains no mapping for the key
	 */
	public String getValue(String key){
		return instance.mapper.get(key);
	}

}
