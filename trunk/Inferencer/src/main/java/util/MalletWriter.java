package util;

import java.io.*;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 7:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MalletWriter {
   



    public static File addToCvs(Vector<String> words) throws IOException {
        File csvFile = File.createTempFile(UUID.randomUUID().toString(), ".malletCsv");
        csvFile.deleteOnExit();
        BufferedWriter out;
        out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(csvFile), "UTF-8"));

        out.write(1);
        out.write("\t");
        out.write("-");
        out.write("\t");
        for (String token : words){
            out.write(token+" ");
        };
        out.write(".\n");
        out.close();
        return csvFile;
    }


}
