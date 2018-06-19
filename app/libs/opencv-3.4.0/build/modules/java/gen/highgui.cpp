
//
// This file is auto-generated, please don't edit!
//

#define LOG_TAG "org.opencv.highgui"

#include "common.h"

#include "opencv2/opencv_modules.hpp"
#ifdef HAVE_OPENCV_HIGHGUI

#include <string>

#include "opencv2/highgui.hpp"

#include "/home/jeon/다운로드/opencv-3.4.0/modules/highgui/include/opencv2/highgui/highgui_c.h"
#include "/home/jeon/다운로드/opencv-3.4.0/modules/highgui/include/opencv2/highgui.hpp"

using namespace cv;

/// throw java exception
static void throwJavaException(JNIEnv *env, const std::exception *e, const char *method) {
  std::string what = "unknown exception";
  jclass je = 0;

  if(e) {
    std::string exception_type = "std::exception";

    if(dynamic_cast<const cv::Exception*>(e)) {
      exception_type = "cv::Exception";
      je = env->FindClass("org/opencv/core/CvException");
    }

    what = exception_type + ": " + e->what();
  }

  if(!je) je = env->FindClass("java/lang/Exception");
  env->ThrowNew(je, what.c_str());

  LOGE("%s caught %s", method, what.c_str());
  (void)method;        // avoid "unused" warning
}


