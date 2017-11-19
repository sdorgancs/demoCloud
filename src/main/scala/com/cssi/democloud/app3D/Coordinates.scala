package com.cssi.democloud.app3D

/**
  * Coordinates of a tile or a ROI
  * @param x top left pixel x coordinates
  * @param y top left pixel y coordinates
  * @param w number of pixels width
  * @param h number of pixels height
  */
case class Coordinates(val x: Int, val y:Int, val w:Int, val h:Int) {

}
