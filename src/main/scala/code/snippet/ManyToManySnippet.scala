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
  val voyager1 = findOrCreateProbe("Voyager 1")

  val allPlanets = List(jupiter, saturn)
  val allProbes = List(juno, voyager1)

  // You can insert relations like this:
  // juno.planets.associate(jupiter)
  // voyager1.planets.associate(jupiter)
  // voyager1.planets.associate(saturn)

  // Or, like this:
  // jupiter.probes.associate(juno)
  // jupiter.probes.associate(voyager1)
  // saturn.probes.associate(voyager1)

  // Either way, you get the same result.

  // To add properties on the joining table:
  probeVisits.insert(juno.planets.assign(jupiter).year(2016))
  probeVisits.insert(voyager1.planets.assign(jupiter).year(1979))
  probeVisits.insert(voyager1.planets.assign(saturn).year(1980))

}


class ManyToManySnippet {

  def render = {

    import SpaceMissions._

    "#planet-visits" #> allPlanets.map { planet =>
      ".planet-name *" #> planet.name.is &
      ".probe-name *" #> planet.probes.map(_.name.is)
      } &
    "#probe-visits" #> allProbes.map { probe =>
       ".probe-name *" #> probe.name.is &
       ".visit" #> probe.planets.associationMap.collect {
         case (planet, visit) => ".planet-name *" #> planet.name.is &
            ".year" #> visit.year.is
       }
     }

  }
}
