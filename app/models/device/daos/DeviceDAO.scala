package models.device.daos

import com.mohiva.play.silhouette.api.LoginInfo
import models.device.Device

import scala.concurrent.Future

/**
 * Gives access to the device storage.
 */
trait DeviceDAO {

  /**
   * Finds a device by its login info.
   *
   * @param deviceLoginInfo The login info of the device to find.
   * @return The found device or None if no device for the given login info could be found.
   */
  def find(deviceLoginInfo: LoginInfo): Future[Option[Device]]

  /**
   * Saves a device.
   *
   * @param device The device to save.
   * @return The saved device.
   */
  def save(device: Device): Future[Device]

  /**
   * Updates a user.
   *
   * @param device The device to update.
   * @return The saved device.
   */
  def update(device: Device): Future[Device]
}
