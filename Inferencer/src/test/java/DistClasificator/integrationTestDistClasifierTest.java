package DistClasificator;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/8/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
//
//ignore
//        1   1_a     4 0.1   3 0.2     2 0.1     1 0.1   0   0.1
//        2   2_a     4 0.1   3 0.3     2 0.1     1 0.2   0   0.1
//        1   3_a     1 0.3   2 0.1     3 0.4     4 0.1   0   0.1
//        1   1_b     4 0.1   3 0.2     2 0.1     1 0.1   0   0.1
//        2   2_b     4 0.1   3 0.3     2 0.1     1 0.2   0   0.3
//        1   3_b     1 0.3   2 0.1     3 0.4     4 0.1   0   0.5

public class integrationTestDistClasifierTest extends TestCase {

    @Test
    public void testCalcCategories() throws IOException {
        DistanceClassifier distanceClassifier= new DistanceClassifier();
        distanceClassifier.readDocTopicasStream(
                getClass().getClassLoader().getResourceAsStream("itegrationDistClassifier/TopicPerDoc1.txt"));
        Categories categories =distanceClassifier.getCategories();

        Assert.assertEquals(categories.getCategories().size(), 2);

        Category cat1 = categories.getCategories().get(0);
        Category cat2 = categories.getCategories().get(1);
        if(cat1.getCategoryName().equals("b")){
            Category tmp = cat2;
            cat2 =cat1;
            cat1 = tmp;
        }

        Assert.assertEquals(cat1.getCategoryName(), "a");
        Assert.assertEquals(cat2.getCategoryName(), "b");

        double cat1Mean []= {0.1, 0.2, 0.1, 0.3, 0.1};
        double cat2Mean []= {0.3, 0.2, 0.1,0.3,0.1};



        org.junit.Assert.assertArrayEquals(cat1.getWeights(), cat1Mean, 0.01);
        org.junit.Assert.assertArrayEquals(cat2.getWeights(), cat2Mean, 0.01);


    }
    
    @Test
    public void testClasification1() throws IOException {
        DistanceClassifier distanceClassifier= new DistanceClassifier();
        distanceClassifier.readDocTopicasStream(
                getClass().getClassLoader().getResourceAsStream("itegrationDistClassifier/TopicPerDoc1.txt"));

        double [] arg1 ={0.1, 0.2, 0.1,0.3,0.1};
        Vector<Category> categories = distanceClassifier.classifyDouble(arg1);
        Assert.assertEquals(categories.get(0).getCategoryName(), "a");

        double arg2[] = {0.3, 0.2, 0.1,0.3,0.1};
        categories = distanceClassifier.classifyDouble(arg2);
        Assert.assertEquals(categories.get(0).getCategoryName(), "b");

        double arg3[] = {0.3, 0.2, 0.4,0.3,0.1};
        categories = distanceClassifier.classifyDouble(arg2);
        Assert.assertEquals(categories.get(0).getCategoryName(), "b");
    
    }

    @Test
    public void testClasification2() throws IOException {
//        ignore
//        1   1_a     4 0.1   3 0.2     2 0.3     1 0.14   0   0.13
//        2   2_b     4 0.4   3 0.3     2 0.4     1 0.21   0   0.12
//        1   3_c     1 0.3   2 0.1     3 0.4     4 0.11   0   0.11
        DistanceClassifier distanceClassifier= new DistanceClassifier();
        distanceClassifier.readDocTopicasStream(
                getClass().getClassLoader().getResourceAsStream("itegrationDistClassifier/TopicPerDoc1.txt"));

        double [] arg1 ={0.13, 0.14, 0.3,0.3,0.1};
        Vector<Category> categories = distanceClassifier.classifyDouble(arg1);
        Assert.assertEquals(categories.get(0).getCategoryName(), "a");


    }
}
