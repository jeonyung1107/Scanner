
//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.highgui;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.utils.Converters;

// C++: class Highgui
//javadoc: Highgui

public class Highgui {

    public static final int
            CV_FONT_LIGHT = 25,
            CV_FONT_NORMAL = 50,
            CV_FONT_DEMIBOLD = 63,
            CV_FONT_BOLD = 75,
            CV_FONT_BLACK = 87,
            CV_STYLE_NORMAL = 0,
            CV_STYLE_ITALIC = 1,
            CV_STYLE_OBLIQUE = 2,
            QT_FONT_LIGHT = 25,
            QT_FONT_NORMAL = 50,
            QT_FONT_DEMIBOLD = 63,
            QT_FONT_BOLD = 75,
            QT_FONT_BLACK = 87,
            QT_STYLE_NORMAL = 0,
            QT_STYLE_ITALIC = 1,
            QT_STYLE_OBLIQUE = 2,
            QT_PUSH_BUTTON = 0,
            QT_CHECKBOX = 1,
            QT_RADIOBOX = 2,
            QT_NEW_BUTTONBAR = 1024;


    //
    // C++:  Rect selectROI(Mat img, bool showCrosshair = true, bool fromCenter = false)
    //

    //javadoc: selectROI(img, showCrosshair, fromCenter)
    public static Rect selectROI(Mat img, boolean showCrosshair, boolean fromCenter)
    {
        
        Rect retVal = new Rect(selectROI_0(img.nativeObj, showCrosshair, fromCenter));
        
        return retVal;
    }

