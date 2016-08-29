package sputter.jvm.components

import upickle.default.{Reader, Writer}

/**
  * Default autowire router that defines how to serialise and deserialise
  * REST data.
  */
object RestRouter extends autowire.Server[String, Reader, Writer]{
  def read[Result: Reader](p: String) = upickle.default.read[Result](p)
  def write[Result: Writer](r: Result) = upickle.default.write(r)
}
