package com.marketplace.processamento.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendaDTO {

    private Long compradorId;
    private Long vendedorId;
    private Calendar data;
    private PagamentoDTO pagamento;
    private List<VendaItemDTO> itens = new ArrayList<>();

}
