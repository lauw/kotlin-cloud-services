package nl.muller.bedrijfsapp.edge

import com.netflix.zuul.context.RequestContext
import org.springframework.stereotype.Component

@Component
class RequestPreFilter : BaseFilter() {
    override fun run(): Any? {
        val context = RequestContext.getCurrentContext()
        val request = context.request

        //if the header for userId is already set, overwrite it, because this should not be possible
        if (request.getHeader(USER_ID_HEADER_NAME) != null) {
            context.addZuulRequestHeader(USER_ID_HEADER_NAME, "")
        }

        return null
    }

    override fun filterOrder(): Int {
        return 5
    }
}