package models.device.services

import com.mohiva.play.silhouette.api.services.IdentityService
import models.device.Device

import scala.concurrent.Future

/**
 * Handles actions to devices.
 */
trait DeviceService extends IdentityService[Device] {

  /**
   * Saves a device.
   *
   * @param device The device to save.
   * @return The saved device.
   */
  def save(device: Device): Future[Device]

  /**
   * Updates a device.
   *
   * @param device The device to update.
   * @return The updated device.
   */
  def update(device: Device): Future[Device]
}
