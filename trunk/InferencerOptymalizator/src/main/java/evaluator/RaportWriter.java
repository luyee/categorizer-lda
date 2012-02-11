package evaluator;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/9/12
 * Time: 11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RaportWriter {
    
    public void writeRaport( Evaluator evaluator,OutputStream outputStream){
        PrintStream out = new PrintStream(outputStream);
        int[] referenceValues = evaluator.getReferenceValues();
        for (int i=0;i< referenceValues.length;i++){
            out.print(referenceValues[i] +": "+ evaluator.getCorrect()[i] +"\t" );
        }
        out.print("2: "+evaluator.getStatic2());
        out.print("5:"+evaluator.getStatic5());
        
        for (EvaluationInstance evaluationInstance : evaluator.getEvaluationInstances()){
            out.print(evaluationInstance.getInstanceName()+"\t");
            out.print(evaluationInstance.getRef() + "\t");
            out.print(evaluationInstance.getCategoryNames().toString()+"\t");
            out.println(evaluationInstance.getInferencedCategories() +"\t");
        }
    }
}
