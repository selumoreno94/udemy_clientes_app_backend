package com.udemycourse.springboot.backend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.udemycourse.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {

	/**
	 * 
	 * @return
	 */
	public List<Cliente> findAll();

	/**
	 * 
	 * @param clienteId
	 * @return
	 */
	public Cliente findById(Long clienteId);
	
	/**
	 * 
	 * @param cliente
	 * @return
	 */
	public Cliente save(Cliente cliente);
	
	/**
	 * 
	 * @param clienteId
	 */
	public void delete(Long clienteId);
	
	/**
	 * 
	 * @param pageable
	 * @return
	 */
	public Page<Cliente> findAll(Pageable pageable);
}
