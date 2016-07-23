package sputter.jvm.auth

import java.io.FileOutputStream
import java.nio.file.{Files, Paths}
import java.security.SecureRandom
import java.util.{Date, UUID}

import com.nimbusds.jose.crypto.{MACSigner, MACVerifier}
import com.nimbusds.jose.{JWSAlgorithm, JWSHeader}
import com.nimbusds.jwt.{JWTClaimsSet, SignedJWT}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import sun.misc.BASE64Encoder

import scala.collection.JavaConversions._
import scala.util.Try


/**
  * Exception indicating that the supplied JWT token is invalid somehow and
  * that the user should reauthenticate.
  *
  * @param message
  */
case class JwtTokenInvalidException(message: String)
  extends Exception(message)


/**
  * Functions for creating and validating JWT auth tokens. Some methods are
  * not public to prevent clients from using insecure helpers.
  */
object JwtAuthToken {

  val logger = LoggerFactory.getLogger(JwtAuthToken.getClass)

  val conf = ConfigFactory.load()
  val issuer = conf.getString("sponvo.security.jwtIssuer")

  val sharedSecretFilePath = conf.getString("sponvo.security.sharedSecretPath")
  val sharedSecret = Try(Files.readAllBytes(Paths.get(sharedSecretFilePath)))
    .recover {
      case _: Exception =>
        logger.info(s"No shared secret file exists at $sharedSecretFilePath. " +
          s"Will generate one.")
        writeSecretToFile(sharedSecretFilePath)
        Files.readAllBytes(Paths.get(sharedSecretFilePath))
    }

  /**
    * Creates an array of random bytes. Can be used for HMAC signing.
    *
    * @param length Number of bytes to return.
    * @return
    */
  private def createSecureRandomBytes(length: Int): Array[Byte] = {

    val random = new SecureRandom()
    val randomBytes = new Array[Byte](length)
    random.nextBytes(randomBytes)
    randomBytes
  }

  /**
    * Generates and writes a new secret for signing tokens to the given path.
    * Used for one-time regeneration of the secret file.
    *
    * @param path Path to write file to. Must not already exist.
    */
  // todo: turn this into an app that can be run from the CLI, optionally forcing
  // overwriting an existing file or something. Update README deployment notes to
  // say we need to run this to generate a shared secret with the chat server.
  def writeSecretToFile(path: String) = {

    if (new java.io.File(path).exists) {
      throw new RuntimeException(s"Path $path already exists. Will not " +
        s"overwrite with new secret.")
    }

    // the length of this secret must match the signing algorithm used
    // in `signClaimSet`
    val secret = createSecureRandomBytes(length = 512)
    val outputStream = new FileOutputStream(path)
    outputStream.write(secret)
    outputStream.close()
  }

  /**
    * Returns a signed JSON web token (JWT) the user can use to authenticate with.
    *
    * @param firstName User's first name.
    * @param userId User's ID in the database
    * @param expiresInSeconds Number of seconds for the token to remain valid.
    * @param additionalClaims A map of additional claims to add to the token
    * @return
    */
  def getSignedJwt(firstName: String, userId: Int,
                   expiresInSeconds: Int,
                   additionalClaims: Map[String, String] = Map()): String = {

    val claimsSet = buildJwtClaimSet(firstName = firstName,
      userId = userId, expiresInSeconds = expiresInSeconds,
      additionalClaims = additionalClaims)
    val signedJwt = signClaimSet(claimsSet = claimsSet,
      sharedSecret = sharedSecret.getOrElse(
        throw new RuntimeException("Error: No shared secret found")))

    signedJwt.serialize()
  }

