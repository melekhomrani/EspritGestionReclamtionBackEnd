package tn.esprit.authService.Config;

import jakarta.servlet.*;
import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class TrustedEndpointFilter implements Filter {
    private int trustedPortNum = 0;
    private String trustedPathPrefix;
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    TrustedEndpointFilter(String trustedPort, String trustedPathPrefix) {
        if (trustedPort != null && trustedPathPrefix != null && !"null".equals(trustedPathPrefix)) {
            trustedPortNum = Integer.valueOf(trustedPort);
            this.trustedPathPrefix = trustedPathPrefix;
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.debug("trusted port filter init: " + trustedPortNum + ":" + trustedPathPrefix);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, ServletException, IOException {
        if (trustedPortNum != 0) {

            if (isRequestForTrustedEndpoint(servletRequest) && servletRequest.getLocalPort() != trustedPortNum) {
                log.warn("denying request for trusted endpoint on untrusted port");
                ((ResponseFacade) servletResponse).setStatus(404);
                servletResponse.getOutputStream().close();
                return;
            }

            if (!isRequestForTrustedEndpoint(servletRequest) && servletRequest.getLocalPort() == trustedPortNum) {
                log.warn("denying request for untrusted endpoint on trusted port");
                ((ResponseFacade) servletResponse).setStatus(404);
                servletResponse.getOutputStream().close();
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isRequestForTrustedEndpoint(ServletRequest servletRequest) {
        return ((RequestFacade) servletRequest).getRequestURI().startsWith(trustedPathPrefix);
    }

    @Override
    public void destroy() {
    }
}
