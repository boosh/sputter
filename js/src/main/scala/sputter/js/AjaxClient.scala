package sputter.js

import scalajs.concurrent.JSExecutionContext.Implicits.queue
import org.scalajs.dom.ext.Ajax
import upickle.default.{Reader, Writer}


/**
  * Generic client for making type-safe JSON REST requests
  */
object AjaxClient extends autowire.Client[String, Reader, Writer]{
  override def doCall(req: Request) = {
    Ajax.post(
      // todo: Pull server path from config
      url = "http://localhost:8080/api/" + req.path.mkString("/"),
      data = upickle.default.write(req.args)
    ).map(_.responseText)
  }

  def read[Result: Reader](p: String) = upickle.default.read[Result](p)
  def write[Result: Writer](r: Result) = upickle.default.write(r)
}
