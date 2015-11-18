import java.util.*;

/*
    大体思想是：通过用户输入语句中包含的关键字，来模糊的推测用户可能的需求，然后从可能的回答里随机输出一个作为答案。
    比如：如果用户的输入里包含有“power”，那么他的问题很可能与耗电、电池等相关，于是程序也就返回一个大致相关的回答。
    当然，这种做法显然不能真实准确达到题目的要求，只是一种粗糙的演示而已。

    一种改进是，模仿现代的搜索引擎，会把输入切分成多个tokens，然后应用一些算法，比如求query与回答中关键词向量的相似度等等，来选出最可能的答案。

 */

/**
 * Created by LIJIE on 10/15/15.
 */
public class TechSupport
{
    static HashMap<String,ArrayList<String>> response = new HashMap<>();

    static boolean init()
    {
        String key;
        ArrayList<String> answer = new ArrayList<>();

        key="power";
        answer.add("Have you tried reboot your machine?");
        answer.add("I suggest you check your battery.");
        answer.add("Can you give me more information?");
        response.put(key,answer);
        answer= new ArrayList<>();

        key="internet";
        answer.add("Do you plug the internet wire correctly");
        answer.add("May be you should first contact your IPS.");
        response.put(key,answer);
        answer= new ArrayList<>();

        key="backup";
        answer.add("Please make sure you have an Internet access.");
        answer.add("Can you give me more information?");
        answer.add("If you want know more about our backup privacy,please look up our handbook.");
        answer.add("I am not sure what you mean, can you describe more about backup in detail?");
        response.put(key,answer);
        answer= new ArrayList<>();

        key="display";
        answer.add("Probably it's the hardware's problem, I guess.");
        answer.add("We will check this later, and then we will contact you.");
        response.put(key,answer);
        answer= new ArrayList<>();

        key="hello";
        answer.add("Hello, I'm Jack.");
        answer.add("Hi, what can I do for you?");
        response.put(key,answer);
        answer= new ArrayList<>();

        key="start";
        answer.add("Emm...That's sounds not good. Can you describe more detail?");
        answer.add("I guess there is something wrong with your software.");
        answer.add("I suggest you to upgrade the software");
        response.put(key,answer);
        answer= new ArrayList<>();

        key="help";
        answer.add("Don't worry, I will help you. What's wrong with you,dear?");
        answer.add("Wait for a moment, I will check it for you.");
        response.put(key,answer);
        answer= new ArrayList<>();

        return true;
    }

    public static void main(String [] args)
    {
        Scanner input = new Scanner(System.in);
        String question = "";

        //init() to load basic question-answer data
        if(!init())
        {
            System.out.println("We are very sorry for the service is not available! please contact DodgySoft later");
            return ;
        }


        while(true)
        {
            ArrayList<String> possibleAnswers = new ArrayList<>();
            question = input.nextLine().toLowerCase();
            String answer;
            boolean foundKey=false;

            for(String key:response.keySet())
            {
                if(question.contains(key))
                {
                    for(String r:response.get(key))
                    {
                        foundKey=true;
                        possibleAnswers.add(r);
                    }
                }
            }
            if(foundKey)
            {
                Random random = new Random();
                int index = random.nextInt();
                index %= possibleAnswers.size();
                if(index<0)
                    index = -index;
                answer = possibleAnswers.get(index);
                System.out.println(answer);
            }
            else if(question.contains("bye") || question.contains("thank you"))  // match quit condition
            {
                System.out.println("Glad to help you,see you~");
                break;
            }
            else
            {
                System.out.println("Sorry,I'm not sure what you mean.");
            }
        }
    }



}
