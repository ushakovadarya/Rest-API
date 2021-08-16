import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import scala.util._
import scala.io.StdIn._

object trainingThree {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "system")
    implicit val context = system.executionContext
    lazy val homeRoute = {
      get {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "This is home page"))
      }
    }
    lazy val userRoute = {
      get {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "This is user page"))
      }
    }

    val route = {
      concat(
      pathEndOrSingleSlash(homeRoute),
        path("user")(userRoute),
        path(Remaining){
          _ => {
            complete(StatusCodes.NotFound)
          }
        })
    }

    val bindingFuture = Http().newServerAt("localhost", 4000).bind(route)

    bindingFuture.onComplete{
      case Success(binding) =>
        val address = binding.localAddress
        println(s"Server online on ${address.getHostName}:${address.getPort}/\nPress enter to exit...")
        readLine()
        bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
      case Failure(ex) =>
        println(s"Happened error: $ex")
        system.terminate()
    }
  }
}
