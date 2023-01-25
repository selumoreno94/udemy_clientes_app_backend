package com.udemycourse.springboot.backend.apirest.models.controllers;

import java.util.List;

import com.udemycourse.springboot.backend.apirest.models.entity.Cliente;
import com.udemycourse.springboot.backend.apirest.models.services.IClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	
	@GetMapping("/clientes")
	//@ResponseStatus(code = HttpStatus.OK)
	public List<Cliente> findAll() {
		return this.clienteService.findAll();
	}
	
	@GetMapping("/clientes/{clienteId}")
	//@ResponseStatus(code = HttpStatus.OK)
	private Cliente showClientById(@PathVariable Long clienteId) {
		return this.clienteService.findById(clienteId);
	}
	
	@PostMapping("/clientes")
	@ResponseStatus(code = HttpStatus.CREATED)
	private Cliente createCliente(@RequestBody Cliente cliente) {
		//cliente.setCreateAt(new Date()); --> Forma fea (forma chula con el @PrePersist en el modelo)
		return this.clienteService.save(cliente);
	}
	
	@PutMapping("/clientes/{clienteId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	private Cliente updateCliente(@RequestBody Cliente clienteUpdated, @PathVariable Long clienteId) {
		Cliente clienteActual = this.clienteService.findById(clienteId);
		clienteActual.setNombre(clienteUpdated.getNombre());
		clienteActual.setApellido(clienteUpdated.getApellido());
		clienteActual.setEmail(clienteUpdated.getEmail());
		clienteActual.setEdad(clienteUpdated.getEdad());
		
		return this.clienteService.save(clienteActual);
	}
	
	@DeleteMapping("/clientes/{clienteId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	private void deleteCliente(@PathVariable Long clienteId) {
		this.clienteService.delete(clienteId);
	}
	
	
}
