package dev.erique.loja.infra;

import dev.erique.loja.model.carrinho.CarrinhoDto;
import dev.erique.loja.model.carrinho.CarrinhoEntity;
import dev.erique.loja.model.compraEfetuada.CompraEfetuadaDto;
import dev.erique.loja.model.compraEfetuada.CompraEfetuadaEntity;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public static CompraEfetuadaDto convertCompraEfetuadaEntityToDto(CompraEfetuadaEntity entity) {
        CompraEfetuadaDto dto = new CompraEfetuadaDto();
        dto.setIdPay(entity.getIdPay());
        dto.setValue(entity.getValue());
        dto.setStatus(entity.getStatus());
        dto.setBillingType(entity.getBillingType());
        dto.setStatusPayment(entity.getStatusPayment());
        dto.setLinkPayment(entity.getLinkPayment());
        dto.setCarrinhoEntity(convertCarrinhoEntityToDto(entity.getCarrinhoEntity()));
        return dto;
    }

    public static CarrinhoDto convertCarrinhoEntityToDto(CarrinhoEntity entity) {
        CarrinhoDto dto = new CarrinhoDto();
        dto.setProdutos(entity.getProdutos());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static List<CompraEfetuadaDto> convertListCompraEfetuadaEntityToListDto(List<CompraEfetuadaEntity> entities) {
        return entities.stream()
                .map(Converter::convertCompraEfetuadaEntityToDto)
                .collect(Collectors.toList());
    }

}
