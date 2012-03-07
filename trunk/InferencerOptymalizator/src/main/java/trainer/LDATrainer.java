package trainer;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.InstanceList;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/16/12
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class LDATrainer {
    ArrayList<String> a;
    ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
    private String trainingPath;
    private double docTopicsThreshold=0.0;
    private int docTopicsMax =-1;
    private String docTopicFilename;
    private String inferencerFilename;


    public void run() throws IOException, UnsupportedEncodingException {

        pipeList.add(new CharSequenceLowercase());

        pipeList.add(new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{M}]+")));

        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));

        Reader fileReader = new InputStreamReader(new FileInputStream(new File(trainingPath)), "UTF-8");
        instances.addThruPipe(new CsvIterator(fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                3, 2, 1)); // data, label, name fields


        //topicModel = new ParallelTopicModel(numTopics.value, alpha.value, beta.value);
        int numTopics = 2;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(50);
        model.estimate();

        //ArrayList<TopicAssignment> topicAssignments = model.getData();
        //model.getTopicProbabilities()

        PrintWriter out = new PrintWriter(docTopicFilename);
        model.printDocumentTopics(out, docTopicsThreshold, docTopicsMax);
        out.close();

        ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(inferencerFilename));
        oos.writeObject(model.getInferencer());
        oos.close();


    }
}
