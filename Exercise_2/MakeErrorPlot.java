import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MakeErrorPlot extends JPanel {
    ArrayList<Double> errorList;
    ArrayList<Integer> groupsList;

    public MakeErrorPlot(ArrayList<Double> errorList, ArrayList<Integer> groupsList) {
        this.errorList = errorList;
        this.groupsList = groupsList;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(80, 500, 530, 500); // x axis, used for number of groups
        g.drawLine(80, 500, 80, 50);  // y axis, used for errors

        g.drawString("Number of Groups", 250, 540);
        g.drawString("Errors", 0, 300);

        int[] M_list = {4, 6, 8, 10, 12};

        for (int i = 0; i < 5; i++) {
            int x = 50 + (int) ((i + 1) * 90);
            g.drawLine(x, 500, x, 510);
            g.drawString(Double.toString(M_list[i]), x - 10, 520);
        }

        for (int j = 0; j < 20; j++) {
            int y = 500 - (int) (j * 23);
            g.drawLine(70, y-10, 80, y-10);
            g.drawString(Double.toString(Math.round(errorList.get(j) * 100) / 100.0), 25, y - 5);
        }

        int v = 0;
        for (int k = 0; k < errorList.size(); k++) {
            int y = 500 - (int) (k * 23);
            if(groupsList.get(k) == 4){
                v=0;
            }else if(groupsList.get(k) == 6){
                v=1;
            }else if(groupsList.get(k) == 8){
                v=2;
            }else if(groupsList.get(k) == 10){
                v=3;
            }else if(groupsList.get(k) == 12){
                v=4;
            }
            int x = 50 + (int) ((v + 1) * 90);
            g.drawString("+", x-3, y);
        }

    }
}