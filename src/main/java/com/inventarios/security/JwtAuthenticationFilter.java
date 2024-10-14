package com.inventarios.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.inventarios.security.services.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// la funcion de esta clase , sera validar la informacion del token y si esto es exitoso , establecera la autenticacion de un usuario en la solicitud
// o en el contexto de seguridad
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private  JwtGenerator jwtGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

            String token = obtenerTokenDeSolicitud(request);
            if(StringUtils.hasText(token) && jwtGenerator.validarToken(token)){
                String username = jwtGenerator.obtenerUsernameJwt(token);
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                List<String> userRoles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                if (userRoles.contains("usuario") || userRoles.contains("admin")){
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
            }
            filterChain.doFilter(request, response);

        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
    }

    private String obtenerTokenDeSolicitud(HttpServletRequest request){
        String bearerToken = request.getHeader( "Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
