package com.cssi.democloud.app3D

import breeze.linalg._
import com.cssi.democloud.app3D.Algorithms._
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

object App3D {
  var sc: SparkContext = _
  var cfg: StereoConfiguration = _

  /**
    * Read configuration file productPath
    * Filters blinded tiles
    * Creates tiles working directories, these are collocated with tiles in ignite cache
    *
    * @param productPath path of the product configuration
    * @param createMask tells if the mask has to be created or not
    * @return an Ignite RDD containing the tiles to compute
    */
  def initialize(productPath: String, createMask: Boolean): RDD[(String, Tile)] = {

    //Initialize spark context
    val conf = new SparkConf().setAppName("app3D").setMaster("local")
    sc = SparkContext.getOrCreate(conf)
    val jsonString = Source.fromFile(productPath).mkString

    //Load configurations file
    val product = StereoProduct.fromString(productPath, jsonString)
    cfg = product.configuration

    //Save tiles into Ignite Cache
    val igniteRdd = IgniteUtils.getContext(sc).fromCache[String, Tile]("tiles")
    igniteRdd.savePairs(sc.parallelize(product.tiles).map(t => (t.tileDir, t)))

    //Create mask if needed for each tile
    igniteRdd.foreach(e => if (createMask) e._2.computeMask())

    //filter blind tiles
    val tileRdd = igniteRdd.filter(e => any(e._2.mask))

    //Create tiles, working dirs are collocated with tiles in ignite cache
    tileRdd.foreach(e => e._2.createTile())
    tileRdd
  }


  def main(rdd: RDD[(String, Tile)], steps: List[String]): Unit = {
    val tiles = rdd.map(e => e._2).collect()
    if (steps contains "local-pointing") {
      //Computes pointing corrections locally to each stereo tile
      rdd.foreach(e => computeStereoTiles(pointingCorrection, e._2))
    }
    if (steps contains "global-pointing") {
      //Computes global pointing correction
      globalPointingCorrection(tiles)
    }
    if (steps contains "rectification") {
      //Computes rectification of each stereo tile
      rdd.foreach(e => computeStereoTiles(rectificationPair, e._2))
    }
    if (steps contains "matching") {
      //Computes matching of each stereo tile
      rdd.foreach(e => computeStereoTiles(stereoMatching, e._2))
    }

    if (cfg.images.length > 2 && cfg.triangulation_mode == "pairwise") {
      if (steps contains "disparity-to-height") {
        //Computes a height map from the disparity map for each stereo tile
        rdd.foreach(e => computeStereoTiles(disparityToHeight, e._2))
        //Computes mean height of each tile
        rdd.foreach(e => meanHeights(e._2))
      }
      if (steps contains "global-mean-heights") {
        //Computes global mean height
        globalMeanHeights(tiles)
      }
      if (steps contains "heights-to-ply") {
        //Generate a ply cloud for each tile
        rdd.foreach(e => heightToPly(e._2))
      }
    }
    else{
      if (steps contains "triangulation"){
        if (cfg.triangulation_mode == "geometric"){
          //Computes a point cloud from the disparity maps of N-pairs of image tiles.
          rdd.foreach(e => multiDisparitiesToPly(e._2))
        }
        else if(cfg.triangulation_mode == "geometric"){
          //Computes a point cloud from the disparity map of a pair of image tiles.
          rdd.foreach(e => disparityToPly(e._2))
        }
        else{
          throw new IllegalArgumentException("possible values for 'triangulation_mode' : 'pairwise' or 'geometric'")
        }
      }
    }
    if (steps.contains("local-dsm-rasterization")){
      //computing DSM by tile...
      rdd.foreach(e => plysToDsm(e._2))
    }
    if (steps.contains("global-dsm-rasterization")){
      //computing global DSM..
      globalDsm(tiles)
    }
    if (steps.contains("lidar-preprocessor")){
      //computing global DSM..
      lidarPreprocessor(tiles)
    }
  }
}

/**
  * Spark application running S2P pipelines
  */
object Main {
  /**
    * Args 0 is the path of the configuration to process
    */
  def main(args: Array[String]) {
    //Pipeline steps
    val steps = List(
      "initialisation",
      "local-pointing",
      "rectification",
      "matching",
      "triangulation",
      "local-dsm-rasterization"
    )

    //Initialize the tiles, create the RDDs and the working directories in IGFS
    val rdd = App3D.initialize(args(0), steps.contains("initialisation"))

    //Run the Pipeline in spark context
    App3D.main(rdd, steps)

  }
}
