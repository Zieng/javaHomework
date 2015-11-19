import javax.swing.*;
import java.awt.*;

/**
 * Created by zieng on 11/18/15.
 */
public class TestCanvasPanel
{
    static GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    static Font[] allFonts = e.getAllFonts();

    public static void main(String [] args)
    {
        DrawBoard.CanvasPanel cp = new DrawBoard.CanvasPanel();
        JFrame frame = new JFrame();
        frame.add(cp);

        System.out.print(allFonts[0]);
//        cp.repaint();
        cp.set_mode("oval");

//        DrawBoard.MyText myText = new DrawBoard.MyText();
//        myText.x = myText.y = 20;
//        myText.content = "test text";
//        myText.color = Color.red;
//        myText.font = cp.getFont();
//        cp.textList.add(myText);

        DrawBoard.MyOval myOval = new DrawBoard.MyOval(10,10,100,100,Color.black,true);
//        cp.ovalList.add(myOval);

        cp.repaint();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400,400);

    }
}
