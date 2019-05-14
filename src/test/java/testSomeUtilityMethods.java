import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class testSomeUtilityMethods {


    @Test
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


    @Test
    public void testFileRessourceLoader() throws URISyntaxException, IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        File file = new File(loader.getResource("exacts/graph_968_26_8.lst").getFile());
        System.out.println(file.getName());


        Path exacts = Paths.get(getClass().getResource("/exacts").toURI());
        Stream<Path> files = Files.list(exacts);
        files.forEach(p -> System.out.println(p.getFileName()));
    }







}
