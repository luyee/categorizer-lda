package distClasificator;

import junit.framework.TestCase;
import org.apache.commons.math.linear.ArrayRealVector;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 3:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryTest extends TestCase {
    public void testCalcMean() throws Exception {
        final double [][] arr ={ {1.0, 1.0, 1.0},{2.0,2.0,2.0},{3.0,3.0,3.0}};

        Vector<Category> cats = new Vector<Category>();
        
        for (int i=0;i<3;i++){
            final int finalI = i;
            Category cat = new Category("pies"){
                public double[] getWeights() {
                    return arr[finalI];
                }
            };

            cats.add(cat);
        }
        for (int i=0;i<3;i++){

            final int finalI = i;
            Category cat = new Category("kot"){
                public double[] getWeights() {
                    return arr[0];
                }
            };

            cats.add(cat);
        }

        Category category = new Category();

        ArrayRealVector ret =category.calcMean(cats, "pies");
        
        for(int i=0;i<3;i++){
            assertEquals(2.0, ret.getData()[i]);
        }


    }

    public void testSortWeigths() throws Exception {
        Category category = new Category();
        HashMap<Integer,Double> map = new HashMap<Integer, Double>();
        map.put(new Integer(2),new Double(1));
        map.put(new Integer(1),new Double(3));
        map.put(new Integer(3), new Double(2));

        double [] ret =category.sortWeigths(map);
        assertEquals(ret[0],3.0);
        assertEquals(ret[1],1.0);
        assertEquals(ret[2],2.0);

    }

    public void testCountLength() throws Exception {

        double [] in ={0,3.0};
        Category category = new Category(in);
        double [] next ={4.0,0.0};
        Double d = category.countLength(next);
        assertEquals(d,new Double(5.0));
    }
}
