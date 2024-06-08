package dev.erique.loja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erique.loja.infra.handler.ResponseFactory;
import dev.erique.loja.integracao.asaas.CadastroCliente;
import dev.erique.loja.model.cliente.ClienteEntity;
import dev.erique.loja.model.cliente.ClienteRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AssasService {

    private static final String ASAAS_API_URL = "https://sandbox.asaas.com/api/v3/customers";
    private static final String ASAAS_API_KEY = "key";

    private static final String ACCEPT = "Accept";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCESS_TOKEN = "access_token";

    private static final String MESSAGE1 = "Erro ao compreender o retorno da API";
    private static final String MESSAGE2 = "Contate o suporte";
    private static final String MESSAGE3 = "Id não encontrado";
    private static final String MESSAGE4 = "Use outro id";

    public int getCustomerCount(String cpfCnpj) { //verifica se tem um cliente com o cpf cadastrado
        String url = ASAAS_API_URL+"?cpfCnpj="+cpfCnpj;

        HttpHeaders headers = new HttpHeaders();
        headers.add(ACCEPT, APPLICATION_JSON);
        headers.add(ACCESS_TOKEN, ASAAS_API_KEY);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                Map<String, Object> responseMap = new ObjectMapper().readValue(responseEntity.getBody(), Map.class);

                return (int) responseMap.get("totalCount");
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFactory.error(400,MESSAGE1,MESSAGE2)).getStatusCodeValue();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFactory.error(400,MESSAGE1,MESSAGE2)).getStatusCodeValue();
        }
    }

    public String createCustomer(ClienteRequest request){ //cria um cliente

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(ACCESS_TOKEN, ASAAS_API_KEY);

        CadastroCliente cadastroRequest = new CadastroCliente();

        BeanUtils.copyProperties(request,cadastroRequest);

        HttpEntity<CadastroCliente> requestEntity = new HttpEntity<>(cadastroRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(ASAAS_API_URL, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return String.valueOf(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseFactory.error(400,
                    MESSAGE1,MESSAGE2)).getStatusCodeValue());
        }

    }


    public ResponseEntity<String> deleteCustomer(ClienteEntity entity) { //deleta um cliente

            String idCus = entity.getCustomerId();

            String url = String.format(ASAAS_API_URL+"/%s", idCus);

            HttpHeaders headers = new HttpHeaders();
            headers.add(ACCEPT, APPLICATION_JSON);
            headers.add(ACCESS_TOKEN, ASAAS_API_KEY);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity;
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.valueOf(ResponseFactory.error(404,
                        MESSAGE3, MESSAGE4)));
            }
    }


}
