package distClasificator;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 3:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class DocsTopicsLoaderTest extends TestCase {
    Map<String,Map<Integer,Double>> map;


    protected void setUp() throws IOException {
        final Vector<String> data = new Vector<String>();

        data.add("pies");
        data.add("1 1_cat 1 0.7 3 0.4");
        data.add("2     1_cat1   3  0.6     4   0.1");

        final int[] i = {0};
        DocsTopicsLoader docsTopicsLoader = new DocsTopicsLoader(){
            protected void close() throws IOException {}

            protected String getNext() throws IOException {
                String strLine;
                if ( i[0]<3){
                    String s=  data.get(i[0]);
                    i[0]++;
                    return s;
                }
                return null;

            }
        };
        map= docsTopicsLoader.getItMaped();

    }
    
    @Test
    public void testLength() throws Exception {
        assertEquals(map.size(), 2);
    } 
    
    @Test
    public void testhasCatItem(){
        assertTrue(map.containsKey("1_cat"));
        
    }

    @Test
    public void testhasCat1Item(){
        assertTrue(map.containsKey("1_cat1"));

    }
    
    @Test
    public  void testCatMap(){
        //data.add("1 1_cat 1 0.7 3 0.4");
        Map<Integer,Double> mp =map.get("1_cat");
        assertTrue(mp.containsKey(new Integer(1)));
        assertTrue(mp.containsKey(new Integer(3)));
        assertEquals(mp.get(new Integer(1)), new Double(0.7));
        assertEquals(mp.get(new Integer(3)),new Double(0.4));

    }

    @Test
    public  void testCat1Map(){
        //data.add("2     1_cat1   3  0.6     4   0.1");
        Map<Integer,Double> mp =map.get("1_cat1");
        assertTrue(mp.containsKey(new Integer(3)));
        assertTrue(mp.containsKey(new Integer(4)));
        assertEquals(mp.get(new Integer(3)),new Double(0.6));
        assertEquals(mp.get(new Integer(4)),new Double(0.1));

    }
}
