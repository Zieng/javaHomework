/**
 * Created by zieng on 11/18/15.
 */
/*
    程序主要是扫描一遍目标表达式，把其中的数字和操作符分别解析出来；
    在解析的过程中，分别会对符号和数字进行出栈入栈的操作，通过操作符的优先级的判断，实现计算过程
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by LIJIE on 10/15/15.
 */
public class Calculator
{
    static Stack<Character> opStack = new Stack<>();
    static Stack<Integer> numStack = new Stack<>();

    public static void main(String [] args)
    {
        Scanner input = new Scanner(System.in);
        String infix = input.nextLine();
        infix = infix.replaceAll(" ","");  // remove whitespaces
        int op1=0,op2=0;
        String subString="";
        boolean maybeSign=true;           //用于解决例如：3*(-2)、(-2)+2这类特殊输入
        for(int i=0;i<infix.length();i++)   // 只需扫描一边，就能得出结果
        {
            ArrayList<Character> op = new ArrayList<>();   //存储可能弹出的一系列操作
            Character ch=infix.charAt(i);

            if(ch.equals('+')||ch.equals('-')||ch.equals('*')||ch.equals('/')||ch.equals('%')||ch.equals('(')||ch.equals(')'))
            {
                if(maybeSign && ( ch.equals('+')||ch.equals('-') ) )  // 如果是符号，则需添加到substring，最后转成数字存起来
                {
                    subString+=ch;
                    continue;
                }

                if(!subString.equals(""))
                {
                    numStack.push(Integer.parseInt(subString));
                    subString="";
                }
                switch (ch)
                {
                    case '+':
                    case '-':
                        op=handle_operator(ch,1);
                        break;
                    case '*':
                    case '/':
                    case '%':
                        op=handle_operator(ch,2);
                        break;
                    case '(':
                        opStack.push(ch);
                        break;
                    case ')':
                        op=pop_until_match_parentheses();
                        break;
                }
                for(Character x:op)
                {
                    op2=numStack.pop();
                    op1=numStack.pop();
                    numStack.push(calculate(op1, x, op2));   //把计算结果压入numStack，以便进一步计算
                }
            }
            else
            {
                subString+=ch;
            }
            maybeSign = ch.equals('(');  // 在出现'('之后或者是表达式开头，＋－可能是符号而不是加减号。
        }
        // 收尾工作，把栈中没来得及运算的运算出来
        if(!subString.equals(""))
            numStack.push(Integer.parseInt(subString));
        while(!opStack.isEmpty())
        {
            op2=numStack.pop();
            op1=numStack.pop();
            numStack.push(calculate(op1,opStack.pop(), op2));
        }
        // 到最后numStack中应该只有一个最终的结果
        if(numStack.size()!=1)
            System.out.println("咦？？");

        System.out.println(numStack.pop());  //输出结果
    }

    public static ArrayList<Character> handle_operator(Character c, int priority)  // 优先级不同，操作也不同；priority越大，优先级越高
    {
        ArrayList<Character> op = new ArrayList<>();   //存储依次弹出的操作符，用作运算

        while( !opStack.isEmpty() )
        {
            Character topChar = opStack.pop();
            int top_priority;

            if(topChar.equals('('))
            {
                opStack.push(topChar);
                break;
            }
            else if(topChar.equals('+') || topChar.equals('-'))// priority=1
                top_priority=1;
            else
                top_priority=2;

            if(priority > top_priority)   // 栈顶的优先级高时，才会弹出操作符；否则入栈，结束循环
            {
                opStack.push(topChar);
                break;
            }
            else
                op.add(topChar);
        }
        opStack.push(c);

        return op;
    }

    public static ArrayList<Character> pop_until_match_parentheses()
    {
        ArrayList<Character> op = new ArrayList<>();

        while(!opStack.isEmpty())
        {
            Character topChar= opStack.pop();
            if(!topChar.equals('('))
                op.add(topChar);
            else
                break;
        }

        return op;
    }

    public static int calculate(int op1,Character operator, int op2)
    {
        switch (operator)
        {
            case '+':
                op1+=op2;
                break;
            case '-':
                op1-=op2;
                break;
            case '*':
                op1*=op2;
                break;
            case '/':
                op1/=op2;
                break;
            case '%':
                op1%=op2;
                break;
            default:System.out.println("invalid operator!");
                break;
        }
        return op1;
    }
}
