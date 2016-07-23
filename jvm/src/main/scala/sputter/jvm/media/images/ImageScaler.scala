package sputter.jvm.media.images

import java.io.File
import java.net.URL
import java.util.UUID

import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.nio.JpegWriter
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Scale local or remote images.
  */
object ImageScaler {

  val logger = LoggerFactory.getLogger(classOf)

  /**
    * Scale a local JPEG file
    *
    * @param path Path to JPEG to scale
    * @param width Target width (pixels)
    * @param height Target height (pixels)
    * @param compression Compression level (1-100)
    * @return Scaled JPEG File object
    */
  def scaleJpeg(path: File, width: Int, height: Int, compression: Int): Future[File] = {

    val tempFile = File.createTempFile(UUID.randomUUID().toString, ".jpg")

    logger.debug(s"Scaling $path to ${width}x$height at $tempFile...")

    Future {
      Image.fromFile(path).cover(width, height)
        .output(tempFile)(JpegWriter().withCompression(compression))

      logger.debug(s"Image $path scaled to ${width}x$height at $tempFile")

      tempFile
    }
  }

  /**
    * Downloads a remote JPEG and scales it
    *
    * @param url URL to the JPEG to download
    * @param width Target width (pixels)
    * @param height Target height (pixels)
    * @param compression Compression level (1-100)
    * @return Scaled JPEG File object
    */
  def scaleJpeg(url: URL, width: Int, height: Int, compression: Int): Future[File] = {

    val tempFile = File.createTempFile(UUID.randomUUID().toString, ".jpg")

    logger.debug(s"Downloading $url to $tempFile before scaling...")

    Future {
      FileUtils.copyURLToFile(url, tempFile)
      logger.debug(s"Successfully downloaded $url to $tempFile")
    }.flatMap { _ =>
      scaleJpeg(path = tempFile, width = width, height = height, compression = compression)
    }
  }
}
