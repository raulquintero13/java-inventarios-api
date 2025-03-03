package com.inventarios.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name =  "id_usuario")
	private Long idUsuario;
	
	private String username;
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id_usuario")
	, inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_role"))
	private List<Role> roles = new ArrayList<>();

	
	
	public Usuario() {
		super();
	}

	public Usuario(Long idUsuario, String username, String password, List<Role> roles) {
		super();
		this.idUsuario = idUsuario;
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", username=" + username + ", password=" + password + ", roles="
				+ roles + "]";
	}
	
	
	
	
	
	

}