  /**
    * Verifies that a JWT was signed by our secret, that it hasn't expired,
    * and that all other fields are as expected.
    *
    * @param signedJwt
    * @return
    */
  private def validateJwt(signedJwt: SignedJWT): Boolean = {
    val verifier = new MACVerifier(sharedSecret.getOrElse(
      throw new RuntimeException("Error: No shared secret found")))
    val claimsSet = signedJwt.getJWTClaimsSet

    val now = new Date()

    // verify the signature, then make sure it hasn't expired, and that all
    // other fields correspond to what we set
    if (signedJwt.verify(verifier) && claimsSet.getExpirationTime.after(now) &&
      now.after(claimsSet.getNotBeforeTime) &&
      claimsSet.getIssuer == issuer && claimsSet.getAudience.size() == 1 &&
      claimsSet.getAudience.head == claimsSet.getSubject) {

      getUserIdFromJwt(signedJwt = signedJwt).isDefined
    }
    else {
      false
    }
  }

  /**
    * Extracts the user ID from the signed JWT if possible. Note: This
    * function doesn't perform any validation of the JWT.
    *
    * @param signedJwt
    * @return Int or None on failure
    */
  private def getUserIdFromJwt(signedJwt: SignedJWT): Option[Int] = {

    val userIdParts = new String("this needs to base64 decode the string: " +
      "signedJwt.getJWTClaimsSet.getSubject.toByteArray").split(':')

    if (userIdParts.length == 2) {
      try {
        Some(userIdParts(1).toInt)
      }
      catch {
        case _: NumberFormatException => None
      }
    }
    else {
      None
    }
  }

  /**
    * Fully validates a JWT and if the token is valid it extracts and returns
    * the user ID it contains.
    *
    * @param jwt JWT to validate and extract the user ID from.
    * @throws JwtTokenInvalidException if there are any problems with the token.
    * @return
    */
  def validateAndExtractUserId(jwt: String): Int = {

    val signedJwt = SignedJWT.parse(jwt)

    if (validateJwt(signedJwt)) {

      getUserIdFromJwt(signedJwt = signedJwt)
        .getOrElse(throw new JwtTokenInvalidException("Failed to extract " +
          "user ID from validated token"))
    }
    else {
      throw new JwtTokenInvalidException("Invalid token supplied.")
    }
  }

  /**
    * Builds a JWT claim set. This is the raw contents of the JWT.
    *
    * @param firstName User's first name.
    * @param userId User ID of the user to create the token for.
    * @param expiresInSeconds Number of seconds this token will be valid for.
    * @param additionalClaims A map of additional claims to add to the token
    * @return
    */
  private def buildJwtClaimSet(firstName: String, userId: Int,
                       expiresInSeconds: Int,
                               additionalClaims: Map[String, String]): JWTClaimsSet = {

    // encode the user ID to the same format as used by Sangria
//    val encodedUserId = s"User:$userId".getBytes.toBase64

    val encoder = new BASE64Encoder()
    val authToken = s"User:$userId"
    val encodedUserId = encoder.encode(authToken.getBytes())

    val builder = new JWTClaimsSet.Builder()
      .jwtID(UUID.randomUUID().toString)
      .issuer(issuer)
      .issueTime(new Date())
      // permit a 5 second skew
      .notBeforeTime(new Date(new Date().getTime -5 * 1000))
      .expirationTime(new Date(new Date().getTime + expiresInSeconds * 1000))
      .subject(encodedUserId)
      .audience(encodedUserId)
      .claim("firstName", firstName)

    additionalClaims.foreach {
      case (k, v) => builder.claim(k, v)
    }

    builder.build()
  }

  /**
    * Signs a JWT claim set with an HMAC code
    *
    * @param claimsSet Claim set to sign
    * @param sharedSecret Random bytes to use as the shared secret for signing
    */
  private def signClaimSet(claimsSet: JWTClaimsSet, sharedSecret: Array[Byte]): SignedJWT = {

    val signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), claimsSet)
    val signer = new MACSigner(sharedSecret)
    signedJWT.sign(signer)
    signedJWT
  }
}


object WriteJwtAuthTokenToFile extends App {

  val conf = ConfigFactory.load()
  val sharedSecretFilePath = conf.getString("sponvo.security.sharedSecretPath")

  JwtAuthToken.writeSecretToFile(sharedSecretFilePath)

  println(s"Auth token written to $sharedSecretFilePath")
}
