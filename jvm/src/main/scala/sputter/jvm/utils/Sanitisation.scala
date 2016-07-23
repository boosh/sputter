package sputter.jvm.utils

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.safety.Whitelist

/**
 * Simple sanitisation library that strips out bad input.
 */
object Sanitisation {

  /**
   * Converts HTML to plain text
   *
   * @param html
   * @return
   */
  def htmlToText(html: String): String = {

    Jsoup.clean(html, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false)).trim()
  }
}
