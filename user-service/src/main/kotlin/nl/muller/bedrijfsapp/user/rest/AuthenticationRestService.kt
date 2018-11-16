package nl.muller.bedrijfsapp.user.rest

import nl.muller.bedrijfsapp.user.model.UserAuthentication
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod


@FeignClient("authentication-rest")
interface AuthenticationRestService {
    @RequestMapping(method = [RequestMethod.POST], value = ["/encode"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTokenForUser(userAuth: UserAuthentication): String
}