package models.device.services

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import models.device.Device
import models.device.daos.DeviceDAO

import scala.concurrent.{ ExecutionContext, Future }

/**
 * Handles actions to users.
 *
 * @param deviceDAO The device DAO implementation.
 * @param ex The execution context.
 */
class DeviceServiceImpl @Inject() (deviceDAO: DeviceDAO)(implicit ex: ExecutionContext) extends DeviceService {

  /**
   * Retrieves a user that matches the specified login info.
   *
   * @param deviceLoginInfo The device login info to retrieve a device.
   * @return The retrieved device or None if no device could be retrieved for the given login info.
   */
  override def retrieve(deviceLoginInfo: LoginInfo): Future[Option[Device]] = deviceDAO.find(deviceLoginInfo)

  /**
   * Saves a device.
   *
   * @param device The device to save.
   * @return The saved device.
   */
  override def save(device: Device): Future[Device] = deviceDAO.save(device)

  /**
   * Updates a user.
   *
   * @param device The device to update.
   * @return The updated device.
   */
  override def update(device: Device): Future[Device] = deviceDAO.update(device)
}
