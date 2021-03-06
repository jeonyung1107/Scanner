# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.5

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /home/jeon/다운로드/opencv-3.4.0

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /home/jeon/다운로드/opencv-3.4.0/build

# Utility rule file for pch_Generate_opencv_photo.

# Include the progress variables for this target.
include modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/progress.make

modules/photo/CMakeFiles/pch_Generate_opencv_photo: modules/photo/precomp.hpp.gch/opencv_photo_Release.gch


modules/photo/precomp.hpp.gch/opencv_photo_Release.gch: ../modules/photo/src/precomp.hpp
modules/photo/precomp.hpp.gch/opencv_photo_Release.gch: modules/photo/precomp.hpp
modules/photo/precomp.hpp.gch/opencv_photo_Release.gch: lib/libopencv_photo_pch_dephelp.a
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold --progress-dir=/home/jeon/다운로드/opencv-3.4.0/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Generating precomp.hpp.gch/opencv_photo_Release.gch"
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/photo && /usr/bin/cmake -E make_directory /home/jeon/다운로드/opencv-3.4.0/build/modules/photo/precomp.hpp.gch
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/photo && /usr/bin/c++ -O3 -DNDEBUG -DNDEBUG "-D__OPENCV_BUILD=1" "-D_USE_MATH_DEFINES" "-D__STDC_CONSTANT_MACROS" "-D__STDC_LIMIT_MACROS" "-D__STDC_FORMAT_MACROS" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippicv_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippiw_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippicv_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippiw_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build" -I"/home/jeon/다운로드/opencv-3.4.0/modules/photo/include" -I"/home/jeon/다운로드/opencv-3.4.0/modules/photo/src" -I"/home/jeon/다운로드/opencv-3.4.0/build/modules/photo" -I"/home/jeon/다운로드/opencv-3.4.0/modules/core/include" -I"/home/jeon/다운로드/opencv-3.4.0/modules/imgproc/include" -fsigned-char -W -Wall -Werror=return-type -Werror=non-virtual-dtor -Werror=address -Werror=sequence-point -Wformat -Werror=format-security -Wmissing-declarations -Wundef -Winit-self -Wpointer-arith -Wshadow -Wsign-promo -Wuninitialized -Winit-self -Wno-narrowing -Wno-delete-non-virtual-dtor -Wno-comment -fdiagnostics-show-option -Wno-long-long -pthread -fomit-frame-pointer -ffunction-sections -fdata-sections -msse -msse2 -msse3 -fvisibility=hidden -fvisibility-inlines-hidden -fPIC -x c++-header -o /home/jeon/다운로드/opencv-3.4.0/build/modules/photo/precomp.hpp.gch/opencv_photo_Release.gch /home/jeon/다운로드/opencv-3.4.0/build/modules/photo/precomp.hpp

modules/photo/precomp.hpp: ../modules/photo/src/precomp.hpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold --progress-dir=/home/jeon/다운로드/opencv-3.4.0/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Generating precomp.hpp"
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/photo && /usr/bin/cmake -E copy_if_different /home/jeon/다운로드/opencv-3.4.0/modules/photo/src/precomp.hpp /home/jeon/다운로드/opencv-3.4.0/build/modules/photo/precomp.hpp

pch_Generate_opencv_photo: modules/photo/CMakeFiles/pch_Generate_opencv_photo
pch_Generate_opencv_photo: modules/photo/precomp.hpp.gch/opencv_photo_Release.gch
pch_Generate_opencv_photo: modules/photo/precomp.hpp
pch_Generate_opencv_photo: modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/build.make

.PHONY : pch_Generate_opencv_photo

# Rule to build all files generated by this target.
modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/build: pch_Generate_opencv_photo

.PHONY : modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/build

modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/clean:
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/photo && $(CMAKE_COMMAND) -P CMakeFiles/pch_Generate_opencv_photo.dir/cmake_clean.cmake
.PHONY : modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/clean

modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/depend:
	cd /home/jeon/다운로드/opencv-3.4.0/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/jeon/다운로드/opencv-3.4.0 /home/jeon/다운로드/opencv-3.4.0/modules/photo /home/jeon/다운로드/opencv-3.4.0/build /home/jeon/다운로드/opencv-3.4.0/build/modules/photo /home/jeon/다운로드/opencv-3.4.0/build/modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : modules/photo/CMakeFiles/pch_Generate_opencv_photo.dir/depend

