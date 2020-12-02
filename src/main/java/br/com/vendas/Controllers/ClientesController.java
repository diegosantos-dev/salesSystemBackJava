package br.com.vendas.Controllers;

import br.com.vendas.Domain.Entity.Cliente;
import br.com.vendas.Domain.Repositorys.ClienteRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/clientes")
public class ClientesController {

    private ClienteRepository clienteRepository;

    public ClientesController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cliente getCliente(@PathVariable Integer id) {
       return clienteRepository.findById(id)
               .map((cliente) -> {
                   return cliente;
               }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cliente não encontrado"));
    }

    @PostMapping
    public ResponseEntity postCliente(@RequestBody Cliente cliente) {
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCliente(@PathVariable Integer id) {
        clienteRepository.findById(id)
                .map( cliente -> {
                    clienteRepository.delete(cliente );
                    return cliente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente não encontrado"));
    }

    @PutMapping("{id}")
    public ResponseEntity updateCliente(@PathVariable Integer id,
                                        @RequestBody Cliente cliente) {
         return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clienteRepository.save(cliente);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping()
    public ResponseEntity findCliente(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                                .matching()
                                .withIgnoreCase()
                                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example example = Example.of(filtro, matcher);
        return ResponseEntity.ok(clienteRepository.findAll(example));
    }
}
