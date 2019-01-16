import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;

/**
 * This is most old-fashioned parsing with Scanner;
 * the only "advanced" (from java 7) feature is the context
 * manager
 */

public class JacksonSampler {
	
	public static void main(String[] args) throws IOException {
		assert args != null & args.length > 0;
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> tweet = new HashMap<>();
		String line;
		// the use of Scanner(filename, encoding) is important to get
		// the unicode scanning right
		try (Scanner sc = new Scanner(new File(args[0]), "utf-8")) { 
			while (sc.hasNextLine()) {
				line = sc.nextLine();
				tweet = mapper.readValue(line, Map.class);
				for (String key : tweet.keySet()) {
					System.out.printf("%s: %s%n", 
						key, tweet.getOrDefault(key, "Missing value"));
				}
				System.out.println("=================");
				tweet.clear();
			}
		}
	}
}