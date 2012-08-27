package distClasificator;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.linear.ArrayRealVector;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 1:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class Category {
    private String categoryName;
    private double[] weights;

    public Category() {
        //To change body of created methods use File | Settings | File Templates.
    }

    protected Category(String name) {
        categoryName = name;
    }

    public Category(double[] in) {
        weights = in;
    }

    public double[] getWeights() {
        return weights;
    }

    public Category(Map<Integer, Double> map, String categoryName_Id) {
        this.weights = sortWeigths(map);
        this.categoryName = categoryName_Id.split("_")[1];

    }

    public Category(Vector<Category> categories, String name) {
        ArrayRealVector fst = calcMean(categories, name);
        this.categoryName = name;
        this.weights = fst.getData();

    }

    public ArrayRealVector calcMean(Vector<Category> categories, String name) {
        ArrayRealVector fst = new ArrayRealVector(categories.get(0).getWeights().length, 0.0);
        int count = 0;

        for (Category category : categories) {
            if (category.getCategoryName().equals(name)) {
                count++;
                ArrayRealVector v = new ArrayRealVector(category.getWeights());
                fst = fst.add(v);

            }

        }
        fst.mapDivideToSelf(count);
        return fst;
    }

    public double[] sortWeigths(Map<Integer, Double> map) {
        TreeMap<Integer, Double> treeMap = new TreeMap<Integer, Double>(map);
        Collection<Double> collection = treeMap.values();
        Double[] array =
                (Double[]) map.values().toArray(new Double[collection.size()]);
        return ArrayUtils.toPrimitive(array);
    }


    public Double countLength(double[] weigths) {
        ArrayRealVector input = new ArrayRealVector(weigths);
        return new Double(input.getDistance(weights));
    }

    public String getCategoryName() {
        return categoryName;
    }
}
