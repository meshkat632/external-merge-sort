package org.aid.externalsorting.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
/**
 * combine multiple line reader into one stream.  		  	 
 */
public class JoinLineReader {
	private Iterator<String> joinIterator;
	private List<StringLineReader> readers;
	private StringLineReader currentReader;
	private int currentReaderId = 0;
	public JoinLineReader(List<StringLineReader> _readers){
		readers = _readers;
		currentReaderId = 0;
		currentReader = this.readers.get(currentReaderId);

		joinIterator = new Iterator<String>() {
			@Override
			public boolean hasNext() {
				if(!currentReader.hasNextLine()){
					currentReaderId = currentReaderId+ 1;
					if(currentReaderId  < _readers.size())
						currentReader = readers.get(currentReaderId);
					else return false;
				}
				return currentReader.hasNextLine();
			}

			@Override
			public String next() {
				if(!currentReader.hasNextLine()){
					currentReaderId = currentReaderId+ 1;
					currentReader = readers.get(currentReaderId);
				}
				return currentReader.nextLine();
			}
		};
	}
	
	public Iterator<String> getStream(){
		return joinIterator;
	}

	public boolean hasNext() {
		return joinIterator.hasNext();
	}

	public Stream<String> readNext(int count) {
		int i = 0;
		List<String> ret = new ArrayList<>();
		while(hasNext() && i< count) {
			String x = joinIterator.next();			
			ret.add(x);
			i ++;
		}
		return ret.stream();	
	}

}
