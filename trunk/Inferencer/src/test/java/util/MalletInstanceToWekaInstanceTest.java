package util;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import weka.core.Attribute;
import weka.core.EuclideanDistance;
import weka.core.Instances;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class MalletInstanceToWekaInstanceTest {
    private Logger logger = Logger.getLogger(MalletInstanceToWekaInstance.class);

    @Mock
    private Instance malletInstance;
    private MalletInstanceToWekaInstance instanceAdapter;


//    @Test
//    public void testGetCategories(){
//        CategoriesGetter categoriesGetter =new CategoriesGetter();
//        InstanceDecoder decoder = mock(InstanceDecoder.class);
//        categoriesGetter.decoder =decoder;
//        InstanceList instanceList = new InstanceList()
//        Instance instance = mock(Instance.class);
//        int times = 10;
//        for (int i=0;i< times;i++){
//            instanceList.add(instance);
//        }
//        when(decoder.getCategory(any(Instance.class))).thenReturn("a");
//        Collection<String> ret =categoriesGetter.getCategories(instanceList);
//        assertEquals(ret.iterator().next(), "a");
//        assertEquals(ret.size(),1);
//        verify(decoder,times(times)).getCategory(instance);
//
//    }


    @Before
    public void setUp(){
        instanceAdapter = new MalletInstanceToWekaInstance();
    }

    /**
     * No better way to test this  due to weka
     * TODO refactor;
     */
    @Test
    public void testCreateWekaInstance(){
        String text = "text";
        String[] cats= {"a","b","c"};
        Collection<String> categories = Arrays.asList(cats);
        Instances instances = instanceAdapter.createWekaInstances(categories,text,"test");
        Attribute attribute = instances.attribute(text);
        InstanceDecoder decoder = mock(InstanceDecoder.class);
        instanceAdapter.decoder = decoder;
        malletInstance = mock(Instance.class);
        when(decoder.getCategory(malletInstance)).thenReturn("a");
        when(decoder.getTextAsFeatures(malletInstance)).thenReturn("1 2 3 4");
        weka.core.Instance wekaInstance = new weka.core.Instance(2);
        wekaInstance.setDataset(instances);
        instanceAdapter.setText(attribute, malletInstance, wekaInstance);
        instanceAdapter.setClass(malletInstance, wekaInstance);
        String message = wekaInstance.toString();
        logger.debug(message);
        junit.framework.Assert.assertEquals(message,"'1 2 3 4',a");
    }


    @Test
    public void testInsertInstance(){
        Instances instances = mock(Instances.class);
        weka.core.Instance wekaInstance = mock(weka.core.Instance.class);
        instanceAdapter.insert(wekaInstance,instances);
        verify(instances).add(wekaInstance);
        EuclideanDistance euclideanDistance;
    }

    /**
     * mallet sucks
     */
//    @Test
//    public void testSetClass(){
//        Instance instance = mock(Instance.class);
//        weka.core.Instance wekaInstance = mock(weka.core.Instance.class);
//        InstanceDecoder decoder = mock(InstanceDecoder.class);
//        instanceAdapter.decoder = decoder;
//        when(decoder.getCategory(instance)).thenReturn("cat");
//        doNothing().when(wekaInstance).setClassValue(any(String.class));
//        instanceAdapter.setClass(instance,wekaInstance);
//        verify(wekaInstance).setClassValue("cat");
//    }


//    @Test TODO not testable due to weka
//    public void testSetText(){
//        Instance instance = mock(Instance.class);
//        weka.core.Instance wekaInstance = mock(weka.core.Instance.class);
//        InstanceDecoder decoder = mock(InstanceDecoder.class);
//        instanceAdapter.decoder = decoder;
//        String s = "1 2 3 4";
//        when(decoder.getTextAsFeatures(instance)).thenReturn(s);
//        Attribute attribute = mock(Attribute.class);
//        when(attribute.addStringValue(any(String.class))).thenReturn(145);
//        instanceAdapter.setText(attribute, instance, wekaInstance);
//        verify(wekaInstance).setValue(any(Attribute.class), eq(145));
//    }
}
