package io.github._7isenko;

import java.util.ArrayList;

/**
 * @author 7isenko
 */
public class SimpsonIntegralSolver {
    private final Function function;
    private final double accuracy;
    private double lastInaccuracy;
    private int splits;


    public SimpsonIntegralSolver(Function function, double accuracy) {
        this.function = function;
        this.accuracy = accuracy;
        this.splits = 1;
        this.lastInaccuracy = 0;
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


        do {
            splits *= 2;
            ArrayList<Double> values = new ArrayList<>(splits);
            double step = (rightBorder - leftBorder) / splits;

            for (double x = leftBorder; x <= rightBorder; x += step) {
                values.add(calcFunc(x));
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
        } while (lastInaccuracy >= accuracy && splits < 1024);


        return swap ? -result : result;
    }

    private double calcFunc(double x) {
        double result = function.solve(x);
        if (Double.isFinite(result)) {
            return result;
        }
        if (Double.isNaN(result)) {
            return calcFuncOrZero(x + accuracy / 2);
        } else {
            return 0;
        }
    }

    private double calcFuncOrZero(double x) {
        double result = function.solve(x);
        return Double.isFinite(result) ? result : 0;
    }

    private double getRunge(double result, double prevResult) {
        return Math.abs((prevResult - result) / 15);
    }

    public double getLastInaccuracy() {
        return lastInaccuracy;
    }

    public int getSplits() {
        return splits;
    }
}
