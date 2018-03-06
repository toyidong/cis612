import java.util.*;
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Paths;
public class t2{
    public static void main(String[] args) throws Exception {
        String url = "https://www.infoplease.com/homework-help/us-documents/state-union-address-james-madison-november-4-1812";
        Pattern pattern = Pattern.compile("</div></div><p>");
        String contentFile = "t2.txt";
        getMatchedContentFromURL(url, pattern, contentFile);
        // createCSVandAddressTxtFiles(contentFile);
        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
    }
    static void getMatchedContentFromURL(String url, Pattern pattern, String contentFile) throws Exception {
        String content = "";
        URLConnection connection = null;
        File temp = new File("t2temp.txt");
            connection =  new URL(url).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            FileOutputStream outStream = new FileOutputStream(temp);
            PrintStream printStream = new PrintStream(outStream);
            // String[] regexeList = {"\\b[1-9][0-9]+\\b","</p><p>"};
            // Pattern pattern2 = Pattern.compile(regexeList[1]);
            scanner.useDelimiter(pattern);
        while(scanner.hasNextLine()){
            content = scanner.nextLine();
            // content.replace("</p></div><div class=", "</div></div><p>");
            if(content.contains(" </p></div><div class=")){
                String s = "</h2></div></div></div><p> ";
                String t = " </p></div><div class=\"navfooter\">";
                String r = " </p><p> ";
                // int index = content.IndexOf(s);
                int beginIndex, endIndex = 0;
                beginIndex = content.indexOf(s) + s.length();
                endIndex = content.indexOf(t);
                System.out.println(beginIndex + " " + endIndex);
                String[] stringList = content.substring(beginIndex, endIndex).split(r);
                for(int i=0;i<stringList.length;i++) printStream.println(stringList[i]);               // print output to file
            }          
            // replace matching pattern with newline
            
        }
            printStream.close();    // close output stream
            scanner.close();        // close scanner
            //extractDataFromTxt("temp.txt", contentFile, regexeList[choice]);
        // temp.delete(); // after job done, delete temp file
    }
}