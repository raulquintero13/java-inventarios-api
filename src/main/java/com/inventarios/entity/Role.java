package com.inventarios.entity;

import jakarta.persistence.*;

@Entity
@Table(name="role")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_role")
	private Long idRole;
	private String name;
	
	
	
	
	public Role() {
		super();
	}
	
	public Role(Long idRole, String name) {
		super();
		this.idRole = idRole;
		this.name = name;
	}
	
	public Long getIdRole() {
		return idRole;
	}
	public void setIdRole(Long idRole) {
		this.idRole = idRole;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
