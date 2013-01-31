package code.snippet


import net.liftweb.util._
import Helpers._
import code.model.MySchema._
import net.liftweb.squerylrecord.RecordTypeMode._

object SpaceMissions {

  def findOrCreatePlanet(n: String) : Planet =
    from(planets)(p => where(p.name === n) select(p)).headOption getOrElse {
      planets.insert(Planet.createRecord.name(n))
    }

  def findOrCreateProbe(n: String) : Probe =
    from(probes)(s => where(s.name === n) select(s)).headOption getOrElse {
      probes.insert(Probe.createRecord.name(n))
    }

  val jupiter = findOrCreatePlanet("Jupiter")
  val saturn = findOrCreatePlanet("Saturn")

  val juno = findOrCreateProbe("Juno")
  val voyager1 = findOrCreateProbe("Voyager")

  val allPlanets = List(jupiter, saturn)
  val allProbes = List(juno, voyager1)

  juno.planets.associate(jupiter)
  voyager1.planets.associate(jupiter)
  voyager1.planets.associate(saturn)

  // Or... same results with:
  // jupiter.probes.associate(juno)
  // jupiter.probes.associate(voyager1)
  // saturn.probes.associate(voyager1)

}


class ManyToManySnippet {

  def render = {

    org.squeryl.Session.currentSession.setLogger( s => println(s) )

    import SpaceMissions._

    "#planet-visits" #> allPlanets.map { p =>
      ".planet-name *" #> p.name.is &
      ".probe-name *" #> p.probes.map(_.name.is)
      } &
    "#probe-visits" #> allProbes.map { p =>
       ".probe-name *" #> p.name.is &
       ".planet-name *" #> p.planets.map(_.name.is)
     }

  }
}
