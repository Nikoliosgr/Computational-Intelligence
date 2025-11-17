public class Dataset2 {
    Point[] set = new Point[1000];

    // #1
    double[] range1_x1 = {-2,-1.6};
    double[] range1_x2 = {1.6,2};

    // #2
    double[] range2_x1 = {-1.2,-0.8};
    // range2_x2 = range1_x2

    // #3
    double[] range3_x1 = {-0.4,0};
    // range3_x2 = range1_x2

    // #4
    double[] range4_x1 = {-1.8,-1.4};
    double[] range4_x2 = {0.8,1.2};

    // #5
    double[] range5_x1 = {-0.6,-0.2};
    // range5_x2 = range4_x2

    // #6
    // range6_x1 = range1_x1
    double[] range6_x2 = {0,0.4};

    // #7
    // range7_x1 = range2_x1
    // range7_x2 = range6_x2

    // #8
    // range8_x1 = range3_x1
    // range8_x2 = range6_x2

    // #9
    double[] range9_x1 = {-2,0};
    double[] range9_x2 = {0,2};

    public Dataset2(){
        for(int a = 0; a<100; a++){
            Point point = new Point(range1_x1, range1_x2);
            set[a] = point;
        }
        for(int b = 100; b<200; b++){
            Point point = new Point(range2_x1, range1_x2);
            set[b] = point;
        }
        for(int c = 200; c<300; c++){
            Point point = new Point(range3_x1, range1_x2);
            set[c] = point;
        }
        for(int d = 300; d<400; d++){
            Point point = new Point(range4_x1, range4_x2);
            set[d] = point;
        }
        for(int e = 400; e<500; e++){
            Point point = new Point(range5_x1, range4_x2);
            set[e] = point;
        }
        for(int f = 500; f<600; f++){
            Point point = new Point(range1_x1, range6_x2);
            set[f] = point;
        }
        for(int g = 600; g<700; g++){
            Point point = new Point(range2_x1, range6_x2);
            set[g] = point;
        }
        for(int h = 700; h<800; h++){
            Point point = new Point(range3_x1, range6_x2);
            set[h] = point;
        }
        for(int i = 800; i<1000; i++){
            Point point = new Point(range9_x1, range9_x2);
            set[i] = point;
        }
    }
}
