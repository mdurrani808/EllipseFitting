package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import georegression.fitting.*;
import georegression.fitting.curves.FitEllipseAlgebraic_F64;
import georegression.struct.curve.EllipseQuadratic_F64;
import georegression.struct.point.Point2D_F64;
public class Ellipse2 {
    public static List<Point2D_F64> convertToPoint2D(double[][] points)
    {
        List<Point2D_F64> obs = new ArrayList<Point2D_F64>();

        for(int i=0; i<points.length; i++)
        {
            obs.add(new Point2D_F64(points[i][0], points[i][1]));
        }
        return obs;
    }
    public static double[] getXArray1D(double[][] points)
    {
        double xArray[] = new double[points.length];
        for(int i = 0; i<points.length; i++)
        {
            xArray[i] = points[i][0];
        }
        return xArray;
    }
    public static double[] getYArray1D(double[][] points)
    {
        double yArray[] = new double[points.length];
        for(int i = 0; i<points.length; i++)
        {
            yArray[i] = points[i][1];
        }
        return yArray;
    }

    public static void main(String args[])
    {
        FitEllipseAlgebraic_F64 ellipse = new FitEllipseAlgebraic_F64();

        double points[][] = new double[][] {
            {1.22,1.382},
            {0.88,1.483},
            {.455,1.548},
            {-.42,1.504},
            {0, 1.555},
            {-.81, 1.4},
            {-1.16,1.242},
        };
        double newPoints[][] = Regression.append(Regression.generatePointsFromRegression(points, 25), points);

        if(ellipse.process(convertToPoint2D(newPoints)))
        {
            System.out.println(ellipse.getEllipse().toString());
        }
        {

        }
    }
}
