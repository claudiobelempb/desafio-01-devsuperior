package com.example.desafio.desafio.services;

import com.example.desafio.desafio.dto.ClientDTO;
import com.example.desafio.desafio.entities.Client;

import com.example.desafio.desafio.repositories.ClientRepository;
import com.example.desafio.desafio.services.exceptions.DataBaseException;
import com.example.desafio.desafio.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ClientService {

  @Autowired
  private ClientRepository clientRepository;

  @Transactional(readOnly = true)
  public Page<ClientDTO> index(PageRequest pageRequest) {
    Page<Client> clients = clientRepository.findAll(pageRequest);
    return clients.map(ClientDTO::new);
  }

  @Transactional(readOnly = true)
  public ClientDTO show(Long id) {
    Optional<Client> obj = clientRepository.findById(id);
    Client client = obj.orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    return new ClientDTO(client);
  }

  @Transactional
  public ClientDTO create(ClientDTO clientDTO) {
    Client client = new Client();
    copyDtoToClient(clientDTO, client);
    client = clientRepository.save(client);
    return new ClientDTO(client);
  }

  @Transactional
  public ClientDTO update(Long id, ClientDTO clientDTO) {
    try {
      Client client = clientRepository.getOne(id);
      copyDtoToClient(clientDTO, client);
      return new ClientDTO(client);
    } catch (EntityNotFoundException e) {
      throw new DataBaseException("Id not found" + id);
    }
  }

  public void delete(Long id) {
    try {
      clientRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResourceNotFoundException("Id not found" + id);
    } catch (DataIntegrityViolationException e) {
      throw new DataBaseException("Integrity violation");
    }
  }

  private void copyDtoToClient(ClientDTO clientDTO, Client client){
    client.setName(clientDTO.getName());
    client.setCpf(clientDTO.getCpf());
    client.setIncome(clientDTO.getIncome());
    client.setBirthDate(clientDTO.getBirthDate());
    client.setChildren(clientDTO.getChildren());
  }
}
