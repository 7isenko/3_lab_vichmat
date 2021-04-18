package io.github._7isenko;

import java.util.Map;

import static java.lang.Math.*;

/**
 * @author 7isenko
 */
public class Main {

    private static final InputReader inputReader = new InputReader();

    public static void main(String[] args) {

        System.out.println("Выберите уравнение, интеграл от которого хотите найти");
        System.out.println("1 - y = -x^2 + 8x - 12");
        System.out.println("2 - y = 1/x");
        System.out.println("3 - y = sin(x) / x");
        System.out.println("4 - y = 1/ln(x)");
        System.out.println("5 - y = sqrt(1 + 2x^2 - x^3)");
        int chosenAlgorithm = inputReader.readIntFromConsole();

        if (chosenAlgorithm > 5 || chosenAlgorithm <= 0) {
            System.out.println("Таких я не знаю!");
            return;
        }

        Function chosenFunction;
        String strFunc;

        switch (chosenAlgorithm) {
            case 1:
                chosenFunction = x -> -pow(x, 2) + 8 * x - 12;
                strFunc = "y = -x^2 + 8x - 12";
                break;
            case 2:
                chosenFunction = x -> 1 / x;
                strFunc = "y = 1/x";
                break;
            case 3:
                chosenFunction = x -> sin(x) / x;
                strFunc = "y = sin(x) / x";
                break;
            case 4:
                chosenFunction = x -> 1 / log(x);
                strFunc = "y = 1/ln(x)";
                break;
            case 5:
                chosenFunction = x -> sqrt(1 + 2 * pow(x, 2) - pow(x, 3));
                strFunc = "y = sqrt(1 + 2x^2 - x^3)";
                break;
            default:
                return;
        }

        double xLeft = -10, xRight = 10;

        GraphBuilder.createExampleGraph(chosenFunction, strFunc, xLeft, xRight);
        System.out.println("Посмотрите на выбранный график и выберите границы интегрирования");


        do {
            System.out.println("Левая граница: ");
            xLeft = inputReader.readDoubleFromConsole();
            System.out.println("Правая граница: ");
            xRight = inputReader.readDoubleFromConsole();

            GraphBuilder.createIntegralExampleGraph(chosenFunction, strFunc, xLeft, xRight);
            System.out.println("Вы хотите изменить выбранные границы? y/n");
        } while (inputReader.parseYesOrNo());

        double accuracy;
        System.out.println("Введите точность: ");
        accuracy = inputReader.readDoubleFromConsole();

        SimpsonIntegralSolver integralSolver = new SimpsonIntegralSolver(chosenFunction, accuracy);
        double answer = integralSolver.solve(xLeft, xRight);
        int splits = integralSolver.getSplits();

        if (splits == 1024)
            System.out.println("Было проведено слишком много итераций. Попробуйте задать равные по модулю границы.");
        System.out.printf("Ваш ответ: S = %.4f; количество разбиений: %d\n", answer, splits);
        System.out.printf("Полученная погрешность: %.8f", integralSolver.getLastInaccuracy());

    }

}
