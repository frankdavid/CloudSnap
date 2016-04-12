package controllers

import java.util.Date

import anorm._
import hu.frankdavid.cloudsnap.AmazonService._
import hu.frankdavid.cloudsnap.{AmazonService, ScreenshotRequest}
import models.Url
import play.api.Play.current
import play.api.cache.Cache
import play.api.db.DB
import play.api.mvc.{Action, Controller}



object Main extends Controller {
  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def url(url: String) = Action { implicit request =>
    val urlWithHttp = if (url.startsWith("http")) url else "http://" + url
    val urlObject = Cache.getAs[Url](urlWithHttp).map{x => println(s"Found $urlWithHttp in cache"); x} getOrElse {
      println(s"Loading $urlWithHttp from DB")
      val url = DB.withConnection { implicit connection =>
        SQL"SELECT uuid, url, addedAt, refreshedAt FROM urls WHERE url = $urlWithHttp"().headOption map { row =>
          val urlObject = new Url(row[String]("url"), row[String]("uuid"), row[Date]("addedAt"), row[Date]("refreshedAt"))

          urlObject
        } getOrElse {
          val urlObject = new Url(urlWithHttp)
          SQL"""INSERT INTO urls (uuid, url, addedAt, refreshedAt)
              VALUES (${urlObject.uuid}, $urlWithHttp, ${urlObject.addedAt}, ${urlObject.refreshedAt})""".executeInsert()
          AmazonService.SnapshotUrlQueue.add(ScreenshotRequest.serialize(ScreenshotRequest(urlObject.uuid, urlWithHttp)))
          urlObject
        }
      }
      Cache.set(urlWithHttp, url)
      url
    }

    Ok(views.html.url(urlObject, AmazonService.screenshotUrl(urlObject.uuid)))
  }

}
