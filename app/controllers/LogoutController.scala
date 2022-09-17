package controllers

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LogoutEvent
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.user.User
import play.api.libs.json.Json
import play.api.mvc.{ Action, AnyContent }
import utils.auth.{ JWTEnvironment, WithProvider }

import scala.concurrent.ExecutionContext

/**
 * The `Logout` controller.
 */

class LogoutController @Inject() (scc: SilhouetteControllerComponents)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {
  implicit val userFormat = Json.format[User]

  /**
   * Handles logout request
   *
   * @return Account information of authenticated user
   */
  def logout: Action[AnyContent] = SecuredAction(WithProvider[AuthType](CredentialsProvider.ID)).async {
    request: SecuredRequest[JWTEnvironment, AnyContent] =>
      eventBus.publish(LogoutEvent(request.identity, request))
      for {
        result <- authenticatorService.discard(request.authenticator, Ok.discardingHeader("X-Auth").withNewSession)(request)
      } yield {
        result
      }
  }
}