package com.marketplace.processamento.consumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.processamento.model.constantes.Fila;
import com.marketplace.processamento.model.dto.RetornoPagamentoDTO;
import com.marketplace.processamento.model.dto.VendaDTO;
import com.marketplace.processamento.service.MQService;
import com.marketplace.processamento.service.PagamentoService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Component
public class ProcessarVendaListener {

    @Autowired
    private PagamentoService pagamentoService;

    @Autowired
    private MQService mqService;

    @HystrixCommand(fallbackMethod = "republicarVenda")
    @RabbitListener(queues= Fila.VENDA_AGUARDANDO)
    public void vendaListener (Message message) throws UnsupportedEncodingException, JsonProcessingException {
        String json = new String(message.getBody(), "UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        VendaDTO vendaDTO = mapper.readValue(json, VendaDTO.class);

        RetornoPagamentoDTO retornoPagamento = pagamentoService.processarPagamento(vendaDTO.getPagamento());

        if (retornoPagamento.getCodigoRetorno() == 0) {
            mqService.enviarParaFilaVendaFinalizada(vendaDTO);
        } else {
            mqService.enviarParaFilaVendaFalha(vendaDTO);
        }
    }

    public void republicarVenda(Message message) throws JsonParseException, JsonMappingException, IOException {
        System.out.println("Republicando mensagem...");
        mqService.retornarParaFila(message);
    }
}
