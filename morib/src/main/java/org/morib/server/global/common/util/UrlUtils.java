package org.morib.server.global.common.util;

import com.google.common.net.InternetDomainName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlUtils {

    public static String extractTopPrivateDomain(String urlString) {
        if (urlString == null || urlString.isBlank()) {
            log.warn("입력된 URL 문자열이 null이거나 비어있습니다.");
            return "";
        }
        try {
            String urlWithProtocol = urlString;
            if (!urlString.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
                urlWithProtocol = "http://" + urlString;
            }

            URL url = new URL(urlWithProtocol);
            String host = url.getHost();

            if (host == null || host.isBlank()) {
                log.warn("URL에서 호스트를 추출할 수 없습니다: {}", urlString);
                return extractDomainFromRawUrl(urlString);
            }

            if (host.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$")) {
                log.debug("입력된 호스트가 IP 주소 형식이므로 호스트 자체를 반환합니다: {}", host);
                return host;
            }

            InternetDomainName domainName = InternetDomainName.from(host);
            if (domainName.isTopPrivateDomain()) {
                return domainName.toString();
            } else if (domainName.hasParent()) {
                return domainName.topPrivateDomain().toString();
            } else {
                log.warn("최상위 개인 도메인을 찾을 수 없습니다: {}", host);
                return extractDomainFromRawUrl(urlString);
            }
        } catch (MalformedURLException e) {
            log.warn("잘못된 형식의 URL입니다: {}, 기본 도메인 추출 시도 - {}", urlString, e.getMessage());
            return extractDomainFromRawUrl(urlString);
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.warn("URL로부터 도메인 파싱 중 오류 발생: {}, 기본 도메인 추출 시도 - {}", urlString, e.getMessage());
            return extractDomainFromRawUrl(urlString);
        } catch (Exception e) {
            log.error("도메인 추출 중 예상치 못한 오류 발생: {}", urlString, e);
            return extractDomainFromRawUrl(urlString);
        }
    }

    public static String getTopDomainUrl(String urlString) {
        if (urlString == null || urlString.isBlank()) {
            log.warn("입력된 URL 문자열이 null이거나 비어있습니다.");
            return "";
        }
        try {
            String topPrivateDomain = extractTopPrivateDomain(urlString);
            if (topPrivateDomain.isEmpty() || topPrivateDomain.equals(extractDomainFromRawUrl(urlString))) {
                return extractDomainFromRawUrl(urlString);
            }

            String urlWithProtocol = urlString;
            if (!urlString.matches("^[a-zA-Z][a-zA-Z0-9+.-]*://.*")) {
                urlWithProtocol = "http://" + urlString;
            }
            URL originalUrl = new URL(urlWithProtocol);
            String protocol = originalUrl.getProtocol();

            return protocol + "://" + topPrivateDomain + "/";

        } catch (MalformedURLException e) {
            log.warn("최상위 도메인 URL 생성 중 잘못된 형식의 URL 발생: {}, 기본 도메인 추출 시도 - {}", urlString, e.getMessage());
            return extractDomainFromRawUrl(urlString);
        } catch (Exception e) {
            log.error("최상위 도메인 URL 생성 중 예상치 못한 오류 발생: {}", urlString, e);
            return extractDomainFromRawUrl(urlString);
        }
    }

    /**
     * URL 파싱 실패 시 또는 기본적인 방법으로 URL 문자열에서 도메인 부분을 추출합니다.
     * 예: "https://www.example.com/path" -> "example.com"
     * 예: "http://example.co.uk/" -> "example.co.uk"
     * 예: "example.com" -> "example.com"
     *
     * @param urlString 원본 URL 문자열
     * @return 추출된 도메인 문자열, 실패 시 원본 문자열 반환 가능성 있음.
     */
    private static String extractDomainFromRawUrl(String urlString) {
        if (urlString == null || urlString.isBlank()) {
            return "";
        }
        String domain = urlString;
        try {
            // 프로토콜 제거 (존재하는 경우)
            if (domain.contains("://")) {
                domain = domain.split("://")[1];
            }
            // 경로 및 쿼리 파라미터 제거
            if (domain.contains("/")) {
                domain = domain.split("/")[0];
            }
            // 포트 번호 제거
            if (domain.contains(":")) {
                domain = domain.split(":")[0];
            }
            // 'www.' 제거 (존재하는 경우)
            if (domain.startsWith("www.")) {
                domain = domain.substring(4);
            }
            // 유효성 검사 추가 (간단히 . 포함 여부만 확인)
            if (!domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")){
                log.warn("Raw URL에서 유효한 도메인 추출 실패: {}, 원본 반환 시도", urlString);
                return urlString; // 유효하지 않다고 판단되면 원본 반환 (혹은 다른 처리)
            }
        } catch (Exception e) {
            log.error("Raw URL에서 도메인 추출 중 오류 발생: {}, 원본 반환", urlString, e);
            return urlString; // 예외 발생 시 안전하게 원본 반환
        }
        return domain;
    }
}
