import cc.mallet.types.InstanceList;
import ldainference.Inference;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class InferenceCommandLineUtil {
    public static final String OUTPUT = "output";
    public static final String INFERENCER = "inferencer";
    public static final String TRAINING = "training";
    private static final String FILE_TO_INFERENCE = "input";


    public static void main(String args[]) throws Exception {


        Option infFileOpt = OptionBuilder.withArgName(FILE_TO_INFERENCE)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of file to perform inference on")
                .create(FILE_TO_INFERENCE);

        Option outputFileOptOpt = OptionBuilder.withArgName(OUTPUT)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of file to perform inference on")
                .create(OUTPUT);

        Option inferencerModelOpt = OptionBuilder.withArgName(INFERENCER)
                .hasArg()
                .withDescription("specify the location of file to perform inference on")
                .create(INFERENCER);

        Option trainingFileOpt = OptionBuilder.withArgName(TRAINING)
                .hasArg()
                .withDescription("specify the location of file to perform inference on")
                .create(TRAINING);


        Options options = new Options();

        options.addOption(infFileOpt);
        options.addOption(outputFileOptOpt);
        options.addOption(inferencerModelOpt);
        options.addOption(trainingFileOpt);

        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            String fileToInferenceName = line.getOptionValue(FILE_TO_INFERENCE);
            File fileToInference = new File(fileToInferenceName);
            if (!fileToInference.exists())
                throw new IOException("no inference File");

            File outputFile = new File(line.getOptionValue(OUTPUT));

            Inference inference;

            if (line.hasOption(INFERENCER) || line.hasOption(TRAINING)) {
                File inferenceFile = new File(line.getOptionValue(INFERENCER));
                if (!inferenceFile.exists())
                    throw new IOException("no inference File");

                File trainingFile = new File(line.getOptionValue(TRAINING));
                if (!trainingFile.exists())
                    throw new IOException("no training file");

                inference = new Inference(trainingFile, outputFile, inferenceFile, fileToInference);
            } else {
                inference = new Inference(outputFile, fileToInference);
            }

            InstanceList instances = inference.createInstances();
            inference.inference(instances);


            //aaaaa

        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }

    }
}
