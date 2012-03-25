package ldainference;

import DistClasificator.Category;
import DistClasificator.DistanceClassifier;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import util.MalletWriter;

import java.io.*;
import java.util.Vector;
import java.util.regex.Pattern;

public class Inferencer {

    private final File docPerTopic;
    private final TopicInferencer inferencer;
    private final InstanceList trainingInstanceList;

    public Inferencer( File docPerTopic, TopicInferencer inferencer, InstanceList trainingInstanceList) {

        this.docPerTopic = docPerTopic;
        this.inferencer = inferencer;
        this.trainingInstanceList = trainingInstanceList;
    }

    public Vector<String> inferenceCategories(Vector<String> instance) throws Exception {
        
        InstanceList instances = MalletWriter.createInsatnceList(instance,trainingInstanceList);
        Category cats = infCategories(instances).get(0);
        DistanceClassifier distanceClassifier = new DistanceClassifier();

        distanceClassifier.readDocTopicasFile(docPerTopic);

        return distanceClassifier.classifyStr(cats);

    }

    private Vector<Category> infCategories(InstanceList instanceList) throws Exception {


        InstanceList instances = instanceList;

        int numIterations = 100;
        int sampleInterval = 10;
        int burnInIterations = 10;

        Vector<Category> vector = new Vector<Category>();
        for (Instance instance : instances) {

            double[] topicDistribution =
                    inferencer.getSampledDistribution(instance, numIterations,
                            sampleInterval, burnInIterations);
            vector.add(new Category(topicDistribution));
        }
        return vector;

    }

    public InstanceList createInstances(File fileToBeInferenced) throws IOException, UnsupportedEncodingException {

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
}