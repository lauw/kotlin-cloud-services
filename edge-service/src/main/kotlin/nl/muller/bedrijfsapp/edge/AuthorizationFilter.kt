package nl.muller.bedrijfsapp.edge

import com.netflix.zuul.context.RequestContext
import org.apache.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class AuthorizationFilter @Autowired constructor(private val authenticationService : AuthenticationService) : BaseFilter() {
    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        val request = context.request
        val servletPath = request.servletPath

        try {
            val userHasAccess = authenticationService.checkUserAccess(servletPath)

            if (!userHasAccess) {
                sendErrorResponse(context, HttpStatus.SC_UNAUTHORIZED)
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            sendErrorResponse(context, HttpStatus.SC_INTERNAL_SERVER_ERROR)
        }

        return null
    }

    override fun shouldFilter(): Boolean {
        //TODO: re-enable filter when checkUserAccess is working
        return false
    }

    override fun filterOrder(): Int {
        return 7
    }
}