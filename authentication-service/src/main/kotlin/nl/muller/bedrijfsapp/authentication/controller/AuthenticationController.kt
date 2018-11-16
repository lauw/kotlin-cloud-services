package nl.muller.bedrijfsapp.authentication.controller

import nl.muller.bedrijfsapp.authentication.security.TokenHandler
import nl.muller.bedrijfsapp.authentication.security.data.UserAuthentication
import nl.muller.bedrijfsapp.authentication.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController @Autowired constructor(private val tokenHandler : TokenHandler, private val userService : UserService) {

    @PostMapping("/encode")
    fun getToken(@RequestBody userAuthentication: UserAuthentication) : String {
        return tokenHandler.createTokenForUser(userAuthentication)
    }

    @PostMapping("/decode")
    fun getAuthenticatedUser(@RequestBody token : String) : ResponseEntity<UserAuthentication> {
        tokenHandler.parseUserFromToken(token)?.let { userAuthentication ->
            return ResponseEntity.ok(userAuthentication)
        }

        return ResponseEntity.unprocessableEntity().build()
    }

    @PostMapping("/authenticate")
    @ResponseBody
    fun authenticate(@RequestBody token: String) : ResponseEntity<Long> {
        tokenHandler.parseUserFromToken(token)?.let {
            user -> return ResponseEntity.accepted().body(user.id)
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}