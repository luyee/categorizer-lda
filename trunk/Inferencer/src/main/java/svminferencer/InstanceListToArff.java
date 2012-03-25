package svminferencer;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/22/12
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceListToArff {

    private Set<String> categoriesNames;
    private InstanceList training;
    private int size2=-1;

    public InstanceListToArff() {
        categoriesNames = null;
    }


    public InstanceListToArff(Set<String> categoriesNames,
                              InstanceList training) {
        this.categoriesNames = categoriesNames;
        this.size2 = training.getDataAlphabet().size();

    }

    public Set<String> getCategoriesNames() {
        return categoriesNames;
    }




    public void convert2ARFF(InstanceList instances, PrintWriter pWriter, String description) {

        Alphabet dataAlphabet = instances.getDataAlphabet();
        Alphabet targetAlphabet = instances.getTargetAlphabet();

        pWriter.write("@Relation " + description + "\n\n");

        int size = dataAlphabet.size();



        if (size2!=-1){
            size=size2;
        }



        for (int i = 0; i < size; i++) {
            pWriter.write("@attribute " +
                    dataAlphabet.lookupObject(i).toString().replaceAll("\\s+", "_")
                    + " NUMERIC\n");
        }


        pWriter.write(classAtr(instances) + "\n");



        pWriter.write("\n\n@data\n");

        String cat;

        int[] featuresCnt = new int[size];
        for (Instance instance : instances) {
//            System.out.println(instance.getSource().toString()+ " source");
            //System.out.println(instance.getName().toString() + " name");

            FeatureSequence fs = (FeatureSequence) instance.getData();

            atrrsVector(fs, dataAlphabet, featuresCnt);
            for (int i = 0; i < size; i++) {
                pWriter.write(featuresCnt[i] + ",");
            }


            cat = getCategory(instance);
            if (cat==null)
                cat = (String)categoriesNames.toArray()[0];
            pWriter.write( cat+ "\n");
        }
        pWriter.flush();
        pWriter.close();
    }


    private String getCategory(Instance instance) {

        String[] split = instance.getName().toString().split("_");
        if (split.length > 1)
            return split[1];
        return null;
    }

    private String classAtr(InstanceList instanceList) {
        String beg = "@attribute class {";
        if (categoriesNames == null) {
            categoriesNames = new HashSet<String>();
            for (Instance i : instanceList) {
                categoriesNames.add(getCategory(i));
            }
        }
        for (String categorie : categoriesNames) {
            beg += " " + categorie + ",";
        }
        return beg.substring(0, beg.length() - 1) + "}";


    }

    private void atrrsVector(FeatureSequence fs, Alphabet dataAlphabet, int[] ret) {
        for (int i = 0; i < ret.length; i++) ret[i] = 0;

        int[] features = fs.getFeatures();
        for (int i = 0; i < features.length; i++) {
            if (features[i]< ret.length){
                ret[features[i]] += 1;
            }
        }
    }
}
