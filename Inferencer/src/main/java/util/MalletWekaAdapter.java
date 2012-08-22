package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import weka.core.Attribute;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Collection;

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

    public Instances tfidf(Instances instances) throws Exception {
        filter = new StringToWordVector();
        // Initialize filter and tell it about the input format.
         filter.setInputFormat(instances);

        // Generate word counts from the training data.
        return Filter.useFilter(instances, filter);

    }
}
