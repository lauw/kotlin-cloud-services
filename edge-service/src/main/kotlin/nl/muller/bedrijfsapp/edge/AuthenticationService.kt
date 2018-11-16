package nl.muller.bedrijfsapp.edge

import nl.muller.bedrijfsapp.edge.security.data.UserAuthentication
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient("authentication-rest")
interface AuthenticationService {
    @RequestMapping(method = [RequestMethod.POST], value = ["/decode"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAuthenticatedUser(token: String): UserAuthentication?

    @RequestMapping(method = [RequestMethod.POST], value = ["/authorize"], consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun checkUserAccess(urlPath: String): Boolean
}

