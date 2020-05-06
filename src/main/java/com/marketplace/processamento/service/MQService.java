package com.marketplace.processamento.service;

import com.marketplace.processamento.model.dto.RabbitObjectConfig;
import com.marketplace.processamento.model.dto.VendaDTO;
import com.marketplace.processamento.model.constantes.Fila;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MQService {

    private static RabbitTemplate rabbitTemplate;

    public MQService(RabbitTemplate template){
        rabbitTemplate = template;
    }

    private void enviarParaFila(RabbitObjectConfig config) {
        rabbitTemplate.convertAndSend(config.getQueueName(), config.getMesssage());
    }

    public void retornarParaFila(Message message) {
        rabbitTemplate.convertAndSend(Fila.VENDA_AGUARDANDO, message);
    }

    public void enviarParaFilaVendaFinalizada (VendaDTO venda) {
        enviarParaFila(RabbitObjectConfig.builder()
                .object(venda)
                .queueName(Fila.VENDA_FINALIZADO)
                .build());
    }

    public void enviarParaFilaVendaFalha (VendaDTO venda) {
        enviarParaFila(RabbitObjectConfig.builder()
                .object(venda)
                .queueName(Fila.VENDA_FALHA)
                .build());
    }
}
