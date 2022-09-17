package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.user.User
import play.api.i18n.Lang
import play.api.libs.json.{ JsString, Json }
import play.api.mvc.{ Action, AnyContent }
import utils.auth.{ JWTEnvironment, WithProvider }

import scala.concurrent.{ ExecutionContext, Future }

/**
 * The `Account` controller.
 */

class AccountController @Inject() (scc: SilhouetteControllerComponents)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {
  implicit val userFormat = Json.format[User]

  /**
   * Handles account request
   *
   * @return Account information of authenticated user
   */
  def account: Action[AnyContent] = SecuredAction(WithProvider[AuthType](CredentialsProvider.ID)).async {
    request: SecuredRequest[JWTEnvironment, AnyContent] =>
      implicit val lang: Lang = supportedLangs.availables.head
      userService.retrieve(LoginInfo(CredentialsProvider.ID, request.identity.email)).flatMap {
        case Some(user) => Future.successful(Ok(Json.toJson(user.copy(id = None, password = None))))
        case _ => Future.successful(BadRequest(JsString(messagesApi("invalid.user.not_found"))))
      }
  }
}
