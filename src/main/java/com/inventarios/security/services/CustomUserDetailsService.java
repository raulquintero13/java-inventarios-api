package com.inventarios.security.services;

import org.springframework.stereotype.Service;

import com.inventarios.entity.Role;
import com.inventarios.entity.Usuario;
import com.inventarios.repository.IUserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService{


    private IUserRepository usuariosRepo;

    @Autowired
    public CustomUserDetailsService(IUserRepository usuariosRepo){
        this.usuariosRepo = usuariosRepo;
    }

    //Metodo para traernos una lista de autoridades, por medio de una lista de roles
    public Collection<GrantedAuthority> mapToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    // metodo para traernos todos sus dataos por medio de su username
    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuarios = usuariosRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(usuarios.getUsername(), usuarios.getPassword(), mapToAuthorities(usuarios.getRoles()));
        // return null;
    }


}
