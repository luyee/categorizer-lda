package interfaces;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/25/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractInferencer {

    /**
     * Inference categories.
     *
     * @param instance is vector of String that represents the
     *                 learning features (e.g basic form of nouns).
     * @return Returned Vector contains categories
     *         ordered decreasingly by their probabilities.
     */
    public abstract Vector<String> inferenceCategories(Vector<String> instance) throws Exception;


}
