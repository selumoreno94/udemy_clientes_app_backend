package com.udemycourse.springboot.backend.apirest.models.services;

import java.util.List;

import com.udemycourse.springboot.backend.apirest.models.dao.IClienteDao;
import com.udemycourse.springboot.backend.apirest.models.entity.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	
	//@Override
	// CrudRepository (heredado de clienteDao, ya tiene la transactional, pero se puede mantener aqui para mas control)
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteDao.findAll(pageable);
	}

	//@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long clienteId) {
		return clienteDao.findById(clienteId).orElse(null);
	}

	//@Override
	@Transactional
	public Cliente save(Cliente cliente) {
		return clienteDao.save(cliente);
	}

	//@Override
	@Transactional
	public void delete(Long clienteId) {
		clienteDao.deleteById(clienteId);
	}

}
