package RestExample

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import scala.util._

object RestAPI {
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]) = {
    import system.executionContext

    val bindingFuture = Http().newServerAt("localhost", 4000).bind(routes)

    bindingFuture.onComplete{
      case Success(binding) =>
        val address = binding.localAddress
        println(s"Server online at http://${address.getHostName}:${address.getPort}/")
      case Failure(ex) =>
        println(s"Happened error: $ex")
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    val rootBehavior = Behaviors.setup[Nothing]{ context =>
      val userActor = context.spawn(UserActor(), "UserActor")
      context.watch(userActor)

      val userRoute = new UserRoute(userActor)(context.system)
      startHttpServer(userRoute.userRoute)(context.system)

      Behaviors.empty
    }
    val system = ActorSystem[Nothing](rootBehavior, "SystemActor")
  }
}
