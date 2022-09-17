package modules

import com.google.inject.AbstractModule
import models.device.daos.{ DeviceDAO, DeviceDAOImpl }
import models.device.services.{ DeviceService, DeviceServiceImpl }
import models.user.daos.{ UserDAO, UserDAOImpl }
import models.user.services.{ UserService, UserServiceImpl }
import net.codingwell.scalaguice.ScalaModule

/**
 * The base Guice module.
 */
class BaseModule extends AbstractModule with ScalaModule {

  /**
   * Configures the module.
   */
  override def configure(): Unit = {
    bind[UserDAO].to[UserDAOImpl]
    bind[UserService].to[UserServiceImpl]
    bind[DeviceDAO].to[DeviceDAOImpl]
    bind[DeviceService].to[DeviceServiceImpl]
  }
}
