package evertonsavio.webservice.api.ResourceServer.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import evertonsavio.webservice.api.ResourceServer.model.TokenResponse;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping
    public Map<String, Object> getToken(@AuthenticationPrincipal Jwt jwt){

        return Collections.singletonMap("principal", jwt);
    }

    @GetMapping("/try")
    public ResponseEntity testApp() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //headers.add("PRIVATE-TOKEN", "xyz");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", "admin-cli");
        map.add("client_secret", "ae7c033f-99a8-4ea3-93b5-6b609b7aa72b");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<TokenResponse> response = restTemplate.exchange("http://localhost:8080/auth/realms/master/protocol/openid-connect/token",
                HttpMethod.POST,
                entity, TokenResponse.class);

        System.out.println(response.getBody().getAccess_token());
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccess_token(response.getBody().getAccess_token());

       /* HttpHeaders createUserHeaders = new HttpHeaders();
        createUserHeaders.setContentType(MediaType.APPLICATION_JSON);
        createUserHeaders.add("Authorization", "Bearer "+ tokenResponse.getAccess_token());

        MultiValueMap<String, String> properties = new LinkedMultiValueMap<>();
        properties.add("firstName", "Sergey");
        properties.add("lastName", "Kargopolov");
        properties.add("email", "test@test.com");
        properties.add("enabled", "true");
        properties.add("username", "app-user");

        HttpEntity<String> request = new HttpEntity<>(properties.toString(), createUserHeaders);
        String res = restTemplate.postForObject("http://localhost:8080/auth/admin/realms/ms-users/users", request, String.class);

        System.out.println(res);*/

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);

    }
}
