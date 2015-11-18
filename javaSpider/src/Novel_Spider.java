/**
 * Created by zieng on 10/22/15.
 */
/**
 * Created by yicun on 10/21/2015.
 */
//package novel_spider;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Novel_Spider {
    public static void main(String[] args) {
        try {
            String urlst="http://www.gulongbbs.com/jinyong/tlbb/";
            URL url = new URL(urlst);
            String filter1="<a class=\"1\" href=\"";
            String filter2="\"";
            //Vector content= new Vector();
            Vector contents=new Vector();
            String title_begin="<title>";
            String title_end="</title>";
            String para_begin="<P>";
            String para_end="</P>";
            Vector urls = html_sparse(url,filter1,filter2);
            //System.out.println(urls.size());
            //contain_sparse(urls,title_begin,title_end,para_begin,para_end,content);
            ExecutorService pool = Executors.newFixedThreadPool(20);
            timer excount=new timer(urls.size());
            for(int i=0;i<urls.size();i++){
                Vector content_unit=new Vector();
                contents.add(content_unit);
                System.out.println("parsing: "+(i+1)+" / "+urls.size());
                System.out.println("content size: "+contents.size());
                Thread t=new Html_Thread((String)urls.elementAt(i),title_begin,title_end,para_begin,para_end,content_unit,i,excount);
                pool.execute(t);
            }
            pool.shutdown();
            while(!excount.isZero()){}
            for(int i=0;i<contents.size();i++){
                System.out.println("Element "+i+" : "+((Vector)contents.elementAt(i)).elementAt(0));
            }
            write_novels("novel.txt", contents);
        }
        catch(Exception e){
            System.out.println("Error top:");
            System.out.println(e.getMessage()+"  "+e.getClass()+"  "+e.getCause());
        }
    }

    private static Vector html_sparse(URL url, String filter_head, String filter_end){
        Vector urls=new Vector();
        try{
            DataInputStream in=new DataInputStream(new BufferedInputStream(url.openStream()));
            String cont;
            int index1,index2;
            urls.clear();
            while((cont=in.readLine())!=null){
                cont=(new String(cont.getBytes("ISO-8859-1"),"UTF-8"));
                while((index1=cont.indexOf(filter_head))>=0){
                    if((index2=cont.indexOf(filter_end,index1+filter_head.length()))>=0){
                        String tempurl=cont.substring(index1+filter_head.length(),index2);
                        //System.out.println(tempurl);
                        if(!tempurl.contains("http")) {
                            tempurl=url.toString().substring(0,url.toString().indexOf("com")+3)+tempurl;
                            //System.out.println(tempurl);
                            urls.add(tempurl);
                        }
                        cont=cont.substring(index2);
                        continue;
                    }
                    cont=cont.substring(index1);
                }
            }
        }
        catch(IOException ioe){
            System.out.println("Error IO:");
            System.out.println(ioe.getMessage());
        }
        //System.out.println(urls.size());
        return urls;
    }

    private static void contain_sparse(Vector urls,String title_begin,String title_end,String para_begin,String para_end,Vector cont){
        try{
            for(int i=0;i<urls.size();i++){
                //System.out.println("parsing: "+(i+1)+" / "+urls.size());
                URL addr=new URL(urls.elementAt(i).toString());
                int index1,index2;
                String content;
                DataInputStream in=new DataInputStream(new BufferedInputStream(addr.openStream()));
                while((content=in.readLine())!=null){
                    content=(new String(content.getBytes("ISO-8859-1"),"GB2312"));
                    //System.out.println(content);
                    if((index1=content.indexOf(title_begin))>=0){
                        index2=content.indexOf(title_end,index1+title_begin.length());

                        if(index2>=0){
                            cont.add(content.substring(index1+title_begin.length(),index2));
                        }
                        continue;
                    }
                    while((index1=content.indexOf(para_begin))>=0){
                        //System.out.println(index1+"   "+content.length()+"   "+para_begin.length()+"   "+content.indexOf(para_end,index1+para_begin.length()));
                        if((index2=content.indexOf(para_end,index1+para_begin.length()))>=0){
                            //    System.out.println(index1+"   "+index2);
                            cont.add("  " + content.substring(index1 + para_begin.length(), index2).replace("&nbsp",""));
                            content=content.substring(index2);
                            continue;
                        }
                        else{
                            cont.add("  " + content.substring(index1 + para_begin.length()).replace("&nbsp",""));
                            content=new String();
                            continue;
                        }
                    }
                }
                cont.add("");
                cont.add("");
            }
        }
        catch(IOException ioe){
            System.out.println("Error IO:");
            System.out.println(ioe.getMessage());
        }
    }

    public static void write_novel(String filename, Vector contents) throws FileNotFoundException {
        FileOutputStream out=new FileOutputStream(new File(filename));
        OutputStreamWriter bw=new OutputStreamWriter(out);
        BufferedWriter br=new BufferedWriter(bw);
        for(int i=0;i<contents.size();i++){
            try {
                br.write((String)contents.elementAt(i));
                br.newLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            br.close();
            bw.close();
            out.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static void write_novels(String filename, Vector contents) throws FileNotFoundException {
        FileOutputStream out=new FileOutputStream(new File(filename));
        OutputStreamWriter bw=new OutputStreamWriter(out);
        BufferedWriter br=new BufferedWriter(bw);
        for(int i=0;i<contents.size();i++){
            for(int j=0;j< ((Vector)contents.elementAt(i)).size();j++){
                try {
                    br.write((String)((Vector) contents.elementAt(i)).elementAt(j));
                    br.newLine();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        try {
            br.close();
            bw.close();
            out.close();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static class Html_Thread extends Thread{
        String url;
        String title_begin;
        String title_end;
        String para_begin;
        String para_end;
        Vector cont;
        int No;
        timer count;
        Html_Thread(String tu,String ttb, String tte,String tpb,String tpe,Vector tc,int NO,timer c){
            url=tu;
            title_begin=ttb;
            title_end=tte;
            para_begin=tpb;
            para_end=tpe;
            cont=tc;
            No=NO;
            count=c;
        }
        @Override
        public void run(){
            Vector temp=new Vector();
            temp.add(url);
            contain_sparse(temp,title_begin,title_end,para_begin,para_end,cont);
            count.delete();
        }
    }

    public static class timer{
        int times;
        timer(int t){
            times=t;
        }
        public void delete(){
            times--;
        }
        public boolean isZero(){
            if(times==0){
                return true;
            }
            else{
                System.out.println(times);
                return false;
            }
        }
    }
}
