package org.aid.externalsorting.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;


/**
 * 
 * collection of utility methods. 
 */
public class Utils {
	
	public  static void cleanDirectory(String directoryPath)throws IOException{		
        File dir = new File(directoryPath);
        if (!dir.exists() && !dir.isDirectory()) { throw new IllegalArgumentException("not a valid directoy @:" +directoryPath);}
        else {
        	for(File file: dir.listFiles())
                if (!file.isDirectory())
                    file.delete();	
        }       
        
    }	

	public static void nullCheck(Object object, String message) throws IllegalArgumentException {
		if (object == null)
			throw new IllegalArgumentException(message);
	}

	public static void append(String content, String filePath) {

		try {

			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			Files.write(Paths.get(filePath), (content).getBytes(), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String convertsItemsToFileContent(Stream<?> numbers) {
		return numbers.map(number -> {
			return number + "";
		}).reduce("", (a, b) -> {
			if (a == "")
				return b;
			else
				return a + "\n" + b;
		});
	}

	public static void cloneDirectory(File sourceLocation, File targetLocation) throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				cloneDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
		} else {

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);
			
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public static void cloneDirectory(String srcDir, String destDir) throws IOException {
		cloneDirectory(new File(srcDir), new File(destDir));
	}

}
