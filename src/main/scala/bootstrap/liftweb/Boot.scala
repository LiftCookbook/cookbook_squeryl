package bootstrap.liftweb

import net.liftweb._
import common.Full
import http.Html5Properties
import util._
import util.Helpers._

import common._
import http._
import sitemap._
import Loc._
import net.liftmodules.JQueryModule
import net.liftweb.http.js.jquery._
import java.sql.{DriverManager, Connection}


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    // where to search snippet
    LiftRules.addToPackages("code")

    // Build SiteMap
    val entries = List(
      Menu.i("Home") / "index", // the simple way to declare a menu

      Menu.i("One To Many") / "onetomany", // the simple way to declare a menu


      // more complex because this menu allows anything in the
      // /static path to be visible
      Menu(Loc("Static", Link(List("static"), true, "/static/index"),
	       "Static Content")))

    // set the sitemap.  Note if you don't want access control for
    // each page, just comment this line out.
    LiftRules.setSiteMap(SiteMap(entries:_*))

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    //Init the jQuery module, see http://liftweb.net/jquery for more information.
    LiftRules.jsArtifacts = JQueryArtifacts
    JQueryModule.InitParam.JQuery=JQueryModule.JQuery172
    JQueryModule.init()

    // For Postgress:
    // initPostgresql()

    // For H2:
    initH2()

    import net.liftweb.squerylrecord.RecordTypeMode._
    import net.liftweb.http.S
    import net.liftweb.util.LoanWrapper

    S.addAround(new LoanWrapper {
      override def apply[T](f: => T): T = inTransaction { f }
    })


    // Create the schema and add query logging:
    inTransaction {
      code.model.MySchema.printDdl
      code.model.MySchema.create
      org.squeryl.Session.currentSession.setLogger( s => println(s) )
    }

  }

  def initH2() {

    Class.forName("org.h2.Driver")

    import org.squeryl.adapters.H2Adapter
    import net.liftweb.squerylrecord.SquerylRecord
    import org.squeryl.Session

    SquerylRecord.initWithSquerylSession(Session.create(
      DriverManager.getConnection("jdbc:h2:mem:dbname;DB_CLOSE_DELAY=-1", "sa", ""),
      new H2Adapter))


    if (Props.devMode) {
      LiftRules.liftRequest.append({
        case Req("console" ::_, _, _) => false
      })
    }

  }




  def initPostgresql() {

    Class.forName("org.postgresql.Driver")

    import org.squeryl.Session
    import org.squeryl.adapters._
    import net.liftweb.squerylrecord.SquerylRecord

    def connection = DriverManager.getConnection("jdbc:postgresql://localhost/cookbook")

    SquerylRecord.initWithSquerylSession(Session.create(connection, new PostgreSqlAdapter))
  }

}
