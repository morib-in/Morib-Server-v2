package org.morib.server.global.common.util;

import com.google.common.net.InternetDomainName;
import org.morib.server.global.exception.InvalidURLException;
import org.morib.server.global.message.ErrorMessage;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class UrlUtils {

    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$"
    );

    public static String getTopDomain(String originalUrl) {
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            throw new InvalidURLException(ErrorMessage.URL_IS_EMPTY);
        }

        String urlToParse = originalUrl.trim();

        if (!urlToParse.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
             if (!urlToParse.startsWith("//")) {
                 urlToParse = "https://" + urlToParse;
             } else {
                 urlToParse = "https:" + urlToParse;
             }
        }

        try {
            URI uri = new URI(urlToParse);
            String host = uri.getHost();

            if (host == null || host.isEmpty()) {
                return normalizeUrl(originalUrl);
            }

            if ("localhost".equalsIgnoreCase(host)) {
                return "localhost";
            }

            if (IP_ADDRESS_PATTERN.matcher(host).matches()) {
                return host;
            }

            InternetDomainName domainName = InternetDomainName.from(host);
            if (domainName.hasPublicSuffix()) {
                return domainName.topPrivateDomain().toString();
            } else {
                return normalizeHost(host);
            }

        } catch (URISyntaxException | IllegalArgumentException e) {
            return normalizeUrl(originalUrl);
        }
    }

    private static class HostPathPair {
        final String host;
        final String path;

        HostPathPair(String host, String path) {
            this.host = host;
            this.path = path;
        }
    }

    public static String normalizeUrl(String originalUrl) {
        if (originalUrl == null || originalUrl.trim().isEmpty())
            throw new InvalidURLException(ErrorMessage.URL_IS_EMPTY);

        String processedUrl = originalUrl.trim();
        processedUrl = removeProtocol(processedUrl);
        processedUrl = removeQueryAndFragment(processedUrl);

        HostPathPair parts = splitAndNormalizeHostPath(processedUrl);

        if (parts.path.isEmpty()) {
            return parts.host;
        } else {
            return parts.host + parts.path;
        }
    }

    private static String removeProtocol(String url) {
        int protocolIndex = url.indexOf("://");
        if (protocolIndex != -1) {
            return url.substring(protocolIndex + 3);
        }
        return url;
    }

    private static String removeQueryAndFragment(String url) {
        String result = url;
        int queryIndex = result.indexOf('?');
        if (queryIndex != -1) {
            result = result.substring(0, queryIndex);
        }
        int fragmentIndex = result.indexOf('#');
        if (fragmentIndex != -1) {
            result = result.substring(0, fragmentIndex);
        }
        return result;
    }

    private static HostPathPair splitAndNormalizeHostPath(String urlWithoutProtocolQuery) {
        String hostPart;
        String pathPart = "";
        int firstSlashIndex = urlWithoutProtocolQuery.indexOf('/');

        if (firstSlashIndex != -1) {
            hostPart = urlWithoutProtocolQuery.substring(0, firstSlashIndex);
            pathPart = urlWithoutProtocolQuery.substring(firstSlashIndex);
        } else {
            hostPart = urlWithoutProtocolQuery;
        }

        String normalizedHost = normalizeHost(hostPart);
        String normalizedPath = normalizePath(pathPart);

        return new HostPathPair(normalizedHost, normalizedPath);
    }

    private static String normalizeHost(String host) {
        if (host == null) return "";
        String normalized = host.toLowerCase();
        if (normalized.startsWith("www.")) {
            normalized = normalized.substring(4);
        }
        return normalized;
    }

    private static String normalizePath(String path) {
        if (path == null) return "";
        if (path.equals("/")) {
            return "";
        } else if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static String normalizeUrlForFavicon(String urlString) {
        try {
            // 프로토콜이 없으면 "https://" 추가
            if (!urlString.matches("^(http://|https://).*")) {
                urlString = "https://" + urlString;
            }

            URL url = new URL(urlString);

            // 항상 https로 강제 (요구사항에 따라 변경 가능)
            String scheme = "https";

            // 호스트를 소문자로 변환하고 "www." 접두어 제거
            String host = url.getHost().toLowerCase();
            if (host.startsWith("www.")) {
                host = host.substring(4);
            }

            // 포트: 기본 포트가 아니라면 포함 (예: https의 기본 포트 443)
            int port = url.getPort();
            String portPart = "";
            if (port != -1 && port != 443) {
                portPart = ":" + port;
            }

            // 경로: "/"인 경우나 빈 문자열은 빈 문자열로 처리하고, 나머지 경우 끝의 "/" 제거
            String path = url.getPath();
            if (path == null || path.equals("/") || path.isEmpty()) {
                path = "";
            } else if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }

            return scheme + "://" + host + portPart + path;
        } catch (Exception e) {
            return null;
        }
    }

}