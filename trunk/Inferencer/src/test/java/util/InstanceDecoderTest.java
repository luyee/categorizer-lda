package util;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class InstanceDecoderTest  {
    @Mock
    Instance mockInstance;

    private InstanceDecoder instanceDecoder;
    private Logger logger = Logger.getLogger(InstanceDecoderTest.class);

    @Before
    public void setUp(){
        instanceDecoder = new InstanceDecoder();
    }

    @Test
    public void testGetMessage() throws Exception {

        FeatureSequence featureSequence = mock(FeatureSequence.class);
        int [] data = {1,2,3,4};
        when(featureSequence.getFeatures()).thenReturn(data);
        when(mockInstance.getData()).thenReturn(featureSequence);
        String ret = instanceDecoder.getMessage(mockInstance);
        junit.framework.Assert.assertEquals("1 2 3 4",ret);
    }

    @Test
    public void testGetCategoryOk() throws Exception {
        when(mockInstance.getName()).thenReturn("a_b_c");
        String ret = instanceDecoder.getCategory(mockInstance);
        junit.framework.Assert.assertEquals("b",ret);
    }

    @Test
    public void testGetCategoryNull() throws Exception {
        when(mockInstance.getName()).thenReturn("a_");
        String ret = instanceDecoder.getCategory(mockInstance);
        junit.framework.Assert.assertNull(ret);
    }


}
