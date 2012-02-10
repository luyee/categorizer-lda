package DistClasificator;

import java.io.*;
import java.util.Map;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class DistanceClassifier {
    private Categories categories;

    public DistanceClassifier(){

    }

    public void readDocTopicasStream(InputStream resourceAsStream) throws IOException {
        DocsTopicsLoader docsTopicsLoader = new DocsTopicsLoader(resourceAsStream);
        Map<String,Map<Integer,Double>> itMaped = docsTopicsLoader.getItMaped();
        categories = new Categories(itMaped);

    }


    public Categories getCategories() {
        return categories;
    }

    public Vector<Category> classifyDouble(double[] weights) {
        return new Vector<Category>(categories.assignCategories(weights));

    }

    public Vector<Category> classify(Category cat) {
       return new Vector<Category>(categories.assignCategories(cat.getWeights()));
        
    }
    
    public Vector<String> classifyStr(Category cat){
        Vector<Category> categories1 = classify(cat);
        Vector<String> str = new Vector<String>();
        for (Category cats : categories1){
            str.add(cats.getCategoryName());
        }
        return str;
    }

    public void readDocTopicasFile(File docPerTopic) throws IOException {
        InputStream inputStream = new FileInputStream(docPerTopic);
        readDocTopicasStream(inputStream);
    }
}
