package models.device.daos

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import models.device.Device
import models.device.tables.DeviceTable
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ ExecutionContext, Future }

/**
 * Gives access to the device storage.
 */
class DeviceDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends DeviceDAO {

  private val _devices = TableQuery[DeviceTable]
  private val _db = dbConfigProvider.get[JdbcProfile].db

  /**
   * Finds a device by its login info.
   *
   * @param deviceLoginInfo The login info of the device to find.
   * @return The found device or None if no device for the given login info could be found.
   */
  override def find(deviceLoginInfo: LoginInfo): Future[Option[Device]] = _db.run {
    _devices.filter(_.deviceUUID === deviceLoginInfo.providerKey).result.headOption
  }

  /**
   * Saves a device.
   *
   * @param device The device to save.
   * @return The saved device.
   */
  override def save(device: Device): Future[Device] = _db.run {
    _devices returning _devices += device
  }

  /**
   * Updates a user.
   *
   * @param device The device to update.
   * @return The saved device.
   */
  override def update(device: Device): Future[Device] = _db.run {
    _devices.filter(_.deviceUUID === device.deviceUUID).update(device).map(_ => device)
  }
}
