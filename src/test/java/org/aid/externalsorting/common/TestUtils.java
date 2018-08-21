package org.aid.externalsorting.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

public class TestUtils {
	public  static void cleanDirectory(String directoryPath){
		File dir = new File(directoryPath);
		if(!dir.exists() && !dir.isDirectory()) {        		
			dir.mkdir();
			return;
    	}        
        for(File file: dir.listFiles())
            if (!file.isDirectory())
                file.delete();
        
    	
    }

    public  static void nullCheck(Object object, String message) throws IllegalArgumentException{
        if(object == null) throw new IllegalArgumentException(message);
    }

    public static void append(String content, String filePath) {
    	append( content, filePath, null);
    }
    public static void append(String content, String filePath, Charset charset) {	
    	
    	try {  		
    		
        	File file = new File(filePath);
        	if(!file.exists()) {        		
        		file.createNewFile();
        	}        	
        	Files.write(Paths.get(filePath),(content).getBytes(),StandardOpenOption.APPEND);
    	}catch (Exception e) {
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

	public static List<String> readLines(File testCase) {
		// TODO Auto-generated method stub
		return null;
	}
	public static void cloneDirectory(File sourceLocation , File targetLocation)
			 throws IOException {
			      
			     if (sourceLocation.isDirectory()) {
			         if (!targetLocation.exists()) {
			             targetLocation.mkdir();
			         }
			          
			         String[] children = sourceLocation.list();
			         for (int i=0; i<children.length; i++) {
			        	 cloneDirectory(new File(sourceLocation, children[i]),
			                     new File(targetLocation, children[i]));
			         }
			     } else {
			          
			         InputStream in = new FileInputStream(sourceLocation);
			         OutputStream out = new FileOutputStream(targetLocation);
			          
			         // Copy the bits from instream to outstream
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

	public static void generateRandomNumbers(int count, String filePath) throws IOException{
		File newFile = new File(filePath);
		if(!newFile.exists())	
			newFile.createNewFile();		
		
		int i = 0;		
		StringBuilder s = new StringBuilder();
		Random rand = new Random();
		while(i < count) {
			
			int  n = rand.nextInt(count) + 1;			
			if(i == 0)
				s.append(""+n);
			else 
				s.append("\n"+n);
			
			i++;
		}			
		Utils.append(s.toString(), newFile.getPath());		
	}

	public static boolean isIntegerSorted(String filePath) throws IOException {
		StringLineReader aSimpleLineReader = StringLineReader.createLineReader(new File(filePath));        
        Iterator<String> iterator = aSimpleLineReader.getIterator();
        Integer lastValue = null; 
        while (iterator.hasNext()) {        	
        	if(lastValue == null) { 
        		lastValue = Integer.parseInt(iterator.next());
        	}
        	else {
        		Integer newNextValue = Integer.parseInt(iterator.next());
        		if (lastValue > newNextValue) return false;
        		lastValue = newNextValue;        		               
        	}
           }
		return true;		
	}
	
	public static boolean isStringSorted(String filePath) throws IOException {
		StringLineReader aSimpleLineReader = StringLineReader.createLineReader(new File(filePath));        
        Iterator<String> iterator = aSimpleLineReader.getIterator();
        String lastValue = null; 
        while (iterator.hasNext()) {
        	
        	if(lastValue == null) { 
        		lastValue = iterator.next();
        	}
        	else {
        		String newNextValue = iterator.next();
        		if (lastValue.compareTo(newNextValue) > 0) return false;
        		lastValue = newNextValue;        		               
        	}       	
           }
		return true;
	}

	public static void generateSortedNumbers(int count, String filePath) throws IOException{
		File newFile = new File(filePath);
		if(!newFile.exists())	
			newFile.createNewFile();
		else 
			Utils.append("", newFile.getPath());
		
		int i = 0;		
		StringBuilder s = new StringBuilder();		
		while(i < count) {		
						
			if(i == 0)
				s.append(""+i);
			else 
				s.append("\n"+i);
			
			i++;
		}			
		Utils.append(s.toString(), newFile.getPath());
		
	}
	private static void generateRandomStrings(int count, String filePath) throws IOException{
		File newFile = new File(filePath);
		if(!newFile.exists())	
			newFile.createNewFile();
		else 
			Utils.append("", newFile.getPath());
		
		int i = 0;		
		StringBuilder s = new StringBuilder();		
		while(i < count) {		
			final String randomText = UUID.randomUUID().toString();
			if(i == 0)
				s.append(""+randomText);
			else 
				s.append("\n"+randomText);
			
			i++;
		}			
		Utils.append(s.toString(), newFile.getPath());
		
	}

	public static File generateRandomNumbers(int count) throws IOException {		
		Path path = Files.createTempDirectory(UUID.randomUUID().toString());	
		File testFile = new File(path.toFile().getPath()+"/"+UUID.randomUUID().toString()+".txt");
		generateRandomNumbers(count, testFile.getPath());	    
		return path.toFile();
	}
	
	

	public static File generateRandomNumbersInFolder(int count, String directoryPath) throws IOException {
		File dir = new File(directoryPath);
		if(dir.exists() && dir.isDirectory())
			cleanDirectory(directoryPath);
		else dir.mkdir();
		
		System.out.println(dir.exists()+":"+dir.isDirectory());
		File testFile = new File(dir.getPath()+"/"+UUID.randomUUID().toString()+".txt");		
		generateRandomNumbers(count, testFile.getPath());
		return dir;
		
	}
	
	 

	public static File generateRandomNumbersFilesInFolder(int count, int filesCount, String directoryPath) throws IOException {		
		File dir = new File(directoryPath);
		if(dir.exists() && dir.isDirectory())
			cleanDirectory(directoryPath);
		else dir.mkdir();
		
		while(filesCount >= 0) {
			generateRandomNumbers(count, dir.getPath()+"/"+UUID.randomUUID().toString()+".txt");
			filesCount = filesCount - 1;			
		}		
		return dir;
	}

	public static File generateRandomStringsFilesInFolder(int count, int filesCount, String directoryPath) throws IOException{
		File dir = new File(directoryPath);
		if(dir.exists() && dir.isDirectory())
			cleanDirectory(directoryPath);
		else dir.mkdir();		
		while(filesCount >= 0) {
			generateRandomStrings(count, dir.getPath()+"/"+UUID.randomUUID().toString()+".txt");
			filesCount = filesCount - 1;			
		}		
		return dir;
	}

	
	
 

}
