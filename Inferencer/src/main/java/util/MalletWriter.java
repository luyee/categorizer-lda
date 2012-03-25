package util;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.InstanceList;

import java.io.*;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Pattern;

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

    public static InstanceList createInstances(File fileToBeInferenced,
                                               InstanceList trainingInstanceList) throws IOException, UnsupportedEncodingException {

        Pipe instancePipe;
        instancePipe = trainingInstanceList.getPipe();

        //
        // Create the instance list and open the input file
        //
        InstanceList instances = new InstanceList(instancePipe);
        Reader fileReader;

        fileReader = new InputStreamReader(new FileInputStream(fileToBeInferenced), "UTF-8");


        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1));

        return instances;

    }

    public static InstanceList createInsatnceList(
            Vector<String> words,
            InstanceList trainingInstanceList) throws IOException {
        return  MalletWriter.createInstances(
                MalletWriter.addToCvs(words),
                trainingInstanceList);
    }

}
