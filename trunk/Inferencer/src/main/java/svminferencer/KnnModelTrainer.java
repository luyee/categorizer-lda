package svminferencer;

import util.CosineDistance;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/25/12
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class KnnModelTrainer extends WekaModelTrainer {


    private final int k;

    public KnnModelTrainer(String trainingCsvPath, int numCategories) {
        super(trainingCsvPath, numCategories);
        k = 12;
    }

    public KnnModelTrainer(String trainingCsvPath, int numCategories, int k) {
        super(trainingCsvPath, numCategories);
        this.k = k;
    }

    @Override
    protected void createClassifier() throws Exception {
        IBk ibk = new IBk(k);
        //ibk.getNearestNeighbourSearchAlgorithm().setDistanceFunction(new CosineDistance());
        classifier = ibk;


    }

    @Override
    protected Classifier castToProprerClassifier(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (IBk) ois.readObject();
    }

    @Override
    public String getSaveSuffix() {
        return "/KNNModel.weka";
    }
}
