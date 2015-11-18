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
        CanvasPanel cp = new CanvasPanel();
        JFrame frame = new JFrame();
        frame.add(cp);


//        cp.repaint();
        cp.set_mode(CanvasPanel.DrawMode.text);
        cp.set_color(Color.black);
        cp.set_font( allFonts[0]);

        MyText myText = new MyText();
        myText.x = myText.y = 0;
        myText.content = "test text";
        myText.color = Color.red;
        myText.font = allFonts[0];
        cp.textList.add(myText);

        MyOval myOval = new MyOval(10,10,100,100,Color.black);
        cp.ovalList.add(myOval);

        cp.repaint();

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400,400);

    }
}
