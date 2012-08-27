package util;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.apache.commons.lang.ArrayUtils.*;
import static org.apache.commons.lang.StringUtils.*;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 20/08/12
 * Time: 14:56
 * To change this template use File | Settings | File Templates.
 */
public class InstanceDecoder {

    /**
     * return array of features (which are int) as one long string
     * format is [\int\" "]+
     *
     * @param instance
     * @return
     */
    public String getTextAsFeatures(Instance instance) {
        StringBuilder builder = new StringBuilder();
        FeatureSequence featureSequence = (FeatureSequence) instance.getData();
        int[] features = featureSequence.getFeatures();
        return join(toObject(features), ' ');
    }


    public String getCategory(Instance instance) {
        String[] split = instance.getName().toString().split("_");
        if (split.length > 1)
            return split[1];
        return null;
    }


}
