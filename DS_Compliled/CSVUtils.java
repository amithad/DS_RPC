//package CMS.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by ruchi on 2/3/17.
 */
public class CSVUtils {
    private static final char DEFAULT_SEPARATOR = ',';
    private static String CSVFile = "" + Paths.get("SearchResults.csv").toAbsolutePath();
    private static String CSVFile_node = "" + Paths.get("NodeResults.csv").toAbsolutePath();

    public static class NodeResults {

        int sentMessages=0;
        int forwardedMessages=0;
        int answeredMessages=0;
        int nodeDegree=0;


         public NodeResults(int sentMessages,int forwardedMessages,int answeredMessages,int nodeDegree){
            this.sentMessages=sentMessages;
            this.forwardedMessages=forwardedMessages;
            this.answeredMessages=answeredMessages;
            this.nodeDegree=nodeDegree;
        }
    }

    public static class SearchNodeResults {
        String fileName;
        long latency;
        int hopCount;

        public SearchNodeResults(String fileName, Long latency, int hopCount) {
            this.fileName = fileName;
            this.latency = latency;
            this.hopCount = hopCount;
        }
    }

    public static void writeNodeHeaderCSV(){
        try {
            FileWriter w = new FileWriter(CSVFile_node);
            writeLine(w, Arrays.asList("Time_Stamp"+"Sent","Forward","Answered",""));
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeResults(NodeResults nodeResults){
        try {
            FileWriter w = new FileWriter(CSVFile_node, true);
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            writeLine(w, Arrays.asList(timeStamp,""+nodeResults.sentMessages,nodeResults.forwardedMessages+"",""+nodeResults.answeredMessages,""+nodeResults.nodeDegree));
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeBreakCSV(){
        try {
            FileWriter w = new FileWriter(CSVFile, true);
            writeLine(w, Arrays.asList("-","-","-","-"));
            w.flush();
            w.close();
            w = new FileWriter(CSVFile_node, true);
            writeLine(w, Arrays.asList("-","-","-","-"));
            w.flush();
            w.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSearchHeader() {
        try {
            FileWriter w = new FileWriter(CSVFile);
            writeLine(w, Arrays.asList("Time_Stamp", "FileName", "Latency", "HopCount"));
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSearchResults(SearchNodeResults nodeResults) {
        try {
            FileWriter w = new FileWriter(CSVFile, true);
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            writeLine(w, Arrays.asList(timeStamp, "" + nodeResults.fileName, nodeResults.latency + "", "" + nodeResults.hopCount));
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    private static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    //https://tools.ietf.org/html/rfc4180

    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    private static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());

    }
}
