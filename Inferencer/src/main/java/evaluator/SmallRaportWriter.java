package evaluator;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/14/12
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class SmallRaportWriter extends RaportWriterInterface {

    @Override
    public void writeRaport(Evaluator evaluator, OutputStream outputStream) {
        DecimalFormat df = new DecimalFormat("#.####");
        PrintStream out = new PrintStream(outputStream);
        int[] referenceValues = evaluator.getReferenceValues();
        for (int i = 0; i < referenceValues.length; i++) {
            out.print(referenceValues[i] + ": " + evaluator.getCorrect()[i] + "\t");
        }
        out.print("2: " + evaluator.getStatic2());
        out.println("5:" + evaluator.getStatic5() + "\n");
    }

}
