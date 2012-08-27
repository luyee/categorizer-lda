package util;

import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.PerformanceStats;

import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: kacper
 * Date: 27/08/12
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */
public class CosineDistance implements DistanceFunction{


    @Override
    public void setInstances(Instances insts) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Instances getInstances() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setAttributeIndices(String value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAttributeIndices() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setInvertSelection(boolean value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getInvertSelection() {
        return false;
    }

    @Override
    public double distance(Instance first, Instance second) {
        double[] x,y;
        x=first.toDoubleArray();
        y=second.toDoubleArray();
        assert(x.length==y.length);
        double dot = dot(x, y);
        double norm = Math.sqrt(dot(x,x)*dot(y,y));
        return dot/norm;
    }

    @Override
    public double distance(Instance first, Instance second, PerformanceStats stats) throws Exception {
        return distance(first,second);
    }

    @Override
    public double distance(Instance first, Instance second, double cutOffValue) {
        return distance(first,second);
    }

    @Override
    public double distance(Instance first, Instance second, double cutOffValue, PerformanceStats stats) {
        return distance(first,second);
    }

    private double dot(double[] x, double[] y) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
            sum += x[i]*y[i];
        return sum;
    }

    @Override
    public void postProcessDistances(double[] distances) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update(Instance ins) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Enumeration listOptions() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOptions(String[] options) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getOptions() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
