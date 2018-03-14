package com.coffdope.jeon.scanner.imgProcess;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Point;
import org.opencv.core.Scalar;


import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
// TODO: 18. 1. 8 need refactoring with Camera2 
/**
 * Created by jeon on 17. 8. 7.
 */
/*영역 인식과 관련된 기능을 담당하는 클래스*/
public class RectangleDetector {
    private static RectangleDetector instance = new RectangleDetector();

    private final static String TAG = "RectangleDetector";

    /*constructors*/
    private RectangleDetector() {
    }

    public static RectangleDetector getInstance(){ return instance; }

    /*영역 인식 메서드,
    * byte배열로 주어진 이미지에서 윤곽선을 찾아 반환한다.
    * 이미지는 연산 속도를 위해 축소되어 처리된다.
    * */
    public static ArrayList<MatOfPoint> detectPage(byte[] bytes,Size size){

        Mat input_image,output_image,inter_image;
        ArrayList<MatOfPoint> result_cnt = new ArrayList<MatOfPoint>();
        ArrayList<MatOfPoint> cnt = new ArrayList<MatOfPoint>();
        float ratio;

        ratio = (float)size.height/300;

        input_image = new Mat((int)size.height,(int)size.width,CvType.CV_8UC1);
        inter_image = new Mat((int)(size.height/ratio),(int)(size.width/ratio),CvType.CV_8UC1);
        output_image = new Mat(inter_image.rows(),inter_image.cols(),CvType.CV_8UC1);
        input_image.put(0,0,bytes);

        Imgproc.resize(input_image,inter_image,new Size(inter_image.width(),inter_image.height()));

        /*이미지 전처리*/
        Imgproc.GaussianBlur(inter_image,inter_image,new Size(5,5),8,8);
        Imgproc.Canny(inter_image,output_image,75,200,3,false);

        /*contour*/
        Imgproc.findContours(output_image,cnt,new Mat(),0,2,new Point(0,0));
        Collections.sort( cnt, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint matOfPoint, MatOfPoint t1) {
                double a = Imgproc.contourArea(matOfPoint);
                double b = Imgproc.contourArea(t1);
                if(a> b) return -1;
                else if (a<b) return 1;
                else return 0;
            }
        });

        /*top 5 저장*/
        if(cnt.size()>5){
            int cnt_size = cnt.size();
            for(int i = cnt_size; i>5; i--){
                cnt.remove(i-5);
            }
        }

        /*가장 큰 영역부터 4개의 꼭지점을 가지는 contour찾는다.*/
        double arclength;
        MatOfPoint2f mat2 = new MatOfPoint2f(); //contour 결과물 저장하는 motofpoint2f
        MatOfPoint2f approx = new MatOfPoint2f();

        /*cnt에 저장되어있는 contour들 조건 탐색*/
        for(MatOfPoint c :cnt){
            mat2.fromArray(c.toArray()); //matofpoint2f 형태로 변환
            arclength = Imgproc.arcLength(mat2,true);
            Imgproc.approxPolyDP(mat2,approx,0.1*arclength,true); //단순화
            if(approx.toArray().length==4&&Imgproc.contourArea(c)>1000){
                result_cnt.add(new MatOfPoint(approx.toArray())); //선택된 contour만 추가한다.
                Log.i(TAG,"success!!");
                break;
            }

        }

        /*결과물 반환*/
        if(!result_cnt.isEmpty()) {
            Core.multiply(result_cnt.get(0), new Scalar(ratio, ratio), result_cnt.get(0)); //원래 크기로 복구
        }

        return result_cnt; //contour 반환
    }

}
