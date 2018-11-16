package nl.muller.bedrijfsapp.user.controller

import nl.muller.bedrijfsapp.user.data.UserRepository
import nl.muller.bedrijfsapp.user.model.LoginRequest
import nl.muller.bedrijfsapp.user.model.UserAuthentication
import nl.muller.bedrijfsapp.user.rest.AuthenticationRestService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

@RestController
class UserController @Autowired constructor(private val userRepository: UserRepository, private val authenticationRestService: AuthenticationRestService) {
    private val passwordEncoder : BCryptPasswordEncoder = BCryptPasswordEncoder()
    private val TEN_DAYS = 1000 * 60 * 60 * 24 * 10.toLong()

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<String> {
        val user = userRepository.findByEmail(loginRequest.email)

        user?.let {
            if (passwordEncoder.matches(loginRequest.password, user.password)) {
                val userAuth = UserAuthentication(user.id, System.currentTimeMillis() + TEN_DAYS)
                val token = authenticationRestService.getTokenForUser(userAuth)

                if (StringUtils.isEmpty(token)) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
                }

                return ResponseEntity.ok(token)
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        }

        return ResponseEntity.notFound().build()
    }

    @GetMapping("/")
    fun me(@RequestHeader("USER_ID") userId : Long) = userRepository.findById(userId)

    @GetMapping("/hello")
    @ResponseBody
    fun hello(@RequestHeader("USER_ID") userId : Long) = "Hello ${userRepository.findById(userId).get().name()}!"
}