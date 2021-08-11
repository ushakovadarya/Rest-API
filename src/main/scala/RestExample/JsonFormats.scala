package RestExample
import RestExample.UserActor.ActionPerfomed
import spray.json.DefaultJsonProtocol
object JsonFormats {
  import DefaultJsonProtocol._
  implicit val userJsonFormat = jsonFormat2(User)
  implicit val usersJsonFormat = jsonFormat1(Users)
  implicit val actionPerfomedJsonFormat = jsonFormat1(ActionPerfomed)
}
