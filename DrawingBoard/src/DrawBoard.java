import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zieng on 11/19/15.
 */
public class DrawBoard extends JFrame
{

    public class DrawObject
    {
        String type;
        Color color;
        int startX,startY,width,height;

        public String toString()
        {
            String str = type;
            str+=" "+color.getRed()+" "+color.getGreen()+" "+color.getBlue()+" "+color.getAlpha();

            str+=" "+startX;
            str+=" "+startY;
            str+=" "+width;
            str+=" "+height;

            return str;
        }

        public boolean is_selected(int xx,int yy)
        {
            //todo need more accurate way
            if( xx >= startX && xx<=startX+width && yy>=startY && yy<= startY+height)
                return true;
            else
                return false;
        }
    }

    public class MyRectangle extends DrawObject
    {
        boolean filled;

        public MyRectangle(int xx,int yy,int w,int h,Color c, boolean isFill)
        {
            type = "MyRectangle";
            startX = xx;
            startY = yy;
            width = w;
            height = h;
            color = c;
            filled = isFill;
        }

        public String toString()
        {

            String str = super.toString();

            str+=" "+filled;

            return str;
        }

        public boolean is_selected(int xx,int yy)
        {
            if( xx >= startX-5 && xx <=(startX+width+5) && yy>=startY-5 && yy<=(startY+height+5)) // inside outer border
            {
                if( xx >= startX+5 && xx<=(startX+width-5) && yy>=startY+5 && yy <=(startY+height-5)  )   // inside inner border
                    return false;
                else
                    return true;
            }

            return false;
        }
    }


    public class MyOval extends DrawObject
    {
        boolean filled;

        public MyOval(int xx,int yy,int w,int h,Color c,boolean isFill)
        {
            type = "MyOval";
            startX = xx;
            startY = yy;
            width  = w;
            height = h;
            color = c;
            filled = isFill;
        }

        public String toString()
        {
            String str = super.toString();
            str += " "+filled;

            return str;
        }
    }

    public class MyLine extends DrawObject
    {
        int endX,endY;

        public MyLine(int x1,int y1,int x2,int y2,Color c)
        {
            type = "MyLine";
            startX = x1;
            startY = y1;
            endX = x2;
            endY = y2;

            width = Math.abs(x2-x1);
            height = Math.abs(y2-y1);

            color = c;
        }

        public String toString()
        {
            String str = type;
            str+=" "+color.getRed()+" "+color.getGreen()+" "+color.getBlue()+" "+color.getAlpha();

            str+=" "+startX;
            str+=" "+startY;
            str+=" "+endX;
            str+=" "+endY;

            return str;
        }
    }

    public class MyText extends DrawObject
    {
        Font font;
        String content;

        public MyText(int x,int y,Color c,Font f,String text)
        {
            type = "MyText";
            startX = x;
            startY = y;
            content = text;
            font = f;
            color = c;

            width = content.length() * font.getSize();
            height = font.getSize();
        }

        public String toString()
        {
            String str = type;
            str+=" "+color.getRed()+" "+color.getGreen()+" "+color.getBlue()+" "+color.getAlpha();

            str+=" "+startX;
            str+=" "+startY;

            str+=" "+font.getFontName();
            str+=" "+font.getStyle();
            str+=" "+font.getSize();

            str+=" "+content;

            return str;
        }

    }

    public class CanvasPanel extends JPanel
    {
        ArrayList<DrawObject> drawObjectArrayList = new ArrayList<>();


        String drawMode="wait";

        private Color currentColor;
        private Font currentFont;
        private boolean isFill;

        private int x1,y1,x2,y2;
        private int clickTimes;
        DrawObject activeDrawObject;  // the  selected draw object (used when deleting an object)
        String message;

