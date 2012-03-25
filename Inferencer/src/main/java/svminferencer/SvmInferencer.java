package svminferencer;

import cc.mallet.types.InstanceList;
import interfaces.AbstractInferencer;
import util.MalletWriter;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.io.*;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/22/12
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class SvmInferencer extends AbstractInferencer {

    
    private final InstanceListToArff instanceListToArff;
    private File arrfToBeClassifedFile;
    private InstanceList toBeClassifiedInstanceList;
    private final InstanceList trainingInstanceList;
    private final Classifier classifier;
    private final int  numCategories;


    public SvmInferencer(InstanceList trainingInstanceList,
                         Classifier classifier,
                         Set<String> categoiresNames, int numCategories) {
        this.trainingInstanceList = trainingInstanceList;
        this.classifier = classifier;
        this.numCategories = numCategories;
        instanceListToArff = new InstanceListToArff(categoiresNames,trainingInstanceList);
    }
    
                /*
                TODO REMOVE TMP FILES
                 */

    public void  createArffToBeClassifedFile() throws IOException {
        arrfToBeClassifedFile = File.createTempFile(UUID.randomUUID().toString(), ".arff");
        arrfToBeClassifedFile.deleteOnExit();
        //System.out.println("file to be inf :" + arrfToBeClassifedFile.getAbsolutePath());
        PrintWriter pw = new PrintWriter(arrfToBeClassifedFile);
        instanceListToArff.convert2ARFF(toBeClassifiedInstanceList, pw, "arrfToBeClassifedFile");
    }

    public Vector<String> inferenceCategories(Vector<String> instance) throws Exception {
        /*
        Create Mallet instance List to be clasified
         */
        //System.out.println("1: "+trainingInstanceList.getDataAlphabet().size());
        toBeClassifiedInstanceList = MalletWriter.createInsatnceList(instance,trainingInstanceList);
        //System.out.println("2: "+trainingInstanceList.getDataAlphabet().size());
        createArffToBeClassifedFile();
        //System.out.println("3: "+trainingInstanceList.getDataAlphabet().size());
        // load unlabeled data
        Instances unlabeled = new Instances(
                new BufferedReader(
                        new FileReader(arrfToBeClassifedFile)));

        // set class attribute
        int numAtrs = unlabeled.numAttributes() - 1;
        unlabeled.setClassIndex(numAtrs);

        // create copy
        Instances labeled = new Instances(unlabeled);

        // label instances

        Vector<String> ret = new Vector<String>();
        double [] distribution;
        Map<Double,Integer> map = new TreeMap<Double,Integer>();
        for (int i = 0; i < unlabeled.numInstances(); i++) {

            double clsLabel = classifier.classifyInstance(unlabeled.instance(i));

            distribution = classifier.distributionForInstance(unlabeled.instance(i));
            labeled.instance(i).setClassValue(clsLabel);

            for( int j=0;j<distribution.length;j++ ){

                map.put(distribution[j],j);
            }


            for(Double d: map.keySet()){

                ret.add(unlabeled.classAttribute().value((int) map.get(d))) ;
            }

            while (ret.size() < numCategories)
                ret.add(labeled.instance(i).toString(numAtrs));


        }
        
        return ret;
    }
}
