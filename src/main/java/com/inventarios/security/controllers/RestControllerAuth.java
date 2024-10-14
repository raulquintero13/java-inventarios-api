package com.inventarios.security.controllers;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperationsExtensionsKt;

import com.inventarios.entity.Role;
import com.inventarios.entity.Usuario;
import com.inventarios.repository.IRoleRepository;
import com.inventarios.repository.IUserRepository;
import com.inventarios.security.JwtGenerator;
import com.inventarios.security.dtos.DtoAuthRespuesta;
import com.inventarios.security.dtos.DtoLogin;
import com.inventarios.security.dtos.DtoRegistro;

@RestController
@RequestMapping("/api/auth/")
public class RestControllerAuth {



    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private IRoleRepository rolesRepository;
    private IUserRepository usuariosRepository;
    private JwtGenerator jwtGenerator;
    
    @Autowired
    
    public RestControllerAuth(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
            IRoleRepository rolesRepository, IUserRepository usuariosRepository, JwtGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolesRepository = rolesRepository;
        this.usuariosRepository = usuariosRepository;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("register")
    public ResponseEntity<String>registrar(@RequestBody DtoRegistro dtoRegistro){
        if (usuariosRepository.existsByUsername(dtoRegistro.getUsername())){
            return new ResponseEntity<>("El usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
            
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(dtoRegistro.getUsername());
        usuario.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        Role roles = rolesRepository.findByName("usuario").get();
        usuario.setRoles(Collections.singletonList(roles));
        System.out.println(">>>>>>>>>>>>>>>>>>>"+usuario.toString());
        usuariosRepository.save(usuario);
        return new ResponseEntity<>("Registro de usuario exitoso", HttpStatus.OK);
    }
    

       // Método para poder logear un usuario y obtener un token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DtoLogin dtoLogin) {
       Authentication authentication = authenticationManager.authenticate( 
            new UsernamePasswordAuthenticationToken(dtoLogin.getUsername(), dtoLogin.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generarToken(authentication);
        return new ResponseEntity<>(new DtoAuthRespuesta(token),HttpStatus.OK);
        

    }



}
