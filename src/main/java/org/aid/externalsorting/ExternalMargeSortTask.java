package org.aid.externalsorting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aid.externalsorting.common.InputDirectoryScanner;
import org.aid.externalsorting.common.NWayMerge;
import org.aid.externalsorting.common.StringLineReader;
import org.aid.externalsorting.common.Utils;
import org.aid.externalsorting.common.NWayMerge.OnChunkSortedListener;


public class ExternalMargeSortTask<T> {	
	
	
	private Converter<T> converter;
	private File inputFolder;
	private File workFolder;
	private int chunkSize;
	
	
	/**
	 * This method is responsible to do the initial tasks needed for the actual sorting to run. 
	 * We assume that our unsorted files are in 'input' folder. This methods,
	 * 	        clean the 'workFolder' () 
	 * 			create a uniform stream of chunks of data
	 * 			load each chunk into memory
	 * 			sort with conventional method (in these we are using the default sorting method provided by java)
	 * 			write back the sorted chunks into files inside 'working' directory.
	 * 			invoke the 'sort' method to start the actual sorting procedure.
	 * 
	 *    
	 * @param inputFolder the folder contains all the files ( with unsorted data) 
	 * @param workFolder  the folder contains all the files ( with sorted data )              	 	
	 * @param chunkSize its defines the number of data elements the sorting algorithm will use. 
	 * 		            Usually the chunkSize is the bounded memory capacity the algorithm should use. 
	 * 					Here in this implementation we are defined chunkSize as the maximum number of elements we are allowed 
	 * 					to store in memory. Note: Since we are not setting limits on the size of each element, our implementation 
	 * 					does not respect the actual bounded memory model. For this first implementation, we are just ignoring 
	 * 					the size of each element, to simplify the task. :-) 
	 * 
	 *  @param converter Since we are working with string, we need to provide the 'string to object' convention method. 
	 *  				 this is used by the default sorting algorithm to sort the small chunks we are producing.	 *  				  	 
	 *   
	 */
	
	private ExternalMargeSortTask(File inputFolder, File workFolder, int chunkSize , Converter<T> converter) {
		this.converter = converter;
		this.inputFolder = inputFolder;
		this.workFolder = workFolder;
		this.chunkSize = chunkSize;
	}
	public interface Converter<T>{
		 public T convert(String content);
	}
	/**
	 * builder method for element type Integer
	 */
	public static ExternalMargeSortTask<Integer> creatIntergerSortingTask(File inputFolder, File workFolder, int chunkSize) {
		if(chunkSize < 2 ) throw new IllegalArgumentException("the mimumm value for chunkSize is 2.");
		return new ExternalMargeSortTask<Integer>(inputFolder, workFolder, chunkSize, new ExternalMargeSortTask.Converter<Integer>() {
			@Override
			public Integer convert(String content) {				
				return Integer.parseInt(content);
			}
		});
	}
	/**
	 * builder method for element type String
	 */
	public static ExternalMargeSortTask<String> creatStringSortingTask(File inputFolder, File workFolder, int chunkSize) {
		if(chunkSize < 2 ) throw new IllegalArgumentException("the mimumm value for chunkSize is 2.");
		return new ExternalMargeSortTask<String>(inputFolder, workFolder, chunkSize, new ExternalMargeSortTask.Converter<String>() {
			@Override
			public String convert(String content) {				
				return content;
			}
		});
	}
	
