package svminferencer;

import cc.mallet.types.InstanceList;
import interfaces.AbstractInferencer;
import org.apache.log4j.Logger;
import util.MalletWekaAdapter;
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
public class WekaInferencer extends AbstractInferencer {

    
    private final InstanceListToArff instanceListToArff;
    private File arrfToBeClassifedFile;
    private InstanceList toBeClassifiedInstanceList;
    private final InstanceList trainingInstanceList;
    private final Classifier classifier;
    private final int  numCategories;
    private Set<String> categoriesNames;
    private Logger logger = Logger.getLogger(WekaInferencer.class);


    public WekaInferencer(InstanceList trainingInstanceList,
                          Classifier classifier,
                          Set<String> categoiresNames, int numCategories) {
        this.trainingInstanceList = trainingInstanceList;
        this.classifier = classifier;
        this.numCategories = numCategories;
        this.categoriesNames = new HashSet<String>(categoiresNames);
        instanceListToArff = new InstanceListToArff(categoiresNames,trainingInstanceList);

    }
    

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

        MalletWekaAdapter malletWekaAdapter = new MalletWekaAdapter();


        Instances unlabeled = malletWekaAdapter.toInstances(categoriesNames,toBeClassifiedInstanceList);
        unlabeled = malletWekaAdapter.tfidf(unlabeled);



        // set class attribute

        int numAtrs = unlabeled.numAttributes() - 1;
        assert(numAtrs==1);

        unlabeled.setClassIndex(numAtrs);

        // create copy
        Instances labeled = new Instances(unlabeled);



        logger.debug("sample instance "+unlabeled.instance(0).toString());
        // Get index of predicted class value.
        double predicted = classifier.classifyInstance(labeled.instance(0));

        // Output class value.
        logger.debug("Message classified as : " +
                labeled.classAttribute().value((int)predicted));

        Vector<String> ret = new Vector<String>();
        double [] distribution;
        Map<Double,Integer> map = new TreeMap<Double,Integer>();
        for (int i = 0; i < unlabeled.numInstances(); i++) {

            //double clsLabel = classifier.classifyInstance(unlabeled.instance(i));

            distribution = classifier.distributionForInstance(unlabeled.instance(i));
            //labeled.instance(i).setClassValue(clsLabel);

            for( int j=0;j<distribution.length;j++ ){

                map.put(distribution[j],j);
            }


            for(Double d: map.keySet()){

                ret.add(unlabeled.classAttribute().value((int) map.get(d))) ;

            }
            Collections.reverse(ret);

            while (ret.size() < numCategories)
                ret.add(labeled.instance(i).toString(numAtrs));


        }

        logger.debug(Arrays.toString(ret.toArray()));
        return ret;
    }
}
