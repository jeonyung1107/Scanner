if(NOT TARGET ippicv)
  add_library(ippicv STATIC IMPORTED)
  set_target_properties(ippicv PROPERTIES
    IMPORTED_LINK_INTERFACE_LIBRARIES ""
    IMPORTED_LOCATION "${OpenCV_INSTALL_PATH}/share/OpenCV/3rdparty/lib/libippicv.a"
  )
endif()
