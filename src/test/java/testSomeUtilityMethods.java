import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
@Disabled
public class testSomeUtilityMethods {





    @Test@Disabled
    public void testContainement(){
        Set<Integer> setOne = IntStream.of(0,1,4,3)
                                       .boxed()
                                       .collect(Collectors.toCollection(HashSet::new));

        Set<Integer> setTwo = IntStream.of(0,1,4,5)
                .boxed()
                .collect(Collectors.toCollection(HashSet::new));

        Assertions.assertEquals(true,testSets(setOne,setTwo));

    }

    private boolean testSets( Set<Integer> setOne, Set<Integer> setTwo ) {
        int counter=0;
        for (int x: setOne){
            if (setTwo.contains(x))
                counter++;
        }
        return counter > 2;
    }


    @Test@Disabled
    public void testfileParsing(){

        String lineOne = "1: 2 3 4";
        String lineTwo = "10: 6 1 14";

        String[] array = lineOne.split(" ");
        int verticesCounter = array.length;
        int vertexOne   = Integer.parseInt(array[0].substring(0,array[0].length()-1));
        System.out.print(vertexOne+" : ");
        for(int i=1; i<verticesCounter; i++)
        {
            System.out.print(" "+Integer.parseInt(array[i]));
        }
    }


    @Test@Disabled
    public void testFileRessourceLoader() throws URISyntaxException, IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        File file = new File(loader.getResource("houseOfGraphs/graph_968_26_8.lst").getFile());
        System.out.println(file.getName());


        Path exacts = Paths.get(getClass().getResource("/exacts").toURI());
        Stream<Path> files = Files.list(exacts);
        files.forEach(p -> System.out.println(p.getFileName()));
    }







}
