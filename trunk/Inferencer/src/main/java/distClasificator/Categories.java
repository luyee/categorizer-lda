package distClasificator;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 1:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class Categories {


    protected Vector<Category> getCategories() {
        return categories;
    }

    private Vector<Category> categories;

    public Categories(Map< String, Map<Integer,Double> > map){
          categories = merge(mapToVector(map));
    }

    public Categories() {
        //To change body of created methods use File | Settings | File Templates.
    }

    protected Vector<Category> mapToVector(Map< String, Map<Integer,Double> > map){
        Vector<Category> cats= new Vector<Category>();
        for(String catNames: map.keySet()){
            cats.add(newSingleCategorie(map, catNames));
        }
        return cats;
    }

    protected Category newSingleCategorie(Map<String, Map<Integer, Double>> map, String catNames) {
        return new Category(map.get(catNames), catNames);
    }

    protected Vector<Category> merge(Vector<Category> cats){
        Set<String> names = new HashSet<String>();
        for (Category cat: cats){
            names.add(cat.getCategoryName());
        }
        Vector<Category> ret = new Vector<Category>();
        for (String name: names){
           ret.add(createMergedCategoties(cats, name));
        }
        
        return ret;
    }

    protected Category createMergedCategoties(Vector<Category> cats, String name) {
        return new Category(cats,name);
    }

    protected Collection<Category> assignCategories(double [] weigths){
        TreeMap<Double, Category>  treeMap=
                new TreeMap<Double, Category>();
        for (Category catetegory: getCategories()){
            treeMap.put(countDist(weigths, catetegory),
                    catetegory);
        }

        return treeMap.values();

    }

    protected Double countDist(double[] weigths, Category catetegory) {
        return catetegory.countLength(weigths);
    }

}
