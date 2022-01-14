package com.example;

import java.util.ArrayList;
import java.util.Arrays;

import org.ejml.simple.SimpleMatrix;
import org.ejml.simple.SimpleSVD;

/**
 *  FitEllipse Copyright 2009 2010 Michael Doube
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
// CODE TAKEN FROM: https://github.com/mdoube/BoneJ/blob/master/src/org/doube/geometry/FitEllipse.java
// Changed into EJML from JAMA

public class Ellipse {
    private static double a = 0;
    private static double b = 0;
    private static double c = 0;
    private static double d = 0;
    private static double e = 0;
    private static double f = 0;
    private static double[] center = new double[2];
    public ArrayList<Double> x_coords = new ArrayList<Double>();
    public ArrayList<Double> y_coords = new ArrayList<Double>();

    public static double[] fit_ellipse(double[][] points) {
        final int nPoints = points.length;
        double[] centroid = Centroid.getCentroid(points);
        final double xC = centroid[0];
        final double yC = centroid[1];
        double[][] d1 = new double[nPoints][3];
        for (int i = 0; i < nPoints; i++) {
            final double xixC = points[i][0] - xC;
            final double yiyC = points[i][1] - yC;
            d1[i][0] = xixC * xixC;
            d1[i][1] = xixC * yiyC;
            d1[i][2] = yiyC * yiyC;
        }
        SimpleMatrix D1 = new SimpleMatrix(d1);
        double[][] d2 = new double[nPoints][3];
        for (int i = 0; i < nPoints; i++) {
            d2[i][0] = points[i][0] - xC;
            d2[i][1] = points[i][1] - yC;
            d2[i][2] = 1;
        }
        SimpleMatrix D2 = new SimpleMatrix(d2);
        SimpleMatrix S1 = D1.transpose().mult(D1);
        SimpleMatrix S2 = D1.transpose().mult(D2);
        SimpleMatrix S3 = D2.transpose().mult(D2);
        SimpleMatrix T = (S3.invert().scale(-1)).mult(S2.transpose());
        SimpleMatrix M = S1.plus(S2.mult(T));
        double[][] m = matrix2Array2D(M);
        double[][] n = { { m[2][0] / 2, m[2][1] / 2, m[2][2] / 2 }, { -m[1][0], -m[1][1], -m[1][2] },
                { m[0][0] / 2, m[0][1] / 2, m[0][2] / 2 } };
        SimpleMatrix N = new SimpleMatrix(n); 

        SimpleSVD<SimpleMatrix> E = N.svd();
        SimpleMatrix eVec = E.getV();
        SimpleMatrix R1 = eVec.extractMatrix(0, 1, 0, 3);
        SimpleMatrix R2 = eVec.extractMatrix(1, 2, 0, 3);
        SimpleMatrix R3 = eVec.extractMatrix(2, 3, 0, 3);
        SimpleMatrix cond = (R1.scale(4)).transpose().mult(R3).minus(R2.transpose().mult(R2));

        int f = 0;
        for (int i = 0; i < 3; i++) {
            if (cond.get(0, i) > 0) {
                f = i;
                break;
            }
        }

        SimpleMatrix A1 = eVec.extractMatrix(0, 3, f, f+1);
        SimpleMatrix A = new SimpleMatrix(6,1);
        A.insertIntoThis(0, 0,A1);
        A.insertIntoThis(3, 0, T.mult(A1));
        double[] a = matrix2Array1D(A);
        double a4 = a[3] - 2 * a[0] * xC - a[1] * yC;
        double a5 = a[4] - 2 * a[2] * yC - a[1] * xC;
        double a6 = a[5] + a[0] * xC * xC + a[2] * yC * yC + a[1] * xC * yC - a[3] * xC - a[4] * yC;
        A.set(3, 0, a4);
        A.set(4, 0, a5);
        A.set(5, 0, a6);
        A = A.scale(1.0/ A.normF());
        return matrix2Array1D(A);
    }

    public static double[] cart_to_polar(double[] coefficients) throws Exception {
        // We use the
        // formulas from https://mathworld.wolfram.com/Ellipse.html // which
        // assumes a cartesian form ax^2 + 2bxy + cy^2 + 2dx + 2ey + f = 0.
        // Therefore, rename and scale b, d and e appropriately.
        a = coefficients[0];
        b = coefficients[1] / 2.0;
        c = coefficients[2];
        d = coefficients[3] / 2.0;
        e = coefficients[4] / 2.0;
        f = coefficients[5];
        double discriminant = Math.pow(b, 2) - a * c;

        center[0] = (c * d - b * e) / discriminant;
        center[1] = (a * e - b * d) / discriminant;
        return center;
    }

    public static double[] matrix2Array1D(SimpleMatrix matrix) {
        double[] array = new double[matrix.numRows()];
        for (int r = 0; r < matrix.numRows(); r++) {
            array[r] = matrix.get(r, 0);
        }
        return array;
    }

    public static double[][] matrix2Array2D(SimpleMatrix matrix) {
        double[][] array = new double[matrix.numRows()][matrix.numCols()];
        for (int r = 0; r < matrix.numRows(); r++) {
            for (int c = 0; c < matrix.numCols(); c++) {
                array[r][c] = matrix.get(r, c);
            }
        }
        return array;
    }
    public static void main(String args[]) throws Exception
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
        System.out.println(Arrays.toString(fit_ellipse(points)));
        System.out.println(Arrays.toString(cart_to_polar(fit_ellipse(points))));
        //System.out.println(Arrays.toString(cart_to_polar(coefficients)));
    }
}
