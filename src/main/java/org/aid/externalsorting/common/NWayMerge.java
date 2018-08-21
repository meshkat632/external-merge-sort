package org.aid.externalsorting.common;

import java.util.*;
import java.util.stream.Stream;



public class NWayMerge<T> {

	/**
	 * Simple implementation of N-way marge. It takes a list of iterators and produces sorted chunks
	 * where number of iterators is same the chunkSize.    

	 * @param iterators list of iterators to do marge and sort.		  	 
	 *   
	 */
	public NWayMerge(List<Iterator<T>> iterators) {

        this.iteratorList = new ArrayList<>();
        iterators.forEach(iterator -> {
            iteratorList.add(new PeekIterator<T>(iterator));
        });

    }
	
		

    public interface OnChunkSortedListener<T> {
        public void onSortedChaunk(int batchNumber, Stream<T> sortedStream);
    }

    private List<PeekIterator<T>> iteratorList;    


    public void sort(OnChunkSortedListener<T> listener) {
        int batchNumber = 0;
        while (hasNext()) {
            batchNumber++;
            Stream<T> sortedChhunk = readNext(iteratorList.size());
            if (listener != null) listener.onSortedChaunk(batchNumber, sortedChhunk);
        }
    }

    private boolean hasNext() {
        if (iteratorList.stream().map(iterator -> {
            return iterator.hasNext();
        }).filter(item -> item).count() > 0) return true;
        return false;
    }

    private Optional<T> getTheMin() {
        HashMap<T, PeekIterator<T>> ret = new HashMap<>();
        iteratorList.forEach(iterator -> {
            try {
                T x = iterator.peek();
                ret.put(x, iterator);
            } catch (NoSuchElementException exp) {
            }
        });        
        Optional<T> min = ret.keySet().stream().sorted().findFirst();
        if(min.isPresent()) {
        	if (ret.get(min.get()).hasNext()) {
                ret.get(min.get()).next();
            }	
        }       
        return min;
    }


    private Stream<T> readNext(int count) {
        List<T> ret = new ArrayList<>();
        for (int i = 0; i < count; i++) {
        	Optional<T> min = getTheMin();
        	if(min.isPresent()) {
        		ret.add(min.get());	
        	}            
        }
        System.out.println(ret);
        return ret.stream();
    }


}