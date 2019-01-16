import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.*;

/**
 * This version employs NIO (to stream the tweets file)
 * and stream pipeline processing (uses Java SE 8)
 */

public class JacksonSamplerStream {
	
	public static void main(String[] args) throws IOException {
		assert args != null & args.length > 0;
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> tweet = new HashMap<>();
		
		Stream<String> lines = Files.lines(Paths.get(args[0]),
			StandardCharsets.UTF_8);
		lines.forEach(s -> {
			try {
				tweet.putAll(mapper.readValue(s, Map.class));
				tweet.forEach((k,v) -> 
					System.out.printf("%s: %s%n", k, v));
				System.out.println("=================");
			tweet.clear();
			} catch (IOException ioe) {
				System.out.printf("Bad json record %s%n", ioe);
			}
		});

		// System.out.printf("The file %s has %d lines%n",args[0],lineCount[0]);
	}
}