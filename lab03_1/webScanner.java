import java.net.*;
import java.util.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class webScanner{
    public static void main(String[] args) {
        String content = "";
        String temp = "";
        URLConnection connection = null;
        Pattern pattern = Pattern.compile("class=\"article\"><a href=");
        try {
            connection =  new URL("https://www.infoplease.com/homework-help/history/collected-state-union-addresses-us-presidents").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            FileOutputStream output = new FileOutputStream("out2.txt");
            PrintStream out = new PrintStream(output);
            scanner.useDelimiter(pattern);
            while(scanner.hasNext()){
                content = scanner.next();
                Matcher matcher = pattern.matcher(content);
                temp = matcher.replaceAll("\n");
                out.println(temp);
            }
            // content = scanner.next();
            // Matcher matcher = pattern.matcher(content);
            // temp = matcher.replaceAll("\n");
            // System.out.println(temp);
            //temp = scanner.next();
            // if((content = scanner.next()).contains("<dt><span xmlns=\"\" class=\"article\"><div>")){
            // while(scanner.hasNext()){
            //     content = scanner.next();
            //     out.println(content);
            //     Matcher matcher = pattern.matcher(content);
            //     if(matcher.find()){
            //         System.out.println(matcher.start());
            //     }
            //     else{
            //         System.out.println("no match");
            //     }    
            // }
            // content += scanner.next();
            // out.println(content);
            // Matcher matcher = pattern.matcher(content);
            // if(matcher.find()){
            //     System.out.println(matcher.start());
            // }
            // else{
            //     System.out.println("no match");
            // }
            
            
                // System.out.println(content);               
            
            // content = scanner.next();
            // out.print(content);
            out.close();
            scanner.close();
        }catch ( Exception ex ) {
            ex.printStackTrace();
        }
        //System.out.println(content);
        

    }
}

