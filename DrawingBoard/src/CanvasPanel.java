import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zieng on 11/18/15.
 */
public class CanvasPanel extends JPanel
{
    ArrayList<MyLine> lineList = new ArrayList<>();
    ArrayList<MyOval> ovalList = new ArrayList<>();
    ArrayList<MyRectangle> rectangleList = new ArrayList<>();
    ArrayList<MyText> textList = new ArrayList<>();

    enum DrawMode{
        select,line,rectangle,oval,text,wait,
    }
    DrawMode mode;

    private Color currentColor;
    private Font currentFont;
    private boolean isFill;
    private boolean isRoundCorner;

    int width,height;
    private int x1,y1,x2,y2;
    private int clickTimes;
    Object activeDrawObject;  // the  selected draw object (used when deleting an object)
    String message;

    public CanvasPanel()
    {
        x1=x2=y1=y2=clickTimes=0;
        mode = DrawMode.wait;

        addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(clickTimes == 0)
                {
                    x1 = e.getX();
                    y1 = e.getY();
                    if(mode == DrawMode.select)
                    {
                        clickTimes = 0;
                        check_focus();
                    }
                    else if(mode == DrawMode.text)
                    {
                        create_draw_object();
                    }
                    else
                        clickTimes++;
                }
                else if(clickTimes == 1)
                {
                    x2 = e.getX();
                    y2 = e.getY();
                    clickTimes = 0;

                    if(mode != DrawMode.select && mode != DrawMode.wait && mode != DrawMode.text)
                        create_draw_object();
                }
            }

            @Override
            public void mousePressed(MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                System.out.print(message);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {

            }

            @Override
            public void mouseExited(MouseEvent e)
            {

            }
        });
    }

    void set_mode(DrawMode m)
    {
        mode = m;
    }

    DrawMode get_mode()
    {
        return mode;
    }

    void set_color(Color c)
    {
        currentColor = c;
    }

    Color get_color()
    {
        return currentColor;
    }

    void set_font(Font f)
    {
        currentFont = f;
    }

    Font get_font()
    {
        return currentFont;
    }


    protected void paintComponent(Graphics g)
    {
        super.paintComponents(g);

        message = "Painting items......\n";
        System.out.println("out create function: "+textList.size());

        Iterator iter = lineList.iterator();
        while(iter.hasNext())
        {
            MyLine myLine = (MyLine)iter.next();
            g.setColor(myLine.color);
            g.drawLine(myLine.x1,myLine.y1,myLine.x2,myLine.y2);
        }

        iter = ovalList.iterator();
        while(iter.hasNext())
        {
            MyOval myOval = (MyOval) iter.next();
            g.setColor(myOval.color);
            if(myOval.filled)
                g.drawOval(myOval.x,myOval.y,myOval.width,myOval.height);
            else
                g.drawOval(myOval.x,myOval.y,myOval.width,myOval.height);

        }

        iter = rectangleList.iterator();
        while(iter.hasNext())
        {
            MyRectangle myRectangle = (MyRectangle) iter.next();
            g.setColor(myRectangle.color);
            if(myRectangle.filled)
            {
                if(myRectangle.isRounCorner)
                    g.fillRoundRect(myRectangle.x,myRectangle.y,myRectangle.width,myRectangle.height,myRectangle.arcWidth,myRectangle.arcHeight);
                else
                    g.fillRect(myRectangle.x,myRectangle.y,myRectangle.width,myRectangle.height);
            }
            else
            {
                if(myRectangle.isRounCorner)
                    g.drawRoundRect(myRectangle.x,myRectangle.y,myRectangle.width,myRectangle.height,myRectangle.arcWidth,myRectangle.arcHeight);
                else
                    g.drawRect(myRectangle.x,myRectangle.y,myRectangle.width,myRectangle.height);
            }
        }

        iter = textList.iterator();
        while(iter.hasNext())
        {
            MyText myText = (MyText) iter.next();
            g.setColor(myText.color);
            g.setFont(myText.font);
            g.drawString(myText.content,myText.x,myText.y);
        }
    }

    public boolean add_draw_object(Object o)
    {
        if( o instanceof MyLine)
            lineList.add((MyLine)o);
        else if( o instanceof MyRectangle)
            rectangleList.add((MyRectangle) o);
        else if( o instanceof MyOval)
            ovalList.add((MyOval) o);
        else if( o instanceof MyText)
            textList.add((MyText) o);
        else
        {
            System.out.println("Invalid draw object");
            //todo a dialog is better
            JOptionPane.showMessageDialog(new JFrame(),"Invalid draw object", "Dialog", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void create_draw_object()
    {
        Object newGraphic;

        switch (mode)
        {
            case line:
                MyLine myLine = new MyLine(x1,y1,x2,y2,currentColor);
                lineList.add(myLine);
                break;
            case rectangle:
                MyRectangle myRectangle = new MyRectangle(x1,y1,x2,y2,currentColor);
                myRectangle.filled = isFill;
                myRectangle.isRounCorner = isRoundCorner;
                rectangleList.add(myRectangle);
                break;
            case oval:
                MyOval myOval = new MyOval(x1,y1,x2,y2,currentColor);
                myOval.filled = isFill;
                ovalList.add(myOval);
                break;
            case text:
                MyText myText = new MyText();
                String s = null;
                while(s==null || s.trim().equals(""))
                {
                    s = JOptionPane.showInputDialog("Please input your text: ");
                }
                myText.content = s;
                myText.color = currentColor;
                myText.font = currentFont;
                message = "You input "+myText.content+".\n";
                textList.add(myText);
                break;
        }

        System.out.print(message);
        System.out.println("in create function: "+textList.size());

        repaint();
    }

    public void check_focus()
    {
        //todo check focus
    }

    public boolean delete_draw_object(Object o)
    {
        Iterator iter;

        if( o instanceof MyLine)
        {
            o = (MyLine) o;
            iter = lineList.iterator();
            while(iter.hasNext())
            {
                if(o == (MyLine) iter.next())
                {
                    iter.remove();
                    repaint();
                    return true;
                }
            }
        }
        else if( o instanceof MyRectangle)
        {
            o = (MyRectangle) o;
            iter = rectangleList.iterator();
            while(iter.hasNext())
            {
                if( o == (MyRectangle) iter.next())
                {
                    iter.remove();
                    repaint();
                    return true;
                }
            }
        }
        else if( o instanceof MyOval)
        {
            o= (MyOval) o;
            iter = ovalList.iterator();
            while(iter.hasNext())
            {
                if( o== (MyOval) iter.next())
                {
                    iter.remove();
                    repaint();
                    return true;
                }
            }
        }
        else if( o instanceof MyText)
        {
            o = (MyText) o;
            iter = textList.iterator();
            while(iter.hasNext())
            {
                if( o == (MyText) iter.next())
                {
                    iter.remove();
                    repaint();
                    return true;
                }
            }
        }
        else
        {
            System.out.println("Invalid draw object");
            JOptionPane.showMessageDialog(new JFrame(),"Invalid draw object", "Dialog", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        System.out.println("Object not Found!");
        JOptionPane.showMessageDialog(new JFrame(),"No deletion ", "Dialog", JOptionPane.ERROR_MESSAGE);

        return false;
    }


}
