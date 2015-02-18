import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Main Class.
 * 
 * This is the main class of the solution for the test.
 * It only reads a http-get request and create a csv file.
 *
 * Saul Hidalgo
 * saulhidalgoaular@gmail.com
 * Software Engineer
 * Los Teques
 * Venezuela
 */
public class Main {
	public static void main(String[] args) {
		if ( args.length != 1 ){
			help();
			return;
		}

		try {
			// We load the configuration needed for all the software.
			Properties.getInstance().loadProperties(
				Constants.getInstance().getValue("configFile")
			);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		
		try {
			// At this point, we are ready to read the json.
			String url = Properties.getInstance().getProperty("url") + args[0];

			// We create the http objects needed...
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			
			HttpResponse response = httpClient.execute(get);
			
			if ( response.getStatusLine().getStatusCode() != 200 ){
				throw new IOException("Status Code was not OK");
			}

			String httpAnswer = EntityUtils.toString(response.getEntity(), "UTF-8");

			ObjectMapper objectMapper = new ObjectMapper();
			List jsonMapped;
			ArrayList<String[]> csvInfo = new ArrayList<String[]>();
			
			try {
				// Lets parse the json received to a List.
				jsonMapped = objectMapper.readValue(httpAnswer, List.class);

				// Now we extract the data needed for the csv file.
				for (Object currentO : jsonMapped){
					LinkedHashMap<String, Object> current = (LinkedHashMap<String, Object>) currentO;
					LinkedHashMap<String, Object> getPosition = (LinkedHashMap<String, Object>) current.get("geo_position");
					csvInfo.add(
						new String[]{
							current.get("_id").toString(),
							(String) current.get("name"),
							(String) current.get("type"),
							getPosition.get("latitude").toString(),
							getPosition.get("longitude").toString()
						}
					);
				}
				
			}catch (Exception e){
				throw new Exception("Error: Invalid json received\n" + e.getMessage());
			}

			String defaultOutputName = Properties.getInstance().getProperty("defaultOutputName");
			try {
				// Lets write the CSV file.
				writeCsvFile(csvInfo, defaultOutputName);
			}catch (Exception e){
				throw new Exception("Cannot write the file " + defaultOutputName + "\n" + e.getMessage());
			}

		} catch (IOException e) {
			System.out.println("Cannot connect properly to the API.");
			System.out.println(e.getMessage());
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Create a csv file with the data passed as a list of String[]
	 * @param csvInfo Data to be written in the csv file.
	 * @param outputFile Name of the csv file.
	 * @throws FileNotFoundException
	 */
	private static void writeCsvFile(ArrayList<String[]> csvInfo, String outputFile) throws IOException {
		FileWriter fileWriter = new FileWriter(outputFile);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		for (String[] current : csvInfo) {
			StringBuilder builder = new StringBuilder();
			for ( String token : current ){
				builder.append(token);
				builder.append(",");
			}
			builder.setLength(builder.length());
			builder.append("\n");
			writer.write(builder.toString());
		}
		writer.flush();
		fileWriter.flush();
		writer.close();
		fileWriter.close();
	}

	/**
	 * It prints to stdout a help message.
	 *
	 * It must be used when the amount of parameter is not correct.
	 *
	 **/
	private static void help() {
		System.out.println("Usage: java -jar GoEuroTest.jar \"STRING\"");
		System.out.println(" where STRING is the name of the place that is going" +
			" to be checked");
		System.out.println("Example: java -jar GoEuroTest.jar Potsdam");
	}
	
}
