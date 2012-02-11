package evaluator;

import junit.framework.TestCase;

import java.util.Vector;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/10/12
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class EvaluatorTest extends TestCase {
    public void testEvaluate() throws Exception {
        final Vector<EvaluationInstance> evaluationInstances = new
                Vector<EvaluationInstance>();

        for (int i =0 ;i<100 ;i++){
            evaluationInstances.add(new EvaluationInstance());
        }

        Evaluator evaluator = new Evaluator(){
            protected void singleEvalCalculation(EvaluationInstance evaluationInstance){
                setStatic2(0.1 + this.getStatic2());
                setStatic5(0.2 + this.getStatic5());
                double  [] correct = getCorrect();
                for (int i=0;i< getReferenceValues().length;i++ ){
                    correct[i] += (i*0.1);
                }
                setCorrect(correct);
            }
            public  Vector<EvaluationInstance> getEvaluationInstances(){
                return  evaluationInstances;
            }
        };



        evaluator.evaluate();

        assertEquals(evaluator.getStatic2(),0.1,0.01);
        assertEquals(evaluator.getStatic5(),0.2,0.01);
        assertArrayEquals(evaluator.getCorrect(), new double[]{0.0, 0.1, 0.2, 0.3,0.4,0.5},0.01);


    }

    public void testSingleEvalCalculation() throws Exception {
        final double arr[] = {1.0,2.0,3.0,4.0,5.0,6.0};
        
        Evaluator evaluator = new Evaluator(){           
            protected double getCorrectPrecentageStatic(EvaluationInstance evaluationInstance, int i) {
                if (i==2)
                    return 3.0;
                return 5.0;
            }

            protected double getCorrectPrecentageByReference(EvaluationInstance evaluationInstance, int i) {
                return arr[i];
            }

            protected Vector<String> inference(EvaluationInstance evaluationInstance) throws Exception {
                return null;
            }
        };

        EvaluationInstance e1= new EvaluationInstance();
        evaluator.singleEvalCalculation(e1);


        assertArrayEquals(arr,evaluator.getCorrect(),0.01);
        assertEquals(3.0, evaluator.getStatic2(), 0.01);
        assertEquals(5.0,evaluator.getStatic5(),0.01);

    }

//    1_cat1_inst1 cat1 pies pies pies kot kot kot
//    2_cat1_inst1 cat2 pies pies pies kot kot kot
//    3_cat2_inst1 cat1 pies pies pies kot kot kot
//    4_cat2_inst1 cat3 pies pies pies kot kot kot
    public void testParseInput(){
        Vector<String> parselist = new Vector<String>();
        parselist.add("1_cat1_inst1 - pies pies pies kot kot kot");
        parselist.add("2_cat2_inst1 - pies pies pies kot kot kot");
        parselist.add("3_cat1_inst2 - pies pies pies kot kot kot");
        parselist.add("4_cat3_inst2 - pies pies pies kot kot kot");
        
        Evaluator evaluator = new Evaluator(parselist,null);
        
        Vector<EvaluationInstance> evs =evaluator.getEvaluationInstances();
        assertEquals(2,evs.size());
        EvaluationInstance ev1 = evs.get(0);
        EvaluationInstance ev2 = evs.get(1);
        if(ev1.getInstanceName().equals("inst2")){
            EvaluationInstance tmp = ev2;
            ev2=ev1;
            ev1=tmp;
        }


        assertTrue(ev1.getCategoryNames().contains("cat1"));
        assertTrue(ev1.getCategoryNames().contains("cat2"));
        assertEquals(ev1.getCategoryNames().size(),2);

        assertTrue(ev2.getCategoryNames().contains("cat1"));
        assertTrue(ev2.getCategoryNames().contains("cat3"));
        assertEquals(ev2.getCategoryNames().size(),2);
        
        System.out.println(ev1.getData().toString());
        System.out.println(ev2.getData().toString());
        
        
        

    }
}
