package sputter.demo.web.components

import sputter.demo.web.styles.Colors
import sri.scalacss.Defaults._
import sputter.demo.web.routes.AppRouter.{ContactPage, HomePage}
import sri.web.all._
import sri.web.router.{WebDynamicPage, WebRouterComponent, WebStaticPage}
import sri.web.vdom.htmltags._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{literal => json}
import scala.scalajs.js.annotation.ScalaJSDefined
import scala.scalajs.js.{UndefOr => U}
import scalacss.Defaults._

object TopNav {

  @ScalaJSDefined
  class Component extends WebRouterComponent[Unit, Unit] {
    def render() = {
      div(className = styles.navMenu)(
        getStaticItem("Home", HomePage),
        getStaticItem("Contact", ContactPage)
      )
    }

    def getStaticItem(text: String, page: WebStaticPage,
                      query: js.UndefOr[js.Object] = js.undefined,
                      state: js.UndefOr[js.Object] = js.undefined) = {
      Button(style = styles.menuItem(page == currentRoute.page),
        onPress = () => navigateTo(page, query = query, state = state))(
          span()(text)
        )
    }

    def getDynamicItem(text: String, page: WebDynamicPage[_],
                       placeholder: String,
                       query: js.UndefOr[js.Object] = js.undefined,
                       state: js.UndefOr[js.Object] = js.undefined) = {
      Button(style = styles.menuItem(page == currentRoute.page),
        onPress = () => navigateToDynamic(page, placeholder = placeholder, query = query, state = state))(
          span()(text)
        )
    }
  }

  @ScalaJSDefined
  class StaticQuery(val sorting: String, val option: js.UndefOr[String] = js.undefined) extends js.Object


  object styles extends StyleSheet.Inline {

    import dsl._

    val navMenu = style(display.flex,
      flexDirection.row,
      alignItems.center,
      backgroundColor :=! Colors.gold,
      margin.`0`,
      height(60.px),
      paddingLeft(40 px))

    val menuItem = styleF.bool(selected => {
      styleS(padding(20.px),
        fontSize :=! "1.5em",
        cursor.pointer,
        color :=! c"rgb(244, 233, 233)",
        marginLeft(10.px),
        marginRight(10.px),
        mixinIfElse(selected)(backgroundColor :=! Colors.darkGold,
          fontWeight._500)(backgroundColor :=! Colors.transparent,
            fontWeight.normal)
      )
    })
  }

  val constructor = getTypedConstructor(js.constructorOf[Component], classOf[Component])

  constructor.contextTypes = sri.web.router.routerContextTypes

  def apply(ref: js.UndefOr[String] = "", key: js.Any = {}) = createElementNoProps(constructor)
}
