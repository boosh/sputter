package sputter.jvm.thirdparties

import java.io.File

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{PutObjectRequest, PutObjectResult}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
  * Wrapper for working with S3.
  */
object AmazonS3 {

  val logger = LoggerFactory.getLogger(classOf)

  /**
    * This uses the default credentials provider chain which looks in env vars,
    * system properties, the credentials file, etc. See
    * http://docs.aws.amazon.com/AWSSdkDocsJava/latest/DeveloperGuide/credentials.html
    */
  val s3Client = new AmazonS3Client()

  /**
    * Copy a local file to S3
    *
    * @param source File to upload
    * @param s3Path Path to write the file to on S3
    */
  def copyToS3(source: File, s3Path: String): Future[PutObjectResult] = {

    val bucketKey = splitS3Path(s3Path)

    val bucketName = bucketKey._1
    val key = bucketKey._2.replace("//", "/")

    logger.debug(s"Uploading local file from ${source.getAbsolutePath} to " +
      s"s3://$bucketName/$key")

    Future {
      // todo: error handling & retries
      s3Client.putObject(new PutObjectRequest(bucketName, key, source))
    }
  }

  /**
    * Return the given S3 path as a tuple of (bucketName, key)
    *
    * @param s3Path
    * @return
    */
  def splitS3Path(s3Path: String): (String, String) = {

    if (!s3Path.startsWith("s3://")) {
      throw new IllegalArgumentException(s"s3Path must start with `s3://`. " +
        s"Was given $s3Path")
    }

    val pattern = "s3://([^/]+)/(.*)$".r

    s3Path match {
      case pattern(bucket, key) => (bucket, key)
    }
  }
}
