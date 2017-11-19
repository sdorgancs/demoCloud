package com.cssi.democloud.app3D

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.file.Paths
import javax.imageio.ImageIO

import breeze.linalg.DenseMatrix
import org.apache.ignite.igfs.IgfsPath
import net.liftweb.json._
import net.liftweb.json.Serialization._

import scala.reflect.io.File

/**
  *
  * Class used to represent a tile, stores configuration, coordinates and processing mask
  * @param product the parent stereo product
  * @param coordinates coordinates of the tile
  * @param neighborhood coordinates of the neighbors tiles
  */
case class Tile(product: StereoProduct,
                coordinates: Coordinates,
                neighborhood: List[Coordinates]){

  val configuration: StereoConfiguration = product.configuration.copy()
  var mask: DenseMatrix[Boolean] = _
  var tileDir: String = _
  var neighborhoodDirs: List[String] = _
  var tileJson: String = _
  var stereoTiles : Range = 1 to configuration.images.length

  /**
    * Compute processing mask: cloud + water + domain
    */
  def computeMask(): Unit = {
    val image = product.configuration.images(0)
    val rpc = image.rpc
    val roi_msk = image.roi
    val cld_msk = image.cld
    val wat_msk = image.wat
    val use_srtm_for_water = product.configuration.use_srtm_for_water
    this.mask = Masking.cloud_water_image_domain(coordinates, rpc, roi_msk, cld_msk, wat_msk, use_srtm_for_water)
  }

  /**
    * Writes the tile in the Ignite file system
    */
  def createTile(): Unit = {
    val x = coordinates.x
    val y = coordinates.y
    val w = coordinates.w
    val h = coordinates.h

    tileDir = getTileDir(x, y, w, h)
    neighborhood.foreach { coord =>
      neighborhoodDirs = neighborhoodDirs ::: List(getTileDir(coord.x, coord.y, coord.w, coord.h))
    }

    val fs = IgniteUtils.getOrStart()

    for (i <- 1 to product.configuration.images.length) {
      fs.mkdirs(new IgfsPath(new IgfsPath(tileDir), f"pair_$i"))
    }
    configuration.roi = coordinates.copy()
    configuration.full_img = false
    configuration.neighborhoodDirs = neighborhoodDirs
    configuration.out_dir = "../../.."

    //Save tile configuration in json file
    implicit val formats: DefaultFormats = DefaultFormats
    val jsonString = writePretty(configuration)
    fs.mkdirs(new IgfsPath(new IgfsPath(tileDir), "config.json"))
    val outJson = fs.create(new IgfsPath(tileJson), true)
    outJson.write(jsonString.getBytes)

    //Save cloud/water/domain mask in png
    val img = new BufferedImage(mask.cols, mask.rows, BufferedImage.TYPE_BYTE_GRAY);
    for(x <- 0 to mask.cols)
      for(y <- 0 to mask.cols)
        img.setRGB(x,y, if(mask(x, y)) 256 else 0)
    val outPng = fs.create(new IgfsPath(new IgfsPath(tileDir), "cloud_water_image_domain_mask.png"), true)
    //Write image content in a buffer
    val ba = new ByteArrayOutputStream()
    ImageIO.write(img, "PNG", ba)
    //Write buffer into IGFS file
    outPng.write(ba.toByteArray)
  }

  /**
    * Compute the name of the directory where the tile will be written
    * @param x x coordinates of the ROI
    * @param y y coordinates of the ROI
    * @param w width of the ROI
    * @param h height of the ROI
    * @return name of the directory
    */
  private def getTileDir(x: Int, y: Int, w: Int, h: Int): String = {
    val path = Paths.get(product.workingDir, "tiles", f"row_$y%07d_height_$h", f"col_$x%07d_width_$w")
    path.toFile.getPath
  }




}


