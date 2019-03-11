package umons.algorithm.dominatingset.toDelete;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtilities {



    public static void main( String[] args ) {

     Set<Integer> set1 = Stream.of(1,2,3).collect(Collectors.toCollection(HashSet::new));
     Set<Integer> set2 = Stream.of(5,4).collect(Collectors.toCollection(HashSet::new));
     Set<Integer> set3 = null;
     System.out.println(minOfTheSet(set1,set2,set3));
    }

    public static Set<Integer> minOfTheSet( Set<Integer>... sets) {
        return  Arrays.stream(sets)
                      .filter(set->set!=null)
                      .min(Comparator.comparingInt(Set::size))
                      .get();
    }
}
