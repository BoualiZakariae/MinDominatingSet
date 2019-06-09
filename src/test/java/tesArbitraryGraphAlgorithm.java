import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import umons.algorithm.dominatingset.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
@Disabled
public class tesArbitraryGraphAlgorithm {


    @Test@Disabled
    public void testCombinationUtility(){
        int [] array = new int[10];
        IntStream.range(0,10).forEach(i->array[i]=i);
        List<int[]> lists = new ArrayList<>();
        combinations2(array, 3, 0, new int[3],lists);
        System.out.println("finishedOne9");
        System.out.println(lists.size());

    }

    public static List<int[]> combinations2(int[] arr, int len, int startPosition, int[] result, List<int[]> subsets){
        if (len == 0){
            subsets.add(Arrays.copyOf(result,result.length));
            return subsets;
        }
        for (int i = startPosition; i <= arr.length-len; i++){
            result[result.length - len] = arr[i];
            combinations2(arr, len-1, i+1, result, subsets);
        }
        return subsets;
    }


}
