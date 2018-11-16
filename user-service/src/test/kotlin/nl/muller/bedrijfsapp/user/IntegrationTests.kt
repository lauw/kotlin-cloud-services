package nl.muller.bedrijfsapp.user


import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.whenever
import nl.muller.bedrijfsapp.user.data.User
import nl.muller.bedrijfsapp.user.data.UserRepository
import nl.muller.bedrijfsapp.user.rest.AuthenticationRestService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ExtendWith(SpringExtension::class)
@WebMvcTest
class IntegrationTests(@Autowired val mockMvc: MockMvc) {
    private val passwordEncoder : BCryptPasswordEncoder = BCryptPasswordEncoder()

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var authenticationRestService: AuthenticationRestService

    @Test
    fun `Assert login success status 200 - ok`() {
        whenever(userRepository.findByEmail(anyString()))
                .thenReturn(User("user@user.com", passwordEncoder.encode("user"), "user", "user", 1))

        whenever(authenticationRestService.getTokenForUser(anyOrNull()))
                .thenReturn("token")

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson().toJson(LoginRequest("user@user.com", "user"))))
                .andExpect(status().isOk)
    }


    @Test
    fun `Assert login failure status 401 - unauthorized`() {
        whenever(userRepository.findByEmail(anyString()))
                .thenReturn(User("user@user.com", passwordEncoder.encode("wrong"), "user", "user", 1))

        whenever(authenticationRestService.getTokenForUser(anyOrNull()))
                .thenReturn("")

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson().toJson(LoginRequest("user@user.com", "user"))))
                .andExpect(status().isUnauthorized)
    }

    @Test
    fun `Assert login failure status 404 - could not find user`() {
        whenever(userRepository.findByEmail(anyString()))
                .thenReturn(null)

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson().toJson(LoginRequest("user@user.com", "user"))))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `Assert login failure status 500 - could not create token`() {
        whenever(userRepository.findByEmail(anyString()))
                .thenReturn(User("user@user.com", passwordEncoder.encode("user"), "user", "user", 1))

        whenever(authenticationRestService.getTokenForUser(anyOrNull()))
                .thenReturn("")

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Gson().toJson(LoginRequest("user@user.com", "user"))))
                .andExpect(status().isInternalServerError)
    }
}
