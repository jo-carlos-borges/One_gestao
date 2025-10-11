package br.com.one.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.one.security.JwtHelper;
import br.com.one.security.UserDetailsServiceImpl;
import br.com.one.utils.CookieUtils;
import br.com.one.utils.ErrorResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Order(1)
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtHelper jwtHelper;
	private final ErrorResponseWriter errorResponseWriter;
	private final UserDetailsServiceImpl userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			// Primeiro tenta pegar do header Authorization
			String authHeader = request.getHeader("Authorization");
			String token = null;
			
			System.out.println("JWT Auth: Authorization header: " + authHeader);
			
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				System.out.println("JWT Auth: Token extracted from header: " + token.substring(0, Math.min(20, token.length())) + "...");
			} else {
				// Fallback para cookie se não encontrar no header
				token = CookieUtils.getCookieValue(request, "jwt");
				System.out.println("JWT Auth: Token extracted from cookie: " + (token != null ? token.substring(0, Math.min(20, token.length())) + "..." : "null"));
			}
			
			boolean isAuthEndpoint = request.getRequestURI().equals("/api/v1/auth/login");
			
			if (isAuthEndpoint) {
				filterChain.doFilter(request, response);
				return;
			}

			if (token == null || token.isBlank()) {
				System.out.println("JWT Auth: Token is null or empty for URI: " + request.getRequestURI());
				sendAuthError(response, request, "Token is null or empty");
				return;
			}

			if (jwtHelper.isTokenExpired(token)) {
				System.out.println("JWT Auth: Token is expired for URI: " + request.getRequestURI());
				sendAuthError(response, request, "Token is expired");
				return;
			}

			String username = jwtHelper.extractUsername(token);
			System.out.println("JWT Auth: Extracted username: " + username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				System.out.println("JWT Auth: UserDetails loaded: " + (userDetails != null ? userDetails.getUsername() : "null"));
				if (!jwtHelper.validateToken(token, userDetails)) {
					System.out.println("JWT Auth: Invalid token for URI: " + request.getRequestURI());
					sendAuthError(response, request, "Invalid token");
					return;
				}
				System.out.println("JWT Auth: Token validated successfully");
			}

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			sendAuthError(response, request, e.getMessage());
		}
	}

	private void sendAuthError(HttpServletResponse response, HttpServletRequest request, String message) throws IOException {
		System.out.println("JWT Auth Error: " + message + " for URI: " + request.getRequestURI());
		errorResponseWriter.write(response, HttpStatus.FORBIDDEN, message, request.getRequestURI());
	}
}