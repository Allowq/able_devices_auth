package controllers.device

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import controllers.{ SilhouetteController, SilhouetteControllerComponents }
import models.device.{ Device, DeviceModel }
import play.api.i18n.Lang
import play.api.libs.json.{ JsString, Json }
import play.api.libs.json._
import play.api.mvc.AnyContent
import utils.auth.{ JWTEnvironment, WithProvider }

import scala.concurrent.{ ExecutionContext, Future }

/**
 * The `Create Device` controller. It is used for create devices that belong to a user account.
 */
class CreateDeviceController @Inject() (scc: SilhouetteControllerComponents)(implicit ec: ExecutionContext) extends SilhouetteController(scc) {
  implicit val deviceReads = Json.format[DeviceModel]
  implicit val deviceWrites = new Writes[Device] {
    override def writes(d: Device): JsValue = {
      Json.obj(
        "UUID" -> d.deviceUUID,
        "device_name" -> d.deviceName,
        "is_registered" -> d.isRegistered
      )
    }
  }

  /**
   * Handles create device request.
   *
   * @return The result of request to display.
   */
  def createDevice = SecuredAction(WithProvider[AuthType](CredentialsProvider.ID)).async {
    request: SecuredRequest[JWTEnvironment, AnyContent] =>
      implicit val lang: Lang = supportedLangs.availables.head
      request.body.asJson.flatMap(_.asOpt[DeviceModel]) match {
        case Some(newDevice) => {
          userService.retrieve(LoginInfo(CredentialsProvider.ID, request.identity.email)).flatMap {
            case Some(user) => {
              val deviceAuthInfo = passwordHasherRegistry.current.hash(newDevice.password)
              val deviceObj = newDevice.deviceObj.copy(userIdOpt = user.id, passwordHashOpt = Some(deviceAuthInfo.password))
              deviceService.save(deviceObj).map(d => Ok(Json.toJson(d)))
            }
            case _ => Future.successful(BadRequest(JsString(messagesApi("invalid.user.not_found"))))
          }
        }
        case _ => Future.successful(BadRequest(JsString(messagesApi("invalid.body"))))
      }
  }
}
