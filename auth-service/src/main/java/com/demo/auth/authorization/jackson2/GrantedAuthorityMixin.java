package com.demo.auth.authorization.jackson2;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.boot.jackson.JsonMixin;
import org.springframework.security.core.GrantedAuthority;

@JsonMixin(GrantedAuthority.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
@JsonDeserialize(using = GrantedAuthorityDeserializer.class)
@JsonAutoDetect(
		fieldVisibility = JsonAutoDetect.Visibility.NONE,
		getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class GrantedAuthorityMixin {
	@JsonCreator
	public GrantedAuthorityMixin(@JsonProperty("authority") String authority) {
	}
}
