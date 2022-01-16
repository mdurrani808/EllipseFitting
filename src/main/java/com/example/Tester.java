package com.example;

import java.util.Arrays;
import org.apache.commons.math3.fitting.*;

public class Tester {
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
        double newPoints[][] = Regression.append(Regression.generatePointsFromRegression(points, 1000), points);
        //System.out.println(Arrays.toString());

    }
}
