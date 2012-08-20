package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import weka.core.Attribute;
import weka.core.Instances;

import static util.MalletInstanceToWekaInstance.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class MalletWekaAdapter {



    /**
     *TODO: NOT unit tested too much fucking with weka
     * @param instanceList
     */
    public Instances toInstances(InstanceList instanceList){
        String text = "Text";
        String nameOfDataset = "malletToWeka";

        MalletInstanceToWekaInstance instanceAdapter = new MalletInstanceToWekaInstance();

        CategoriesGetter categoriesGetter = new CategoriesGetter();
        Instances instances = instanceAdapter.createWekaInstances(
                categoriesGetter.getCategories(instanceList), text, nameOfDataset);


        for (Instance malletInstance: instanceList){
            Attribute textAtt = instances.attribute(text);
            weka.core.Instance wekaInstance = instanceAdapter.create(textAtt, malletInstance,instances);
            instanceAdapter.insert(wekaInstance,instances);
        }

        return instances;

    }
}
