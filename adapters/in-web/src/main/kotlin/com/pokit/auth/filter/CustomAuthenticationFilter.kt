package com.pokit.auth.filter

import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.port.`in`.TokenProvider
import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.token.exception.AuthErrorCode
import com.pokit.user.exception.UserErrorCode
import com.pokit.user.port.out.UserPort
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val userPort: UserPort,
) : OncePerRequestFilter() {
    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val excludePath = arrayOf(
            "/api/v1/auth/signin",
            "/api/v1/auth/reissue",
            "/swagger-ui/index.html#/",
            "/swagger", "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs",
            "/api-docs/**",
            "/v3/api-docs/**"
        )
        val path = request.requestURI
        val shouldNotFilter =
            excludePath
                .any { it.equals(path) }

        return shouldNotFilter
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val authentication = getAuthentication(request)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (e: Exception) {
            request.setAttribute("exception", e)
        }

        filterChain.doFilter(request, response)
    }

    private fun getAuthentication(request: HttpServletRequest): Authentication {
        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (!StringUtils.hasText(header)) {
            throw ClientValidationException(AuthErrorCode.TOKEN_REQUIRED)
        }

        val token = header.split(" ")[1]
        val userId = tokenProvider.getUserId(token)
        val user = (
            userPort.loadById(userId)
                ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)
        )

        val principalUser = PrincipalUser.of(user)
        val authorities = listOf(SimpleGrantedAuthority(principalUser.role.description))

        val authentication =
            UsernamePasswordAuthenticationToken(
                principalUser,
                null,
                authorities,
            )
        authentication.details = WebAuthenticationDetails(request)
        return authentication
    }
}
