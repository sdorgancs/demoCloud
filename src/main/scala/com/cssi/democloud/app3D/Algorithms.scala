package com.cssi.democloud.app3D

/**
  * S2P Algorithms
  */
object Algorithms {

  /**
    * Apply algorithm to each stereo tile related to tile
    * @param algorithm algorithm to run
    * @param tile tile to process
    */
  def computeStereoTiles(algorithm:(Tile, Int) => Unit, tile: Tile): Unit ={
      tile.stereoTiles.foreach(i => algorithm(tile, i))
  }
  /**
    * Computes the translation that corrects the pointing error on a pair of tiles.
    * @param tile tile to process
    * @param i number of the paired tile
    */
  def pointingCorrection(tile: Tile, i:Int): Unit ={
    //
  }

  /**
    * Computes the global pointing corrections for each pair of images.
    *
    * @param tiles list of Tile objects
    */
  def globalPointingCorrection(tiles: Array[Tile]): Unit   ={

  }

  /**
    * Rectifies a pair of images on a given tile.
    * @param tile tile to process
    * @param i number of the paired tile
    */
  def rectificationPair(tile: Tile, i:Int): Unit ={

  }

  /**
    * Computes the disparity of a pair of images on a given tile.
    * @param tile tile to process
    * @param i number of the paired tile
    */
  def stereoMatching(tile: Tile, i:Int): Unit = {

  }

  /**
    * Computes a height map from the disparity map of a pair of image tiles.
    * @param tile tile to process
    * @param i number of the paired tile
    */
  def disparityToHeight(tile: Tile, i:Int): Unit = {

  }

  /**
    * Computes a point cloud from the disparity map of a pair of image tiles.
    * @param tile Tile object containing the information needed to process the tile
    */
  def disparityToPly(tile: Tile) : Unit = {

  }
  /**
    * Computes a point cloud from the disparity maps of N-pairs of image tiles.
    * @param tile Tile object containing the information needed to process the tile
    */
  def multiDisparitiesToPly(tile: Tile) : Unit = {

  }

  /**
    * Computes mean height
    * @param tile Tile object containing the information needed to process the tile
    */
  def meanHeights(tile: Tile) : Unit = {

  }

  /**
    * Computes the global mean height
    * @param tiles list of Tile objects
    */
  def globalMeanHeights(tiles: Array[Tile]): Unit   ={

  }

  /**
    *  Generate a ply cloud.
    * @param tile Tile object containing the information needed to process the tile
    */
  def heightToPly(tile: Tile) : Unit = {

  }
  /**
    * Converts PLY to DSM using plyflatten tool
    * @param tile Tile object containing the information needed to process the tile
    */
  def plysToDsm(tile: Tile):Unit = {

  }


  /**
    * Computes global DSM using gdalbuildvrt and gdalTranslate tools
    * @param tiles list of Tile objects
    */
  def globalDsm(tiles: Array[Tile]): Unit   ={

  }

  /**
    * Produce a single multiscale point cloud for the whole processed region.
    * @param tiles list of Tile objects
    */
  def lidarPreprocessor(tiles: Array[Tile]): Unit   ={

  }
}
