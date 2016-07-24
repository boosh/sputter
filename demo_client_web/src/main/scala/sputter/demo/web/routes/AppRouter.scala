package sputter.demo.web.routes

import sputter.demo.web.components.TopNav
import sri.core.ReactElement
import sri.scalacss.Defaults._
import sputter.demo.web.components._
import sputter.demo.web.screens.{ContactScreen, HomeScreen}
import sputter.demo.web.styles.GlobalStyle
import sri.web.router._
import sri.web.vdom.htmltags._

object AppRouter {

  object HomePage extends WebStaticPage
  object ContactPage extends WebStaticPage

  object Config extends WebRouterConfig {

    override val history: History = HistoryFactory.browserHistory()

    override val initialRoute: (WebStaticPage, WebRoute) = defineInitialRoute(HomePage, (route: WebRoute) => HomeScreen())

    override val notFound: WebRouteNotFound = WebRouteNotFound(HomePage)

    staticRoute(page = ContactPage, path = "contact", component = (route: WebRoute) => ContactScreen())

    /**
     * this method is responsible for rendering components
     * @param route current route that is pushed to stack
     * @return
     */
    override def renderScene(route: WebRoute): ReactElement = {
      div(className = GlobalStyle.flexOneAndDirectionVertical)(
        TopNav(),
        super.renderScene(route)
      )
    }
  }

  val router = WebRouter(Config)
}
