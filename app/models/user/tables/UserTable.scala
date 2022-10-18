package models.user.tables

import models.user.User
import slick.jdbc.PostgresProfile.api._

/**
 * Here we define the table. It will have a name of people
 */
class UserTable(tag: Tag) extends Table[User](tag, Some("able_io"), "users") {

  /** The ID column, which is the primary key, and auto incremented */
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc, O.Unique)

  /** The email column */
  def email = column[String]("email", O.Unique)

  /** The password column */
  def password = column[Option[String]]("password")

  /** The name column */
  def firstName = column[String]("first_name")

  /** The last name column */
  def lastName = column[String]("last_name")

  /**
   * This is the table's default "projection".
   *
   * It defines how the columns are converted to and from the User object.
   *
   * In this case, we are simply passing the id, name, email and password parameters to the User case classes
   * apply and unapply methods.
   */
  def * = (id, email, password, firstName, lastName).<>((User.apply _).tupled, User.unapply)
}