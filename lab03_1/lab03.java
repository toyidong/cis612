import java.util.*;
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class lab03 {
    public static void main(String[] args) throws Exception {
        String url = "https://www.infoplease.com/homework-help/history/collected-state-union-addresses-us-presidents";
        // prepare a pattern for substring search
        Pattern pattern = Pattern.compile("class=\"article\"><a href=");
        // prepare outfile name 
        String contentFile = "extractData.txt";
        // File f = File.createTempFile("extractData", ".txt");
        // extract content from URL, by matched pattern
        getMatchedContentFromURL(url, pattern, contentFile);
        TimeUnit.MILLISECONDS.sleep(100);
        // create CSV file and txt files with address 
        createCSVandAddressTxtFiles(contentFile);
        TimeUnit.MILLISECONDS.sleep(100);

        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
        
    }
    /* method to retrieve content from webpage and process data for first time */
    static void getMatchedContentFromURL(String url, Pattern pattern, String contentFile) throws Exception {
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
            String regexe = "\\b[1-9][0-9]+\\b";
            String s = "class=\"article\"><a href=";
            scanner.useDelimiter(pattern);
            while(scanner.hasNextLine()){
                content = scanner.nextLine();
                if(content.contains(s)){
                    content = content.replace("class=\"article\"><a href=", "\n");
                    printStream.println(content);               // print output to file
                }               
            }
            printStream.close();    // close output stream
            scanner.close();        // close scanner
            extractDataFromTxt("temp.txt", contentFile, regexe);
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        temp.delete(); // after job done, delete temp file
        TimeUnit.MILLISECONDS.sleep(100);
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
        TimeUnit.MILLISECONDS.sleep(100);
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
        String addressUrl = "https://www.infoplease.com/homework-help/us-documents/state-union-address";
        // prepare objects for File output stream and Print output stream
        FileOutputStream out = new FileOutputStream("table.csv");
        PrintStream outprint = new PrintStream(out);
        outprint.println("Name of President\tDate of Unoin Address\tLink to Address\tFilename_Address\tText of Address\t");

        File inFile = new File(inputFile);
        Scanner input = new Scanner(inFile);
        Integer index = 1;
        FileOutputStream bigFile = new FileOutputStream("bigFile.txt");
        PrintStream printBigFile = new PrintStream(bigFile);
        printBigFile.print("");
        printBigFile.close();
        while(input.hasNextLine()){
            String[] tokenList = null;
            String output = "", tempUrl = "", txtFileName = "";
            String startLine = input.nextLine();
            tokenList = startLine.replace(" (", " ").replace(", ", " ").replace(")", " ").split(" ");
            tempUrl = getAddressUrl(addressUrl, tokenList);
            if(tokenList[tokenList.length-1].contains("1843")){
                output += tokenList[0] + " " + tokenList[1] + 
                "\t" + tokenList[2] + " " + tokenList[3];
            }
            else if(tokenList.length>5){
                for(int i=0;i<3;i++) output += tokenList[i] + " ";
                output += "\t";
                output += tokenList[3] + " " + tokenList[4] + ", " + tokenList[5];
            }
            else{
                output += tokenList[0] + " " + tokenList[1] + 
                    "\t" + tokenList[2] + " " + tokenList[3] + ", " + tokenList[4];
            }        
            txtFileName = "InfoUnionAddress_"+index.toString()+".txt";
            index++;
            TimeUnit.MILLISECONDS.sleep(100);
            File temp1 = new File(txtFileName);
            FileWriter tempWrite = new FileWriter(temp1);
            tempWrite.write("");
            tempWrite.close();
            TimeUnit.MILLISECONDS.sleep(100);
            
            output += "\t" + path+"/"+txtFileName;
            getAddressContentFromURL(tempUrl, txtFileName, startLine);
            TimeUnit.MILLISECONDS.sleep(100);

            outprint.println(output);
            // if(index > 50)break;
        }
        input.close();
        outprint.close();
    }
    static void getAddressContentFromURL(String url, String txtFileName, String startLine) throws Exception {
        try{
        String content = "";
        URLConnection connection = null;
        File temp = new File(txtFileName);
        connection =  new URL(url).openConnection();
            
                Scanner scanner = new Scanner(connection.getInputStream());
             
                FileOutputStream outStream = new FileOutputStream(temp);
                PrintStream printStream = new PrintStream(outStream);
                printStream.println(startLine);
                BufferedWriter bw = new BufferedWriter(new FileWriter("bigFile.txt", true));
                bw.write(startLine);
                bw.write("\n");
                bw.flush();
                       
        while(scanner.hasNextLine()){
            TimeUnit.MILLISECONDS.sleep(100);
            // int i = 0;
            // if(i<200){
            //     i++;
            //     content = scanner.nextLine();
            //     continue;
            // }
            content = scanner.nextLine();
            if(content.contains(" </p></div><div class=")){
                String begin = "</h2></div></div></div><p> ";
                String end = " </p></div><div class=\"navfooter\">";
                String change = " </p><p> ";
                // int index = content.IndexOf(s);
                int beginIndex, endIndex = 0;
                beginIndex = content.indexOf(begin) + begin.length();
                endIndex = content.indexOf(end);
                System.out.println(beginIndex + " " + endIndex);
                String paragraph = content.substring(beginIndex, endIndex).replaceAll(change, "\n");
                bw.write(paragraph);
                bw.write("\n");
                bw.flush();
                TimeUnit.MILLISECONDS.sleep(100);
                printStream.println(paragraph); // print output to file
                TimeUnit.MILLISECONDS.sleep(100);
            }                      
        }

            printStream.close(); // close output stream
            scanner.close();   // close scanner
            bw.close();
        //temp.delete(); // after job done, delete temp file
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println(startLine + "  Warning: ArrayIndexOutOfBoundsException");
            return;
         }
         catch(FileNotFoundException e){
            System.out.println(startLine + "  Warning: GetInputStreamFileNotFoundException" + "\t"+ url);
            return;
         }
        // catch (Exception ex){
        //     System.err.println(startLine + " " + Trace);
        //     return;
        // }
    }

    static String getAddressUrl(String url, String[] tokenList){
        if(tokenList.length > 5){
            String newString = "";
            for(int i=0;i<tokenList[1].length();i++){
                if(tokenList[1].charAt(i) !='.') newString += tokenList[1].charAt(i);
            }
            tokenList[1] = newString;
        }
        for(int i=0; i<tokenList.length;i++)
            url += "-" + tokenList[i];
        if(url.contains(".-")) url.replaceAll(".-", "-");
        return url;
    }
    static String getOutputForCSV(String[] tokenList){
        String s = "";
        for(int i=0;i<tokenList.length-1;i++) s += "\t";
        return s;
    }
}