package hu.frankdavid.cloudsnap

import java.util.logging.Logger

import com.amazonaws.services.s3.model.ObjectMetadata
import hu.frankdavid.cloudsnap.AmazonService._
import org.openqa.selenium.OutputType
import org.openqa.selenium.firefox.{FirefoxBinary, FirefoxDriver}

object ScreenshotService {

  private val DisplayNumber: Int = 98

  private val XVFB: String = "/usr/bin/Xvfb"

  private val XVFBCommand: String = XVFB + " :" + DisplayNumber

  private val logger = Logger.getLogger("ScreenshotService")

//  lazy val driver = {
//    val firefox = new FirefoxBinary()
//    firefox.setEnvironmentProperty("DISPLAY", ":" + DisplayNumber)
//    new FirefoxDriver(firefox, null)
//  }

  def main(args: Array[String]) {

    while (true) {
      AmazonService.SnapshotUrlQueue.messages().foreach { message =>
        val request = ScreenshotRequest.deserialize(message.body)
        //        Future {
        logger.info(s"trying to process ${request.url} as ${request.id}")
        try {
          if (processRequest(request)) {
            message.destroy()
            logger.info(s"${request.url} processed as ${request.id}")
          }
        }
        //        }
      }
      Thread.sleep(100)
    }
  }

  def processRequest(request: ScreenshotRequest): Boolean = {
    val process = Runtime.getRuntime.exec(XVFBCommand)
    val driver = {
      val firefox = new FirefoxBinary()
      firefox.setEnvironmentProperty("DISPLAY", ":" + DisplayNumber)
      new FirefoxDriver(firefox, null)
    }
    try {
      driver.get(request.url)
      val scrFile = driver.getScreenshotAs(OutputType.BYTES)
      val metadata = new ObjectMetadata()
      metadata.setContentType("image/jpeg")
      metadata.addUserMetadata("url", request.url)
      metadata.setContentLength(scrFile.length)
      AmazonService.ScreenshotsBucket.putObjectAsPublicRead(request.id, scrFile, metadata)
      true
    }
    catch {
      case e => e.printStackTrace(); false
    }
    finally {
      driver.close()
      process.destroy()
    }
  }

}
