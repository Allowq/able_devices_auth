package models.user

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.password.BCryptSha256PasswordHasher

/**
 * The user object.
 *
 * @param id The unique ID of the user.
 * @param lastName the last name of the authenticated user.
 * @param password the user's password.
 */
case class User(
  id: Option[Long],
  email: String,
  password: Option[String] = None,
  firstName: String,
  lastName: String) extends Identity {
  /**
   * Generates login info from email
   *
   * @return login info
   */
  def loginInfo = LoginInfo(CredentialsProvider.ID, email)

  /**
   * Generates password info from password.
   *
   * @return password info
   */
  def passwordInfo = PasswordInfo(BCryptSha256PasswordHasher.ID, password.get)
}
