package RestExample
import akka.actor.typed._
import akka.actor.typed.scaladsl.Behaviors
import scala.collection.immutable

final case class User(name: String, age: Int)
final case class Users(users: immutable.Seq[User])

object UserActor {
  sealed trait Command
  final case class GetUsers(replyTo: ActorRef[Users]) extends Command
  final case class CreateUser(user: User, replyTo: ActorRef[ActionPerfomed]) extends Command
  final case class GetUser(name: String, replyTo: ActorRef[GetUserResponse]) extends Command
  final case class DeleteUser(name: String, replyTo: ActorRef[ActionPerfomed]) extends Command

  final case class ActionPerfomed(description: String)
  final case class GetUserResponse(maybeUser: Option[User])

  def apply(): Behavior[Command] = registry(Set.empty)

  private def registry(users: Set[User]): Behavior[Command] =
    Behaviors.receiveMessage{
      case GetUsers(replyTo) =>
        replyTo ! Users(users.to[immutable.Seq])
        Behaviors.same
      case CreateUser(user, replyTo) =>
        replyTo ! ActionPerfomed(s"User ${user.name} created")
        registry(users + user)
      case GetUser(name, replyTo) =>
        replyTo ! GetUserResponse(users.find(_.name == name))
        Behaviors.same
      case DeleteUser(name, replyTo) =>
        replyTo ! ActionPerfomed(s"User $name deleted")
        registry(users.filterNot(_.name == name))
    }
}
