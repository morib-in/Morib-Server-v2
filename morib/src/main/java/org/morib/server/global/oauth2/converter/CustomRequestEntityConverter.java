package org.morib.server.global.oauth2.converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.morib.server.global.config.AppleProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.nimbusds.jose.util.IOUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

	private final OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

	@Value("${jwt.expiration.access-token}")
	private String jwtExpireAt;

	private final String path;
	private final String keyId;
	private final String teamId;
	private final String clientId;
	private final String url;

	public CustomRequestEntityConverter(AppleProperties appleProperties) {
		this.defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
		this.path = appleProperties.getPath();
		this.keyId=appleProperties.getKey_id();
		this.clientId= appleProperties.getClient_id();
		this.url = appleProperties.getUrl();
		this.teamId = appleProperties.getTeam_id();
	}

	@Override
	public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {

		RequestEntity<?> entity= defaultConverter.convert(req);

		String registrationId = req.getClientRegistration().getRegistrationId();

		MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();

		if(registrationId.contains("apple")) {
			try {
				params.add("client_secret", createClientSecret());
				log.info("now in apple social login request params {}", params);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
	}

	private String createClientSecret() throws IOException{

		Map<String, Object> jwtHeader = new HashMap<>();

		jwtHeader.put("kid", keyId);
		jwtHeader.put("alg", "ES256");


		return Jwts.builder()
			.setHeaderParams(jwtHeader)
			.setIssuer(teamId)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 + 5)))
			.setAudience(url)
			.setSubject(clientId)
			.signWith(getPrivateKey(), SignatureAlgorithm.ES256)
			.compact();
	}

	private PrivateKey getPrivateKey() throws IOException {

		ClassPathResource resource = new ClassPathResource(path);

		InputStream in = resource.getInputStream();
		PEMParser pemParser = new PEMParser(
			new StringReader(IOUtils.readInputStreamToString(in, StandardCharsets.UTF_8)));

		PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter();

		return converter.getPrivateKey(object);
	}
}
