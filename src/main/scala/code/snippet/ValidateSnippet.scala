package code
package snippet

import net.liftweb.http.{S,SHtml}
import net.liftweb.util.Helpers._

import model.MySchema._

class ValidateSnippet {

  def render = {

    val newPlanet = Planet.createRecord

    def validateAndSave() : Unit = newPlanet.validate match {
      case Nil =>
        planets.insert(newPlanet)
        S.notice("Planet '%s' saved" format newPlanet.name.is)

      case errors =>
        S.error(errors)
    }

    "#planetName" #> newPlanet.name.toForm &
    "type=submit" #> SHtml.onSubmitUnit(validateAndSave)
  }

}