        public CanvasPanel()
        {
            currentColor = Color.black;
            currentFont = new Font("Serif",Font.BOLD,10);
            setBorder(new LineBorder(Color.BLACK,2));
            setBackground(Color.LIGHT_GRAY);
            x1=x2=y1=y2=clickTimes=0;

            addMouseListener(new MouseListener()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    System.out.println(message);
                    if(clickTimes == 0)
                    {
                        x1 = e.getX();
                        y1 = e.getY();

                        clickTimes++;

                        if(drawMode.equals("select"))
                        {
                            clickTimes = 0;
                            check_focus();
                        }
                        else if(drawMode.equals("text"))
                        {
                            create_draw_object();
                        }

                        message = "You clicked mouse at ("+x1+","+y1+").";
                    }
                    else if(clickTimes == 1)
                    {
                        x2 = e.getX();
                        y2 = e.getY();
                        clickTimes = 0;

                        if(drawMode.equals("rectangle") || drawMode.equals("line") || drawMode.equals("oval"))
                            create_draw_object();

                        message = "You clicked mouse at ("+x2+","+y2+").";
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

        void set_mode(String m)
        {
            drawMode = m;
        }

        String get_mode()
        {
            return drawMode;
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

        String get_message()
        {
            return message;
        }

        void set_fill(boolean b)
        {
            isFill =b;
        }

        boolean get_fill_state()
        {
            return isFill;
        }

        protected void paintComponent(Graphics g)
        {
            super.paintComponents(g);

            message = "Painting items......\n";

            Iterator iter = drawObjectArrayList.iterator();
            while(iter.hasNext())
            {
                DrawObject temp = (DrawObject) iter.next();
                draw( g, temp);
            }
        }

        protected void draw(Graphics g,DrawObject toDraw)
        {
            g.setColor(toDraw.color);

            if(toDraw instanceof MyLine)
            {
                g.drawLine(toDraw.startX,toDraw.startY,((MyLine) toDraw).endX,((MyLine) toDraw).endY);
            }
            else if(toDraw instanceof MyRectangle)
            {
                if(((MyRectangle) toDraw).filled)
                    g.fillRect(toDraw.startX,toDraw.startY,toDraw.width,toDraw.height);
                else
                    g.drawRect(toDraw.startX,toDraw.startY,toDraw.width,toDraw.height);
            }
            else if (toDraw instanceof MyOval)
            {
                if(((MyOval) toDraw).filled)
                    g.fillRect(toDraw.startX,toDraw.startY,toDraw.width,toDraw.height);
                else
                    g.drawRect(toDraw.startX,toDraw.startY,toDraw.width,toDraw.height);
            }
            else if(toDraw instanceof MyText)
            {
                g.setFont(((MyText) toDraw).font);
                g.drawString(((MyText) toDraw).content,toDraw.startX,toDraw.startY);
            }
            else
            {
                JOptionPane.showMessageDialog(new JFrame(),"Invalid draw object", "Dialog", JOptionPane.ERROR_MESSAGE);
            }

        }

        public boolean add_draw_object(DrawObject toAdd)
        {
            if(toAdd == null)
            {
                JOptionPane.showMessageDialog(new JFrame(),"Add a null draw object", "Dialog", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            drawObjectArrayList.add(toAdd);

            return true;
        }

        public void create_draw_object()
        {
            int x,y,w,h;

            if(drawMode.equals("MyLine"))
            {
                MyLine myLine = new MyLine(x1,y1,x2,y2,currentColor);
                drawObjectArrayList.add(myLine);
            }
            else if(drawMode.equals("MyRectangle"))
            {
                w = Math.abs(x2-x1);
                h = Math.abs(y2-y1);
                x = Math.min(x1,x2);
                y = Math.min(y1,y2);
                MyRectangle myRectangle = new MyRectangle(x,y,w,h,currentColor,isFill);

                drawObjectArrayList.add(myRectangle);
            }
            else if(drawMode.equals("MyOval"))
            {
                w = Math.abs(x2-x1);
                h = Math.abs(y2-y1);
                x = Math.min(x1,x2);
                y = Math.min(y1,y2);
                MyOval myOval = new MyOval(x,y,w,h,currentColor,isFill);
                myOval.filled = isFill;
            }
            else if(drawMode.equals("MyText"))
            {
                String s = JOptionPane.showInputDialog("Please input your text: ");
                if(s==null)
                    return;

                MyText myText = new MyText(x1,y1,currentColor,currentFont,s);
                if(!myText.content.trim().equals(""))
                    drawObjectArrayList.add(myText);

            }

            System.out.print(message);

            repaint();
        }

        public void check_focus()
        {
            for(DrawObject toCheck: drawObjectArrayList)
            {
                if(toCheck.is_selected(x1,y1))
                {
                    activeDrawObject = toCheck;
                    return ;
                }
            }

        }

        public boolean delete_draw_object(DrawObject toDelete)
        {
            Iterator iter = drawObjectArrayList.iterator();

            while (iter.hasNext())
            {
                if(toDelete == iter.next())
                {
                    iter.remove();
                    return true;
                }
            }

            JOptionPane.showMessageDialog(new JFrame(),"No deletion ", "Dialog", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        public void clear_canvas()
        {
            drawObjectArrayList.clear();
            repaint();
        }
    }


    // memmber variables

    static GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    static Font[] allFonts = e.getAllFonts();
    ArrayList<String> fontNameSet;
    ArrayList<String> fontSizeSet;

    JMenuBar menuBar;
    JMenu menuFile;
    JMenu menuHelp;
    JMenuItem menuFile_new;
    JMenuItem menuFile_open;
    JMenuItem menuFile_save;
    JMenuItem menuFile_quit;
    JMenuItem menuHelp_guide;
    JMenuItem menuHelp_about;

    JPanel topPanel;
    JPanel leftPanel;
    JPanel bottomPanel;
    JPanel rightPanel;
    CanvasPanel canvasPanel;

    JCheckBox checkFillBox;
    JColorChooser colorChooser;
    JComboBox fontNameChooser;
    JComboBox fontSizeChooser;

    JRadioButton jrbtSelect,jrbtLine,jrbtRectangle,jrbtOval,jrbtText;

    JTextArea messageArea;


    public DrawBoard()
    {
        fontNameSet = new ArrayList<>();
        for(Font f:allFonts)
        {
            fontNameSet.add(f.getFontName());
        }
        for(int i=8;i<=64;i++)
        {
            fontSizeSet.add(i);
        }

        init_layout();
        init_listener();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init_layout()
    {
        menuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuHelp = new JMenu("Help");
        menuFile_new = new JMenuItem("New");
        menuFile_open = new JMenuItem("Open");
        menuFile_save = new JMenuItem("Save");
        menuFile_quit = new JMenuItem("Quit");
        menuHelp_guide = new JMenuItem("Guide");
        menuHelp_about = new JMenuItem("Help");

        menuFile.add(menuFile_new);
        menuFile.add(menuFile_open);
        menuFile.add(menuFile_save);
        menuFile.add(menuFile_quit);
        menuHelp.add(menuHelp_guide);
        menuHelp.add(menuHelp_about);

        menuBar.add(menuFile);
        menuBar.add(menuHelp);
        add(menuBar);

        topPanel = new JPanel();
        leftPanel = new JPanel();
        bottomPanel = new JPanel();
        canvasPanel = new CanvasPanel();

        fontNameChooser = new JComboBox(fontNameSet.toArray());
        fontSizeChooser = new JComboBox(fontSizeSet.toArray());
        colorChooser = new JColorChooser();
        checkFillBox = new JCheckBox();

        GridLayout topPanelLayout = new GridLayout(1,0);
        topPanel.setLayout(topPanelLayout);
        topPanel.add(fontNameChooser);
        topPanel.add(fontSizeChooser);
        topPanel.add(checkFillBox);
        topPanel.add(colorChooser);

        jrbtLine = new JRadioButton("line");
        jrbtOval = new JRadioButton("oval");
        jrbtText = new JRadioButton("text");
        jrbtRectangle = new JRadioButton("rectangle");
        jrbtSelect = new JRadioButton("select");
        ButtonGroup btGroup = new ButtonGroup();
        btGroup.add(jrbtLine);
        btGroup.add(jrbtOval);
        btGroup.add(jrbtRectangle);
        btGroup.add(jrbtSelect);
        btGroup.add(jrbtText);

        GridLayout leftPanelLayout = new GridLayout(0,1);
        leftPanel.setLayout(leftPanelLayout);
        leftPanel.add(jrbtSelect);
        leftPanel.add(jrbtLine);
        leftPanel.add(jrbtRectangle);
        leftPanel.add(jrbtOval);
        leftPanel.add(jrbtText);

        messageArea = new JTextArea(5,60);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        bottomPanel.add(messageArea);


        BorderLayout drawBoardLayout = new BorderLayout();
        setLayout(drawBoardLayout);
        add(topPanel,BorderLayout.NORTH);
        add(leftPanel,BorderLayout.WEST);
        add(bottomPanel,BorderLayout.SOUTH);
        add(canvasPanel,BorderLayout.CENTER);



    }

    public void init_listener()
    {
        menuFile_new.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvasPanel.clear_canvas();
            }
        });

        menuFile_open.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });
    }

}
