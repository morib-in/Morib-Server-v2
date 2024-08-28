package org.morib.server.domain.allowedSite.application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FetchTabNameServiceImpl implements FetchTabNameService {

    @Override
    public String fetch(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.title();
        }
        catch (IOException e) {
            throw new IllegalArgumentException("유효하지 않은 url입니다.");
        }
    }
}
