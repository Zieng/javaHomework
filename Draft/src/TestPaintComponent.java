import javax.swing.*;
import java.awt.*;

/**
 * Created by zieng on 11/18/15.
 */
public class TestPaintComponent extends JFrame
{
    public TestPaintComponent()
    {
        add(new NewPanel());
    }

    public static void main(String agrs[])
    {
        TestPaintComponent frame = new TestPaintComponent();

        frame.setTitle("test paint component");
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


        frame.repaint(10,10,10,10,10);
    }

}

class NewPanel extends JPanel
{

    protected void paintComponent(Graphics g)
    {
        super.paintComponents(g);
        g.drawLine(0,0,50,50);
        g.drawString("hello panel",40,40);
    }
}
