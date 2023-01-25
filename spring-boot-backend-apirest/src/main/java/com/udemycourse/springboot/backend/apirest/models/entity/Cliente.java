package com.udemycourse.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Entity
@Table(name="clientes")
public class Cliente implements Serializable{

	private static final long serialVersionUID = 7568726943577875144L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	@NonNull
	private Long id;
	
	@Column
	@NonNull
	private String nombre;
	
	@Column
	@NonNull
	private String apellido;
	
	@Column
	@NonNull
	private String email;
	
	@Column
	@Nullable
	private Integer edad;
	
	@Column(name = "create_at")
	@NonNull
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	@Override
	public String toString() {
		return "Cliente [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", email=" + email + ", edad="
				+ edad + ", createAt=" + createAt + "]";
	}
	public Cliente() {
		
	}
	public Cliente(Long id, String nombre, String apellido, String email, Integer edad, Date createAt) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.edad = edad;
		this.createAt = createAt;
	}
	
	@PrePersist // ESTA ANOTATION AUTOMATIZA FECHAS A LA HORA DE PERSISTIR, HACIENDO QUE NO HAGA FALTA INDICAR LA FECHA DE CREACION
	private void prePersis() {
		this.createAt = new Date();
	}
	
}
