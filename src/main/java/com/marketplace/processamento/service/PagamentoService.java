package com.marketplace.processamento.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.processamento.model.dto.PagamentoDTO;
import com.marketplace.processamento.model.dto.RetornoPagamentoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Service
public class PagamentoService {

    private final RestTemplate restTemplate;

    @Value("${pagamento.api.url}")
    private String urlValidaPagamento = "";

    @Value("${pagamento.api.endpoint.validar}")
    private String endpointValidarPagamento = "";

    private final ObjectMapper mapper;

    public PagamentoService(RestTemplate restTemplate,ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.mapper= mapper;
    }

    public RetornoPagamentoDTO processarPagamento (PagamentoDTO pagamento) throws JsonProcessingException {
        final String url = urlValidaPagamento + endpointValidarPagamento;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(pagamento), headers);
        //TODO
        //ResponseEntity<RetornoPagamentoDTO> response = restTemplate.postForEntity(url, request, RetornoPagamentoDTO.class);
        RetornoPagamentoDTO response = new RetornoPagamentoDTO();
        Random r = new Random();
        response.setCodigoRetorno(r.nextInt(2));
        return response;
    }
}
