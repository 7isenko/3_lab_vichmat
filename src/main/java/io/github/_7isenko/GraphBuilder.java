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
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        createGraph(chart, "y(x)", function, leftBorder, rightBorder, XYSeries.XYSeriesRenderStyle.Line, Color.GREEN);
        new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        return chart;
    }

    private static void createGraph(XYChart chart, String name, Function function, double leftBorder, double rightBorder, XYSeries.XYSeriesRenderStyle renderStyle, Color lineColor) {
        ArrayList<Double> xGraph = new ArrayList<>();
        ArrayList<Double> yGraph = new ArrayList<>();
        double xVal = leftBorder;
        while (xVal <= rightBorder) {
            xGraph.add(xVal);
            double yVal = function.solve(xVal);
            if (Double.isFinite(yVal)) {
                yGraph.add(yVal);
            } else {
                yGraph.add(function.solve(xVal + 0.0001));
            }
            xVal += 0.1;
        }

        chart.addSeries(name, xGraph, yGraph).setXYSeriesRenderStyle(renderStyle).setMarker(SeriesMarkers.NONE).setLineColor(lineColor);

    }


}
