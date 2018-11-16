cmake_minimum_required(VERSION 3.4.1)


add_library( # Specifies the name of the library.
             ehuman

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/CPP/ehuman.cpp
             #src/main/CPP/mobilenet.cpp
             )

FIND_PACKAGE( OpenMP REQUIRED)
if(OPENMP_FOUND)
message("OPENMP FOUND")
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${OpenMP_EXE_LINKER_FLAGS}")
endif()


include_directories(src/main/CPP/include)
link_directories(src/main/CPP/lib/armeabi-v7a)




target_link_libraries(ehuman
                        ${CMAKE_SOURCE_DIR}/src/main/CPP/lib/armeabi-v7a/libncnn.a log
                        #${CMAKE_SOURCE_DIR}/src/main/CPP/lib/armeabi-v7a/libopencv_core.a
                        #${CMAKE_SOURCE_DIR}/src/main/CPP/lib/armeabi-v7a/libopencv_highgui.a
                        #${CMAKE_SOURCE_DIR}/src/main/CPP/lib/armeabi-v7a/libopencv_imgcodecs.a
                        #${CMAKE_SOURCE_DIR}/src/main/CPP/lib/armeabi-v7a/libopencv_imgproc.a
                        )


