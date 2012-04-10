package svminferencer;

import weka.classifiers.Classifier;
import weka.classifiers.functions.SMO;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/24/12
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SVMModelTrainer extends WekaModelTrainer {


    public SVMModelTrainer(String trainingCsvPath, int numCategories){
        super(trainingCsvPath, numCategories);
        this.modelFromFile=false;
    }


    @Override
    protected void createClassifier() throws Exception {
        classifier = new SMO();

    }

    @Override
    protected Classifier castToProprerClassifier(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        return (SMO) ois.readObject();
    }

    @Override
    public String getSaveSuffix() {
        return "/SVMModel.weka";
    }
}
