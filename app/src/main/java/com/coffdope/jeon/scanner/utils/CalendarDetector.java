package com.coffdope.jeon.scanner.utils;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import Jama.Matrix;

/**
 * Created by jeon on 18. 3. 14.
 */

public class CalendarDetector {
    /*
        * 주어진 이미지에서 격자로 이루어진 사각형을 찾는다.
        * houghtransform을 이용한다.
        * */
    public static ArrayList<Point> findIntersections(Mat src){
        Mat input,inter,hough, thres;
        input = new Mat(src.cols(),src.rows(),src.type());
        Core.rotate(src, input, Core.ROTATE_90_CLOCKWISE);
        inter = new Mat(input.size(), CvType.CV_8UC1);
        thres = new Mat(input.size(), CvType.CV_8UC1);
        hough = new Mat();

        Imgproc.rectangle(input,new Point(0,0),new Point(input.width()-0,input.height()-0),new Scalar(0,0,0),3);
        Imgproc.GaussianBlur(input, inter, new Size(5, 5),8,8);
        Imgproc.adaptiveThreshold(inter,thres,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY_INV,21,10);
        Imgproc.HoughLines(thres,hough,2,Math.PI/180.0,550);

        /*ArrayList for intersect parameters*/
        ArrayList<ArrayList<Double>> intersect = new ArrayList<ArrayList<Double>>();
        ArrayList<Point> intersetionPoints = new ArrayList<Point>();
        double data[][] = new double[hough.rows()][2];

        for(int i=0; i<hough.rows();++i){
            data[i] = hough.get(i, 0);
        }

        double pos_hori=0;
        double pos_vert=0;

        Arrays.sort(data, new Comparator<double[]>() {
            @Override
            public int compare(double[] doubles, double[] t1) {
                if(doubles[0]>t1[0]){
                    return 1;
                }else if(doubles[0]<t1[0]){
                    return -1;
                }else
                return 0;
            }
        });

        int hori_flag=0;
        int vert_flag=0;
        for(int i=0; i<hough.rows(); ++i){
            double rho = data[i][0];
            double theta = data[i][1];

            double cos = Math.cos(theta);
            double sin = Math.sin(theta);
            double x0 = cos * rho;
            double y0 = sin * rho;

            Point pt1 = new Point(x0 + 20000 * (-sin), y0 + 20000 * (cos));
            Point pt2 = new Point(x0 - 20000 * (-sin), y0 - 20000 * (cos));

            if(sin >0.5){
               if(hori_flag==0||rho-pos_hori>100){
                   pos_hori=rho;
                   ArrayList<Double> tmp = new ArrayList<Double>();
                   tmp.add(rho);
                   tmp.add(theta);
                   tmp.add(-1d);
                   intersect.add(tmp);
                   hori_flag=1;
               }
            }else {
                if(vert_flag==0||rho-pos_vert>100){
                    pos_vert=rho;
                    ArrayList<Double> tmp = new ArrayList<Double>();
                    tmp.add(rho);
                    tmp.add(theta);
                    tmp.add(1d);
                    intersect.add(tmp);
                    vert_flag=1;
                }
            }
        }

        /*get intersection points*/
        for(int i=0; i<intersect.size();++i){
           if(intersect.get(i).get(2)<0){
               for(int j=0; j<intersect.size();++j){
                  if(intersect.get(j).get(2)>0){
                      double theta_point_1 = intersect.get(i).get(1);
                      double theta_point_2 = intersect.get(j).get(1);

                      double rho_point_1 = intersect.get(i).get(0);
                      double rho_point_2 = intersect.get(j).get(0);

                      double[][] cossin = {{Math.cos(theta_point_1), Math.sin(theta_point_1)}, {Math.cos(theta_point_2), Math.sin(theta_point_2)}};
                      double[][] rhos = {{rho_point_1}, {rho_point_2}};

                      Matrix cosSin = new Matrix(cossin);
                      Matrix rho_mat = new Matrix(rhos);

                      Matrix x = cosSin.solve(rho_mat);

                      intersetionPoints.add(new Point(x.get(0, 0), x.get(1, 0)));
                  }
               }
           }
        }

        /*sort intersectionPoints*/
        Collections.sort(intersetionPoints, new Comparator<Point>() {
            @Override
            public int compare(Point point, Point t1) {
                if(point.y<t1.y-100){
                    return -1;
                }else if(point.y-100>t1.y){
                    return 1;
                }else if(point.x<t1.x){
                    return -1;
                }else if(point.x>t1.x){
                    return 1;
                }else {
                    return 0;
                }
            }
        });

        /*remove duplicate points*/
        ArrayList<Point> intPoints = new ArrayList<Point>();
        Point pivotP = intersetionPoints.get(0);
        for(Point i:intersetionPoints){
            if(i==intersetionPoints.get(0)||Math.sqrt(Math.pow(pivotP.x-i.x,2)+Math.pow(pivotP.y-i.y,2))>50) {
                intPoints.add(i);
                pivotP = i;
            }
        }
        // TODO: 17. 11. 4  점들 x,y 기준으로 정렬 필요
        return intPoints;
    }
}
