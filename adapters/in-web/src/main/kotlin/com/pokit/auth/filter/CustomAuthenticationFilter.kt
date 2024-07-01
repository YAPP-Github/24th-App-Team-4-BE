package com.pokit.auth.filter

import com.pokit.auth.port.`in`.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val tokenProvider: TokenProvider,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authentication = getAuthentication(request)
        authentication?.let {
            SecurityContextHolder.getContext().authentication = it
        }

        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication? {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (StringUtils.hasText(header)) {
            val token = header.split(" ")[1]

            /** TODO
             * tokenProvider 통해서 유저 정보 가져오기
             *
             */
            val authentication =
                UsernamePasswordAuthenticationToken(
                    null,
                    null,
                    null,
                )
            authentication.details = WebAuthenticationDetails(request)
            return authentication
        }

        return null
    }
}
