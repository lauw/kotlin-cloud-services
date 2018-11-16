package nl.muller.bedrijfsapp.authentication.security

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import nl.muller.bedrijfsapp.authentication.security.data.UserAuthentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.IOException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

@Component
class TokenHandler @Autowired constructor(@Value("\${token.secret}") secret: String) {
    private val hmac: Mac

    init {
        try {
            val secretKey = DatatypeConverter.parseBase64Binary(secret)
            hmac = Mac.getInstance(HMAC_ALGO)
            hmac.init(SecretKeySpec(secretKey, HMAC_ALGO))

        } catch (e: NoSuchAlgorithmException) {
            throw IllegalStateException("failed to initialize HMAC: " + e.message, e)
        } catch (e: InvalidKeyException) {
            throw IllegalStateException("failed to initialize HMAC: " + e.message, e)
        }
    }

    fun parseUserFromToken(token: String): UserAuthentication? {
        val parts = token.split(SEPARATOR_SPLITTER.toRegex()).filter { !it.isEmpty() }.toTypedArray()

        if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
            try {
                val userBytes = fromBase64(parts[0])
                val hash = fromBase64(parts[1])

                val validHash = Arrays.equals(createHmac(userBytes), hash)
                if (validHash) {
                    val user = fromJSON(userBytes)

                    if (Date().time < user.expires) {
                        return user
                    }
                }
            } catch (e: IllegalArgumentException) {
                //log tempering attempt here
            }

        }
        return null
    }

    fun createTokenForUser(user: UserAuthentication): String {
        val userBytes = toJSON(user)
        val hash = createHmac(userBytes)
        val sb = StringBuilder(170)
        sb.append(toBase64(userBytes))
        sb.append(SEPARATOR)
        sb.append(toBase64(hash))
        return sb.toString()
    }

    private fun fromJSON(userBytes: ByteArray): UserAuthentication {
        try {
            return ObjectMapper().readValue<UserAuthentication>(ByteArrayInputStream(userBytes), UserAuthentication::class.java)
        } catch (e: IOException) {
            throw IllegalStateException(e)
        }

    }

    private fun toJSON(user: UserAuthentication): ByteArray {
        try {
            return ObjectMapper().writeValueAsBytes(user)
        } catch (e: JsonProcessingException) {
            throw IllegalStateException(e)
        }

    }

    private fun toBase64(content: ByteArray): String {
        return DatatypeConverter.printBase64Binary(content)
    }

    private fun fromBase64(content: String): ByteArray {
        return DatatypeConverter.parseBase64Binary(content)
    }

    // synchronized to guard internal hmac object
    @Synchronized private fun createHmac(content: ByteArray): ByteArray {
        return hmac.doFinal(content)
    }

    companion object {
        private const val HMAC_ALGO = "HmacSHA256"
        private const val SEPARATOR = "."
        private const val SEPARATOR_SPLITTER = "\\."
    }
}
