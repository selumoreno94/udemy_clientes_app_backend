package com.udemycourse.springboot.backend.apirest.models.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.Binding;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

import com.udemycourse.springboot.backend.apirest.models.entity.Cliente;
import com.udemycourse.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;
	
	Logger logger = LoggerFactory.getLogger(ClienteRestController.class);
	
	@GetMapping("/clientes")
	//@ResponseStatus(code = HttpStatus.OK)
	public List<Cliente> findAll() {
		return this.clienteService.findAll();
	}
	
	@GetMapping("/clientes/page/{numPag}")
	//@ResponseStatus(code = HttpStatus.OK)
	public Page<Cliente> index(@PathVariable Integer numPag) {
		return this.clienteService.findAll(PageRequest.of(numPag, 4));
	}
	
	@GetMapping("/clientes/{clienteId}")
	//@ResponseStatus(code = HttpStatus.OK)
	private ResponseEntity<?> showClientById(@PathVariable Long clienteId) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			cliente = this.clienteService.findById(clienteId);
			
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			response.put("mensaje", "Error al realizar la consulta en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		if (cliente == null) {
			response.put("mensaje", "El cliente con ID ".concat(clienteId.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	//@ResponseStatus(code = HttpStatus.CREATED)
	private ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
		//cliente.setCreateAt(new Date()); --> Forma fea (forma chula con el @PrePersist en el modelo)
		Cliente newCliente = null;
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			// OPC 1 (Usando lambda pero menos optima)
			/*List<String> errors = new ArrayList<>();
			result.getFieldErrors().forEach(errorIt -> {				
				errors.add(((FieldError)errorIt).getDefaultMessage());
			});
			response.put("errors", errors);*/
			
			// OPC 2 - OPTIMA Y LEGIBLE
			/*List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo'" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);*/
			
			//OPC 3 - MAS OPTIMA Y LIGERA
			response.put("errors", result.getFieldErrors().stream()
						.map(err -> "El campo'" + err.getField() + "' " + err.getDefaultMessage())
						.collect(Collectors.toList()));
			
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (cliente == null || (cliente.getNombre().isBlank() || cliente.getApellido().isBlank()
				|| cliente.getEmail().isBlank() || cliente.getEdad() < 1)) {
			response.put("mensaje", "El cliente a insertar tiene fallos en el formulario de creacion");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		
		try {
			newCliente = this.clienteService.save(cliente);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			response.put("mensaje", "Error al realizar el insert del cliente en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido creado con exito");
		response.put("cliente", newCliente);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{clienteId}")
	//@ResponseStatus(code = HttpStatus.CREATED)
	private ResponseEntity<?> updateCliente(@Valid @RequestBody Cliente clienteUpdated, BindingResult result, @PathVariable Long clienteId) {
		Cliente clienteActual = this.clienteService.findById(clienteId);
		Cliente clienteActualizado = null;
		Map<String, Object> response = new HashMap<>();
		
		if (result.hasErrors()) {
			response.put("errors", result.getFieldErrors().stream()
						.map(err -> "El campo'" + err.getField() + "' " + err.getDefaultMessage())
						.collect(Collectors.toList()));
			
			
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		if (clienteActual == null) {
			response.put("mensaje", "El cliente con ID ".concat(clienteId.toString()).concat(" no existe en la base de datos"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		clienteActual.setNombre(clienteUpdated.getNombre());
		clienteActual.setApellido(clienteUpdated.getApellido());
		clienteActual.setEmail(clienteUpdated.getEmail());
		clienteActual.setEdad(clienteUpdated.getEdad());
		clienteActual.setCreateAt(clienteUpdated.getCreateAt());
		
		try {
			clienteActualizado = this.clienteService.save(clienteActual);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			response.put("mensaje", "Error al realizar el update del cliente en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido actualizado con exito");
		response.put("cliente", clienteActualizado);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{clienteId}")
	//@ResponseStatus(code = HttpStatus.NO_CONTENT)
	private ResponseEntity<?> deleteCliente(@PathVariable Long clienteId) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			this.clienteService.delete(clienteId);
		} catch (DataAccessException e) {
			logger.error(e.getMessage());
			response.put("mensaje", "Error al realizar el delete del cliente en base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
}
