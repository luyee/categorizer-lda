package evaluator;

import junit.framework.TestCase;

import java.io.InputStream;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/10/12
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class IntegratinTests extends TestCase {

    public void testIntegration1() throws Exception {


        InputStream is =
                this.getClass().getClassLoader().getResourceAsStream("evaluatorResources/dataSet1.txt");

        DataLoader dataLoader= new DataLoader(is);
        Vector<String> data = dataLoader.getEvaluationInsatnces();

        Evaluator evaluator= new Evaluator(data,null){
            protected Vector<String> inference(EvaluationInstance evaluationInstance) throws Exception {
                Vector<String> ret = new Vector<String>();
                if (evaluationInstance.getInstanceName().equals("inst1")){
                    ret.add("cat1");
                    ret.add("cat5");
                    ret.add("cat2");
                    ret.add("cat4");
                }
                else if (evaluationInstance.getInstanceName().equals("inst2")){
                    ret.add("cat2");
                    ret.add("cat1");
                    ret.add("cat4");
                    ret.add("cat3");
                }
                return ret;
            }
        };

        evaluator.evaluate();
        assertEquals(evaluator.getStatic2(),0.5,0.001);
        assertEquals(evaluator.getStatic5(),1,0.001);
        assertEquals(evaluator.getCorrect()[0],0.5,0.001);
        assertEquals(evaluator.getCorrect()[1],0.75,0.001);
        assertEquals(evaluator.getCorrect()[2],1.0,0.001);
        assertEquals(evaluator.getCorrect()[3],1.0,0.001);



        RaportWriter raportWriter = new RaportWriter();

        raportWriter.writeRaport(evaluator,System.out);



    }

    public void testIntegration3() throws Exception {


        InputStream is =
                this.getClass().getClassLoader().getResourceAsStream("evaluatorResources/dataSet2.txt");

        DataLoader dataLoader= new DataLoader(is);
        Vector<String> data = dataLoader.getEvaluationInsatnces();

        Evaluator evaluator= new Evaluator(data,null){
            protected Vector<String> inference(EvaluationInstance evaluationInstance) throws Exception {
                Vector<String> ret = new Vector<String>();
                if (evaluationInstance.getInstanceName().equals("inst1")){
                    ret.add("cat1");
                    ret.add("cat5");
                    ret.add("cat2");
                    ret.add("cat4");
                }
                else if (evaluationInstance.getInstanceName().equals("inst2")){
                    ret.add("cat2");
                    ret.add("cat1");
                    ret.add("cat4");
                    ret.add("cat3");
                }
                return ret;
            }
        };
//
//                1_cat1_inst1 -  {1 ,2 }
//                2_cat2_inst1 -
//                3_cat2_inst2 -  {2,3,4,5}
//                4_cat3_inst2 -
//                3_cat4_inst2 -
//                4_cat5_inst2 -

        evaluator.evaluate();
        assertEquals(evaluator.getStatic2(),0.5,0.001);
        assertEquals(evaluator.getStatic5(),(1.75/2),0.001);
        assertEquals(evaluator.getCorrect()[0],1.25/2,0.001);
        assertEquals(evaluator.getCorrect()[1],1.75/2,0.001);
        assertEquals(evaluator.getCorrect()[2],1.75/2,0.001);
        assertEquals(evaluator.getCorrect()[3],1.75/2,0.001);



        RaportWriter raportWriter = new RaportWriter();

        raportWriter.writeRaport(evaluator,System.out);



    }

}
