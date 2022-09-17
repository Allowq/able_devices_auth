package models.device.tables

import models.device.Device
import slick.jdbc.PostgresProfile.api._

/**
 * Here we define the table. It will have a description of device
 */
class DeviceTable(tag: Tag) extends Table[Device](tag, Some("able_io"), "devices") {
  /** The ID column, which is the primary key, and auto incremented */
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc, O.Unique)

  /** The device UUID is unique identifier. */
  def deviceUUID = column[String]("device_uuid", O.Unique)

  /** The flag of registration status. Is it created only or also registered? */
  def isRegistered = column[Boolean]("is_registered")

  /** The device's short name. */
  def deviceName = column[String]("device_name")

  /** The reference on id (primary key) from UserTable (device owner) */
  def userIdOpt = column[Option[Long]]("user_id")

  /** The password column */
  def passwordOpt = column[Option[String]]("password")

  /**
   * This is the table's default "projection".
   *
   * It defines how the columns are converted to and from the User object.
   *
   * In this case, we are simply passing the id, name, email and password parameters to the User case classes
   * apply and unapply methods.
   */
  def * = (id, deviceUUID, isRegistered, userIdOpt, deviceName, passwordOpt) <> ((Device.apply _).tupled, Device.unapply)
}
