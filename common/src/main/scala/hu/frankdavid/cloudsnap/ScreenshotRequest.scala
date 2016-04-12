package hu.frankdavid.cloudsnap

case class ScreenshotRequest(id: String, url: String)

object ScreenshotRequest {
   def serialize(obj: ScreenshotRequest) = obj.id + ";" + obj.url
 
   def deserialize(string: String): ScreenshotRequest = {
     val split = string.split(";", 2)
     ScreenshotRequest(split(0), split(1))
   }
 }