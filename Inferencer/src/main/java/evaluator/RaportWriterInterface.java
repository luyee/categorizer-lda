package evaluator;

import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/14/12
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RaportWriterInterface {
    public abstract void writeRaport(Evaluator evaluator, OutputStream outputStream);
}
