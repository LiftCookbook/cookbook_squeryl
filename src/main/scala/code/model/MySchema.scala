package code.model

import org.squeryl.Schema
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.record.field.{IntField, StringField, LongField}
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.dsl.{ManyToMany, StatefulOneToMany, StatefulManyToOne}
import net.liftweb.util.FieldError
import net.liftweb.http.S

object MySchema extends Schema {

  val planets = table[Planet]
  val satellites = table[Satellite]

  val planetToSatellites = oneToManyRelation(planets, satellites).
    via((p,s) => p.id === s.planetId)

  on(satellites) { s =>
    declare(s.planetId defineAs indexed("planet_idx"))
  }

  val probes = table[Probe]

  val probeVisits = manyToManyRelation(probes, planets).via[Visit] {
    (probe, planet, visit) => (visit.probeId === probe.id, visit.planetId === planet.id)
  }

  // Cascading deletes:
  // probeVisits.rightForeignKeyDeclaration.constrainReference(onDelete cascade)
  // probeVisits.leftForeignKeyDeclaration.constrainReference(onDelete cascade)


  class Planet extends Record[Planet] with KeyedRecord[Long] {
    override def meta = Planet
    override val idField = new LongField(this)

    val name = new StringField(this, 256) {
      override def validations =
        valMinLen(5, "Name too short") _ ::
        valUnique("validation.planet") _ ::
        super.validations
    }

    private def valUnique(errorMsg: => String)(name: String): List[FieldError] =
      Planet.unique_?(name) match {
        case true => FieldError(this.name, S ? errorMsg) :: Nil
        case false => Nil
      }

    lazy val satellites : StatefulOneToMany[Satellite] = MySchema.planetToSatellites.leftStateful(this)

    lazy val probes : ManyToMany[Probe,Visit] = MySchema.probeVisits.right(this)
  }

  object Planet extends Planet with MetaRecord[Planet] {
    def unique_?(name: String) = from(planets) { p =>
      where(lower(p.name) === lower(name)) select(p)
    }.isEmpty
  }

  class Satellite extends Record[Satellite] with KeyedRecord[Long] {
     override def meta = Satellite
     override val idField = new LongField(this)
     val name = new StringField(this, 256)
     val planetId = new LongField(this)
     lazy val planet : StatefulManyToOne[Planet] = MySchema.planetToSatellites.rightStateful(this)
  }

  object Satellite extends Satellite with MetaRecord[Satellite]

  class Probe extends Record[Probe] with KeyedRecord[Long] {
    override def meta = Probe
    override val idField = new LongField(this)
    val name = new StringField(this, 256)
    lazy val planets : ManyToMany[Planet,Visit] = MySchema.probeVisits.left(this)
  }

  object Probe extends Probe with MetaRecord[Probe]

  class Visit extends Record[Visit] with KeyedRecord[Long] {
    override def meta = Visit
    override val idField = new LongField(this)
    val planetId = new LongField(this)
    val probeId = new LongField(this)
    val year = new IntField(this)
  }

  object Visit extends Visit with MetaRecord[Visit]

}
