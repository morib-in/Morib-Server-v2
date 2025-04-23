package org.morib.server.domain.allowedSite.application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.morib.server.api.allowGroupView.dto.AllowedSiteVo;
import org.morib.server.global.common.util.UrlUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class FetchSiteInfoServiceImpl implements FetchSiteInfoService {

    @Override
    public AllowedSiteVo fetchSiteMetadataFromUrl(String url) {
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
        catch (Exception e) {
            String normalizedUrl = UrlUtils.normalizeUrl(url);
            return AllowedSiteVo.of("", normalizedUrl, normalizedUrl, url);
        }
    }

}
