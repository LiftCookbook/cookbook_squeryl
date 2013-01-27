package code
package snippet

import model.MySchema._

import net.liftweb.util._
import Helpers._
import net.liftweb.squerylrecord.RecordTypeMode._


class SolarSystem {

  def findOrCreatePlanet(n: String) : Planet =
    from(planets)(p => where(p.name === n) select(p)).headOption getOrElse {
      planets.insert(Planet.createRecord.name(n))
    }


  def findOrCreateSatellite(n: String, p: Planet) : Satellite =
    from(satellites)(s => where(s.name === n) select(s)).headOption getOrElse {
      satellites.insert(Satellite.createRecord.name(n).planetId(p.idField.is))
    }

  val earth = findOrCreatePlanet("Earth")
  val mars = findOrCreatePlanet("Mars")

  val theMoon = findOrCreateSatellite("The Moon", earth)
  val phobos = findOrCreateSatellite("Phobos", mars)
  val deimos = findOrCreateSatellite("Deimos", mars)

  val allPlanets =List(earth,mars)  //from(planets)(p => select(p))

}


class OneToManySnippet {


  def render = {

    val data = new SolarSystem
    import data._

    val express = Satellite.createRecord.name("Mars Express")
    mars.satellites.associate(express)

    "#planets-and-their-moons" #> allPlanets.map { p =>
      ".planet-name *" #> p.name.is &
      ".satellite-name *" #> p.satellites.map(_.name.is)
    }

  }
}
