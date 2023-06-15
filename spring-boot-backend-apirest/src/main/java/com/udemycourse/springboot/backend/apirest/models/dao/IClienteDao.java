package com.udemycourse.springboot.backend.apirest.models.dao;

import com.udemycourse.springboot.backend.apirest.models.entity.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends JpaRepository<Cliente, Long> { //CrudRepository<Cliente, Long> {

}
