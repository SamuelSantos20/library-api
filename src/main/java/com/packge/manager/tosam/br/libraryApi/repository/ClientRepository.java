package com.packge.manager.tosam.br.libraryApi.repository;

import com.packge.manager.tosam.br.libraryApi.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client , UUID> {
    Client findByClientId(String clientID);
}
