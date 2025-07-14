package org.morib.server.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "apple")
@Getter
@Setter
public class AppleProperties {

	private String path;
	private String url;
	private String cid;
	private String tid;
	private String kid;

}
