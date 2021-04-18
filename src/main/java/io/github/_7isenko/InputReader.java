package io.github._7isenko;

import java.util.Locale;
import java.util.Scanner;

/**
 * @author 7isenko
 */
public class InputReader {

    public double readDoubleFromConsole() {
        Scanner in = new Scanner(System.in);
        in.useLocale(Locale.US);
        return in.nextDouble();
    }

    public int readIntFromConsole() {
        Scanner in = new Scanner(System.in);
        in.useLocale(Locale.US);
        return in.nextInt();
    }


    public boolean parseYesOrNo() {
        Scanner in = new Scanner(System.in);
        return in.next().startsWith("y");
    }

}
