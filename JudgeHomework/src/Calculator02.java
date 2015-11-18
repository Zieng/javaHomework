/**
 * Created by zieng on 10/28/15.
 */

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Calculator02
{

    public static void main(String[] args)
    {
        //scan = new Scanner(System.in);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            String foo = sc.nextLine();
            try {
                System.out.println(engine.eval(foo));
            } catch (ScriptException e) {

            }
        }

    }
}
