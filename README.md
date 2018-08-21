# external-merge-sort

The project is a simple Maven based java project.run 'mvn test' should run all the tests.   

# checkout and running the tests
```
git clone https://github.com/meshkat632/external-merge-sort.git
cd external-merge-sort
mvn clean test
```
# overview 
Assumptions: We assume that our unsorted files are in 'input' folder. This methods,

	 step 1:   clean the 'workFolder' () 
	 step 2:   create a uniform stream of chunks of data
	 step 3:   load each chunk into memory
	 step 4:   sort with conventional method (in these we are using the default sorting method provided by java)
	 step 5:   write back the sorted chunks into files inside 'working' directory.
	 step 6	  invoke the multiple file recursive 'sort' procedure to start the actual sorting procedure.  	 

# description of the sorting procedure

This solution is a simple sorting recursive algorithm. Its a simple recursive procedure. At each step it tries to reduce the number of sorted files into fewer (bigger) sorted files. Until there is only one sorted files, the procedure return with that file. It each iteration it takes n (= chukSize) sorted files and passes them through NWayMarger algorithm to produce new file ( sorted with all elements from the old n files ) and delete the selected n files. It keeps doing while there is no old sorted files remain. After this the 'workFolder' should have fewer files. If N was number of files, when the procedure started, it would have N/chunkSize new files. Then the procedure calls
it self. 


	Here is the basic flow: 				  
	  -- if 'workFolder' contains only one file (which is already sorted), we are done return the result.
	  -- other wise
	  -- take n files ( n = chunkSize) and call NWayMarge to produce one sorted streams of data and write to disk.
	  -- remove n files
	  -- continue the above steps as all the old files are removed
	  -- (At this point we have only newly sorted files inside the 'workFolder')
	  -- call again itself with the current 'workFolder'
	  
	           
# Meaning of different terms: 	           
1. 'inputFolder':  the folder contains all the files ( with unsorted data).
2. 'workFolder':  the folder contains all the files ( with sorted data).
3. 'chunkSize': its defines the number of data elements the sorting algorithm will use. Usually the chunkSize is the bounded memory capacity the algorithm should use. Here in this implementation we are defined chunkSize as the maximum number of elements we are allowed to store in memory. Note: Since we are not setting limits on the size of each element, our implementation does not respect the actual bounded memory model. For this first implementation, we are just ignoring the size of each element, to simplify the task. :-).

Here is the list of important classes:

1. ExternalMargeSortTask<T>: The implementation of External sorting. Contains the recursive sorting procedure. 
2. NWayMerge<T>: Simple implementation of N-way marge. It takes a list of iterators and produces sorted chunks where number of iterators is same the chunkSize.   
3.InputDirectoryScanner: Creates a uniform stream of chunks of element from the input put directory ( which can have multiple input files).
4.JoinLineReader: Combine multiple line reader into one stream.
5.PeekIterator: Simple Peek-able Iterator.  
6.StringLineReader: Simple line reader and provide iterator access.
