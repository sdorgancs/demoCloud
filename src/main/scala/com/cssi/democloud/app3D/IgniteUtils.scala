package com.cssi.democloud.app3D

import org.apache.ignite.{IgniteFileSystem, Ignition}
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.spark.IgniteContext
import org.apache.spark.SparkContext

/**
  * Utility object to manage Ignite context
  */
object IgniteUtils {
  val conf : IgniteConfiguration = new IgniteConfiguration()

  /**
    * Returns the IGFS object, starts Ignition if needed
    */
  def getOrStartFS(): IgniteFileSystem ={
    Ignition.getOrStart(conf).fileSystem("igfs")
  }

  /**
    * Creates IgniteContext from SparkContext
    * @param sparkContext a SparkContext
    * @return the corresponding IgniteContext
    */
  def getContext(sparkContext: SparkContext): IgniteContext ={
    new IgniteContext(sparkContext, () =>conf)
  }

}

