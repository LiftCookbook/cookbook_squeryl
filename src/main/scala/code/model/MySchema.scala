package code.model

import org.squeryl.Schema
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.squerylrecord.KeyedRecord
import net.liftweb.record.field.{StringField, LongField}
import net.liftweb.squerylrecord.RecordTypeMode._
import org.squeryl.dsl.{StatefulOneToMany, StatefulManyToOne}

object MySchema extends Schema {

  val planets = table[Planet]
  val satellites = table[Satellite]

  val planetToSatellites = oneToManyRelation(planets, satellites).
    via((p,s) => p.id === s.planetId)

  on(satellites) { s =>
    declare(s.planetId defineAs indexed("planet_idx"))
  }

  class Planet extends Record[Planet] with KeyedRecord[Long]   {
    override def meta = Planet
    override val idField = new LongField(this)
    val name = new StringField(this, 256)
    lazy val satellites : StatefulOneToMany[Satellite] = MySchema.planetToSatellites.leftStateful(this)
  }

  object Planet extends Planet with MetaRecord[Planet]


  class Satellite extends Record[Satellite] with KeyedRecord[Long] {
     override def meta = Satellite
     override val idField = new LongField(this)
     val name = new StringField(this, 256)
     val planetId = new LongField(this)
     lazy val planet : StatefulManyToOne[Planet] = MySchema.planetToSatellites.rightStateful(this)
  }

  object Satellite extends Satellite with MetaRecord[Satellite]

}
