# Install script for directory: /home/jeon/다운로드/opencv-3.4.0/include

# Set the install prefix
if(NOT DEFINED CMAKE_INSTALL_PREFIX)
  set(CMAKE_INSTALL_PREFIX "/usr/local")
endif()
string(REGEX REPLACE "/$" "" CMAKE_INSTALL_PREFIX "${CMAKE_INSTALL_PREFIX}")

# Set the install configuration name.
if(NOT DEFINED CMAKE_INSTALL_CONFIG_NAME)
  if(BUILD_TYPE)
    string(REGEX REPLACE "^[^A-Za-z0-9_]+" ""
           CMAKE_INSTALL_CONFIG_NAME "${BUILD_TYPE}")
  else()
    set(CMAKE_INSTALL_CONFIG_NAME "Release")
  endif()
  message(STATUS "Install configuration: \"${CMAKE_INSTALL_CONFIG_NAME}\"")
endif()

# Set the component getting installed.
if(NOT CMAKE_INSTALL_COMPONENT)
  if(COMPONENT)
    message(STATUS "Install component: \"${COMPONENT}\"")
    set(CMAKE_INSTALL_COMPONENT "${COMPONENT}")
  else()
    set(CMAKE_INSTALL_COMPONENT)
  endif()
endif()

# Install shared libraries without execute permission?
if(NOT DEFINED CMAKE_INSTALL_SO_NO_EXE)
  set(CMAKE_INSTALL_SO_NO_EXE "1")
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "dev")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/opencv" TYPE FILE FILES
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cxcore.hpp"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cxcore.h"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cv.hpp"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/ml.h"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cxmisc.h"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cvaux.hpp"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cvwimage.h"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/highgui.h"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cvaux.h"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cxeigen.hpp"
    "/home/jeon/다운로드/opencv-3.4.0/include/opencv/cv.h"
    )
endif()

if(NOT CMAKE_INSTALL_COMPONENT OR "${CMAKE_INSTALL_COMPONENT}" STREQUAL "dev")
  file(INSTALL DESTINATION "${CMAKE_INSTALL_PREFIX}/include/opencv2" TYPE FILE FILES "/home/jeon/다운로드/opencv-3.4.0/include/opencv2/opencv.hpp")
endif()

