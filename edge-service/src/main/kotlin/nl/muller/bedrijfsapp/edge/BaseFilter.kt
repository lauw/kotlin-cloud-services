package nl.muller.bedrijfsapp.edge

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
abstract class BaseFilter @Autowired constructor() : ZuulFilter() {
    companion object {
        const val USER_ID_HEADER_NAME = "USER_ID"
    }

    override fun shouldFilter(): Boolean {
        val servletPath = RequestContext.getCurrentContext().request.servletPath

        if (isLoginRequest(servletPath)) {
            return false
        }

        return true
    }

    override fun filterType(): String? {
        return "pre"
    }

    override fun filterOrder(): Int {
        return 5
    }

    fun sendErrorResponse(context: RequestContext, httpStatusCode: Int) {
        context.responseStatusCode = httpStatusCode
        context.setSendZuulResponse(false)
    }

    fun sendErrorResponse(context: RequestContext, httpStatusCode: Int, message: String) {
        sendErrorResponse(context, httpStatusCode)
        context.responseBody = message
    }

    private fun isLoginRequest(servletPath: String) : Boolean {
        return servletPath == "/user/login"
    }
}