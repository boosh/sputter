package sputter.js

import upickle.default.{Reader, Writer}

import scala.concurrent.Future

/**
  * Default client for making type-safe REST requests
  */
object AjaxClient extends autowire.Client[String, Reader, Writer]{
  override def doCall(req: Request) = {
//    dom.ext.Ajax.post(
//      url = "/ajax/" + req.path.mkString("/"),
//      data = upickle.write(req.args)
//    ).map(_.responseText)
    // todo remove this...
    import scala.concurrent.ExecutionContext.Implicits.global
    Future("To do")
  }

  def read[Result: Reader](p: String) = upickle.default.read[Result](p)
  def write[Result: Writer](r: Result) = upickle.default.write(r)
}

