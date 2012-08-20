package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

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

    /**
     * TODO not tested. look to test file
     */
    static class CategoriesGetter{

        InstanceDecoder decoder;

        CategoriesGetter() {
            this.decoder = decoder = new InstanceDecoder();
        }

        public Collection<String> getCategories(InstanceList instanceList){
            Set<String> categories = new HashSet<String>();
            for(Instance instance : instanceList){
                categories.add(decoder.getCategory(instance));
            }
            return categories;
        }
    }

    public MalletInstanceToWekaInstance() {
        decoder = new InstanceDecoder();
    }

    protected void insert(weka.core.Instance wekaInstance,Instances instances){
        // Give instance access to attribute information from the dataset.

        // Add instance to training data.
        instances.add(wekaInstance);
    }

    protected weka.core.Instance create(Attribute textAtt,
                                        Instance malletInstance, Instances instances) {

        String instanceText = decoder.getTextAsFeatures(malletInstance);

        // Create instance of length two.  T
        weka.core.Instance wekaInstance = new weka.core.Instance(2);

        // Set value for text attribute
        wekaInstance.setValue(textAtt, textAtt.addStringValue(instanceText));

        String classValue =decoder.getCategory(malletInstance);

        wekaInstance.setDataset(instances);

        // Set class value for instance.
        wekaInstance.setClassValue(classValue);

        return wekaInstance;
    }



    protected Instances createWekaInstances(Collection<String> categories, String text, String nameOfDataset) {
        // Create vector of attributes.
        FastVector attributes = new FastVector(2);

        // Add attribute for holding text.
        attributes.addElement(new Attribute(text, (FastVector)null));

        // Add class attribute.
        FastVector classValues = new FastVector();
        CategoriesGetter categoriesGetter = new CategoriesGetter();
        for (String category: categories){
            classValues.addElement(category);
        }
        attributes.addElement(new Attribute("Class", classValues));

        // Create dataset with initial capacity of 100, and set index of class.
        Instances instances = new Instances(nameOfDataset, attributes, 100);

        //remains of old code
        assert(instances.numAttributes()==2);
        instances.setClassIndex(1);
        return instances;
    }


}
