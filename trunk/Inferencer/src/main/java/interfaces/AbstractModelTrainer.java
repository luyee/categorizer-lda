package interfaces;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 3/25/12
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractModelTrainer {
    protected boolean modelFromFile;

    /**
     * This method simply trains model. Firstly the constructor should be called
     * or model should be read from file by readModelFromFile method
     */
    public abstract void trainModel() throws IOException, UnsupportedEncodingException, Exception;

    /**
     * return the tool to inference on new data
     *
     * @see AbstractInferencer
     */
    public abstract AbstractInferencer getInferencer();

    /**
     * This should be exact same path as one given to saveModel
     *
     * @param modelPath defines location where model is stored
     */
    public abstract void readModelFromFile(String modelPath) throws Exception;

    /**
     * This method should be given modelPath which is path to
     * file where model should be stored. Some subclasses of
     * this class might create more then one file by adding
     * some suffixes to modelPath.
     *
     * @param modelPath defines path where model should be stored
     */
    public abstract void saveModel(String modelPath);
}
