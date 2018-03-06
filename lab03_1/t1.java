import java.util.regex.Pattern;
import java.util.regex.Matcher;
//RegexeFindReplace
public class t1 {
   public static void main(String[] args) {
      String input = "This is an apple. These are 33 (Thirty-three) apples";
    //   String regexe = "apple";        // pattern to be matched
    //   String replacement = "orange";  // replacement pattern


        // String regexe = "\\w+";        // Escape needed for \
        String regexe = "\\b[1-9][0-9]+\\b";
        String replacement = " 1909 ";
   
      // Step 1: Allocate a Pattern object to compile a regexe
      Pattern pattern = Pattern.compile(regexe, Pattern.CASE_INSENSITIVE);
   
      // Step 2: Allocate a Matcher object from the pattern, and provide the input
      Matcher matcher = pattern.matcher(input);
   
      // Step 3: Perform the matching and process the matching result
      String output = matcher.replaceAll(replacement);     // all matches
      //String output = matcher.replaceFirst(replacement); // first match only
      System.out.println(output);
   }
}