package evaluator;

import junit.framework.TestCase;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/10/12
 * Time: 12:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class EvaluationInstanceTest extends TestCase {



    public void testGetCorrectPercentage() throws Exception {
        EvaluationInstance evaluationInstance =
                new EvaluationInstance(){
                    public double getCorrectPercentageStatic(int howMany){
                        return (double)howMany;
                    }
                    public Vector<String> getCategoryNames() {
                        Vector<String> ret = new Vector<String>();
                        ret.add("a");
                        ret.add("a");
                        return  ret;
                    }
                };
        double res = evaluationInstance.getCorrectPercentage(3);
        assertEquals(res,5,0.01);
    }

    public void testGetCorrectPercentageStatic1() throws Exception {
        EvaluationInstance evaluationInstance =
                setCorrectprecentageEvalInst();
        assertEquals(1.0,evaluationInstance.getCorrectPercentageStatic(1),0.01);

    }
    public void testGetCorrectPercentageStatic2() throws Exception {
        EvaluationInstance evaluationInstance =
                setCorrectprecentageEvalInst();
        assertEquals(0.5,evaluationInstance.getCorrectPercentageStatic(2),0.01);

    }

    public void testGetCorrectPercentageStatic3() throws Exception {
        EvaluationInstance evaluationInstance =
                setCorrectprecentageEvalInst();
        assertEquals(0.5,evaluationInstance.getCorrectPercentageStatic(3),0.01);

    }
    public void testGetCorrectPercentageStatic4() throws Exception {
        EvaluationInstance evaluationInstance =
                setCorrectprecentageEvalInst();
        assertEquals(1,evaluationInstance.getCorrectPercentageStatic(4),0.01);

    }

    private EvaluationInstance setCorrectprecentageEvalInst() {
        EvaluationInstance evaluationInstance = new EvaluationInstance();
        evaluationInstance.categoryNames = new Vector<String>();
        evaluationInstance.categoryNames.add("b");
        evaluationInstance.categoryNames.add("a");
        evaluationInstance.inferencedCategories = new Vector<String>();
        evaluationInstance.inferencedCategories.add("a");
        evaluationInstance.inferencedCategories.add("c");
        evaluationInstance.inferencedCategories.add("d");
        evaluationInstance.inferencedCategories.add("b");
        return  evaluationInstance;
    }
    
    public void testExtractCategories1(){

        EvalInstanceTestCreator evalInstanceTestCreator = new EvalInstanceTestCreator().invoke();
        Vector<EvaluationInstance> evaluationInstances = evalInstanceTestCreator.getEvaluationInstances();
        EvaluationInstance e1 = evalInstanceTestCreator.getE1();

        EvaluationInstance ret = new EvaluationInstance(
                evaluationInstances,
                "1",
                e1.data);

        assertTrue(ret.categoryNames.contains("a"));
        assertTrue(ret.categoryNames.contains("b"));
        assertTrue(ret.categoryNames.contains("c"));
        assertEquals(ret.categoryNames.size(),3);
    }

    public void testExtractCategories2(){

        EvalInstanceTestCreator evalInstanceTestCreator = new EvalInstanceTestCreator().invoke();
        Vector<EvaluationInstance> evaluationInstances = evalInstanceTestCreator.getEvaluationInstances();
        EvaluationInstance e1 = evalInstanceTestCreator.getE1();

        EvaluationInstance ret = new EvaluationInstance(
                evaluationInstances,
                "2",
                e1.data);

        assertTrue(ret.categoryNames.contains("f"));
        assertTrue(ret.categoryNames.contains("b"));
        assertEquals(ret.categoryNames.size(),2);
    }

    private class EvalInstanceTestCreator {
        private EvaluationInstance e1;
        private Vector<EvaluationInstance> evaluationInstances;

        public EvaluationInstance getE1() {
            return e1;
        }

        public Vector<EvaluationInstance> getEvaluationInstances() {
            return evaluationInstances;
        }

        public EvalInstanceTestCreator invoke() {
            e1 = new EvaluationInstance();
            e1.categoryNames = new Vector<String>();
            e1.categoryNames.add("a");
            e1.categoryNames.add("b");
            e1.instanceName ="1";
            e1.data = e1.categoryNames;

            EvaluationInstance e2= new EvaluationInstance();
            e2.categoryNames = new Vector<String>();
            e2.categoryNames.add("f");
            e2.categoryNames.add("b");
            e2.instanceName ="2";

            EvaluationInstance e3= new EvaluationInstance();
            e3.categoryNames = new Vector<String>();
            e3.categoryNames.add("a");
            e3.categoryNames.add("c");
            e3.instanceName ="1";

            evaluationInstances = new Vector<EvaluationInstance>();
            evaluationInstances.add(e1);
            evaluationInstances.add(e2);
            evaluationInstances.add(e3);
            return this;
        }
    }

    public void testConstructor(){
        String l ="id_categoryName_instanceName categoryName subj subj subj";
        EvaluationInstance evaluationInstance =
                new EvaluationInstance(l);
        assertEquals(evaluationInstance.categoryNames.get(0),"categoryName");
        assertEquals(evaluationInstance.instanceName,"instanceName");
        assertEquals(evaluationInstance.data.size(),3);
        assertEquals(evaluationInstance.data.get(0),"subj");
    }

}
