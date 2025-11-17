import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.lang.Math;

public class MakePlot extends JPanel {
    ArrayList<Point> pointList;
    Dataset2 dataset2;

    public MakePlot(ArrayList<Point> pointList, Dataset2 dataset2){
        this.pointList = pointList;
        this.dataset2 = dataset2;
    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawLine(50,500,500,500); // x axis, used for x1
        g.drawLine(50,50,500,50);
        g.drawLine(50, 500, 50, 50); // y axis, used for x2
        g.drawLine(500, 500, 500, 50);

        for (double i = -2.0; i <= -0; i += 0.2) {
            int x = 50 + (int) ((i + 2) * 225);
            g.drawLine(x, 500, x, 510);
            g.drawString(Double.toString(Math.round((i)*10)/10.0), x-10, 520);
        }

        for (double j = 0; j <= 2; j += 0.2) {
            int y = 500 - (int) (j * 225);
            g.drawLine(40, y, 50, y);
            g.drawString(Double.toString(Math.round((j)*10)/10.0), 20, y + 5);
        }

        for (Point p : dataset2.set) {
            int x = 50 + (int) ((p.getX1() + 2) * 225);
            int y = 500 - (int) (p.getX2() * 225);
            g.drawString("+", x, y);
        }

        g.setColor(Color.RED);
        for(int k = 0; k<pointList.size(); k++) {
            for (Point p : pointList) {
                int x = 50 + (int) ((p.getX1() + 2) * 225);
                int y = 500 - (int) (p.getX2() * 225);
                g.drawString("*", x, y);
            }
        }
    }
}
