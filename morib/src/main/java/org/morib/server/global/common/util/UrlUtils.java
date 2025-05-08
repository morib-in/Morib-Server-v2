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

    // 상위 도메인의 Full URL 반환. maps.naver.com -> https://naver.com
    public static String getTopDomainUrl(String originalUrl) {
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
                return "http://localhost"; // 또는 http://localhost 등 요구사항에 맞게
            }

            // IP 주소 처리
            if (IP_ADDRESS_PATTERN.matcher(host).matches()) {
                return "https://" + host; // IP 주소에 https 붙여 반환
            }

            // Guava를 사용하여 도메인 분석
            InternetDomainName domainName = InternetDomainName.from(host);

            // 공개 접미사(public suffix)를 가지고 있는지 확인 (예: .com, .co.uk)
            if (domainName.hasPublicSuffix()) {
                // 최상위 등록 도메인 추출 (예: naver.com, google.co.uk)
                InternetDomainName topPrivateDomain = domainName.topPrivateDomain();
                // 추출된 도메인 문자열에 https:// 추가하여 반환
                return "https://" + topPrivateDomain.toString();
            } else {
                // 공개 접미사가 없는 경우 (예: 내부망 호스트 이름)
                // 이 경우, 호스트 이름 자체에 https:// 를 붙여 반환하거나 다른 규칙 적용 가능
                return "https://" + normalizeHost(host);
            }

        } catch (URISyntaxException e) {
            return normalizeUrl(originalUrl);
        } catch (IllegalArgumentException e) {
            return normalizeUrl(originalUrl);
        }
    }

    // 상위 도메인만을 호출 maps.naver.com -> naver
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
                InternetDomainName topPrivateDomain = domainName.topPrivateDomain();
                if (!topPrivateDomain.parts().isEmpty()) {
                    return topPrivateDomain.parts().get(0);
                } else {
                    return topPrivateDomain.toString();
                }
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