import evaluator.*;
import ldainference.Inference;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/10/12
 * Time: 5:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EvaluatorRunner {
    public static final String INFERENCER = "inferencer";
    public static final String TRAINING = "training";
    private static final String FILE_TO_INFERENCE = "input";
    private static final String DOCPERTOPIC = "docpertopic";

    public static void main(String[] args) throws Exception {
        Option infFileOpt = OptionBuilder.withArgName(FILE_TO_INFERENCE)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of file to perform inference on")
                .create(FILE_TO_INFERENCE);


        Option inferencerModelOpt = OptionBuilder.withArgName(INFERENCER)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of file to perform inference on")
                .create(INFERENCER);

        Option trainingFileOpt = OptionBuilder.withArgName(TRAINING)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of file to perform inference on")
                .create(TRAINING);

        Option docPerTopicOpt =
                OptionBuilder.withArgName(DOCPERTOPIC)
                        .hasArg()
                        .isRequired()
                        .withDescription("specify the location of file to perform inference on")
                        .create(DOCPERTOPIC);


        Options options = new Options();

        options.addOption(infFileOpt);
        options.addOption(docPerTopicOpt);
        options.addOption(inferencerModelOpt);
        options.addOption(trainingFileOpt);

        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            String fileToInferenceName = line.getOptionValue(FILE_TO_INFERENCE);
            File fileToInference = new File(fileToInferenceName);
            if (!fileToInference.exists())
                throw new IOException("no file to be inferenced: " + fileToInference.getAbsolutePath());

            String fileDocTopicName = line.getOptionValue(DOCPERTOPIC);
            File docPerTopicFile = new File(fileDocTopicName);
            if (!docPerTopicFile.exists())
                throw new IOException("no docpertopic file");


            File inferenceFile = new File(line.getOptionValue(INFERENCER));
            if (!inferenceFile.exists())
                throw new IOException("no inferencer File");

            File trainingFile = new File(line.getOptionValue(TRAINING));
            if (!trainingFile.exists())
                throw new IOException("no training file");

            Inference inference = new Inference(trainingFile, inferenceFile, docPerTopicFile);

            DataLoader dataLoader= new DataLoader(fileToInference.getAbsolutePath());
            Vector<String> data = dataLoader.getEvaluationInsatnces();
            Evaluator evaluator = new Evaluator(data,inference);
            evaluator.evaluate();


            RaportWriterInterface raportWriter = new SmallRaportWriter();

            raportWriter.writeRaport(evaluator,System.out);
            inference.close();



        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
    }
}
