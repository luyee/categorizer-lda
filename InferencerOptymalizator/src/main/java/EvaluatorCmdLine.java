import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.InstanceList;
import evaluator.DataLoader;
import evaluator.Evaluator;
import evaluator.RaportWriterInterface;
import evaluator.SmallRaportWriter;
import ldainference.Inferencer;
import ldainference.ModelTrainer;
import org.apache.commons.cli.*;
import svminferencer.SvmInferencer;

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
public class EvaluatorCmdLine {
    public static final String TRAINING = "training";
    private static final String FILE_TO_INFERENCE = "input";

    public static void main(String[] args) throws Exception {
        Option infFileOpt = OptionBuilder.withArgName(FILE_TO_INFERENCE)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of file to perform inference on")
                .create(FILE_TO_INFERENCE);


        Option trainingFileOpt = OptionBuilder.withArgName(TRAINING)
                .hasArg()
                .isRequired()
                .withDescription("specify the location of the csv file to perform training on")
                .create(TRAINING);



        Options options = new Options();

        options.addOption(infFileOpt);

        options.addOption(trainingFileOpt);

        CommandLineParser parser = new GnuParser();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            String fileToInferenceName = line.getOptionValue(FILE_TO_INFERENCE);
            File fileToInference = new File(fileToInferenceName);
            if (!fileToInference.exists())
                throw new IOException("no file to be inferenced: " + fileToInference.getAbsolutePath());


            File trainingFile = new File(line.getOptionValue(TRAINING));
            if (!trainingFile.exists())
                throw new IOException("no training file");

            //Inference inference = new Inference(trainingFile, inferenceFile, docPerTopicFile);

             Inferencer inferencer;
             ModelTrainer modelTrainer;
             SvmInferencer svmInferencer;
            modelTrainer = new ModelTrainer(trainingFile.getAbsolutePath(),100);

            modelTrainer.trainModel();

            File docPerTopic = modelTrainer.getDocPerTopic();
            TopicInferencer ldaInferencer = modelTrainer.getInderencer();
            InstanceList trainingInstanceList = modelTrainer.getTrainingInstanceList();

            //SVMModelTrainer svmModelTrainer = new SVMModelTrainer(trainingInstanceList);
            //svmModelTrainer.train();


            inferencer = new Inferencer(docPerTopic,ldaInferencer,trainingInstanceList);



            //Classifier svm =  svmModelTrainer.getSmo();
            //Set<String> categoriesNames = svmModelTrainer.getCategoriesNames();

            //svmInferencer = new SvmInferencer(trainingInstanceList,svm,categoriesNames, 2);

            DataLoader dataLoader= new DataLoader(fileToInference.getAbsolutePath());
            Vector<String> data = dataLoader.getEvaluationInsatnces();
            Evaluator evaluator = new Evaluator(data,inferencer);
            evaluator.evaluate();



            RaportWriterInterface raportWriter = new SmallRaportWriter();

            raportWriter.writeRaport(evaluator,System.out);
            //inference.close();



        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }
    }
}
