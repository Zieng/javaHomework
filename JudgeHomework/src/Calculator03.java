/**
 * Created by zieng on 10/28/15.
 */

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
public class Calculator03
{
    Stack<Double> dataStack = new Stack<Double>(); // 数据栈，用于存放数值
    Stack<Character> charStack = new Stack<Character>(); // 符号栈，用于存放运算符
    String formula;
    char[] symbol = { '#','(',')','*','+','-','/'};
    int[] in = { 0,1,6,5,3,3,5 }; // 栈内优先级
    int[] out = { 0,6,1,4,2,2,4 }; // 栈外优先级

    /* 构造函数，参数为表达式 */

    public Calculator03( String formula ){
        this.formula = formula;
        charStack.push('#');
    }

    /* 算数运算 */

    public double calculate(double num1,char operator,double num2){
        switch(operator){
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                return num1 / num2;
            default:
                System.out.println("请输入合法的运算符");
                return 0;
        }
    }

    /* 判断运算符的优先级，第一个参数为栈内元素，第二个为栈外元素 */

    public int compare( char c1, char c2 ){
        if( in[Arrays.binarySearch( symbol, c1 )] > out[Arrays.binarySearch( symbol, c2 )]){
            return 1;
        }
        if( in[Arrays.binarySearch( symbol, c1 )] < out[Arrays.binarySearch( symbol, c2 )]){
            return -1;
        }
        else{
            return 0;
        }

    }

    /* 执行过程  */

    public double getResult(){
        double result;
        int i = 0;

        while( i <= formula.length() - 1 ){
            double num = 0;
            int flag = 1;

            if(formula.charAt(i) == '-' ){ // 判断‘-’是负号还是减号
                if(i == 0){ // 如果字符串第一个就是‘-’，那么他是负号
                    flag = -1;
                    i++;
                }else{
                    if(formula.charAt( i - 1 ) > '9' || formula.charAt(i - 1) < '0'){ // 如果他前一个不是数字，那么他也是负号
                        flag = -1;
                        i++;
                    }
                }
            }

            if( formula.charAt(i) <= '9' && formula.charAt(i) >= '0' ){ // 读入字符串中的数字
                while( i <= formula.length() - 1 && formula.charAt(i) <= '9' && formula.charAt(i) >= '0'){
                    num = num * 10 + flag * ( formula.charAt(i) - '0' );
                    i++;
                }
                dataStack.push(num); // 数字入栈
            }else{
                if(formula.charAt(i) == ')'){
                    while(charStack.peek() != '('){
                        double a = dataStack.pop();
                        double b = dataStack.pop();
                        double c = calculate( b, charStack.pop(), a);
                        dataStack.push(c);
                    }
                    charStack.pop();
                }else{
                    switch( compare( charStack.peek(), formula.charAt(i))){
                        case 0:
                        case 1:
                            double a = dataStack.pop();
                            double b = dataStack.pop();
                            double c = calculate( b, charStack.pop(), a);
                            dataStack.push(c);
                            charStack.push(formula.charAt(i));
                            break;
                        case -1:
                            charStack.push(formula.charAt(i));
                            break;
                        default:
                            break;
                    }
                }
                i++;
            }
        }
        if(dataStack.size() == 1){ // 若输入的表达式被括号包括，例如（2+3）则最后数字栈中剩余一个数，即运算结果
            return dataStack.pop();
        }
        if(dataStack.size() == 2){
            double a = dataStack.pop();
            double b = dataStack.pop();
            result = calculate( b, charStack.pop(), a);
            return result;
        }
        if(dataStack.size() == 3){
            double a = dataStack.pop();
            double b = dataStack.pop();
            double c = calculate( b, charStack.pop(), a);
            result = calculate(dataStack.pop(), charStack.pop(), c);
            return result;
        }
        else{
            System.out.println("error!");
            return 0;
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        Calculator03 c = new Calculator03(s);
        System.out.println(c.getResult());
        in.close();
    }
}