extern "C" {


//
//  Rect selectROI(Mat img, bool showCrosshair = true, bool fromCenter = false)
//



JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_10 (JNIEnv*, jclass, jlong, jboolean, jboolean);

JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_10
  (JNIEnv* env, jclass , jlong img_nativeObj, jboolean showCrosshair, jboolean fromCenter)
{
    static const char method_name[] = "highgui::selectROI_10()";
    try {
        LOGD("%s", method_name);
        Mat& img = *((Mat*)img_nativeObj);
        Rect _retval_ = cv::selectROI( img, (bool)showCrosshair, (bool)fromCenter );
        jdoubleArray _da_retval_ = env->NewDoubleArray(4);  jdouble _tmp_retval_[4] = {(jdouble)_retval_.x, (jdouble)_retval_.y, (jdouble)_retval_.width, (jdouble)_retval_.height}; env->SetDoubleArrayRegion(_da_retval_, 0, 4, _tmp_retval_);
        return _da_retval_;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return 0;
}





JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_11 (JNIEnv*, jclass, jlong);

JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_11
  (JNIEnv* env, jclass , jlong img_nativeObj)
{
    static const char method_name[] = "highgui::selectROI_11()";
    try {
        LOGD("%s", method_name);
        Mat& img = *((Mat*)img_nativeObj);
        Rect _retval_ = cv::selectROI( img );
        jdoubleArray _da_retval_ = env->NewDoubleArray(4);  jdouble _tmp_retval_[4] = {(jdouble)_retval_.x, (jdouble)_retval_.y, (jdouble)_retval_.width, (jdouble)_retval_.height}; env->SetDoubleArrayRegion(_da_retval_, 0, 4, _tmp_retval_);
        return _da_retval_;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return 0;
}



//
//  Rect selectROI(String windowName, Mat img, bool showCrosshair = true, bool fromCenter = false)
//



JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_12 (JNIEnv*, jclass, jstring, jlong, jboolean, jboolean);

JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_12
  (JNIEnv* env, jclass , jstring windowName, jlong img_nativeObj, jboolean showCrosshair, jboolean fromCenter)
{
    static const char method_name[] = "highgui::selectROI_12()";
    try {
        LOGD("%s", method_name);
        const char* utf_windowName = env->GetStringUTFChars(windowName, 0); String n_windowName( utf_windowName ? utf_windowName : "" ); env->ReleaseStringUTFChars(windowName, utf_windowName);
        Mat& img = *((Mat*)img_nativeObj);
        Rect _retval_ = cv::selectROI( n_windowName, img, (bool)showCrosshair, (bool)fromCenter );
        jdoubleArray _da_retval_ = env->NewDoubleArray(4);  jdouble _tmp_retval_[4] = {(jdouble)_retval_.x, (jdouble)_retval_.y, (jdouble)_retval_.width, (jdouble)_retval_.height}; env->SetDoubleArrayRegion(_da_retval_, 0, 4, _tmp_retval_);
        return _da_retval_;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return 0;
}





JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_13 (JNIEnv*, jclass, jstring, jlong);

JNIEXPORT jdoubleArray JNICALL Java_org_opencv_highgui_Highgui_selectROI_13
  (JNIEnv* env, jclass , jstring windowName, jlong img_nativeObj)
{
    static const char method_name[] = "highgui::selectROI_13()";
    try {
        LOGD("%s", method_name);
        const char* utf_windowName = env->GetStringUTFChars(windowName, 0); String n_windowName( utf_windowName ? utf_windowName : "" ); env->ReleaseStringUTFChars(windowName, utf_windowName);
        Mat& img = *((Mat*)img_nativeObj);
        Rect _retval_ = cv::selectROI( n_windowName, img );
        jdoubleArray _da_retval_ = env->NewDoubleArray(4);  jdouble _tmp_retval_[4] = {(jdouble)_retval_.x, (jdouble)_retval_.y, (jdouble)_retval_.width, (jdouble)_retval_.height}; env->SetDoubleArrayRegion(_da_retval_, 0, 4, _tmp_retval_);
        return _da_retval_;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return 0;
}



//
//  int waitKeyEx(int delay = 0)
//



JNIEXPORT jint JNICALL Java_org_opencv_highgui_Highgui_waitKeyEx_10 (JNIEnv*, jclass, jint);

JNIEXPORT jint JNICALL Java_org_opencv_highgui_Highgui_waitKeyEx_10
  (JNIEnv* env, jclass , jint delay)
{
    static const char method_name[] = "highgui::waitKeyEx_10()";
    try {
        LOGD("%s", method_name);
        
        int _retval_ = cv::waitKeyEx( (int)delay );
        return _retval_;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return 0;
}





JNIEXPORT jint JNICALL Java_org_opencv_highgui_Highgui_waitKeyEx_11 (JNIEnv*, jclass);

JNIEXPORT jint JNICALL Java_org_opencv_highgui_Highgui_waitKeyEx_11
  (JNIEnv* env, jclass )
{
    static const char method_name[] = "highgui::waitKeyEx_11()";
    try {
        LOGD("%s", method_name);
        
        int _retval_ = cv::waitKeyEx(  );
        return _retval_;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return 0;
}



//
//  void addText(Mat img, String text, Point org, String nameFont, int pointSize = -1, Scalar color = Scalar::all(0), int weight = QT_FONT_NORMAL, int style = QT_STYLE_NORMAL, int spacing = 0)
//



JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_addText_10 (JNIEnv*, jclass, jlong, jstring, jdouble, jdouble, jstring, jint, jdouble, jdouble, jdouble, jdouble, jint, jint, jint);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_addText_10
  (JNIEnv* env, jclass , jlong img_nativeObj, jstring text, jdouble org_x, jdouble org_y, jstring nameFont, jint pointSize, jdouble color_val0, jdouble color_val1, jdouble color_val2, jdouble color_val3, jint weight, jint style, jint spacing)
{
    static const char method_name[] = "highgui::addText_10()";
    try {
        LOGD("%s", method_name);
        Mat& img = *((Mat*)img_nativeObj);
        const char* utf_text = env->GetStringUTFChars(text, 0); String n_text( utf_text ? utf_text : "" ); env->ReleaseStringUTFChars(text, utf_text);
        Point org((int)org_x, (int)org_y);
        const char* utf_nameFont = env->GetStringUTFChars(nameFont, 0); String n_nameFont( utf_nameFont ? utf_nameFont : "" ); env->ReleaseStringUTFChars(nameFont, utf_nameFont);
        Scalar color(color_val0, color_val1, color_val2, color_val3);
        cv::addText( img, n_text, org, n_nameFont, (int)pointSize, color, (int)weight, (int)style, (int)spacing );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}





JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_addText_11 (JNIEnv*, jclass, jlong, jstring, jdouble, jdouble, jstring);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_addText_11
  (JNIEnv* env, jclass , jlong img_nativeObj, jstring text, jdouble org_x, jdouble org_y, jstring nameFont)
{
    static const char method_name[] = "highgui::addText_11()";
    try {
        LOGD("%s", method_name);
        Mat& img = *((Mat*)img_nativeObj);
        const char* utf_text = env->GetStringUTFChars(text, 0); String n_text( utf_text ? utf_text : "" ); env->ReleaseStringUTFChars(text, utf_text);
        Point org((int)org_x, (int)org_y);
        const char* utf_nameFont = env->GetStringUTFChars(nameFont, 0); String n_nameFont( utf_nameFont ? utf_nameFont : "" ); env->ReleaseStringUTFChars(nameFont, utf_nameFont);
        cv::addText( img, n_text, org, n_nameFont );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}



//
//  void displayOverlay(String winname, String text, int delayms = 0)
//



JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayOverlay_10 (JNIEnv*, jclass, jstring, jstring, jint);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayOverlay_10
  (JNIEnv* env, jclass , jstring winname, jstring text, jint delayms)
{
    static const char method_name[] = "highgui::displayOverlay_10()";
    try {
        LOGD("%s", method_name);
        const char* utf_winname = env->GetStringUTFChars(winname, 0); String n_winname( utf_winname ? utf_winname : "" ); env->ReleaseStringUTFChars(winname, utf_winname);
        const char* utf_text = env->GetStringUTFChars(text, 0); String n_text( utf_text ? utf_text : "" ); env->ReleaseStringUTFChars(text, utf_text);
        cv::displayOverlay( n_winname, n_text, (int)delayms );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}





JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayOverlay_11 (JNIEnv*, jclass, jstring, jstring);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayOverlay_11
  (JNIEnv* env, jclass , jstring winname, jstring text)
{
    static const char method_name[] = "highgui::displayOverlay_11()";
    try {
        LOGD("%s", method_name);
        const char* utf_winname = env->GetStringUTFChars(winname, 0); String n_winname( utf_winname ? utf_winname : "" ); env->ReleaseStringUTFChars(winname, utf_winname);
        const char* utf_text = env->GetStringUTFChars(text, 0); String n_text( utf_text ? utf_text : "" ); env->ReleaseStringUTFChars(text, utf_text);
        cv::displayOverlay( n_winname, n_text );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}



//
//  void displayStatusBar(String winname, String text, int delayms = 0)
//



JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayStatusBar_10 (JNIEnv*, jclass, jstring, jstring, jint);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayStatusBar_10
  (JNIEnv* env, jclass , jstring winname, jstring text, jint delayms)
{
    static const char method_name[] = "highgui::displayStatusBar_10()";
    try {
        LOGD("%s", method_name);
        const char* utf_winname = env->GetStringUTFChars(winname, 0); String n_winname( utf_winname ? utf_winname : "" ); env->ReleaseStringUTFChars(winname, utf_winname);
        const char* utf_text = env->GetStringUTFChars(text, 0); String n_text( utf_text ? utf_text : "" ); env->ReleaseStringUTFChars(text, utf_text);
        cv::displayStatusBar( n_winname, n_text, (int)delayms );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}





JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayStatusBar_11 (JNIEnv*, jclass, jstring, jstring);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_displayStatusBar_11
  (JNIEnv* env, jclass , jstring winname, jstring text)
{
    static const char method_name[] = "highgui::displayStatusBar_11()";
    try {
        LOGD("%s", method_name);
        const char* utf_winname = env->GetStringUTFChars(winname, 0); String n_winname( utf_winname ? utf_winname : "" ); env->ReleaseStringUTFChars(winname, utf_winname);
        const char* utf_text = env->GetStringUTFChars(text, 0); String n_text( utf_text ? utf_text : "" ); env->ReleaseStringUTFChars(text, utf_text);
        cv::displayStatusBar( n_winname, n_text );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}



//
//  void selectROIs(String windowName, Mat img, vector_Rect& boundingBoxes, bool showCrosshair = true, bool fromCenter = false)
//



JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_selectROIs_10 (JNIEnv*, jclass, jstring, jlong, jlong, jboolean, jboolean);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_selectROIs_10
  (JNIEnv* env, jclass , jstring windowName, jlong img_nativeObj, jlong boundingBoxes_mat_nativeObj, jboolean showCrosshair, jboolean fromCenter)
{
    static const char method_name[] = "highgui::selectROIs_10()";
    try {
        LOGD("%s", method_name);
        std::vector<Rect> boundingBoxes;
        Mat& boundingBoxes_mat = *((Mat*)boundingBoxes_mat_nativeObj);
        const char* utf_windowName = env->GetStringUTFChars(windowName, 0); String n_windowName( utf_windowName ? utf_windowName : "" ); env->ReleaseStringUTFChars(windowName, utf_windowName);
        Mat& img = *((Mat*)img_nativeObj);
        cv::selectROIs( n_windowName, img, boundingBoxes, (bool)showCrosshair, (bool)fromCenter );
        vector_Rect_to_Mat( boundingBoxes, boundingBoxes_mat );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}





JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_selectROIs_11 (JNIEnv*, jclass, jstring, jlong, jlong);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_selectROIs_11
  (JNIEnv* env, jclass , jstring windowName, jlong img_nativeObj, jlong boundingBoxes_mat_nativeObj)
{
    static const char method_name[] = "highgui::selectROIs_11()";
    try {
        LOGD("%s", method_name);
        std::vector<Rect> boundingBoxes;
        Mat& boundingBoxes_mat = *((Mat*)boundingBoxes_mat_nativeObj);
        const char* utf_windowName = env->GetStringUTFChars(windowName, 0); String n_windowName( utf_windowName ? utf_windowName : "" ); env->ReleaseStringUTFChars(windowName, utf_windowName);
        Mat& img = *((Mat*)img_nativeObj);
        cv::selectROIs( n_windowName, img, boundingBoxes );
        vector_Rect_to_Mat( boundingBoxes, boundingBoxes_mat );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}



//
//  void setTrackbarMax(String trackbarname, String winname, int maxval)
//



JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_setTrackbarMax_10 (JNIEnv*, jclass, jstring, jstring, jint);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_setTrackbarMax_10
  (JNIEnv* env, jclass , jstring trackbarname, jstring winname, jint maxval)
{
    static const char method_name[] = "highgui::setTrackbarMax_10()";
    try {
        LOGD("%s", method_name);
        const char* utf_trackbarname = env->GetStringUTFChars(trackbarname, 0); String n_trackbarname( utf_trackbarname ? utf_trackbarname : "" ); env->ReleaseStringUTFChars(trackbarname, utf_trackbarname);
        const char* utf_winname = env->GetStringUTFChars(winname, 0); String n_winname( utf_winname ? utf_winname : "" ); env->ReleaseStringUTFChars(winname, utf_winname);
        cv::setTrackbarMax( n_trackbarname, n_winname, (int)maxval );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}



//
//  void setTrackbarMin(String trackbarname, String winname, int minval)
//



JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_setTrackbarMin_10 (JNIEnv*, jclass, jstring, jstring, jint);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_setTrackbarMin_10
  (JNIEnv* env, jclass , jstring trackbarname, jstring winname, jint minval)
{
    static const char method_name[] = "highgui::setTrackbarMin_10()";
    try {
        LOGD("%s", method_name);
        const char* utf_trackbarname = env->GetStringUTFChars(trackbarname, 0); String n_trackbarname( utf_trackbarname ? utf_trackbarname : "" ); env->ReleaseStringUTFChars(trackbarname, utf_trackbarname);
        const char* utf_winname = env->GetStringUTFChars(winname, 0); String n_winname( utf_winname ? utf_winname : "" ); env->ReleaseStringUTFChars(winname, utf_winname);
        cv::setTrackbarMin( n_trackbarname, n_winname, (int)minval );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}



//
//  void setWindowTitle(String winname, String title)
//



JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_setWindowTitle_10 (JNIEnv*, jclass, jstring, jstring);

JNIEXPORT void JNICALL Java_org_opencv_highgui_Highgui_setWindowTitle_10
  (JNIEnv* env, jclass , jstring winname, jstring title)
{
    static const char method_name[] = "highgui::setWindowTitle_10()";
    try {
        LOGD("%s", method_name);
        const char* utf_winname = env->GetStringUTFChars(winname, 0); String n_winname( utf_winname ? utf_winname : "" ); env->ReleaseStringUTFChars(winname, utf_winname);
        const char* utf_title = env->GetStringUTFChars(title, 0); String n_title( utf_title ? utf_title : "" ); env->ReleaseStringUTFChars(title, utf_title);
        cv::setWindowTitle( n_winname, n_title );
        return;
    } catch(const std::exception &e) {
        throwJavaException(env, &e, method_name);
    } catch (...) {
        throwJavaException(env, 0, method_name);
    }
    return;
}




} // extern "C"

#endif // HAVE_OPENCV_HIGHGUI
