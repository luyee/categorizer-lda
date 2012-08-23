package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import svminferencer.WekaModelTrainer;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Collection;

import static junit.framework.Assert.assertNotNull;
import static util.MalletInstanceToWekaInstance.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class MalletWekaAdapter {

    private StringToWordVector filter;

    public Instances toInstances(InstanceList instanceList){
        CategoriesGetter categoriesGetter = new CategoriesGetter();
        return toInstances(categoriesGetter.getCategories(instanceList),instanceList);
    }

    public StringToWordVector getFilter() {
        return filter;
    }

    /**
     *TODO: NOT unit tested; too much fucking with weka
     * @param instanceList
     */
    public Instances toInstances(Collection<String> categories,InstanceList instanceList){
        String text = "Text";
        String nameOfDataset = "malletToWeka";

        MalletInstanceToWekaInstance instanceAdapter = new MalletInstanceToWekaInstance();

        Instances instances = instanceAdapter.createWekaInstances(categories,text, nameOfDataset);

        for (Instance malletInstance: instanceList){
            Attribute textAtt = instances.attribute(text);
            weka.core.Instance wekaInstance = instanceAdapter.create(textAtt, malletInstance,instances);
            instanceAdapter.insert(wekaInstance,instances);
        }

        return instances;

    }


    public weka.core.Instance toInferenceInstances(Instance malletInstance , WekaModelTrainer modelTrainer) throws Exception {
        Instances trainingInstances =modelTrainer.getWekaTrainingInstances();

        Attribute messageAtt1 = trainingInstances.attribute("Text");
        assertNotNull(messageAtt1);

        Instances testset = trainingInstances.stringFreeStructure();

        Classifier classifier = modelTrainer.getClassifier();

        // Create instance of length two.
        weka.core.Instance instance = new weka.core.Instance(2);

        // Set value for message attribute
        Attribute messageAtt = testset.attribute("Text");
        InstanceDecoder instanceDecoder = new InstanceDecoder();
        instance.setValue(messageAtt, messageAtt.addStringValue(instanceDecoder.getTextAsFeatures(malletInstance)));

        // Give instance access to attribute information from the dataset.
        instance.setDataset(testset);

        StringToWordVector filter = modelTrainer.getFilter();


        // Filter instance.
        filter.input(instance);
        weka.core.Instance filteredInstance = filter.output();

        return filteredInstance;
//
//        // Get index of predicted class value.
//        double predicted = classifier.classifyInstance(filteredInstance);
//
//        // Output class value.
//        System.err.println("Message classified as : " +
//                trainingInstances.classAttribute().value((int) predicted));

    }

    public Instances tfidf(Instances instances) throws Exception {
        filter = new StringToWordVector();
        // Initialize filter and tell it about the input format.
         filter.setInputFormat(instances);

        // Generate word counts from the training data.
        return Filter.useFilter(instances, filter);

    }
}
