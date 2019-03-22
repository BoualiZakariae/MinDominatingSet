package umons.algorithm.dominatingset.toDelete;
public class TestUtilities {

    public static void main( String[] args ) {

        String [] words ={"0","00","000","0000","00000","000000","0000000"} ;
        for (String str :words) {
            System.out.println(transform(str));
        }
    }
    private static String  transform( String word ) {
        int min =  getMinNumber(word);
        StringBuilder sb = new StringBuilder(word);
        //starting from char at index 1 replace each third '0' with '1'
        //and substract 1 for each replaced char from min
        for(int i = 1; i< word.length(); i = i+3){
            sb.setCharAt(i, '1');
            min--;
        }
        //if minimum replacement count not yet met replace last char
        if(min >0){
            sb.setCharAt(word.length()-1, '1');
        }
        return sb.toString();
    }

    public static int getMinNumber(String word) {
        int min;
        if (word.length() % 3 == 0) {
            min = word.length() / 3;
        } else {
            min = (word.length() / 3) + 1;
        }
        return min;
    }
}
