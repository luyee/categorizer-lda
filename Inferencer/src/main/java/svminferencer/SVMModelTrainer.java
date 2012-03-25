package svminferencer;

import cc.mallet.types.InstanceList;
import weka.classifiers.functions.SMO;
import weka.core.Instances;

import java.io.*;
import java.util.Set;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/24/12
 * Time: 7:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SVMModelTrainer {
    private File arrfTrainingFile;
    private final InstanceListToArff instanceListToArff;
    private final InstanceList trainingInstanceList;
    private final SMO smo;
    private  Set<String> categoriesNames;

    public SVMModelTrainer(InstanceList instanceList){
        this.trainingInstanceList = instanceList;
        instanceListToArff = new InstanceListToArff();
        smo = new SMO();
    }

    public SMO getSmo() {
        return smo;
    }

    public Set<String> getCategoriesNames() {
        return categoriesNames;
    }

    public void  createArffTrainingFile() throws IOException {
        arrfTrainingFile = File.createTempFile(UUID.randomUUID().toString(), ".arff");
        //arrfTrainingFile.deleteOnExit();
        System.out.println("arf file : " + arrfTrainingFile.getAbsolutePath());
        PrintWriter pw = new PrintWriter(arrfTrainingFile);
        instanceListToArff.convert2ARFF(trainingInstanceList, pw, "trainingFile");
        categoriesNames = instanceListToArff.getCategoriesNames();
        pw.flush();
        pw.close();
    }

    public void train() throws Exception {
        createArffTrainingFile();
        BufferedReader reader = new BufferedReader(
                new FileReader(arrfTrainingFile));
        Instances data = new Instances(reader);
        reader.close();
        data.setClassIndex(data.numAttributes() - 1);

        smo.buildClassifier(data);


    }
}
