package nl.muller.bedrijfsapp.edge

import com.netflix.zuul.context.RequestContext
import org.apache.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class AuthenticationFilter @Autowired constructor(private val authenticationService : AuthenticationService) : BaseFilter() {
    companion object {
        private const val AUTH_HEADER_NAME = "X-AUTH-TOKEN"
    }

    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        val request = context.request

        val token = request.getHeader(AUTH_HEADER_NAME)

        try {
            authenticationService.getAuthenticatedUser(token)?.let { user ->
                context.addZuulRequestHeader(USER_ID_HEADER_NAME, "${user.id}")
                return null
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }

        sendErrorResponse(context, HttpStatus.SC_NOT_FOUND)

        return null
    }

    override fun filterOrder(): Int {
        return 6
    }
}