package evaluator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/9/12
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class EvaluationInstance {

    protected Vector<String> categoryNames ;
    protected String instanceName;
    protected Vector<String> data;
    protected Vector<String> inferencedCategories;
    protected Vector<Double> ref;

    /*
   line format:
   id_categoryName_instanceName categoryName subj subj subj ...
    */
    public EvaluationInstance(String line){
        String[] arg = line.split("\\s+");
        String[] ids = arg[0].split("_");
        categoryNames = new Vector<String>();
        categoryNames.add(ids[1]);
        instanceName = ids[2];
        data = new Vector(Arrays.asList(Arrays.copyOfRange(arg, 2, arg.length)));
    }


    public EvaluationInstance(
            Vector<EvaluationInstance> evaluationInstances,
            String instanceName,
            Vector<String> data){

        Vector<String> categories = extractCategories(evaluationInstances, instanceName);
        
        this.categoryNames = categories;
        this.data =data;
        this.instanceName = instanceName;
        
    }

    protected Vector<String> extractCategories(Vector<EvaluationInstance> evaluationInstances, String instanceName) {
        Vector<String> categories = new Vector<String>();

        for (EvaluationInstance evaluationInstance: evaluationInstances){
            if (evaluationInstance.getInstanceName().equals(instanceName)){
                categories.addAll(evaluationInstance.getCategoryNames());
            }
        }

        categories = new Vector<String>(new HashSet<String>(categories));
        return categories;
    }

    public EvaluationInstance() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public Vector<String> getInferencedCategories() {
        return inferencedCategories;
    }


    public Vector<String> getData() {
        return data;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public Vector<String> getCategoryNames() {
        return categoryNames;
    }

    public void setInferencedCategories(Vector<String> inferencedCategories) {
        this.inferencedCategories = inferencedCategories;
    }

    public double getCorrectPercentage(int referenceValue) {
        double ret =getCorrectPercentageStatic(
                referenceValue + getCategoryNames().size());
        this.ref.add(new Double(ret));
        return ret;
    }


    public double getCorrectPercentageStatic(int howMany) {
        howMany = java.lang.Math.min(howMany,inferencedCategories.size());
        int denominator = java.lang.Math.min(howMany,categoryNames.size());
        Set<String> actualCategories =
                new HashSet<String>(this.categoryNames);

        Set<String> inferencedCategories =
                new HashSet<String>(
                        this.inferencedCategories.subList(0,howMany));

        inferencedCategories.retainAll(actualCategories);


        
        return (float)inferencedCategories.size()/(float)denominator;
    }

    public Vector<Double> getRef() {
        return ref;
    }
}
