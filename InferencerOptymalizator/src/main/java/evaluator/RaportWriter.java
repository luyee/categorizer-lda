package evaluator;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/9/12
 * Time: 11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RaportWriter extends RaportWriterInterface {

    class EvaluationInstanceCmp implements Comparator {

        public int compare(Object ev1, Object ev2){

            String s1 = ((EvaluationInstance)ev1).getCategoryNames().get(0);
            String s2 = ((EvaluationInstance)ev2).getCategoryNames().get(0);

            return s1.compareTo(s2);
        }

    }
    
    @Override
    public void writeRaport(Evaluator evaluator, OutputStream outputStream){
        DecimalFormat df = new DecimalFormat("#.####");
        PrintStream out = new PrintStream(outputStream);
        int[] referenceValues = evaluator.getReferenceValues();
        for (int i=0;i< referenceValues.length;i++){
            out.print(referenceValues[i] +": "+ evaluator.getCorrect()[i] +"\t" );
        }
        out.print("2: "+evaluator.getStatic2());
        out.println("5:"+evaluator.getStatic5() +"\n");


        Vector<EvaluationInstance> evaluationInstances = evaluator.getEvaluationInstances();
        Collections.sort(evaluationInstances,new EvaluationInstanceCmp());

        String last = " ";
        for (EvaluationInstance evaluationInstance : evaluationInstances){
            String ne = evaluationInstance.getCategoryNames().get(0);
            if(!last.equals(ne)){
                last=ne;
                out.println("------------------------------------------------\n");
            }
            out.print(evaluationInstance.getInstanceName()+"\t");
            for (Double d: evaluationInstance.getRef()){
                out.print(df.format(d.doubleValue())+" ");

            }
            out.print(evaluationInstance.getCategoryNames().toString()+"\t");
            out.println(evaluationInstance.getInferencedCategories().subList(0, 10) + "\t");
            out.println(" ");
            

        }
    }
}
