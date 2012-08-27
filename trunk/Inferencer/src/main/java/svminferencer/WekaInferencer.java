package svminferencer;

import cc.mallet.types.InstanceList;
import interfaces.AbstractInferencer;
import org.apache.log4j.Logger;
import util.MalletWekaAdapter;
import util.MalletWriter;
import weka.classifiers.Classifier;
import weka.core.Instance;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/22/12
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class WekaInferencer extends AbstractInferencer {


    private InstanceList toBeClassifiedInstanceList;
    private final InstanceList trainingInstanceList;
    private final Classifier classifier;
    private final int numCategories;
    private Logger logger = Logger.getLogger(WekaInferencer.class);
    private WekaModelTrainer wekaModelTrainer;


    public WekaInferencer(InstanceList trainingInstanceList,
                          Classifier classifier,
                          Set<String> categoiresNames, int numCategories,
                          WekaModelTrainer wekaModelTrainer) {
        this.trainingInstanceList = trainingInstanceList;
        this.classifier = classifier;
        this.numCategories = numCategories;
        this.wekaModelTrainer = wekaModelTrainer;

    }


    public Vector<String> inferenceCategories(Vector<String> instance) throws Exception {

        //Create Mallet instance List to be clasified
        toBeClassifiedInstanceList = MalletWriter.createInsatnceList(instance, trainingInstanceList);

        MalletWekaAdapter malletWekaAdapter = new MalletWekaAdapter();

        Instance unlabeled = malletWekaAdapter.toInferenceInstance(
                toBeClassifiedInstanceList.get(0), wekaModelTrainer.getWekaTrainingInstances(), wekaModelTrainer.getFilter());

        Vector<String> ret = new Vector<String>();
        double[] distribution;
        Map<Double, Integer> map = new TreeMap<Double, Integer>();


        //double clsLabel = classifier.classifyInstance(unlabeled.instance(i));

        distribution = classifier.distributionForInstance(unlabeled);
        //labeled.instance(i).setClassValue(clsLabel);

        for (int j = 0; j < distribution.length; j++) {

            map.put(distribution[j], j);
        }


        for (Double d : map.keySet()) {

            ret.add(unlabeled.classAttribute().value((int) map.get(d)));

        }
        Collections.reverse(ret);

        while (ret.size() < numCategories)
            ret.add(ret.get(ret.size() - 1));


        logger.debug(Arrays.toString(ret.toArray()));
        return ret;
    }
}
