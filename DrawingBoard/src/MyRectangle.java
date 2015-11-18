import java.awt.*;

/**
 * Created by zieng on 11/18/15.
 */
public class MyRectangle
{
    boolean filled;
    boolean isRounCorner;
    int x,y,width,height;
    int arcWidth,arcHeight;
    Color color;

    public MyRectangle(int xx,int yy,int w,int h,Color c)
    {
        x = xx;
        y = yy;
        width = w;
        height = h;
        color = c;
    }
}
