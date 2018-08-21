package org.aid.externalsorting.common;

import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NWayMargerTest {

    @Test
    public void basic_test_with_chars() throws IOException {
        Character[] listOne = {'a', 'b', 'c'};
        Character[] listTwo = {'d', 'e', 'f'};
        Character[] listThree = {'g', 'h', 'i'};
        List<Iterator<Character>> lists = new ArrayList<>();
        lists.add(Arrays.asList(listOne).iterator());
        lists.add(Arrays.asList(listTwo).iterator());
        lists.add(Arrays.asList(listThree).iterator());
        NWayMerge<Character> problem = new NWayMerge<Character>(lists);
        List<Character> result = new ArrayList<>();
        problem.sort(new NWayMerge.OnChunkSortedListener<Character>() {
            @Override
            public void onSortedChaunk(int batchNumber, Stream<Character> sortedStream) {                
                result.addAll(sortedStream.collect(Collectors.toList()));
            }
        });
        assertNotNull(result);
        assertEquals(9, result.size());
    }
    @Test
    public void basic_test_with_int() throws IOException {
        Integer[] listOne = {1, 2, 3};
        Integer[] listTwo = {4, 5, 6};
        Integer[] listThree = {7, 8, 9};
        List<Iterator<Integer>> lists = new ArrayList<>();
        lists.add(Arrays.asList(listOne).iterator());
        lists.add(Arrays.asList(listTwo).iterator());
        lists.add(Arrays.asList(listThree).iterator());
        NWayMerge<Integer> problem = new NWayMerge<Integer>(lists);
        List<Integer> result = new ArrayList<>();
        problem.sort(new NWayMerge.OnChunkSortedListener<Integer>() {
            @Override
            public void onSortedChaunk(int batchNumber, Stream<Integer> sortedStream) {
            	result.addAll(sortedStream.collect(Collectors.toList()));                
            }
        });

        assertNotNull(result);
        assertEquals(9, result.size());
        
    }
    
    @Test
    public void basic_test_with_string() throws IOException {
        String[] listOne = {"1", "2", "3"};
        String[] listTwo = {"4","5", "6"};
        String[] listThree = {"7", "8", "9"};
        List<Iterator<String>> lists = new ArrayList<>();
        lists.add(Arrays.asList(listOne).iterator());
        lists.add(Arrays.asList(listTwo).iterator());
        lists.add(Arrays.asList(listThree).iterator());
        NWayMerge<String> problem = new NWayMerge<String>(lists);
        List<String> result = new ArrayList<>();
        problem.sort(new NWayMerge.OnChunkSortedListener<String>() {
            @Override
            public void onSortedChaunk(int batchNumber, Stream<String> sortedStream) {
            	result.addAll(sortedStream.collect(Collectors.toList()));                
            }
        });

        assertNotNull(result);
        assertEquals(9, result.size());        
    }
}
