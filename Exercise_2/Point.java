import java.util.Random;
import java.lang.Math;
public class Point {
    Random rand = new Random();

    double x1;
    double x2;

    double[] x1_range;
    double[] x2_range;

    public Point(){
    }

    public Point(double x1_range[], double x2_range[]){
        this.x1_range = x1_range;
        this.x2_range = x2_range;
        x1 = Math.round((rand.nextDouble() * (x1_range[1] - x1_range[0]) + x1_range[0]) * 10) / 10.0;
        x2 = Math.round((rand.nextDouble() * (x2_range[1] - x2_range[0]) + x2_range[0]) * 10) / 10.0;
    }

    public double getX1(){
        return x1;
    }

    public double getX2(){
        return x2;
    }

    public void setX1(double x1){
        this.x1 = x1 ;
    }

    public void setX2(double x2){
        this.x2 = x2;
    }
}
