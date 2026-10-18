package src;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.Scanner;

public class keisanki {
    private static final Scanner sc = new Scanner(System.in);
    private static final String ERR = "エラー：不正な値が入力されました";

    public static void main(String[] args) {
        while (true) {
            System.out.println("機能を選択してください。(1: 四則演算 2: 消費税計算 0: 終了)");
            switch (readLine()) {
                case "1": calc(); break;
                case "2": tax(); break;
                case "0": return;
                default: System.out.println(ERR);
            }
        }
    }

    // 入力を1行読み、全角→半角に変換して前後の空白を除去する
    private static String readLine() {
        if (!sc.hasNextLine()) System.exit(0);
        return Normalizer.normalize(sc.nextLine(), Normalizer.Form.NFKC).trim();
    }

    private static boolean isNumber(String s) {
        return s.matches("-?\\d+(\\.\\d+)?");
    }

    // 正しい数値が入力されるまで繰り返す
    private static BigDecimal readNumber(String prompt) {
        while (true) {
            System.out.println(prompt);
            String s = readLine();
            if (isNumber(s)) return new BigDecimal(s);
            System.out.println(ERR);
        }
    }

    // 正しい演算記号が入力されるまで繰り返す
    private static String readOperator() {
        while (true) {
            System.out.println("演算記号（+, -, *, /）を入力してください。");
            String s = readLine();
            if (s.matches("[+\\-*/]")) return s;
            System.out.println(ERR);
        }
    }

    private static void calc() {
        BigDecimal a = readNumber("1つ目の数値を入力してください。");
        BigDecimal b = readNumber("2つ目の数値を入力してください。");
        String op = readOperator();

        BigDecimal result;
        switch (op) {
            case "+": result = a.add(b); break;
            case "-": result = a.subtract(b); break;
            case "*": result = a.multiply(b); break;
            default:
                if (b.signum() == 0) {
                    System.out.println("エラー：0で割ることはできません");
                    return;
                }
                result = a.divide(b, 2, RoundingMode.HALF_UP); // 小数第2位まで
        }
        // 小数第2位で四捨五入し、不要な末尾の0は消す(30.00 → 30)
        String text = result.setScale(2, RoundingMode.HALF_UP)
                            .stripTrailingZeros().toPlainString();
        System.out.println("計算結果「" + text + "」");
    }

    private static void tax() {
        while (true) {
            System.out.println("税抜金額を入力してください。");
            String s = readLine();
            if (isNumber(s) && new BigDecimal(s).signum() >= 0) {
                BigDecimal total = new BigDecimal(s)
                        .multiply(new BigDecimal("1.10"))      // 税率10%
                        .setScale(0, RoundingMode.HALF_UP);    // 1円未満を四捨五入
                System.out.println("税込金額「" + total.toPlainString() + "円」");
                return;
            }
            System.out.println(ERR); // 負の値・文字・未入力は再入力
        }
    }
}

