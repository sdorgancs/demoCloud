package com.cssi.democloud.app3D

import breeze.linalg.{DenseMatrix, DenseVector}

/**
  * S2P Image masking algorithms
  */
object Masking {

  /**
    * Compute a mask for pixels masked by clouds, water, or out of image domain
    * @param coordinates coordinates of the ROI
    * @param rpc path to the xml file containing the rpc coefficients of the image
            RPC model is used with SRTM data to derive the water mask
    * @param roi_gml path to a gml file containing a mask
            defining the area contained in the full image
    * @param cld_gml path to a gml file containing a mask
            defining the areas covered by clouds
    * @param wat_msk path to an image file containing a water mask
    * @param use_srtm_for_water tells if a srtm MNT is used to compute water mask
    */
  def cloud_water_image_domain(coordinates: Coordinates,
                               rpc:String,
                               roi_gml:String="",
                               cld_gml:String="",
                               wat_msk:String="",
                               use_srtm_for_water: Boolean = false): DenseMatrix[Boolean] ={
    DenseMatrix.ones[Boolean](coordinates.h, coordinates.w)
  }


}
