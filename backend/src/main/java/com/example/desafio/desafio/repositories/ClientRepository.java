package com.example.desafio.desafio.repositories;

import com.example.desafio.desafio.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
  public interface ClientRepository extends JpaRepository<Client, Long> {
}

