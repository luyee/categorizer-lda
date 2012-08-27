package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import org.apache.log4j.Logger;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class MalletInstanceToWekaInstance {

    InstanceDecoder decoder;
    private Logger logger = Logger.getLogger(MalletInstanceToWekaInstance.class);

    /**
     * TODO not tested. look to test file
     */
    public static class CategoriesGetter {

        InstanceDecoder decoder;

        public CategoriesGetter() {
            this.decoder = new InstanceDecoder();
        }

        public Collection<String> getCategories(InstanceList instanceList) {
            Set<String> categories = new HashSet<String>();
            for (Instance instance : instanceList) {
                categories.add(decoder.getCategory(instance));
            }
            return categories;
        }
    }

    public MalletInstanceToWekaInstance() {
        decoder = new InstanceDecoder();
    }

    protected void insert(weka.core.Instance wekaInstance, Instances instances) {
        // Add instance to training data.
        instances.add(wekaInstance);
    }


    void setClass(Instance malletInstance, weka.core.Instance wekaInstance) {
        String classValue = decoder.getCategory(malletInstance);

        wekaInstance.setClassValue(classValue);
    }

    protected void setText(Attribute textAtt, Instance malletInstance, weka.core.Instance wekaInstance) {
        String instanceText = decoder.getTextAsFeatures(malletInstance);
        // Set value for text attribute
        int value = textAtt.addStringValue(instanceText);
        wekaInstance.setValue(textAtt, value);
    }


    protected Instances createWekaInstances(Collection<String> categories, String text, String nameOfDataset) {
        // Create vector of attributes.
        FastVector attributes = new FastVector(2);

        // Add attribute for holding text.
        attributes.addElement(new Attribute(text, (FastVector) null));

        // Add class attribute.
        FastVector classValues = new FastVector(0);

        for (String category : categories) {
            classValues.addElement(category);
        }

        attributes.addElement(new Attribute("Class", classValues));

        // Create dataset with initial capacity of 100, and set index of class.
        Instances instances = new Instances(nameOfDataset, attributes, 100);

        //remains of old code
        assert (instances.numAttributes() == 2);
        instances.setClassIndex(1);
        return instances;
    }


}
