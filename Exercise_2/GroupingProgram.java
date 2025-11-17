import javax.swing.*;
import java.util.Random;
import java.lang.Math;
import java.util.HashMap;
import java.util.ArrayList;

public class GroupingProgram {
    // number of centers
    private static final int M1 = 4;
    private static final int M2 = 6;
    private static final int M3 = 8;
    private static final int M4 = 10;
    private static final int M5 = 12;

    Random rand = new Random();
    boolean loop = true;
    Dataset2 dataset2;
    ArrayList<Point> initialCenters = new ArrayList<>();
    HashMap<Point, Point> groups = new HashMap<>();
    HashMap<Point, Integer> groupCounters = new HashMap<>();

    public GroupingProgram(int M, Dataset2 dataset2){
        this.dataset2 = dataset2;
        for (int i = 0; i < M; i++) {
            initialCenters.add(dataset2.set[rand.nextInt(1000)]);
        }
        while(loop) {
            Point point;
            for (Point p : dataset2.set) {
                point = minimumEuclideanDistance(p);
                if (!groups.containsKey(point)) {
                    groups.put(point, p);
                    groupCounters.put(point, 1);
                } else {
                    Point buffer = new Point(); //buffer needs to be initialized here so it can be a separate instance
                    buffer.setX1(groups.get(point).getX1() + p.getX1());
                    buffer.setX2(groups.get(point).getX2() + p.getX2());
                    groups.put(point, buffer);
                    groupCounters.put(point, groupCounters.get(point) + 1);
                }
            }
            cleanup();
        }
    }

    public Point minimumEuclideanDistance(Point p){
        double distance;
        double minDistance = 100; //initializing value
        Point minPoint = p; //initializing value
        for(Point c : initialCenters){
            distance = Math.sqrt(Math.pow(p.getX1() - c.getX1(),2) + Math.pow(p.getX2() - c.getX2(),2));
            if(distance < minDistance){
                minDistance = distance;
                minPoint = c;
            }
        }
        return minPoint;
    }

    public void cleanup(){
        ArrayList<Point> bufferCenters = new ArrayList<>();
        for(int j = 0; j<initialCenters.size(); j++){
            if(groups.containsKey(initialCenters.get(j))){
                Point buffer = new Point(); //buffer needs to be initialized here so it can be a separate instance
                buffer.setX1(Math.round(groups.get(initialCenters.get(j)).getX1()/groupCounters.get(initialCenters.get(j))*10)/10.0);
                buffer.setX2(Math.round(groups.get(initialCenters.get(j)).getX2()/groupCounters.get(initialCenters.get(j))*10)/10.0);
                bufferCenters.add(buffer);
            }
        }
        for(Point p : initialCenters){
            if(groups.containsKey(p)){
                groups.remove(p);
                groupCounters.remove(p);
            }
        }

        boolean sameCenters = true;
        if(initialCenters.size() == bufferCenters.size()){
            for(int i = 0; i < initialCenters.size(); i++){
                if(initialCenters.get(i).getX1() != bufferCenters.get(i).getX1() || initialCenters.get(i).getX2() != bufferCenters.get(i).getX2()){
                    sameCenters = false;
                    break;
                }
            }
        } else {
            sameCenters = false;
        }

        if(sameCenters){
            loop = false;
        }

        initialCenters = bufferCenters;
    }

    public double computeError() {
        double error = 0;
        Point closestCenter;
        for (Point p : dataset2.set) {
            closestCenter = minimumEuclideanDistance(p);
            error += Math.pow(p.getX1() - closestCenter.getX1(), 2) + Math.pow(p.getX2() - closestCenter.getX2(), 2);
        }
        return error;
    }


    public static void main(String[] args){
        int[] M = {M1,M2,M3,M4,M5};
        Random rand = new Random();
        int r;
        double error;
        ArrayList<Double> errorList = new ArrayList<>();
        ArrayList<Integer> groupsList = new ArrayList<>();
        ArrayList<ArrayList<Point>> pointList = new ArrayList<>();
        Dataset2 dataset2 = new Dataset2();
        boolean goLast;
        for(int i = 0; i<20; i++){
            r = rand.nextInt(5);
            GroupingProgram gp = new GroupingProgram(M[r], dataset2);
            goLast = true;
            error = gp.computeError();
            if(errorList.size()==0){
                errorList.add(error);
                groupsList.add(M[r]);
                pointList.add(gp.initialCenters);
            }else{
                for(int j = 0; j<errorList.size(); j++){
                    if(errorList.get(j)>error){
                        errorList.add(j,error);
                        groupsList.add(j,M[r]);
                        pointList.add(j,gp.initialCenters);
                        goLast = false;
                        break;
                    }
                }
                if(goLast){
                    errorList.add(error);
                    groupsList.add(M[r]);
                    pointList.add(gp.initialCenters);
                }
            }
        }
        for(int i = 0; i<errorList.size(); i++){
            System.out.println("Error: " + errorList.get(i) + " | Number of Groups: " + groupsList.get(i) + "\n");
        }

        System.out.println("Minimum Error: " + errorList.get(0) + " | Mimimum Number of Groups: " + groupsList.get(0));
        System.out.println("Centers from the minimum error: ");
        for(Point p : pointList.get(0)){
            System.out.println(p.getX1() + " " + p.getX2());
        }

        JFrame frame = new JFrame("Point Plot");
        MakePlot mp = new MakePlot(pointList.get(0), dataset2);
        frame.add(mp);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JFrame errorFrame = new JFrame("Errors per Number of Groups");
        MakeErrorPlot mep = new MakeErrorPlot(errorList, groupsList);
        errorFrame.add(mep);
        errorFrame.setSize(600, 600);
        errorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        errorFrame.setVisible(true);
    }
}
