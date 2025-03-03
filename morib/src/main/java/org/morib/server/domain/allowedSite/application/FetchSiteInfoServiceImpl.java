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

            Element siteElement = doc.selectFirst("meta[property=og.article.author]");
            String siteName;

            if (siteElement != null && !siteElement.attr("content").isEmpty()) {
                siteName = siteElement.attr("content");
            } else {
                siteElement = doc.selectFirst("meta[property=og:title]");
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

            System.out.println("사이트 이름: " + siteName);
            System.out.println("페이지 제목: " + pageName);
            System.out.println("파비콘 URL: " + favicon);

            return AllowedSiteVo.of(favicon, siteName, pageName, url);
        }
        catch (IOException e) {
            throw new InvalidURLException(ErrorMessage.INVALID_URL);
        }
        catch (Exception e) {
            throw new IllegalStateException("요청에 오류가 발생했습니다.");
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
}
