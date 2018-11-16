package nl.muller.bedrijfsapp.authentication.service

import nl.muller.bedrijfsapp.authentication.model.UserLogin
import nl.muller.bedrijfsapp.authentication.model.request.LoginRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient("user-rest")
interface UserService {
    @RequestMapping(method = [RequestMethod.POST], value = ["/login"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun tryLogin(loginRequest: LoginRequest): UserLogin?
}


