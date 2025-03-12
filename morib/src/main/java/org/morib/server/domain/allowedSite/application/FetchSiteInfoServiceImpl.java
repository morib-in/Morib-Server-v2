package org.morib.server.domain.allowedSite.application;

import com.google.common.net.InternetDomainName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.global.exception.InvalidURLException;
import org.morib.server.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Service
public class FetchSiteInfoServiceImpl implements FetchSiteInfoService {

    @Override
    public AllowedSiteVo fetch(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .timeout(5000)
                    .get();

            Element siteElement = doc.selectFirst("meta[property=og.site_name]");
            String siteName;

            if (siteElement != null && !siteElement.attr("content").isEmpty()) {
                siteName = siteElement.attr("content");
            } else {
                siteElement = doc.selectFirst("meta[property=og:author]");
                if (siteElement != null && !siteElement.attr("content").isEmpty()) {
                    siteName = siteElement.attr("content");
                } else {

                    siteName = doc.title();
                }
            }

            Element titleElement = doc.selectFirst("meta[property=og:title]");
            String pageName = (titleElement != null) ? titleElement.attr("content") : doc.title();

            Element faviconElement = doc.selectFirst("link[rel~=(?i)^(shortcut icon|icon|shortcut)$]");
            String favicon = (faviconElement != null) ? faviconElement.absUrl("href") : "";

            if (favicon.isEmpty()) {
                favicon = url + "/favicon.ico"; // 기본 파비콘 경로
            }

            return AllowedSiteVo.of(favicon, siteName, pageName, url);
        }
        catch (IOException e) {
            String domain = getTopDomainWhenParsingFailed(url);
            return AllowedSiteVo.of("", domain, domain, url);
        }
    }

    @Override
    public String getTopDomain(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            InternetDomainName domainName = InternetDomainName.from(host);
            return domainName.topPrivateDomain().toString();
        } catch (Exception e) {
            throw new InvalidURLException(ErrorMessage.INVALID_URL);
        }
    }

    @Override
    public String getTopDomainUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            InternetDomainName domainName = InternetDomainName.from(host);
            String topPrivateDomain = domainName.topPrivateDomain().toString();
            return url.getProtocol() + "://" + topPrivateDomain + "/";
        } catch (Exception e) {
            throw new InvalidURLException(ErrorMessage.INVALID_URL);
        }
    }

    @Override
    public String getTopDomainWhenParsingFailed(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            InternetDomainName domainName = InternetDomainName.from(host);
            return domainName.topPrivateDomain().toString();
        } catch (Exception e) {
            return urlString.split("//www.")[1].split("/")[0];
        }
    }

    @Override
    public String normalizeUrl(String urlString) {
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
            if (port != -1 && port != (scheme.equals("https") ? 443 : 80)) {
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
            return urlString;
        }
    }

    @Override
    public String getDomainExceptHost(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            InternetDomainName domainName = InternetDomainName.from(host);
            return domainName.topPrivateDomain().toString();
        } catch (Exception e) {
            return urlString.split("//www.")[1].split("/")[0];
        }
    }
}
