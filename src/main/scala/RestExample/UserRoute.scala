package RestExample

import akka.actor.typed._
import akka.actor.typed.scaladsl.AskPattern._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.Future
import RestExample.UserActor._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import JsonFormats._

class UserRoute(userActor: ActorRef[UserActor.Command])(implicit val system: ActorSystem[_]){
  private implicit val timeout = Timeout(5.seconds)
  private implicit val scheduler = system.scheduler
  def getUsers(): Future[Users] = {
    userActor.ask(GetUsers)
  }
  def getUser(name: String): Future[GetUserResponse] = {
    userActor.ask(GetUser(name, _))
  }
  def createUser(user: User): Future[ActionPerfomed] = {
    userActor.ask(CreateUser(user, _))
  }
  def deleteUser(name: String): Future[ActionPerfomed] = {
    userActor.ask(DeleteUser(name, _))
  }

  val getUsersRoute = get {
    complete(getUsers())
  }
  val createUserRoute = post {
    entity(as[User]){user =>
      onSuccess(createUser(user)){performed =>
        complete(StatusCodes.Created, performed)
      }
    }
  }
  def getUserRoute(name: String):Route = get {
    rejectEmptyResponse{
      onSuccess(getUser(name)){response =>
        complete(response.maybeUser)
      }
    }
  }
  def deleteUserRoute(name: String):Route = delete{
    onSuccess(deleteUser(name)){performed =>
      complete(StatusCodes.OK, performed)
    }
  }
  val userRoute = {
    pathPrefix("users"){
      concat(
        pathEnd{
          concat(
            getUsersRoute,
            createUserRoute
          )
        },
        path(Segment){ name =>
          concat(
            getUserRoute(name),
            deleteUserRoute(name)
          )
        }
      )
    }
  }
}
