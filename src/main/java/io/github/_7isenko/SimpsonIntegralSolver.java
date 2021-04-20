package io.github._7isenko;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
public class SimpsonIntegralSolver {
    private static SimpsonIntegralSolver instance;
    private final Function function;
    private final double accuracy;
    private double lastInaccuracy;
    private int splits;


    public SimpsonIntegralSolver(Function function, double accuracy) {
        this.function = function;
        this.accuracy = accuracy;
        this.splits = 1;
        this.lastInaccuracy = 0;
        instance = this;
    }

    public double solve(double leftBorder, double rightBorder) {

        boolean swap = false;
        if (leftBorder > rightBorder) {
            swap = true;
            double tmp = leftBorder;
            leftBorder = rightBorder;
            rightBorder = tmp;
        }
        double result = 0;

        if (Double.isInfinite(function.solve(leftBorder))) {
            leftBorder += accuracy;
        }

        if (Double.isInfinite(function.solve(rightBorder))) {
            rightBorder -= accuracy;
        }

        do {
            splits *= 2;
            ArrayList<Double> values = new ArrayList<>(splits);
            double step = (rightBorder - leftBorder) / splits;

            for (double x = leftBorder; x <= rightBorder; x += step) {

                double solvedFunc = function.solve(x);
                if (Double.isFinite(solvedFunc)) {
                    values.add(solvedFunc);
                } else {
                    if (Double.isNaN(solvedFunc)) {
                        values.add(calcFuncOrZero(x + accuracy / 2));
                    } else {
                        SimpsonIntegralSolver solver = new SimpsonIntegralSolver(function, accuracy);
                        solvedFunc = solver.solve(leftBorder, x - accuracy);
                        solvedFunc += solver.solve(x + accuracy, rightBorder);
                        return swap ? -solvedFunc : solvedFunc;
                    }
                }
            }

            double newResult = 0;
            newResult += values.get(0);
            newResult += values.get(values.size() - 1);
            for (int i = 1; i < values.size() - 1; i++) {
                if (i % 2 == 1) {
                    newResult += 4 * values.get(i);
                } else {
                    newResult += 2 * values.get(i);
                }
            }

            newResult *= step / 3;
            lastInaccuracy = getRunge(newResult, result);
            result = newResult;
        } while (lastInaccuracy >= accuracy);

        return swap ? -result : result;
    }

    private double calcFuncOrZero(double x) {
        double result = function.solve(x);
        return Double.isFinite(result) ? result : 0;
    }

    private double getRunge(double result, double prevResult) {
        return Math.abs((prevResult - result)/15);
    }

    public double getLastInaccuracy() {
        return instance.lastInaccuracy;
    }

    public int getSplits() {
        return instance.splits;
    }
}
