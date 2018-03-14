package com.coffdope.jeon.scanner.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.util.ArrayList;

/**
 * Created by jeon on 18. 3. 14.
 */

public class CommonImgTool {
    /*
        * crop image with rect given by pts
        * */
    public static ArrayList<Mat> cropImage(Mat image, ArrayList<Point> pts){
        ArrayList<Mat> result = new ArrayList<Mat>();
        for(int i=0; i<pts.size(); ++i){
            for(int j=0; j<pts.size();++j){
                if(pts.get(j).y-pts.get(i).y>100&&pts.get(j).x-pts.get(i).x>100){
                    Point start = pts.get(i);
                    Point end = pts.get(j);

                    double x1,y1,x2,y2;

                    if(start.x<0){
                        x1=0;
                    }else{
                        x1=start.x;
                    }
                    if(start.y<0){
                        y1=0;
                    }else{
                        y1=start.y;
                    }
                    if(end.x>image.cols()){
                        x2=image.cols();
                    }else{
                        x2=end.x;
                    }
                    if(end.y>image.rows()){
                        y2=image.rows();
                    }else{
                        y2=end.y;
                    }
                    Point pt1 = new Point(x1, y1);
                    Point pt2 = new Point(x2, y2);

                    Rect roi = new Rect(pt1,pt2);
                    Mat croped = new Mat(image,roi);
                    result.add(croped);

                    break;
                }
            }
        }
        return result;
    }

    public static Mat rotate(Mat src){
        Mat roteted = new Mat(src.cols(), src.rows(), src.type());
        Core.rotate(src, roteted, Core.ROTATE_90_CLOCKWISE);
        return roteted;
    }

    /*
        * mat data to size matching bitmap
        * */
    public static Bitmap matToBitmap(Mat src){
        Bitmap result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src,result);
        return result;
    }

    public static Bitmap bitMapTransform(Bitmap original, MatOfPoint contour){
        Bitmap bmp = original;
        Mat forTransform = new Mat(bmp.getHeight(),bmp.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(bmp,forTransform);
        Mat transformed = PerspectiveTransformer.four_point_transform(contour,forTransform);

        return matToBitmap(transformed);
    }
}
