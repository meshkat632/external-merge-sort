package org.aid.externalsorting.common;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Creates a uniform stream of chunks of element from the input put directory ( which can have multiple input files).  	 
 */

public class InputDirectoryScanner {
	private JoinLineReader lineReader;

	public static InputDirectoryScanner create(File inputFolder){

         if (inputFolder.exists() && inputFolder.isDirectory()) {             
			 Stream<StringLineReader> readers = Stream.of(inputFolder.listFiles()).map(file -> {
				 try {
					 return StringLineReader.createLineReader(file);
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
				 return null;
			 }).filter(item ->item !=null);
             return new InputDirectoryScanner(readers);
         }else {
             throw  new IllegalArgumentException("no directory found at:"+inputFolder.getAbsolutePath());
         }
	}
	public static InputDirectoryScanner create(String directoryPath, Charset charset){
		File inputFolder = new File(directoryPath);
		return create(inputFolder);

	}

	private InputDirectoryScanner(Stream<StringLineReader> readers){
		lineReader = new JoinLineReader(readers.collect(Collectors.toList()));
	}

	public boolean hasNext() {
		return lineReader.hasNext();
	}

	public Stream<String> readNext(int count) {
		return lineReader.readNext(count);
	}

	

}
