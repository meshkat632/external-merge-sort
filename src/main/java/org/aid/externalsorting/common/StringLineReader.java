package org.aid.externalsorting.common;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Scanner;
/**
 * Simple line reader and provide iterator.    		  	 
 */
public class StringLineReader {
	
	public static StringLineReader createLineReader(File file) throws IOException {
		if (file.exists() && !file.isDirectory()) {
			Path path = file.toPath();
			Scanner scanner= new Scanner(path);
			return new StringLineReader(scanner);
		} else {
			throw new IOException("no file found at:" + file.getAbsolutePath());
		}
	}

	private Scanner scanner;
	private StringLineReader(Scanner scanner) {
		this.scanner = scanner;
	}

	public boolean hasNextLine(){
		try{
			return this.scanner.hasNextLine();
		}catch (Exception exp){
			return false;
		}
	}

	public String nextLine(){
		String ret = this.scanner.nextLine();
		if(!this.scanner.hasNextLine())
			this.scanner.close();
		return ret;
	}

	public Iterator<String> getIterator() {	
		return new Iterator<String>() {
			@Override
			public boolean hasNext() {				
				return hasNextLine();
			}

			@Override
			public String next() {				
				return nextLine();
			}
			
		};
	}
}
