package org.morib.server.domain.allowedSite.application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.morib.server.global.exception.InvalidURLException;
import org.morib.server.global.message.ErrorMessage;
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
            throw new InvalidURLException(ErrorMessage.INVALID_URL);
        }
        catch (Exception e) {
            throw new IllegalStateException("요청에 오류가 발생했습니다.");
        }
    }
}
