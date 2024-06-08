package dev.erique.loja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.erique.loja.enums.BillingType;
import dev.erique.loja.infra.handler.ResponseFactory;
import dev.erique.loja.infra.handler.exception.RecordNotFoundException;
import dev.erique.loja.infra.security.jwt.JwtProperties;
import dev.erique.loja.integracao.asaas.NovaCobrancaDTO;
import dev.erique.loja.integracao.asaas.NovaCobrancaRequest;
import dev.erique.loja.integracao.asaas.RetornoPagamento;
import dev.erique.loja.integracao.asaas.webHooks.PaymentNotificationDto;
import dev.erique.loja.model.carrinho.CarrinhoEntity;
import dev.erique.loja.model.carrinho.ProdutoQuantidade;
import dev.erique.loja.model.compraEfetuada.CompraEfetuadaEntity;
import dev.erique.loja.model.cliente.ClienteEntity;
import dev.erique.loja.model.produto.ProdutoEntity;
import dev.erique.loja.repository.CarrinhoRepository;
import dev.erique.loja.repository.ClienteRepository;
import dev.erique.loja.repository.CompraEfetuadaRepository;
import dev.erique.loja.repository.ProdutoRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private static final String MEU_TOKEN = "token";

    private static final String ASAAS_API_KEY = "key";

    private static final String MESSAGE = "Id não encontrado";
    private static final String MESSAGE2 = "use outro id";

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CompraEfetuadaRepository compraEfetuadaRepository;

    @Autowired
    private JwtProperties jwtProperties;

    public Object webHook(PaymentNotificationDto paymentNotificationDto, String token) {
        if (token == MEU_TOKEN){
            return null;
        }else {
            return null;
        }
    }

    public Float obterSomaValoresPorIds(List<ProdutoQuantidade> produtos) {
        return (float) produtos.stream()
                .mapToDouble(produtoQuantidade -> {
                    Optional<ProdutoEntity> produtoOptional = produtoRepository.findById(produtoQuantidade.getId());
                    if (produtoOptional.isPresent()) {
                        ProdutoEntity produto = produtoOptional.get();
                        int quantidade = produtoQuantidade.getQuantidade();
                        float preco = produto.getPreco();
                        return (quantidade * preco);
                    } else {
                        return 0;
                    }
                })
                .sum();
    }

    public ClienteEntity buscarPorId(Integer id) {
        ClienteEntity cliente = repository.findById(id).orElse(null);
        if (cliente == null) {
            throw new RecordNotFoundException("Usuário","tente outro Id");
        }
        return cliente;
    }

    public Object createCobranca(NovaCobrancaRequest cobranca, String token) throws JsonProcessingException {

        Integer id = findIdByLogin(extractSubjectFromToken(token));

        String idCus;
        Optional<ClienteEntity> entity = repository.findById(id);
        if (entity.isPresent()){
            idCus = entity.get().getCustomerId();
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                    MESSAGE,MESSAGE2));
        }

        String url = "https://sandbox.asaas.com/api/v3/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "application/json");
        headers.add("content-type", "application/json");
        headers.add("access_token", ASAAS_API_KEY);

        NovaCobrancaDTO novaCobrancaDTO = new NovaCobrancaDTO();
        BeanUtils.copyProperties(cobranca, novaCobrancaDTO);

        CarrinhoEntity carrinhoEntity = new CarrinhoEntity();

        BeanUtils.copyProperties(cobranca, carrinhoEntity);

        carrinhoRepository.save(carrinhoEntity);

        novaCobrancaDTO.setCustomer(idCus);
        novaCobrancaDTO.setValue(obterSomaValoresPorIds(carrinhoEntity.getProdutos()));
        novaCobrancaDTO.setDescription(cobranca.getDescription());

        if (cobranca.getInstallmentCount() > 0 && cobranca.getBillingType() == BillingType.CREDIT_CARD){
            int parcelas = cobranca.getInstallmentCount();

            Float precoProduto = obterSomaValoresPorIds(carrinhoEntity.getProdutos());

            Float taxaVista = 0.0339F;
            Float taxaParcelas2a6 = 0.0354F;
            Float taxaParcelas7a12 = 0.0446F;

            Float valorParcela;

            Float aumentoNecessario;

            //TODO: verificar se aplica a taxa avista quando as parcelas não é preenchida

            if (parcelas < 2 && cobranca.getBillingType() == BillingType.CREDIT_CARD){
                aumentoNecessario = taxaVista*precoProduto;
                precoProduto = precoProduto+aumentoNecessario;

                valorParcela = precoProduto/parcelas;

                novaCobrancaDTO.setValue(precoProduto);
                novaCobrancaDTO.setInstallmentValue(valorParcela);
                novaCobrancaDTO.setInstallmentCount(parcelas);
            }

            else if (parcelas >= 2 && parcelas <= 6){
                aumentoNecessario = taxaParcelas2a6*precoProduto;
                precoProduto = precoProduto+aumentoNecessario;
                valorParcela = precoProduto/parcelas;

                novaCobrancaDTO.setValue(precoProduto);
                novaCobrancaDTO.setInstallmentValue(valorParcela);
                novaCobrancaDTO.setInstallmentCount(parcelas);
            }

            else if (parcelas >= 7 && parcelas <= 12){
                aumentoNecessario = taxaParcelas7a12*precoProduto;
                precoProduto = precoProduto+aumentoNecessario;
                valorParcela = precoProduto/parcelas;

                novaCobrancaDTO.setValue(precoProduto);
                novaCobrancaDTO.setInstallmentValue(valorParcela);
                novaCobrancaDTO.setInstallmentCount(parcelas);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFactory.error("não foi possivel definir o numero de parcelas",""));
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(novaCobrancaDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter cobrança para JSON");
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);


        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            ObjectMapper mapperReturn = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            RetornoPagamento retornoApi = mapperReturn.readValue(responseEntity.getBody(), RetornoPagamento.class);
            RetornoPagamento retornoPagamento = new RetornoPagamento();

            BeanUtils.copyProperties(retornoApi, retornoPagamento);
            retornoPagamento.setDescription(cobranca.getDescription());

            CompraEfetuadaEntity compraEfetuadaEntity = new CompraEfetuadaEntity();
            BeanUtils.copyProperties(retornoApi, compraEfetuadaEntity);
            compraEfetuadaEntity.setIdPay(retornoApi.getId());
            compraEfetuadaEntity.setBillingType(cobranca.getBillingType());
            compraEfetuadaEntity.setCliente(buscarPorId(id));
            compraEfetuadaEntity.setCarrinhoEntity(carrinhoEntity);

            compraEfetuadaRepository.save(compraEfetuadaEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.create(retornoPagamento,"Cobrança gerada","Você já pode acompanhar o sua compra")) ;
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFactory.error());
        }
    }

    private String extractSubjectFromToken(String authorizationHeader) {
        try {
            String token = authorizationHeader.replace(jwtProperties.getPrefix(), "");

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getKey())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findIdByLogin(String login) {
        ClienteEntity manager = findByLogin(login);
        return manager.getId();
    }

    public ClienteEntity findByLogin(String login) {
        Optional<ClienteEntity> optionalCliente = Optional.ofNullable(repository.findByEmail(login));
        if (optionalCliente.isPresent()) {
            return optionalCliente.get();
        } else {
            throw new RecordNotFoundException("Cliente", "Login");
        }
    }

}
