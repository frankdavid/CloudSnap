package models

import java.util.{UUID, Date}

case class Url(url: String, uuid: String = UUID.randomUUID().toString, addedAt: Date = new Date(), refreshedAt: Date = new Date())
