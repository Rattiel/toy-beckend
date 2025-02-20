package com.demo.auth.authorization.jackson2;

import com.demo.auth.authorization.Account;
import com.demo.auth.authorization.MfaVerificationMethod;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Set;

public class AccountDeserializer extends JsonDeserializer<Account> {
    private static final TypeReference<Set<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<>() {
    };

    @Override
    public Account deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode jsonNode = mapper.readTree(parser);
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        String username = readJsonNode(jsonNode, "username").asText();
        String password = passwordNode.asText("");
        String email = readJsonNode(jsonNode, "email").asText();
        String phone = readJsonNode(jsonNode, "phone").asText();
        String firstName = readJsonNode(jsonNode, "firstName").asText();
        String lastName = readJsonNode(jsonNode, "lastName").asText();
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
        boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
        boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
        boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();
        String mfaVerificationMethod = readJsonNode(jsonNode, "mfaVerificationMethod").asText();
        Set<? extends GrantedAuthority> authorities = mapper.convertValue(
                jsonNode.get("authorities"),
                SIMPLE_GRANTED_AUTHORITY_SET
        );
        return new Account(
                username,
                password,
                email,
                phone,
                firstName,
                lastName,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                MfaVerificationMethod.valueOf(mfaVerificationMethod),
                authorities
        );
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