	/**
	 * This  is actual sorting recursive algorithm. Its a simple recursive procedure. At each step it tries to reduce
	 * the number of sorted files into fewer (bigger) sorted files. Until there is only one sorted files, the procedure 
	 * return with that file. It each iteration it takes n (= chukSize) sorted files and passes them through NWayMarger 
	 * algorithm to produce new file ( sorted with all elements from the old n files ) and delete the selected n files.
	 * It keeps doing while there is no old sorted files remain. After this the 'workFolder' should have fewer files.
	 * If N was number of files, when the procedure started, it would have N/chunkSize new files. Then the procedure calls
	 * it self. Here is the basic flow, 				  
	 * 			if workFolder contains only one file (which is already sorted), we are done return the result.
	 * 			other wise 
	 * 				take n files ( n = chunkSize) and call NWAyMarge to produce one sorted file and write to disk.
	 * 				remove n files
	 * 	        continue the above steps as all the old files are removed 
	 *  		(At this point we have only newly sorted files inside the 'workFolder')
	 *          call again itself with the workFolder
	 *          
	 *  		
	 *   		
	 * @param workFolder the folder contains all the files ( with sorted data).
	 * @param chunkSize its defines the number of data elements the sorting algorithm will use. 
	 * 		            Usually the chunkSize is the bounded memory capacity the algorithm should use. 
	 * 					Here in this implementation we are defined chunkSize as the maximum number of elements we are allowed 
	 * 					to store in memory. Note: Since we are not setting limits on the size of each element, our implementation 
	 * 					does not respect the actual bounded memory model. For this first implementation, we are just ignoring 
	 * 					the size of each element, to simplify the task. :-) 
	 * 
	 *  @param converter Since we are working with string, we need to provide the 'string to object' convention method. 
	 *  				 this is used by the default sorting algorithm to sort the small chunks we are producing.	 *  				  	 
	 *   
	 */

	private File sort(File workFolder, int chunkSize) throws IOException {

		if (!(workFolder.exists() && workFolder.isDirectory() && workFolder.listFiles().length > 0)) {
			throw new IllegalArgumentException("workFolder file should be a folder with at least one file");
		}

		Set<File> inputfiles = Arrays.stream(workFolder.listFiles()).collect(Collectors.toSet());

		if (inputfiles.size() == 1) {
			return inputfiles.stream().findFirst().get();
		}

		while (inputfiles.size() > 0) {
			File newFile = new File(workFolder.getPath() + "/" + UUID.randomUUID().toString() + ".txt");
			newFile.createNewFile();
			Set<File> filesToProcess = takeFiles(inputfiles, chunkSize);

			List<Iterator<T>> lists = new ArrayList<>();
			filesToProcess.forEach(file -> {
				StringLineReader aSimpleLineReader;
				try {
					aSimpleLineReader = StringLineReader.createLineReader(file);

					Iterator<String> iterator = aSimpleLineReader.getIterator();
					lists.add(new Iterator<T>() {

						@Override
						public boolean hasNext() {
							return iterator.hasNext();
						}
						@Override
						public T next() {							
							return converter.convert(iterator.next());							
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			NWayMerge<T> problem = new NWayMerge<T>(lists);
			problem.sort(new NWayMerge.OnChunkSortedListener<T>() {
				@Override
				public void onSortedChaunk(int batchNumber, Stream<T> sortedStream) {
					String content = Utils.convertsItemsToFileContent(sortedStream);
					Utils.append(content + "\n", newFile.getPath());
				}
			});
			filesToProcess.forEach(fileTodelete -> {
				fileTodelete.delete();
			});
			inputfiles.removeAll(filesToProcess);
		}
		return sort(workFolder, chunkSize);
	}

	private Set<File> takeFiles(Set<File> inputfiles, int chunkSize) {
		if (inputfiles.size() <= chunkSize) {
			return inputfiles;
		} else {
			return inputfiles.stream().limit(chunkSize).collect(Collectors.toSet());
		}
	}
	

	

	/**
	 * This method is responsible to do the initial tasks needed for the actual sorting to run. 
	 * We assume that our unsorted files are in 'input' folder. This methods,
	 * 	        clean the 'workFolder' () 
	 * 			create a uniform stream of chunks of data
	 * 			load each chunk into memory
	 * 			sort with conventional method (in these we are using the default sorting method provided by java)
	 * 			write back the sorted chunks into files inside 'working' directory.
	 * 			invoke the 'sort' method to start the actual sorting procedure.  	 	
	 * 
	 */
	public File executeSort() throws IOException {
		Utils.cleanDirectory(workFolder.getPath());
		InputDirectoryScanner aDirectoryScanner = InputDirectoryScanner.create(inputFolder);
		while (aDirectoryScanner.hasNext()) {
			Stream<T> unsortedChunk = aDirectoryScanner.readNext(chunkSize).map(item -> {
				return converter.convert(item);
			});
			String content = Utils.convertsItemsToFileContent(unsortedChunk.sorted());			
			Utils.append(content, workFolder.getPath() + "/" + UUID.randomUUID().toString() + ".txt");
		}

		return sort(workFolder, chunkSize);
	}
}
