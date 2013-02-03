package code.model

import org.specs2.mutable._
import net.liftweb.squerylrecord.RecordTypeMode._
import MySchema._

class PlanetsSpec extends Specification with DBTestKit {

  sequential

  "Planets" >> {

    "know that Mars has two moons" >> InMemoryDB {

      val mars = planets.insert(Planet.createRecord.name("Mars"))
      Satellite.createRecord.name("Phobos").planetId(mars.idField.is).save
      Satellite.createRecord.name("Deimos").planetId(mars.idField.is).save

      mars.satellites.size must_== 2
    }

  }

}