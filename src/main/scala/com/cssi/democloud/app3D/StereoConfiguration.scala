package com.cssi.democloud.app3D
import net.liftweb.json._

/**
  * StereoConfiguration factory
  */
object StereoConfiguration {

  /**
    * Creates a configuration from a json string
    * @param jsonString a json string
    * @return a StereoConfiguration
    */
  def fromString(jsonString: String): StereoConfiguration = {
    implicit val formats : DefaultFormats = DefaultFormats
    parse(jsonString).extract[StereoConfiguration]
  }
}


/**
  * Utility object to store s2p configuration
  */
case class StereoConfiguration(
          //path to output directory
          var out_dir: String = "s2p_output",
          //
          // path to directory where (many) temporary files will be stored
          var temporary_dir: String = "s2p_tmp",
          //
          // temporary files are erased when s2p terminates. Switch to False to keep them
          var clean_tmp: Boolean = true,
          //
          // remove all generated files except from ply point clouds and tif raster dsm
          var clean_intermediate: Boolean = true,
          //
          // switch to True if you want to process the whole image
          var full_img: Boolean = false,
          //
          // s2p processes the images tile by tile. The tiles are squares cropped from the
          // reference image. The width and height of the tiles are given by this param, in pixels.
          var tile_size: Boolean = false,
          //
          // margins used to increase the footprint of the rectified tiles, to
          // account for poor disparity estimation close to the borders
          var vertical_margin: Boolean = false,
          //
          // debug mode (more verbose logs and intermediate results saved)
          var debug: Boolean = false,
          //
          // don't rerun when the output file is already there
          var skip_existing: Boolean = false,
          //
          // resolution of the output digital surface model, in meters per pixel
          var dsm_resolution: Int = 4,
          //
          // radius to compute altitudes (and to interpolate the small holes)
          var dsm_radius: Double = 0.0,
          //
          // dsm_sigma controls the spread of the blob from each point for the dsm computation
          // (dsm_resolution by default)
          var dsm_sigma: Int = -1,
          // sift threshold on the first over second best match ratio
          //  cfg['sift_match_thresh'] = 0.6
          //
          var sift_match_thresh: Double = 0.6,
          // disp range expansion facto
          var disp_range_extra_margin: Double = 0.2,
          //
          // register the rectified images with a shear estimated from the rpc data
          var register_with_shear: Boolean = false,
          //
          // number of ground control points per axis in matches from rpc generation
          var n_gcp_per_axis: Int = 5,
          //
          // max distance allowed for a point to the epipolar line of its match
          var epipolar_thresh: Int = 5,
          //
          // triangulation mode : 'pairwise'or 'geometric'
          var triangulation_mode: String = "pairwise",
          //
          // use global pointing for geometric triangulation
          var use_global_pointing_for_geometric_triangulation: Boolean = false,
          //
          // stereo matching algorithm: 'tvl1', 'msmw', 'hirschmuller08',
          // hirschmuller08_laplacian', 'sgbm', 'mgm'
          var matching_algorithm: String = "mgm",
          //
          // size of the Census NCC square windows used in mgm
          var census_ncc_win: String = "mgm",
          // set these params if you want to impose the disparity range manually (cfg['disp_range_method'] == 'fixed_pixel_range')
          var disp_min: String = "",
          var disp_max: String = "",
          // set these params if you want to impose the altitude range manually (cfg['disp_range_method'] == 'fixed_altitude_range')
          var alt_min: String = "",
          var alt_max: String = "",
          // radius for erosion of valid disparity areas. Ignored if less than 2
          var msk_erosion: Int = 2,

          var fusion_operator: String = "average_if_close",
          // threshold (in meters) used for the fusion of two dems in triplet processing
          // It should be adapted to the zoom factor
          var fusion_thresh: Int = 3,
          //
          var disable_srtm: Boolean = false,
          //
          var rpc_alt_range_scale_factor: Int = 1,
          //
          // method to compute the disparity range: "sift", "srtm", "wider_sift_srtm", "fixed_pixel_range", "fixed_altitude_range"
          var disp_range_method: String = "wider_sift_srtm",

          var disp_range_srtm_low_margin: Int = -10,

          var disp_range_srtm_high_margin: Int = +100,
          //
          // url of the srtm database mirror
          //    cfg['srtm_url'] = 'http://138.231.80.250:443/srtm/tiff'
          //  cfg['srtm_url'] = 'ftp://xftp.jrc.it/pub/srtmV4/tiff'
          //  cfg['srtm_url'] = 'http://data_public:GDdci@data.cgiar-csi.org/srtm/tiles/GeoTIFF'
          //
          var srtm_url: String = "ftp://xftp.jrc.it/pub/srtmV4/tiff",
          // directory where to store the srtm tiles
          var srtm_dir: String = "/data/srtm",
          //
          // clean height maps outliers
          var cargarse_basura: Boolean = true,
          //
          // longitude/latitude bounding box
          var ll_bbx: (Double, Double, Double, Double) = (Double.NegativeInfinity, Double.PositiveInfinity, Double.NegativeInfinity, Double.PositiveInfinity),
          //
          // use srtm to generate a watermask
          var use_srtm_for_water: Boolean = false,
          //
          // If true, lidar preprocessor will be called if found in path
          var run_lidar_preprocessor: Boolean = false,
          //Input stereo images
          var images: Array[Image] = Array(),
          //Region of interest
          var roi: Coordinates = _,
          //Tile size
          var tileSize: Int = 0,
          //Directories where tile neighborhood is stored
          var neighborhoodDirs : List[String] = List()
     )
