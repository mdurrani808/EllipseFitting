package com.example;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
public class Regression {
    public static WeightedObservedPoints convertToWeightedObserverPoints(double[][] points)
    {
        WeightedObservedPoints obs = new WeightedObservedPoints();

        for(int i=0; i<points.length; i++)
        {
            obs.add(points[i][0], points[i][1]);
        }
        return obs;
    }
    public static double[] getCoefficients(WeightedObservedPoints observations)
    {
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
        return fitter.fit(observations.toList());
    }
    
    public static double[][] generatePointsFromRegression(double[][] points, int n)
    {
        double[] coefficients = getCoefficients(convertToWeightedObserverPoints(points));
        double generatedPoints[][] = new double[n][2]; 
        Sort2DArrayBasedOnColumnNumber(points, 1);
        double dX = (points[points.length-1][0]-points[0][0]) / (double)n;
        double xPt = points[0][0];
        for(int i = 0; i<generatedPoints.length; i++)
        {
            xPt += dX;
            generatedPoints[i][0] = xPt;
            generatedPoints[i][1] = (coefficients[2]*Math.pow(xPt,2)) + (coefficients[1]*xPt) + coefficients[0];
        }
        return generatedPoints;
    }
    private static  void Sort2DArrayBasedOnColumnNumber (double[][] array, final int columnNumber){
        Arrays.sort(array, new Comparator<double[]>() {
            @Override
            public int compare(double[] first, double[] second) {
               if(first[columnNumber-1] > second[columnNumber-1]) return 1;
               else return -1;
            }
        });
    }
    public static double[][] append(double[][] a, double[][] b) {
        double[][] result = new double[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
    
    public static void main(String args[])
    {
        double points[][] = new double[][] {
            {1.22,1.382},
            {0.88,1.483},
            {.455,1.548},
            {-.42,1.504},
            {0, 1.555},
            {-.81, 1.4},
            {-1.16,1.242},
        };
        System.out.println(Arrays.deepToString(append(generatePointsFromRegression(points, 10),points)));


    }
}
