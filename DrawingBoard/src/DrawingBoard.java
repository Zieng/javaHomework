import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zieng on 11/18/15.
 */

public class DrawingBoard extends JFrame
{
    JMenuBar menuBar = new JMenuBar();

    JMenu menu_file = new JMenu("File");
    JMenuItem file_new = new JMenuItem("New");
    JMenuItem file_open = new JMenuItem("Open");
    JMenuItem file_close = new JMenuItem("Close");
    JMenuItem file_save = new JMenuItem("Save");
    JMenuItem file_save_as = new JMenuItem("Save as");

    JMenu menu_help = new JMenu("Help");
    JMenuItem help_about = new JMenuItem("About");
    JMenuItem help_guide = new JMenuItem("Guide");

    private String message;
    public CanvasPanel CP = new CanvasPanel();

    JPanel topPanel = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JPanel rightPanel = new JPanel();

    JRadioButton jbtSelect = new JRadioButton("select",false);
    JRadioButton jbtLine = new JRadioButton("line",false);
    JRadioButton jbtRectangle = new JRadioButton("rectangle",false);
    JRadioButton jbtOval = new JRadioButton("ellipse",false);
    JRadioButton jbtText = new JRadioButton("text",false);

    //        JRadioButton jbtSelect= create_img_button("select","DrawingBoard/picture/selectIcon.jpg",40,20);
//        JRadioButton jbtLine = create_img_button("line","DrawingBoard/picture/lineIcon.jpg",40,20);
//        JRadioButton jbtRectangle = create_img_button("rectangle","DrawingBoard/picture/rectangleIcon.jpg",40,20);
//        JRadioButton jbtOval = create_img_button("ellipse","DrawingBoard/picture/ellipseIcon.jpg",40,20);
//        JRadioButton jbtText = create_img_button("text","DrawingBoard/picture/textIcon.png",40,20);

    JTextArea messageArea = new JTextArea("empty");



    public DrawingBoard()
    {
        setTitle("Enjoy Drawing!");
        setSize(800,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        CP.setSize(600,600);
        messageArea.setSize(800,200);
        CP.set_mode(CanvasPanel.DrawMode.wait);

        Border lineBorder = new LineBorder(Color.BLACK,2);
        //set menu bar
        setJMenuBar(menuBar);

        // set menu_file
        menu_file.add(file_new);
        menu_file.add(file_open);
        menu_file.add(file_close);
        menu_file.add(file_save);
        menu_file.add(file_save_as);

        menuBar.add(menu_file);
        // set menu_help
        menu_help.add(help_guide);
        menu_help.add(help_about);

        menuBar.add(menu_help);

        // set layout and panel
        BorderLayout bdLayout = new BorderLayout(0,0);
        setLayout(bdLayout);

        topPanel.setBorder(lineBorder);
        leftPanel.setBorder(lineBorder);
        bottomPanel.setBorder(lineBorder);
        rightPanel.setBorder(lineBorder);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(bottomPanel, BorderLayout.SOUTH);
        add(CP, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);


        // set left Panel-- for draw tools
        ButtonGroup btGroup = new ButtonGroup();

        btGroup.add(jbtSelect);
        btGroup.add(jbtLine);
        btGroup.add(jbtRectangle);
        btGroup.add(jbtOval);
        btGroup.add(jbtText);

        jbtSelect.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CP.set_mode(CanvasPanel.DrawMode.select);
                display_message();
            }
        });

        jbtLine.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CP.set_mode(CanvasPanel.DrawMode.line);
                message = "ready to draw a line\n";
                display_message();
            }
        });

        jbtOval.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CP.set_mode(CanvasPanel.DrawMode.oval);
                message = "ready to draw an oval\n";
                display_message();
            }
        });

        jbtRectangle.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CP.set_mode(CanvasPanel.DrawMode.rectangle);
                message = "ready to draw a rectangle\n";
                display_message();
            }
        });

        jbtText.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CP.set_mode(CanvasPanel.DrawMode.text);
                message = "ready to insert a text\n";
                display_message();
            }
        });

        GridLayout grdLayout = new GridLayout(5,1,0,0);
        leftPanel.setLayout(grdLayout);
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.black,2));
        leftPanel.add(jbtSelect);
        leftPanel.add(jbtLine);
        leftPanel.add(jbtRectangle);
        leftPanel.add(jbtOval);
        leftPanel.add(jbtText);

        // set bottom panel--for operation messages
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        bottomPanel.setLayout(flowLayout);
        bottomPanel.add(messageArea);


        // set right panel--- reserve for future use

    }



    public static JRadioButton create_img_button(String text,String imgPath,int width,int height)
    {
        JRadioButton jbt = new JRadioButton(text,false);

        ImageIcon imgIcon = new ImageIcon(imgPath);
        Image img = imgIcon.getImage();
        img = img.getScaledInstance( width, height,  java.awt.Image.SCALE_SMOOTH );
        imgIcon.setImage(img);
        jbt.setIcon(imgIcon);

        jbt.setPreferredSize(new Dimension(width,height));
        jbt.setBorder(new LineBorder(Color.BLACK,2));

        jbt.setBackground(Color.BLUE);

        return jbt;
    }


    public void display_message()
    {
        messageArea.setText(message);
    }

    public static void main(String [] args)
    {
        DrawingBoard db = new DrawingBoard();
    }
}
