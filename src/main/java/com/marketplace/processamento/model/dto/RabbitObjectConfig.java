package com.marketplace.processamento.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.marketplace.processamento.util.JsonUtils.converterObjetoParaJson;

@Getter
@Setter
@Builder
public class RabbitObjectConfig extends Exception{

    private Object object;
    private String queueName;

    public String getMesssage() {
        return converterObjetoParaJson(object);
    }
}
