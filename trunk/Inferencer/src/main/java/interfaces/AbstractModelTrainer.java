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
    protected  boolean modelFromFile;

    public abstract void trainModel() throws IOException, UnsupportedEncodingException, Exception;
    public abstract AbstractInferencer getInferencer();
    public abstract boolean readModelFromFile(String modelPath) throws Exception;
    public abstract void saveModel(String modelPath);
}
