package io.github._7isenko;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author 7isenko
 */
public class GraphBuilder {

    public static void createIntegralExampleGraph(Function chosenFunction, String strFunc, double xLeft, double xRight) {
        XYChart chart = createExampleGraph(chosenFunction, strFunc, xLeft - 1, xRight + 1);
        createGraph(chart, "integral", chosenFunction, xLeft, xRight, XYSeries.XYSeriesRenderStyle.Area, Color.BLUE);
    }

    public static XYChart createExampleGraph(Function function, String formula, double leftBorder, double rightBorder) {
        XYChart chart = new XYChartBuilder().width(600).height(400).title(formula).xAxisTitle("x").yAxisTitle("y").build();
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setZoomEnabled(true);
        createGraph(chart, "y(x)", function, leftBorder, rightBorder, XYSeries.XYSeriesRenderStyle.Line, Color.GREEN);
        SwingWrapper swingWrapper = new SwingWrapper<>(chart);
        JFrame frame = swingWrapper.displayChart();
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        return chart;
    }

    // TODO: красиво рисовать гиперболу
    private static void createGraph(XYChart chart, String name, Function function, double leftBorder, double rightBorder, XYSeries.XYSeriesRenderStyle renderStyle, Color lineColor) {
        ArrayList<Double> xGraph = new ArrayList<>();
        ArrayList<Double> yGraph = new ArrayList<>();
        double xVal = leftBorder;
        while (xVal <= rightBorder) {
            double yVal = function.solve(xVal);
            if (Double.isFinite(yVal) && yVal < 100000D) {
                double last = yGraph.size() > 0 ? yGraph.get(yGraph.size() - 1) : yVal;
                if (Math.min(last, yVal) - Math.max(yVal, last) >= -195) {
                    xGraph.add(xVal);
                    yGraph.add(yVal);
                } else {
                    createGraph(chart, name + "2", function, xVal, rightBorder, renderStyle, lineColor);
                    break;
                }
            } else {
                if (Double.isNaN(yVal)) {
                    xGraph.add(xVal);
                    yGraph.add(function.solve(xVal + 0.0001));
                } else {
                    if (Double.isInfinite(yVal)) {
                        createGraph(chart, name + "2", function, xVal, rightBorder, renderStyle, lineColor);
                        break;
                    }
                }
            }
            xVal += 0.005;
        }
        if (Collections.max(yGraph) > 200D) {
            chart.getStyler().setYAxisMax(8D);
        }
        if (Collections.min(yGraph) < -200D) {
            chart.getStyler().setYAxisMin(-8D);
        }
        chart.addSeries(name, xGraph, yGraph).setXYSeriesRenderStyle(renderStyle).setMarker(SeriesMarkers.NONE).setLineColor(lineColor);

    }


}
