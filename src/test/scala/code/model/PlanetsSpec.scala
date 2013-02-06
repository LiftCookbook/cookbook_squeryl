package code.model

import org.specs2.mutable._
import net.liftweb.squerylrecord.RecordTypeMode._
import MySchema._

class PlanetsSpec extends Specification with DBTestKit {

  sequential

  "Solar System" >> {

    "know that Mars has two moons" >> InMemoryDB {

      val mars = planets.insert(Planet.createRecord.name("Mars"))
      Satellite.createRecord.name("Phobos").planetId(mars.idField.is).save
      Satellite.createRecord.name("Deimos").planetId(mars.idField.is).save

      mars.satellites.size must_== 2
    }

    "moons can be associated to planets" >> InMemoryDB {

      val earth = planets.insert(Planet.createRecord.name("Earth"))

      val moon = Satellite.createRecord.name("Moon").planetId(earth.idField.is)
      moon.save

      moon.planet.one.map(_.name.is) must beSome("Earth")
    }


  }

}