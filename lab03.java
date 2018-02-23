import java.util.*;
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Paths;

public class lab03 {
    public static void main(String[] args) throws Exception {
        String url = "https://www.infoplease.com/homework-help/history/collected-state-union-addresses-us-presidents";
        // prepare a pattern for substring search
        Pattern pattern = Pattern.compile("class=\"article\"><a href=");
        // prepare outfile name 
        String contentFile = "extractData.txt";
        // File f = File.createTempFile("extractData", ".txt");
        // extract content from URL, by matched pattern
        getMatchedContentFromURL(url, pattern, contentFile, 0);
        // create CSV file and txt files with address 
        createCSVandAddressTxtFiles(contentFile);

        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
        
    }
    /* method to retrieve content from webpage and process data for first time */
    static void getMatchedContentFromURL(String url, Pattern pattern, String contentFile, int choice) throws Exception {
        String content = "";
        URLConnection connection = null;
        File temp = new File("temp.txt");
        try {
            // create a URL object for HTTP connection
            connection =  new URL(url).openConnection();
            // use a Scanner object to accept input stream from HTTP connection
            Scanner scanner = new Scanner(connection.getInputStream());
            // prepare objects for file output stream and print output stream
            FileOutputStream outStream = new FileOutputStream(temp);
            PrintStream printStream = new PrintStream(outStream);
            // string for searching pattern creation
            String[] regexeList = {"\\b[1-9][0-9]+\\b","</div><p> "};
            scanner.useDelimiter(pattern);
            while(scanner.hasNext()){
                content = scanner.next();
                Matcher matcher = pattern.matcher(content);     // find lines with matching pattern
                content = matcher.replaceAll("\n");             // replace matching pattern with newline
                printStream.println(content);               // print output to file
            }
            printStream.close();    // close output stream
            scanner.close();        // close scanner
            extractDataFromTxt("temp.txt", contentFile, regexeList[choice]);
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        // temp.delete(); // after job done, delete temp file
    }

    /* method to extract data from txt file and save into another output file */
    static void extractDataFromTxt(String tempFile, String contentFile, String regexe) throws Exception {
        // prepare Scanner object for reading input file
        String s = "";
        File f = new File(tempFile);
        File content = new File(contentFile);
        Scanner scanner = new Scanner(f);
        // prepare objects for File output stream and Print output stream
        FileOutputStream out = new FileOutputStream(content);
        PrintStream outprint = new PrintStream(out);
        
        // Step 1: Allocate a Pattern object to compile a regexe
        Pattern pattern = Pattern.compile(regexe, Pattern.CASE_INSENSITIVE);
        
        while(scanner.hasNext()){
            s = scanner.nextLine();
            // Step 2: Allocate a Matcher object from the pattern, and provide the input
            Matcher matcher = pattern.matcher(s);
            // use Matcher to find the lines with needed data,skip un-needed lines
            if(s.startsWith("\"") && matcher.find()){
                s = findSubstring(s);
                if(s != null){   // find needed substring
                    outprint.println(s);    // write substring to output file
                }    
            }
       }
        scanner.close();    // close the scanner
        outprint.close();   // close the output stream
    }
    /* find the specific substring */
    static String findSubstring(String s){
        int beginIndex = s.indexOf("\">");
        int endIndex = s.indexOf(")</a>");
        if(endIndex <= beginIndex){
            return null;
        }
        return s.substring(beginIndex+2, endIndex+1);
    }
    /* create CSV file and txt files containing Union Address */
    static void createCSVandAddressTxtFiles(String inputFile) throws Exception {
        String path = Paths.get(".").toAbsolutePath().normalize().toString();
        String addressUrl = "https://www.infoplease.com/homework-help/us-documents/state-union-adress";
        // prepare objects for File output stream and Print output stream
        FileOutputStream out = new FileOutputStream("table.csv");
        PrintStream outprint = new PrintStream(out);
        outprint.println("Name of President\tDate of Unoin Address\tLink to Address\tFilename_Address\tText of Address\t");

        File inFile = new File(inputFile);
        Scanner input = new Scanner(inFile);
        Integer index = 1;

        while(input.hasNextLine()){
            String[] tokenList = null;
            String output, tempUrl, txtFileName = null;
            tokenList = input.nextLine().replace(" (", " ").replace(", ", " ").replace(")", " ").split(" ");
            tempUrl = getAddressUrl(addressUrl, tokenList);
            output = "" + tokenList[0] + " " + tokenList[1] + 
                    "\t" + tokenList[2] + " " + tokenList[3] + " " + ", " + tokenList[4];
            txtFileName = path+"\\InfoUnionAddress_"+index.toString()+".txt";
            index++;
            output += "\t" + txtFileName;
            // get content of the Union Address for each president
            String regex = "</div><p> ";
            Pattern pattern = Pattern.compile(regex);
            getMatchedContentFromURL(tempUrl, pattern, txtFileName, 1);

            outprint.println(output);
            for(int i=0; i<tokenList.length;i++) System.out.println(i + "  " + tokenList[i]);
            System.out.println(tempUrl);
            System.out.println("This is output: " + output);
            System.out.println("This is txtfile: " + txtFileName);
            break;
        }
        input.close();
        outprint.close();
    }
    static String getAddressUrl(String url, String[] tokenList){
        for(int i=0; i<tokenList.length;i++)
            url += "-" + tokenList[i];
        return url;
    }
    static String getOutputForCSV(String[] tokenList){
        String s = "";
        for(int i=0;i<tokenList.length-1;i++) s += "\t";
        return s;
    }
}