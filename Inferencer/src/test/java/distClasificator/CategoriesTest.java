package distClasificator;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class CategoriesTest extends TestCase {
    
    @Test
    public void testMapToVector() throws Exception {
        final Category cat1 = new Category();
        Categories categories = new Categories(){
            @Override
            protected Category newSingleCategorie(Map<String, Map<Integer, Double>> map, String catNames) {
                return cat1;
            }
        };
        Map<String,Map<Integer,Double>> map = new HashMap<String, Map<Integer, Double>>();
        map.put("a", new HashMap<Integer, Double>());
        map.put("b", new HashMap<Integer, Double>());

        Vector<Category> vec= categories.mapToVector(map);
        
        Assert.assertEquals(vec.get(0), cat1);
        Assert.assertEquals(vec.get(1), cat1);
    }

    public void testMerge() throws Exception {
       
        final Category cat1 = new Category();
        final Category cat2 = new Category();
        Categories categories = new Categories(){
            @Override
            protected Category newSingleCategorie(Map<String, Map<Integer, Double>> map, String catNames) {
                if(catNames.equals("a"))
                    return cat1;
                return cat2;
            }
        };
        Map<String,Map<Integer,Double>> map = new HashMap<String, Map<Integer, Double>>();
        for(int i=0;i<100;i++){
            map.put("a", new HashMap<Integer, Double>());
            map.put("b", new HashMap<Integer, Double>());
        }

        Vector<Category> vec= categories.mapToVector(map);


        Vector<String> ret =new Vector<String>();


        assertTrue(vec.contains(cat1));
        assertTrue(vec.contains(cat2));
        
    }

    public void testAssignCategories() throws Exception {
         final Vector<Category> cats = new Vector<Category>();
         for(int i=1;i<100;i++){
          cats.add(new Category());
         }
        
        Categories categories = new Categories(){
            protected Double countDist(double[] weigths, Category catetegory) {
                return new Double(catetegory.hashCode());
            }
            protected Vector<Category> getCategories() {
                return cats;
            }
        };
         double [] weight = new double[0];
        Vector<Category> ret = new Vector<Category>(categories.assignCategories(weight));
        for (int i=1;i<ret.size();i++){
            assertTrue(ret.get(i-1).hashCode()<=ret.get(i-1).hashCode());
        }

    }
}
