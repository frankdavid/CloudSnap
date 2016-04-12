package hu.frankdavid.cloudsnap

import awscala.Region
import awscala.s3.S3
import awscala.sqs.{Queue, SQS}

object AmazonService {

  private val accessKey = ""
  private val accessSecret = ""

  private implicit val region = Region.Ireland

//  private val UrlQueue = "https://sqs.eu-central-1.amazonaws.com/509271677385/cloudsnap-url-queue"
  private val UrlQueue = "https://sqs.eu-west-1.amazonaws.com/509271677385/cloudsnap-url-queue"
  implicit val sqs = SQS(accessKey, accessSecret)
  val SnapshotUrlQueue = Queue(UrlQueue)

  val S3ScreenshotsBucketName = "cloudsnap-screenshots-ireland"
  implicit val s3 = S3(accessKey, accessSecret)
  val ScreenshotsBucket = s3.bucket(S3ScreenshotsBucketName).get

  def screenshotUrl(url: String) = s"https://s3-eu-west-1.amazonaws.com/$S3ScreenshotsBucketName/$url"
}
