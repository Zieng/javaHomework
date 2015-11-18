/**
 * Created by zieng on 10/28/15.
 */

import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Calculator04
{
    public static HashMap<String, Integer> cau;
    static{
        cau = new HashMap<String, Integer>();
        cau.put("(", 0);
        cau.put(")", 3);
        cau.put("+", 1);
        cau.put("-", 1);
        cau.put("*", 2);
        cau.put("/", 2);
    }
    public static int cal(int a, int b, char ch){
        switch(ch){
            case '+':a = a+b;break;
            case '-':a = a-b;break;
            case '*':a = a*b;break;
            case '/':a = a/b;break;
            default: System.out.println("error1!");
        }
        return a;
    }
    public static void cacu(String inputline){
        Stack<String> stack = new Stack<String>();
        Stack<Integer> numstack = new Stack<Integer>();
        int i;
        int a , b;
        Integer instr = null;
        boolean flag = true ,flag2 = false;
        char oper;
        int len = inputline.length();
        for(i = 0; i < len;i ++){
            if(Character.isDigit(inputline.charAt(i))){
                if(instr == null){
                    instr = 0;
                }
                instr = (instr * 10) + inputline.charAt(i) - '0';
            }
            else{
                if(instr != null){ //if there is number, flag = false, which means after the
                    //number and before '(', there is no sign of number. And flag2
                    //means we find the '-' after '(' or first of the input line.
                    if (flag2){
                        instr = -instr;
                        flag2 = false;
                    }
                    numstack.push(instr);
                    flag = false;
                    instr = null;
                }
                if(inputline.charAt(i) != ' '){
                    if(cau.containsKey(String.valueOf(inputline.charAt(i)))){
                        while(!stack.empty() && inputline.charAt(i) != '(' &&(cau.get(stack.peek().toString()) > cau.get(String.valueOf(inputline.charAt(i))))){
                            if(!numstack.empty()){
                                b = numstack.pop();
                            }
                            else{
                                System.out.println("error2!");
                                return;
                            }
                            if(!numstack.empty()){
                                a = numstack.pop();
                            }
                            else{
                                System.out.println("error3!");
                                return;
                            }
                            a = cal(a,b,stack.pop().charAt(0));
                            numstack.push(a);
                        }
                        if(inputline.charAt(i) == '('){  //there maybe a negative number
                            flag = true;
                            stack.push("(");
                        }
                        else if(inputline.charAt(i) == ')'){
                            while(!stack.empty() && (oper = stack.pop().charAt(0)) != '('){
                                if(!numstack.empty()){
                                    b = numstack.pop();
                                }
                                else{
                                    System.out.println("error4!");
                                    return;
                                }
                                if(!numstack.empty()){
                                    a = numstack.pop();
                                }
                                else{
                                    System.out.println("error5!");
                                    return;
                                }
                                a = cal(a,b,oper);
                                numstack.push(a);
                            }
                        }
                        else if(flag){   //solve the problem of negative number
                            //if there is no number between '(' and other operator,
                            //it should be the sign of number.
                            if(inputline.charAt(i) == '-'){// '+' is ignored
                                flag2 = true;
                            }
                            else if(inputline.charAt(i) == '*' || inputline.charAt(i) == '/'){
                                System.out.println("error6!");
                                return;
                            }
                            else if(inputline.charAt(i) == '('){
                                stack.push(String.valueOf(inputline.charAt(i)));
                            }
                        }
                        else{
                            stack.push(String.valueOf(inputline.charAt(i)));
                        }
                    }
                    else{
                        System.out.println("error7!");
                        return;
                    }
                }
            }
        }
        if(instr != null){
            numstack.push(instr);
        }
        while(!stack.empty()){
            if(!numstack.empty()){
                b = numstack.pop();
            }
            else{
                System.out.println("error8!");
                return;
            }
            if(!numstack.empty()){
                a = numstack.pop();
            }
            else{
                System.out.println("error9!");
                return;
            }
            a = cal(a,b,stack.pop().charAt(0));
            numstack.push(a);
        }
        a = numstack.pop();
        if(numstack.empty()){
            System.out.println(a);
            return;
        }
        else{
            System.out.println("error10!");
            return;
        }
    }
    public static void main(String[] args) { //the input negative number's '-' or positive number's
        //'+' must after '(',or at the first of the input lines
        //which means -xxxx .... or .... (-xxx *  xxx ...) .
        Scanner scan = new Scanner(System.in);
        String line = new String();
        while((line = scan.nextLine()) != null && line.length() != 0){ //when don't read anything,
            //program breaks.
            cacu(line);
        }
        scan.close();
    }
}
