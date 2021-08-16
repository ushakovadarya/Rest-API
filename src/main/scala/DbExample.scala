import com.zaxxer.hikari._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object DbExample {
  val db = Database.forConfig("postgres")

  case class User(email: String, username:String, age:Int, password:String)

  class UsersTable(tag: Tag) extends Table[User](tag, None, "users") {
    override def * = (email, username, age, password) <> (User.tupled, User.unapply)
    var email : Rep[String] = column[String]("email", O.PrimaryKey)
    var username: Rep[String] = column[String]("username")
    var age : Rep[Int] = column[Int]("age")
    var password : Rep[String] = column[String]("password")
  }

  def main(args: Array[String]): Unit = {
    val usersTable = TableQuery[UsersTable]

    /*val thirtyYearsQuery = usersTable.filter(_.age < 40).result

    val thirtyYears: Future[Seq[User]] = db.run(thirtyYearsQuery)
    val usersResults = Await.result(thirtyYears, 2.seconds)
    var results: Seq[User] = usersResults
    results.map(println(_))
    println("\n")

    val email = readLine("Enter email: ")
    val name = readLine("Enter username: ")
    val age = readLine("Enter age: ").toInt
    val password = readLine("Enter password: ")

    val user = User("kurochka@mail.com", "Alexander", 32, "12345")

    val insert = usersTable += user
    val insertAction = db.run(insert)
    val insertResults = Await.result(insertAction, 2.seconds)

    println(insertResults.toString)*/

    val selectAll = usersTable.result
    val selectAction = db.run(selectAll)
    val selectResults = Await.result(selectAction, 2.seconds)
    var results = selectResults
    results.map(println(_))
    println("\n")

    val select = usersTable.sortBy(p => (p.username.desc, p.age.desc)).drop(1).take(2).map(u => (u.username, u.email, u.age)).result
    val selectAct = db.run(select)
    val SelectResult = Await.result(selectAct, 2.seconds)
    SelectResult.map(p => println(p._1 + " " + p._2 + " " + p._3))
    /*val updateQuery = usersTable.filter(_.email === "uhanka@mail.com").map(_.password).update("123456")
    val updateAction = db.run(updateQuery)
    val updateResults = Await.result(updateAction, 2.seconds)
    if(updateResults == 0) println("Users with that email not exists")
    else println(updateResults)
    println("\n")

    val buhanka = usersTable.filter(_.email === "buhanka@mail.com").result
    val buhankaAction = db.run(buhanka)
    val buhankaResults = Await.result(buhankaAction, 2.seconds)
    results = buhankaResults
    results.map(println(_))

    val deleteQuery = usersTable.filter(_.email === "buhanka@mail.com").delete
    val deleteAction = db.run(deleteQuery)
    val deleteResults = Await.result(deleteAction, 2.seconds)
    println(deleteResults)*/
  }
}
