package evaluator;


import interfaces.AbstractInferencer;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: kacper
 * Date: 2/9/12
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Evaluator {
    private AbstractInferencer inferencer;
    private Vector<EvaluationInstance> evaluationInstances;
    private double[] correct = new double[]{0, 0, 0, 0, 0, 0};
    private int[] referenceValues = new int[]{0, 1, 2, 5, 7, 10};
    private double static2 = 0;
    private double static5 = 0;


    public Evaluator(Vector<String> evalinsts, AbstractInferencer inference) {
        Vector<EvaluationInstance> e2 = parseInputLis(evalinsts);

        evaluationInstances = e2;
        this.inferencer = inference;
    }

    protected Vector<EvaluationInstance> parseInputLis(Vector<String> evalinsts) {
        evaluationInstances = new Vector<EvaluationInstance>();
        for (String evalInst : evalinsts) {
            evaluationInstances.add(new EvaluationInstance(evalInst));
        }
        Map<String, EvaluationInstance> instNames = new HashMap<String, EvaluationInstance>();
        for (EvaluationInstance evaluationInstance : evaluationInstances) {
            instNames.put(evaluationInstance.getInstanceName(), evaluationInstance);
        }
        Vector<EvaluationInstance> e2 = new Vector<EvaluationInstance>();
        for (String name : instNames.keySet()) {
            e2.add(new EvaluationInstance(evaluationInstances, name,
                    instNames.get(name).getData()));
        }
        return e2;
    }


    public Evaluator() {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void setStatic5(double static5) {
        this.static5 = static5;
    }

    public void setStatic2(double static2) {
        this.static2 = static2;
    }

    public void setCorrect(double[] correct) {
        this.correct = correct;
    }

    public Vector<EvaluationInstance> getEvaluationInstances() {
        return evaluationInstances;
    }

    public double[] getCorrect() {
        return correct;
    }

    public int[] getReferenceValues() {
        return referenceValues;
    }

    public double getStatic2() {
        return static2;
    }

    public double getStatic5() {
        return static5;
    }


    protected double getCorrectPrecentageByReference(EvaluationInstance evaluationInstance, int i) {
        return evaluationInstance.getCorrectPercentage(referenceValues[i]);
    }

    protected double getCorrectPrecentageStatic(EvaluationInstance evaluationInstance, int i) {
        return evaluationInstance.getCorrectPercentageStatic(i);
    }

    protected Vector<String> inference(EvaluationInstance evaluationInstance) throws Exception {
        return inferencer.inferenceCategories(evaluationInstance.getData());
    }


    public void evaluate() throws Exception {

        for (EvaluationInstance evaluationInstance : getEvaluationInstances()) {
            singleEvalCalculation(evaluationInstance);
        }

        int size = getEvaluationInstances().size();

        for (int i = 0; i < getReferenceValues().length; i++) {
            correct[i] /= size;
        }
        static2 /= size;
        static5 /= size;

    }

    protected void singleEvalCalculation(EvaluationInstance evaluationInstance) throws Exception {
        Vector<String> categories =
                inference(evaluationInstance);

        evaluationInstance.setInferencedCategories(categories);

        for (int i = 0; i < referenceValues.length; i++) {
            correct[i] +=
                    getCorrectPrecentageByReference(evaluationInstance, i);
        }

        static2 += getCorrectPrecentageStatic(evaluationInstance, 2);
        static5 += getCorrectPrecentageStatic(evaluationInstance, 5);
    }


}
