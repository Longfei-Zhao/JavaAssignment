import java.util.Properties;
import java.io.*;
import java.nio.file.*;

/**
 * pass the path to the topics.properties file,
 * read the names of topics and corresponding files
 * containing the keyword sets, then opens 
 * and reads each topic keyword set
 */

class ShowProperties {
	public static void main(String[] args) throws IOException {
		String filename = "/Users/zhaolongfei/Documents/ANU/COMP6700-Java/comp6700-2017/ass1/topics.properties";
		Path path = Paths.get(filename);
		Path parentDir = path.getParent();
		Properties props = new Properties();
		props.load(new FileReader(new File(filename)));
		// System.setProperties(props);
		System.out.printf("%s is in %s directory%n", path, parentDir);
		props.forEach((k,v) -> {
			System.out.printf("=====%nkeyword set of %s is in %s/%s%n",
				k, parentDir, v);
			try {
				System.out.printf("its contents: ");
				Files.lines(Paths.get(String.format("%s/%s",parentDir,v)))
					.forEach(s -> System.out.printf("%s ", s));
				System.out.println();
			} catch (IOException ioe) {
				System.out.printf("The keyword set %s cannot be open%s", ioe);
			}	
		});
	}
}