    //javadoc: selectROI(img)
    public static Rect selectROI(Mat img)
    {
        
        Rect retVal = new Rect(selectROI_1(img.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Rect selectROI(String windowName, Mat img, bool showCrosshair = true, bool fromCenter = false)
    //

    //javadoc: selectROI(windowName, img, showCrosshair, fromCenter)
    public static Rect selectROI(String windowName, Mat img, boolean showCrosshair, boolean fromCenter)
    {
        
        Rect retVal = new Rect(selectROI_2(windowName, img.nativeObj, showCrosshair, fromCenter));
        
        return retVal;
    }

    //javadoc: selectROI(windowName, img)
    public static Rect selectROI(String windowName, Mat img)
    {
        
        Rect retVal = new Rect(selectROI_3(windowName, img.nativeObj));
        
        return retVal;
    }


    //
    // C++:  int waitKeyEx(int delay = 0)
    //

    //javadoc: waitKeyEx(delay)
    public static int waitKeyEx(int delay)
    {
        
        int retVal = waitKeyEx_0(delay);
        
        return retVal;
    }

    //javadoc: waitKeyEx()
    public static int waitKeyEx()
    {
        
        int retVal = waitKeyEx_1();
        
        return retVal;
    }


    //
    // C++:  void addText(Mat img, String text, Point org, String nameFont, int pointSize = -1, Scalar color = Scalar::all(0), int weight = QT_FONT_NORMAL, int style = QT_STYLE_NORMAL, int spacing = 0)
    //

    //javadoc: addText(img, text, org, nameFont, pointSize, color, weight, style, spacing)
    public static void addText(Mat img, String text, Point org, String nameFont, int pointSize, Scalar color, int weight, int style, int spacing)
    {
        
        addText_0(img.nativeObj, text, org.x, org.y, nameFont, pointSize, color.val[0], color.val[1], color.val[2], color.val[3], weight, style, spacing);
        
        return;
    }

    //javadoc: addText(img, text, org, nameFont)
    public static void addText(Mat img, String text, Point org, String nameFont)
    {
        
        addText_1(img.nativeObj, text, org.x, org.y, nameFont);
        
        return;
    }


    //
    // C++:  void displayOverlay(String winname, String text, int delayms = 0)
    //

    //javadoc: displayOverlay(winname, text, delayms)
    public static void displayOverlay(String winname, String text, int delayms)
    {
        
        displayOverlay_0(winname, text, delayms);
        
        return;
    }

    //javadoc: displayOverlay(winname, text)
    public static void displayOverlay(String winname, String text)
    {
        
        displayOverlay_1(winname, text);
        
        return;
    }


    //
    // C++:  void displayStatusBar(String winname, String text, int delayms = 0)
    //

    //javadoc: displayStatusBar(winname, text, delayms)
    public static void displayStatusBar(String winname, String text, int delayms)
    {
        
        displayStatusBar_0(winname, text, delayms);
        
        return;
    }

    //javadoc: displayStatusBar(winname, text)
    public static void displayStatusBar(String winname, String text)
    {
        
        displayStatusBar_1(winname, text);
        
        return;
    }


    //
    // C++:  void selectROIs(String windowName, Mat img, vector_Rect& boundingBoxes, bool showCrosshair = true, bool fromCenter = false)
    //

    //javadoc: selectROIs(windowName, img, boundingBoxes, showCrosshair, fromCenter)
    public static void selectROIs(String windowName, Mat img, MatOfRect boundingBoxes, boolean showCrosshair, boolean fromCenter)
    {
        Mat boundingBoxes_mat = boundingBoxes;
        selectROIs_0(windowName, img.nativeObj, boundingBoxes_mat.nativeObj, showCrosshair, fromCenter);
        
        return;
    }

    //javadoc: selectROIs(windowName, img, boundingBoxes)
    public static void selectROIs(String windowName, Mat img, MatOfRect boundingBoxes)
    {
        Mat boundingBoxes_mat = boundingBoxes;
        selectROIs_1(windowName, img.nativeObj, boundingBoxes_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void setTrackbarMax(String trackbarname, String winname, int maxval)
    //

    //javadoc: setTrackbarMax(trackbarname, winname, maxval)
    public static void setTrackbarMax(String trackbarname, String winname, int maxval)
    {
        
        setTrackbarMax_0(trackbarname, winname, maxval);
        
        return;
    }


    //
    // C++:  void setTrackbarMin(String trackbarname, String winname, int minval)
    //

    //javadoc: setTrackbarMin(trackbarname, winname, minval)
    public static void setTrackbarMin(String trackbarname, String winname, int minval)
    {
        
        setTrackbarMin_0(trackbarname, winname, minval);
        
        return;
    }


    //
    // C++:  void setWindowTitle(String winname, String title)
    //

    //javadoc: setWindowTitle(winname, title)
    public static void setWindowTitle(String winname, String title)
    {
        
        setWindowTitle_0(winname, title);
        
        return;
    }




    // C++:  Rect selectROI(Mat img, bool showCrosshair = true, bool fromCenter = false)
    private static native double[] selectROI_0(long img_nativeObj, boolean showCrosshair, boolean fromCenter);
    private static native double[] selectROI_1(long img_nativeObj);

    // C++:  Rect selectROI(String windowName, Mat img, bool showCrosshair = true, bool fromCenter = false)
    private static native double[] selectROI_2(String windowName, long img_nativeObj, boolean showCrosshair, boolean fromCenter);
    private static native double[] selectROI_3(String windowName, long img_nativeObj);

    // C++:  int waitKeyEx(int delay = 0)
    private static native int waitKeyEx_0(int delay);
    private static native int waitKeyEx_1();

    // C++:  void addText(Mat img, String text, Point org, String nameFont, int pointSize = -1, Scalar color = Scalar::all(0), int weight = QT_FONT_NORMAL, int style = QT_STYLE_NORMAL, int spacing = 0)
    private static native void addText_0(long img_nativeObj, String text, double org_x, double org_y, String nameFont, int pointSize, double color_val0, double color_val1, double color_val2, double color_val3, int weight, int style, int spacing);
    private static native void addText_1(long img_nativeObj, String text, double org_x, double org_y, String nameFont);

    // C++:  void displayOverlay(String winname, String text, int delayms = 0)
    private static native void displayOverlay_0(String winname, String text, int delayms);
    private static native void displayOverlay_1(String winname, String text);

    // C++:  void displayStatusBar(String winname, String text, int delayms = 0)
    private static native void displayStatusBar_0(String winname, String text, int delayms);
    private static native void displayStatusBar_1(String winname, String text);

    // C++:  void selectROIs(String windowName, Mat img, vector_Rect& boundingBoxes, bool showCrosshair = true, bool fromCenter = false)
    private static native void selectROIs_0(String windowName, long img_nativeObj, long boundingBoxes_mat_nativeObj, boolean showCrosshair, boolean fromCenter);
    private static native void selectROIs_1(String windowName, long img_nativeObj, long boundingBoxes_mat_nativeObj);

    // C++:  void setTrackbarMax(String trackbarname, String winname, int maxval)
    private static native void setTrackbarMax_0(String trackbarname, String winname, int maxval);

    // C++:  void setTrackbarMin(String trackbarname, String winname, int minval)
    private static native void setTrackbarMin_0(String trackbarname, String winname, int minval);

    // C++:  void setWindowTitle(String winname, String title)
    private static native void setWindowTitle_0(String winname, String title);

}
