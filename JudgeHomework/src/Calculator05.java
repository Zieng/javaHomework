/**
 * Created by zieng on 10/28/15.
 */
import java.util.Scanner;
import java.util.Stack;

public class Calculator05
{
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Scanner read=new Scanner(System.in);
        String str;
        while(true)
        {
            str=read.nextLine();
            if(str.equals("exit"))
                break;
            Stack<Character> op=new Stack<Character>();//a stack to save operands
            Stack<Double> num=new Stack<Double>();//a stack to save numbers
            int k=0;
            double ans;
            String str2;
            str2=str.replaceAll("\\s*", "");
            char[] ch = str2.toCharArray();
            while(k<ch.length)
            {
                char q=ch[k];
                double n=0;
                if(k==0&&q=='-')	//-2*3
                {
                    int j=k+1;
                    j=getnum(ch,num,j);
                    n=num.pop();
                    n=-n;
                    num.push(n);
                    k=j;
                    continue;
                }
                if((q>='0'&&q<='9')||q=='.')
                {
                    k=getnum(ch,num,k);
                    continue;
                }
                else if(q=='(')
                {
                    op.push(q);
                    int j=k+1;		//(-2*3)
                    char p=ch[j];
                    if(p=='-')
                    {
                        j++;
                        j=getnum(ch,num,j);
                        n=num.pop();
                        n=-n;
                        num.push(n);
                    }
                    k=j;
                    continue;
                }
                else if(q=='+'||q=='-')
                {
                    op.push(q);
                }
                else if(q=='*'||q=='/'||q=='%')
                {
                    n=num.pop();
                    double t;
                    k++;
                    char p=ch[k];
                    if(p!='(')				//just do the calculation
                    {
                        k=getnum(ch,num,k);
                        t=num.pop();
                        if(q=='*')
                            n=n*t;
                        else if(q=='/')
                            n=n/t;
                        else
                            n=n%t;
                        num.push(n);
                    }
                    else{				//do after the bracket is done
                        if(q=='*')
                            op.push('*');
                        else if(q=='/')
                            op.push('/');
                        else
                            op.push('%');
                        num.push(n);
                    }
                    continue;
                }
                else if(q==')')
                {
                    DoCalculation(op,num);
                    DoTimeMul(op,num);
                }
                k++;
            }//while
            DoCalculation(op,num);
            ans=num.pop();
            System.out.println(ans);
        }//while each case
    }//main
    static void DoTimeMul(Stack<Character> op,Stack<Double> num)
    {
        if(op.empty())
        {
            return;
        }
        char op1=op.pop();
        if(op1=='*'||op1=='/'||op1=='%')
        {
            double a,b;
            b=num.pop();
            a=num.pop();
            if(op1=='*')
                a=a*b;
            else if(op1=='/')
                a=a/b;
            else
                a=a%b;
            num.push(a);
        }
        else op.push(op1);
        return;
    }//DoTimeMul
    static int getnum(char ch[],Stack<Double> num,int k)
    {
        int j=k;
        double n;
        Character p=ch[j];
        String t="";
        while((p>='0'&&p<='9')||p=='.')
        {
            t+=p;
            j++;
            if(j<ch.length)
                p=ch[j];
            else break;
        }
        n=Double.valueOf(t).doubleValue();
        num.push(n);
        return j;
    }//getnum
    static void DoCalculation(Stack<Character> op,Stack<Double> num){
        double n=num.pop(),m=0;
        char q,q2;
        while(true)
        {
            if(op.empty())
                break;
            q=op.pop();
            if(q=='(')
                break;
            if(op.empty())
            {
                if(q=='+')
                {
                    m=num.pop();
                    n=n+m;
                }
                else if(q=='-')
                {
                    m=num.pop();
                    n=m-n;
                }
            }
            else {
                q2=op.pop();
                if(q2=='(')
                {
                    if(q=='+')
                    {
                        m=num.pop();
                        n=n+m;
                    }
                    else if(q=='-')
                    {
                        m=num.pop();
                        n=m-n;
                    }
                    break;
                }
                else if(q==q2)
                {
                    m=num.pop();
                    n=n+m;
                    op.push(q2);
                }
                else{
                    m=num.pop();
                    n=m-n;
                    op.push(q2);
                }
            }
        }//while
        num.push(n);
        return;
    }//DoCalculation
}
