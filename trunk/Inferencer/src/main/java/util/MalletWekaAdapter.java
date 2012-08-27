package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
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
    private static final String TEXT ="Text";
    public static final String NAME_OF_DATASET = "malletToWeka";

    public Instances toInstances(InstanceList instanceList) {
        CategoriesGetter categoriesGetter = new CategoriesGetter();
        return toInstances(categoriesGetter.getCategories(instanceList), instanceList);
    }

    public StringToWordVector getFilter() {
        return filter;
    }

    /**
     * TODO: NOT unit tested; too much fucking with weka
     *
     * @param instanceList
     */
    public Instances toInstances(Collection<String> categories, InstanceList instanceList) {

        MalletInstanceToWekaInstance instanceAdapter = new MalletInstanceToWekaInstance();

        Instances instances = instanceAdapter.createWekaInstances(categories, TEXT, NAME_OF_DATASET);

        for (Instance malletInstance : instanceList) {
            Attribute textAtt = instances.attribute(TEXT);
            weka.core.Instance wekaInstance = new weka.core.Instance(2);

            wekaInstance.setDataset(instances);

            instanceAdapter.setText(textAtt, malletInstance, wekaInstance);

            instanceAdapter.setClass(malletInstance, wekaInstance);

            instanceAdapter.insert(wekaInstance, instances);
        }

        return instances;

    }



    public weka.core.Instance toInferenceInstance(Instance malletInstance,
                                                  Instances wekaTrainingInstances,
                                                  StringToWordVector filter) throws Exception {

        Instances testset = wekaTrainingInstances.stringFreeStructure();

        // Create instance of length two.
        weka.core.Instance instance = new weka.core.Instance(2);

        // Set value for message attribute
        Attribute messageAtt = testset.attribute(TEXT);
        InstanceDecoder instanceDecoder = new InstanceDecoder();
        instance.setValue(messageAtt, messageAtt.addStringValue(
                instanceDecoder.getTextAsFeatures(malletInstance)));

        // Give instance access to attribute information from the dataset.
        instance.setDataset(testset);

        // Filter instance.
        filter.input(instance);

        return filter.output();

    }

    public Instances tfidf(Instances instances) throws Exception {
        filter = new StringToWordVector();
        filter.setIDFTransform(true);
        // Initialize filter and tell it about the input format.
        filter.setInputFormat(instances);

        // Generate word counts from the training data.
        return Filter.useFilter(instances, filter);

    }
}
