import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class test {
   public static void main(String[] args) throws Exception {

      String s = "Hello World! 3 + 3.0 = 6.0 true Wor ";
      File f = new java.io.File("out2.txt");
      FileOutputStream out = new FileOutputStream("out3.txt");
      PrintStream outprint = new PrintStream(out);

      // create a new scanner with the specified String Object
      Scanner scanner = new Scanner(f);
      String regexe = "\\b[1-9][0-9]+\\b";
      String replacement = " 1909 ";
 
    // Step 1: Allocate a Pattern object to compile a regexe
    Pattern pattern = Pattern.compile(regexe, Pattern.CASE_INSENSITIVE);
 
    // Step 2: Allocate a Matcher object from the pattern, and provide the input
    

      // display the new delimiter
      while(scanner.hasNext()){
          s = scanner.nextLine();
          Matcher matcher = pattern.matcher(s);
          if(s.startsWith("\"") && matcher.find()){
            System.out.println(s);
            outprint.println(s);
          }
        //   System.out.println(s);
    //   System.out.println("" + scanner.next());
     }
      // close the scanner
      scanner.close();
      outprint.close();
   }
}