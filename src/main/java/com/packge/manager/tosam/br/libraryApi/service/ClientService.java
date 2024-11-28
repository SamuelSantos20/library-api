package com.packge.manager.tosam.br.libraryApi.service;

import com.packge.manager.tosam.br.libraryApi.model.Client;
import com.packge.manager.tosam.br.libraryApi.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;


    private final PasswordEncoder passwordEncoder;

    public Client salvar(Client client) {

        String encode = passwordEncoder.encode(client.getClientSecret());

        client.setClientSecret(encode);

        return clientRepository.save(client);

    }

    public Client obterPorId(String clientID) {

        return clientRepository.findByClientId(clientID);

    }


}
