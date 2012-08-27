package util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import weka.core.Instance;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 27/08/12
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class CosineDistanceTest {
    @Mock
    Instance a,b;

    @Test
    public void testDistance() throws Exception {
        double [] res1 = {1.0,0};
        when(a.toDoubleArray()).thenReturn(res1);
        when(b.toDoubleArray()).thenReturn(res1);
        CosineDistance cosineDistance = new CosineDistance();
        assertEquals(cosineDistance.distance(a, b), 1.0);

    }

//    @Test
//    public void testDistance() throws Exception {
//
//    }
//
//    @Test
//    public void testDistance() throws Exception {
//
//    }
//
//    @Test
//    public void testDistance() throws Exception {
//
//    }
}
