import java.util.*;
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Paths;

public class lab03 {
    public static void main(String[] args) throws Exception {
        String url = "https://www.infoplease.com/homework-help/history/collected-state-union-addresses-us-presidents";
        // pre-process of content from webpage
        String fileName = getContentFromURL(url);
        // extract data and save into dataFile
        String dataFile = extractDataFromTxt(fileName);
        // create CSV file and txt files with address 
        createCSVandAddressTxtFiles(dataFile);

        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
        
    }
    // method to retrieve content from webpage and process data for first time
    // return output file name
    static String getContentFromURL(String url) throws Exception {
        String content = "";
        URLConnection connection = null;
        // prepare a pattern for substring search
        Pattern pattern = Pattern.compile("class=\"article\"><a href=");
        try {
            // create a URL object for HTTP connection
            connection =  new URL(url).openConnection();
            // use a Scanner object to accept input stream from HTTP connection
            Scanner scanner = new Scanner(connection.getInputStream());
            // prepare objects for file output stream and print output stream
            FileOutputStream outStream = new FileOutputStream("outfile01.txt");
            PrintStream printStream = new PrintStream(outStream);
            scanner.useDelimiter(pattern);
            while(scanner.hasNext()){
                content = scanner.next();
                Matcher matcher = pattern.matcher(content);     // find lines with matching pattern
                content = matcher.replaceAll("\n");             // replace matching pattern with newline
                printStream.println(content);               // print output to file
            }
            printStream.close();    // close output stream
            scanner.close();        // close scanner
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        return "outfile01.txt";     // return the output file name
    }
    // method to extract data from txt file and save into another output file
    // return new output file name
    static String extractDataFromTxt(String inputFile) throws Exception {
        // prepare Scanner object for reading input file
        String s = "";
        File f = new File(inputFile);
        Scanner scanner = new Scanner(f);
        // prepare objects for File output stream and Print output stream
        FileOutputStream out = new FileOutputStream("outfile02.txt");
        PrintStream outprint = new PrintStream(out);
        // string for searching pattern creation
        String regexe = "\\b[1-9][0-9]+\\b";
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
        return "outfile02.txt"; // return outfile name
    }
    // find the specific substring
    static String findSubstring(String s){
        int beginIndex = s.indexOf("\">");
        int endIndex = s.indexOf(")</a>");
        if(endIndex <= beginIndex){
            return null;
        }
        return s.substring(beginIndex+2, endIndex+1);
    }
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
            String output, temp, tempUrl, txtFileName = null;
            tokenList = input.nextLine().replace(" (", " ").replace(", ", " ").replace(")", " ").split(" ");
            tempUrl = getAddressUrl(addressUrl, tokenList);
            output = "" + tokenList[0] + " " + tokenList[1] + 
                    "\t" + tokenList[2] + " " + tokenList[3] + " " + ", " + tokenList[4];
            output += "\t"+path+"\\InfoUnionAddress_"+index.toString()+".txt";
            txtFileName = output;
            outprint.println(output);
            for(int i=0; i<tokenList.length;i++) System.out.println(i + "  " + tokenList[i]);
            System.out.println(tempUrl);
            System.out.println(output);
            break;
        }
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