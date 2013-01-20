package code.model

import org.squeryl.Schema
import org.squeryl.annotations.{ Column, Transient }
import net.liftweb.squerylrecord._
import net.liftweb.record.{MetaRecord, Record}
import net.liftweb.record.field.LongField
import net.liftweb.record.field.StringField


object MySchema extends Schema {



 // class Measurement extends Record[Measurement] with KeyedRecord[Long] with CreatedUpdated[Measurement] {
  //   override def meta = Measurement

  //   @Column(name = "id")
  //   override val idField = new LongField(this)

  //   @Column(name = "v")
  //   val v = new StringField(this, 256)


  // }

  // object Measurement extends Measurement with MetaRecord[Measurement] {

  // }

  // val measurements = table[Measurement]("measurement")

  // override def callbacks = Seq(
  //   beforeUpdate[Measurement] call {_.onUpdate}
  // )

  // def dropAndCreate {
  //   drop
  //   create
  // }


}
