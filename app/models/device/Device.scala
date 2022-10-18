package models.device

import java.util.UUID

import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.password.BCryptSha256PasswordHasher

/**
 * The Json deserializer (model) for device representation
 *
 * @param deviceName the device's short name.
 * @param password the device's password in original format.
 */
case class DeviceModel(deviceName: String, password: String) {
  def deviceObj: Device = Device(deviceName = deviceName, passwordHashOpt = Some(password))
}

/**
 * The device object.
 *
 * @param idOpt The unique ID of the device.
 * @param deviceUUID The unique UUID of the device.
 * @param isRegistered the flag of registration status. Is it created only or also registered?
 * @param deviceName the device's short name.
 * @param userIdOpt the reference on id (primary key) from UserTable (device owner)
 * @param passwordHashOpt the device's hashing password.
 */
case class Device(
  idOpt: Option[Long] = None,
  deviceUUID: String = UUID.randomUUID().toString,
  isRegistered: Boolean = false,
  userIdOpt: Option[Long] = None,
  deviceName: String,
  passwordHashOpt: Option[String] = None) extends Identity {

  /**
   * Generates Device object from pair of name and password
   *
   * @return login info
   */
  def this(name: String, passwordHash: String) = {
    this(deviceName = name, passwordHashOpt = Some(passwordHash))
  }

  /**
   * Generates login info from device's UUID
   *
   * @return login info
   */
  def deviceLoginInfo = LoginInfo(CredentialsProvider.ID, idOpt.get.toString)

  /**
   * Generates password info from password.
   *
   * @return password info
   */
  def passwordInfo = PasswordInfo(BCryptSha256PasswordHasher.ID, passwordHashOpt.get)
}

