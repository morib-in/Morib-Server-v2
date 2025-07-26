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
	private String client_id;
	private String team_id;
	private String key_id;
	private String bundle_id;

}
