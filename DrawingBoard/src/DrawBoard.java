import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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
                            message = "You clicked mouse at ("+x1+","+y1+").";
                            clickTimes = 0;
                            check_focus();
                        }
                        else if(drawMode.equals("MyText"))
                        {
                            message = "You clicked mouse at ("+x1+","+y1+"). Goint to insert a text at this position";
                            create_draw_object();
                        }
                        else
                        {
                            message = "You clicked mouse at ("+x1+","+y1+").";
                            if(!drawMode.equals("wait"))
                            {
                                message += "Need Another click to finish the drawing.";
                            }
                            else
                                clickTimes = 0;
                        }

                        display_message();
                    }
                    else if(clickTimes == 1)
                    {
                        x2 = e.getX();
                        y2 = e.getY();
                        clickTimes = 0;

                        if(drawMode.equals("MyRectangle") || drawMode.equals("MyLine") || drawMode.equals("MyOval"))
                            create_draw_object();

                        message = "You clicked mouse at ("+x2+","+y2+"). Drawing is finished. You can start another operation.";
                        display_message();
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
                DrawObject toDraw = (DrawObject) iter.next();

//                draw(g,toDraw);
                g.setColor(toDraw.color);

                if(toDraw instanceof MyLine)
                {
                    g.drawLine(toDraw.startX,toDraw.startY,((MyLine) toDraw).endX,((MyLine) toDraw).endY);
                    message = "You draw a new line";
                }
                else if(toDraw instanceof MyRectangle)
                {
                    if(((MyRectangle) toDraw).filled)
                        g.fillRect(toDraw.startX,toDraw.startY,toDraw.width,toDraw.height);
                    else
                        g.drawRect(toDraw.startX,toDraw.startY,toDraw.width,toDraw.height);
                    message = "You draw a new rectangle.";
                }
                else if (toDraw instanceof MyOval)
                {
                    if(((MyOval) toDraw).filled)
                        g.fillOval(toDraw.startX, toDraw.startY, toDraw.width, toDraw.height);
                    else
                        g.drawOval(toDraw.startX, toDraw.startY, toDraw.width, toDraw.height);
                    message = "You draw a new ellipse.";
                }
                else if(toDraw instanceof MyText)
                {
                    g.setFont(((MyText) toDraw).font);
                    g.drawString(((MyText) toDraw).content,toDraw.startX,toDraw.startY);
                    message = "You insert a new text.";
                }
                else
                {
                    JOptionPane.showMessageDialog(new JFrame(),"Invalid draw object", "Dialog", JOptionPane.ERROR_MESSAGE);
                }
                display_message();
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

                drawObjectArrayList.add(myOval);
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

            message += "create ok";
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
                    message = "You select :\n\t"+toCheck.toString();
                    display_message();

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

        public DrawObject get_active_object()
        {
            return activeDrawObject;
        }

        public void clear_canvas()
        {
            drawObjectArrayList.clear();
            repaint();
        }

        public String toString()
        {
            String str="";
            Iterator iter = drawObjectArrayList.iterator();
            while(iter.hasNext())
            {
                str += iter.next().toString()+"\n";
            }
            return str;
        }
    }


    // memmber variables

    static GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
    static Font[] allFonts = e.getAllFonts();
    ArrayList<String> fontNameSet;
    ArrayList<Integer> fontSizeSet;
    String currentFontname;
    int currentFontStyle;
    int currentFontSize;

    String message;

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
    JButton colorChooser;
    JComboBox fontNameChooser;
    JComboBox fontSizeChooser;

    JRadioButton jrbtSelect,jrbtLine,jrbtRectangle,jrbtOval,jrbtText;

    JTextArea messageArea;


    public DrawBoard()
    {
        fontNameSet = new ArrayList<>();
        fontSizeSet = new ArrayList<>();
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
        setJMenuBar(menuBar);

        topPanel = new JPanel();
        leftPanel = new JPanel();
        bottomPanel = new JPanel();
        canvasPanel = new CanvasPanel();

        fontNameChooser = new JComboBox(fontNameSet.toArray());
        fontSizeChooser = new JComboBox(fontSizeSet.toArray());
        colorChooser = new JButton("Color");
        checkFillBox = new JCheckBox("Fill");

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
        bottomPanel.add(jScrollPane);


        BorderLayout drawBoardLayout = new BorderLayout();
        setLayout(drawBoardLayout);
        add(topPanel,BorderLayout.NORTH);
        add(leftPanel,BorderLayout.WEST);
        add(bottomPanel,BorderLayout.SOUTH);
        add(canvasPanel,BorderLayout.CENTER);



    }

    public void init_listener()
    {

        // add to DrawBoard itself
        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
//                super.keyPressed(e);
                requestFocus();
                message = "delete..";
                display_message();
                if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE && canvasPanel.get_mode().equals("select"))
                {
                    if(canvasPanel.get_active_object() == null)
                    {
                        message = "You need to choose a figure before you confirm to delete it.";
                        display_message();
                    }
                    else
                    {
                        message = "You delete the :\n\t"+canvasPanel.get_active_object().toString();
                        display_message();
                        canvasPanel.delete_draw_object(canvasPanel.get_active_object());
                        repaint();
                    }
                }
            }
        });

        menuFile_new.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvasPanel.clear_canvas();
                message = "You create a new canvas";
                display_message();
            }
        });

        menuFile_open.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser fc = new JFileChooser();
                int ret = fc.showOpenDialog(null);
                if(ret == JFileChooser.APPROVE_OPTION )
                {
                    try
                    {
                        FileReader fr = new FileReader( fc.getSelectedFile() );
                        BufferedReader bf = new BufferedReader(fr);
                        message = "You Open the "+fc.getSelectedFile().getAbsolutePath();
                        String line;
                        ArrayList<String> list;

                        while((line = bf.readLine()) != null)
                        {
                            int x1,y1,x2,y2;
                            int r,g,b,a;
                            list = new ArrayList<String>(Arrays.asList(line.split(" ")));

                            r = Integer.parseInt(list.get(1));
                            g = Integer.parseInt(list.get(2));
                            b = Integer.parseInt(list.get(3));
                            a = Integer.parseInt(list.get(4));
                            Color color = new Color(r,g,b,a);

                            if(line.contains("MyLine"))
                            {
                                x1 = Integer.parseInt(list.get(5));
                                y1 = Integer.parseInt(list.get(6));
                                x2 = Integer.parseInt(list.get(7));
                                y2 = Integer.parseInt(list.get(8));

                                MyLine myLine = new MyLine(x1,y1,x2,y2,color);
                                canvasPanel.add_draw_object(myLine);
                            }
                            else if(line.contains("MyRectangle"))
                            {
                                x1 = Integer.parseInt(list.get(5));
                                y1 = Integer.parseInt(list.get(6));
                                x2 = Integer.parseInt(list.get(7));
                                y2 = Integer.parseInt(list.get(8));
                                boolean f = Boolean.parseBoolean(list.get(9));

                                MyRectangle myRectangle = new MyRectangle(x1,y1,x2,y2,color,f);
                                canvasPanel.add_draw_object(myRectangle);
                            }
                            else if(line.contains("MyOval"))
                            {
                                x1 = Integer.parseInt(list.get(5));
                                y1 = Integer.parseInt(list.get(6));
                                x2 = Integer.parseInt(list.get(7));
                                y2 = Integer.parseInt(list.get(8));
                                boolean f = Boolean.parseBoolean(list.get(9));

                                MyOval myOval = new MyOval(x1,y1,x2,y2,color,f);
                                canvasPanel.add_draw_object(myOval);
                            }
                            else if(line.contains("MyText"))
                            {
                                x1 = Integer.parseInt(list.get(5));
                                y1 = Integer.parseInt(list.get(6));
                                String fontName = list.get(7);
                                int fontStyle = Integer.parseInt(list.get(8));
                                int fontSize = Integer.parseInt(list.get(9));
                                String content = list.get(10);
                                Font f = new Font(fontName,fontStyle,fontSize);

                                MyText myText = new MyText(x1,y1,color,f,content);
                                canvasPanel.add_draw_object(myText);
                            }
                        }
                        canvasPanel.repaint();

                    }catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }

                }
            }
        });

        menuFile_save.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();

                int retrival = chooser.showSaveDialog(null);
                if (retrival == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        FileWriter fw = new FileWriter(chooser.getSelectedFile()+".txt");
                        fw.write( canvasPanel.toString() );
                        fw.close();
                        message = "You save the canvas to "+chooser.getSelectedFile().getAbsolutePath();
                        display_message();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

            }
        });

        menuFile_quit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //todo
            }
        });

        menuHelp_guide.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //todo
            }
        });

        menuHelp_about.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //todo
            }
        });

        fontNameChooser.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                currentFontname = fontNameSet.get( fontNameChooser.getSelectedIndex());
                Font f = new Font(currentFontname,currentFontStyle,currentFontSize);
                canvasPanel.set_font(f);
                message = "You set the font to "+f.toString();
                display_message();
            }
        });

        fontSizeChooser.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                currentFontSize = fontSizeSet.get(fontSizeChooser.getSelectedIndex());
                Font f = new Font(currentFontname,currentFontStyle,currentFontSize);
                canvasPanel.set_font(f);
                message = "You set the font to "+f.toString();
                display_message();
            }
        });

        checkFillBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                if ( e.getStateChange() == ItemEvent.SELECTED)
                    canvasPanel.set_fill(true);
                else
                    canvasPanel.set_fill(false);
                message = "You change the fill option from "+!canvasPanel.get_fill_state()+" to "+canvasPanel.get_fill_state();
                display_message();
            }
        });

        colorChooser.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Color c = JColorChooser.showDialog(null, "Choose a Color", null);
                canvasPanel.set_color(c);
                message = "You set the color to "+c.toString();
                display_message();
            }
        });

        jrbtSelect.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvasPanel.set_mode("select");
                message = "Ready to select an object";
                display_message();
                requestFocus();
            }
        });

        jrbtLine.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvasPanel.set_mode("MyLine");
                message = "Ready to draw a line";
                display_message();
            }
        });

        jrbtRectangle.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvasPanel.set_mode("MyRectangle");
                message = "Ready to draw a rectangle";
                display_message();
            }
        });

        jrbtOval.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvasPanel.set_mode("MyOval");
                message = "Ready to draw an oval";
                display_message();
            }
        });

        jrbtText.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvasPanel.set_mode("MyText");
                message = "Ready to insert a text";
                display_message();
            }
        });


    }

    public void display_message()
    {
        messageArea.append(message+"\n");
    }






    public static void main(String [] args)
    {
        DrawBoard db = new DrawBoard();
        db.setVisible(true);
        db.setLocationRelativeTo(null);
        db.setSize(800,800);
    }
}
