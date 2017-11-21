package com.cssi.democloud.app3D

import java.nio.file.Paths

import breeze.linalg.min
import org.apache.ignite.igfs.IgfsPath


/**
  * StereoProduct Factory
  */
object StereoProduct{
  def fromString(path: String, jsonString: String): StereoProduct = {
    new StereoProduct(StereoConfiguration.fromString(jsonString),  path)
  }
}

/**
  * Class used to represents a stereo product, stores the configuration and the tiles
  */
class StereoProduct(val configuration: StereoConfiguration, val path: String) {
  var tiles : List[Tile] = List()
  var tw : Int = 0
  var th: Int = 0
  val workingDir :String = Paths.get(path).toFile.getName
  val roi: Coordinates = configuration.roi

  /**
    * Create working dir in Ignite file system
    */
  def createWorkingDir(): Unit ={
    val fs = IgniteUtils.getOrStartFS()
    val outDir = new IgfsPath(configuration.out_dir)
    fs.mkdirs(outDir)
  }


  /**
    * Computes tiles coordinates
    */
  private def computeTilesCoordinates(): Unit={
    var tiles: List[Tile] = List()
    val rx = roi.x
    val ry = roi.y
    val rw = roi.w
    val rh = roi.h

    //Compute tiles coordinates and neighbors tiles coordinates
    for (y <- Array.range(ry, ry + rh, th)){
      val h = min(th, ry + rh - y)
      for(x <- Array.range(rx, rx + rw, tw)){
        //Computes tile coordinates
        val w = min(tw, rx + rw - x)
        val coordinates = Coordinates(x,y,w,h)

        //Computes neighbors tiles coordinates
        var neighborhood :List[Coordinates] = List()
        for (y2 <- Array(y - th, y, y + th)){
          val h2 = min(th, ry + rh - y2)
          for(x2 <- Array(x - tw, x, x + tw)){
            val w2 = min(tw, rx + rw - x2)
            val sx = rx + rw
            if (sx > x2 && sx >= rx){
              val sy = ry + rh
              if (sy > y2 && sy >= ry){
                val neighbor = Coordinates(x2, y2, w2, h2)
                neighborhood = neighborhood:::List(neighbor)
              }
            }
          }
        }
        tiles = tiles:::List(Tile(this, coordinates, neighborhood))
      }
    }
    this.tiles = this.tiles:::tiles
  }

  /**
    * Adjusts a dimension to ROI
    * @param dim dimension to adjust (w or h)
    * @return adjusted dimension
    */
  private def adjust(dim:Int): Int ={
    val size = configuration.tileSize
    var tile_dim = min(dim, size)
    val ntx = math.round(dim.toFloat/tile_dim)
    tile_dim = math.ceil(dim.toFloat/ntx).toInt
    tile_dim
  }

  /**
    * Adjusts tile dimension to ROI
    */
  private def adjustTileSize(): Unit ={
    this.tw = adjust(roi.w)
    this.th = adjust(roi.h)
  }

  /**
    * Adjusts to size to ROI and computes Tile coordinates
    */
  def createROIAdjustedTileCoordinates() : Unit= {
    adjustTileSize()
    computeTilesCoordinates()
  }

  /**
    * Saves the stereo product once the processing is done
    * TODO
    */
  def save(): Unit ={
    // TODO
  }

}
