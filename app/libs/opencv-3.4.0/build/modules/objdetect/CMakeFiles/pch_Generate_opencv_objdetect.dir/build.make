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

# Utility rule file for pch_Generate_opencv_objdetect.

# Include the progress variables for this target.
include modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/progress.make

modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect: modules/objdetect/precomp.hpp.gch/opencv_objdetect_Release.gch


modules/objdetect/precomp.hpp.gch/opencv_objdetect_Release.gch: ../modules/objdetect/src/precomp.hpp
modules/objdetect/precomp.hpp.gch/opencv_objdetect_Release.gch: modules/objdetect/precomp.hpp
modules/objdetect/precomp.hpp.gch/opencv_objdetect_Release.gch: lib/libopencv_objdetect_pch_dephelp.a
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold --progress-dir=/home/jeon/다운로드/opencv-3.4.0/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Generating precomp.hpp.gch/opencv_objdetect_Release.gch"
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect && /usr/bin/cmake -E make_directory /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect/precomp.hpp.gch
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect && /usr/bin/c++ -O3 -DNDEBUG -DNDEBUG "-D__OPENCV_BUILD=1" "-D_USE_MATH_DEFINES" "-D__STDC_CONSTANT_MACROS" "-D__STDC_LIMIT_MACROS" "-D__STDC_FORMAT_MACROS" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippicv_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippiw_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippicv_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build/3rdparty/ippicv/ippiw_lnx/include" -I"/home/jeon/다운로드/opencv-3.4.0/build" -I"/home/jeon/다운로드/opencv-3.4.0/modules/objdetect/include" -I"/home/jeon/다운로드/opencv-3.4.0/modules/objdetect/src" -I"/home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect" -I"/home/jeon/다운로드/opencv-3.4.0/modules/core/include" -I"/home/jeon/다운로드/opencv-3.4.0/modules/imgproc/include" -fsigned-char -W -Wall -Werror=return-type -Werror=non-virtual-dtor -Werror=address -Werror=sequence-point -Wformat -Werror=format-security -Wmissing-declarations -Wundef -Winit-self -Wpointer-arith -Wshadow -Wsign-promo -Wuninitialized -Winit-self -Wno-narrowing -Wno-delete-non-virtual-dtor -Wno-comment -fdiagnostics-show-option -Wno-long-long -pthread -fomit-frame-pointer -ffunction-sections -fdata-sections -msse -msse2 -msse3 -fvisibility=hidden -fvisibility-inlines-hidden -fPIC -x c++-header -o /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect/precomp.hpp.gch/opencv_objdetect_Release.gch /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect/precomp.hpp

modules/objdetect/precomp.hpp: ../modules/objdetect/src/precomp.hpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --blue --bold --progress-dir=/home/jeon/다운로드/opencv-3.4.0/build/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Generating precomp.hpp"
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect && /usr/bin/cmake -E copy_if_different /home/jeon/다운로드/opencv-3.4.0/modules/objdetect/src/precomp.hpp /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect/precomp.hpp

pch_Generate_opencv_objdetect: modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect
pch_Generate_opencv_objdetect: modules/objdetect/precomp.hpp.gch/opencv_objdetect_Release.gch
pch_Generate_opencv_objdetect: modules/objdetect/precomp.hpp
pch_Generate_opencv_objdetect: modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/build.make

.PHONY : pch_Generate_opencv_objdetect

# Rule to build all files generated by this target.
modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/build: pch_Generate_opencv_objdetect

.PHONY : modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/build

modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/clean:
	cd /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect && $(CMAKE_COMMAND) -P CMakeFiles/pch_Generate_opencv_objdetect.dir/cmake_clean.cmake
.PHONY : modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/clean

modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/depend:
	cd /home/jeon/다운로드/opencv-3.4.0/build && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /home/jeon/다운로드/opencv-3.4.0 /home/jeon/다운로드/opencv-3.4.0/modules/objdetect /home/jeon/다운로드/opencv-3.4.0/build /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect /home/jeon/다운로드/opencv-3.4.0/build/modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : modules/objdetect/CMakeFiles/pch_Generate_opencv_objdetect.dir/depend

