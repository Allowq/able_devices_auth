package utils.exceptions

import java.time.LocalDateTime

import javax.inject.{ Inject, Provider }
import play.api.{ Configuration, Environment, OptionalSourceMapper, UsefulException }
import play.api.http.DefaultHttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results.{ InternalServerError, _ }
import play.api.mvc.{ RequestHeader, Result }
import play.api.routing.Router

import scala.concurrent.Future

class ErrorHandler @Inject() (
  env: Environment,
  config: Configuration,
  sourceMapper: OptionalSourceMapper,
  router: Provider[Router])
  extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {
  case class ResourceNotFoundException() extends Exception() {}
  case class ValidationException(message: String) extends Exception(message) {}
  case class ErrorResponse(status: Int, datetime: LocalDateTime, error: String)

  private val logger = org.slf4j.LoggerFactory.getLogger("application.ErrorHandler")

  implicit val errorJsonFormat = Json.format[ErrorResponse]

  lazy val statusCodesMap = Map(
    100 -> "Continue",
    101 -> "Switching Protocols",
    102 -> "Processing",
    200 -> "OK",
    201 -> "Created",
    202 -> "Accepted",
    203 -> "Non-Authoritative Information",
    204 -> "No Content",
    205 -> "Reset Content",
    206 -> "Partial Content",
    207 -> "Multi-Status",
    208 -> "Already Reported",
    226 -> "IM Used",
    300 -> "Multiple Choices",
    301 -> "Moved Permanently",
    302 -> "Found",
    303 -> "See Other",
    304 -> "Not Modified",
    305 -> "Use Proxy",
    306 -> "Switch Proxy",
    307 -> "Temporary Redirect",
    308 -> "Permanent Redirect",
    400 -> "Bad Request",
    401 -> "Unauthorized",
    402 -> "Payment Required",
    403 -> "Forbidden",
    404 -> "Not Found",
    405 -> "Method Not Allowed",
    406 -> "Not Acceptable",
    407 -> "Proxy Authentication Required",
    408 -> "Request Timeout",
    409 -> "Conflict",
    410 -> "Gone",
    411 -> "Length Required",
    412 -> "Precondition Failed",
    413 -> "Payload Too Large",
    414 -> "URI Too Long",
    415 -> "Unsupported Media Type",
    416 -> "Range Not Satisfiable",
    417 -> "Expectation Failed",
    418 -> "I'm a teapot",
    421 -> "Misdirected Request",
    422 -> "Unprocessable Entity",
    423 -> "Locked",
    424 -> "Failed Dependency",
    426 -> "Upgrade Required",
    428 -> "Precondition Required",
    429 -> "Too Many Requests",
    431 -> "Request Header Fields Too Large",
    451 -> "Unavailable For Legal Reasons",
    499 -> "Client Closed Request",
    500 -> "Internal Server Error",
    501 -> "Not Implemented",
    502 -> "Bad Gateway",
    503 -> "Service Unavailable",
    504 -> "Gateway Time-out",
    505 -> "HTTP Version Not Supported",
    506 -> "Variant Also Negotiates",
    507 -> "Insufficient Storage",
    508 -> "Loop Detected",
    510 -> "Not Extended",
    511 -> "Network Authentication Required",
    527 -> "Railgun Error",
    599 -> "Network Connect Timeout Error"
  )

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    logger.debug(s"onClientError: statusCode = $statusCode, uri = ${request.uri}, message = $message")

    Future.successful(Status(statusCode)(Json.toJson(ErrorResponse(
      statusCode,
      LocalDateTime.now(),
      s"${if (statusCodesMap.contains(statusCode)) statusCodesMap(statusCode) else "A client error occurred: something went wrong!"}"))))
  }

  override protected def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    exception.printStackTrace()
    Future.successful(
      InternalServerError(Json.toJson(ErrorResponse(
        play.api.http.Status.INTERNAL_SERVER_ERROR,
        LocalDateTime.now(),
        "A server error occurred: " + exception.getMessage)))
    )
  }

  override protected def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    Future.successful(InternalServerError)
  }
}