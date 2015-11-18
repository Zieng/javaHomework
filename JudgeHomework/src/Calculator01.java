import java.util.Scanner;

import sun.jvm.hotspot.debugger.remote.x86.RemoteX86Thread;
import sun.tools.tree.EqualExpression;

/**
 * Created by zieng on 10/28/15.
 */

public class Calculator01
{
    private static int evaluation(String expression) {
        if (expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')') {
            return evaluation(expression.substring(1, expression.length() - 1));
        } else {
            int priority = 2; // Represent none operator was encountered
            int position = -1;
            int parenthesisCoutner = 0;
            for (int i = 0; i < expression.length(); ++i) {
                char chr = expression.charAt(i);
                switch (chr) {
                    case '+':
                    case '-':
                        if (parenthesisCoutner == 0) {
                            position = i;
                            priority = 0;
                        }
                        break;
                    case '*':
                    case '/':
                    case '%':
                        if (parenthesisCoutner == 0 && priority >= 1) {
                            position = i;
                            priority = 1;
                        }
                        break;
                    case '(':
                        parenthesisCoutner++;
                        break;
                    case ')':
                        parenthesisCoutner--;
                        break;
                    default:
                        break;
                }
            }
            if (position == -1) {
                return Integer.parseInt(expression);
            } else {
                int left = evaluation(expression.substring(0, position));
                int right = evaluation(expression.substring(position + 1, expression.length()));
                switch (expression.charAt(position)) {
                    case '+':
                        return left + right;
                    case '-':
                        return left - right;
                    case '*':
                        return left * right;
                    case '/':
                        return left / right;
                    case '%':
                        return left % right;
                    default:
                        return 0;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String expression = scanner.nextLine();
            expression = expression.replaceAll("\\s", ""); //
            expression = expression.replaceAll("^-", "0-");
            expression = expression.replaceAll("([^0-9])-", "$10-");
            System.out.println(evaluation(expression));
        }
    }
}
