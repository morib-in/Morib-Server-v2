package org.morib.server.domain.user.infra.type;

import lombok.Getter;

import java.util.List;

@Getter
public enum InterestArea {
    BUSINESS_OWNER_EXECUTIVE("Business Owner/Executive",List.of(
        new InterestAreaSiteVo("Google Trends", "https://trends.google.com/trends/?geo=US"),
        new InterestAreaSiteVo("Trello", "https://trello.com/en"),
        new InterestAreaSiteVo("Salesforce", "https://www.salesforce.com/kr/?ir=1"),
        new InterestAreaSiteVo("Slack", "https://slack.com/intl/ko-kr/"),
        new InterestAreaSiteVo("Dropbox", "https://www.dropbox.com/business")
    )
        ),
//    CREATOR("Creator", List.of(
//        new InterestAreaSiteVo("브런치스토리", "https://brunch.co.kr/"),
//        new InterestAreaSiteVo("위키피디아", "https://www.wikipedia.org/"),
//        new InterestAreaSiteVo("ChatGPT", "https://openai.com/index/chatgpt/"),
//        new InterestAreaSiteVo("한국어 맞춤법/문법 검사기", "https://nara-speller.co.kr/speller/"),
//        new InterestAreaSiteVo("포스타입", "https://www.postype.com/")
//    )),
    DESIGNER("Designer", List.of(
        new InterestAreaSiteVo("피그마", "https://www.figma.com/"),
        new InterestAreaSiteVo("언스플레쉬", "https://unsplash.com/ko"),
        new InterestAreaSiteVo("프리픽", "https://www.freepik.com/"),
        new InterestAreaSiteVo("핀터레스트", "https://kr.pinterest.com/"),
        new InterestAreaSiteVo("비핸스", "https://www.behance.net/"),
        new InterestAreaSiteVo("노트폴리오", "https://notefolio.net/"),
        new InterestAreaSiteVo("드리블", "https://dribbble.com/")
    )),
    MARKETER("Marketer",List.of(
        new InterestAreaSiteVo("뉴닉", "https://newneek.co/"),
        new InterestAreaSiteVo("캐릿", "https://www.careet.net/"),
        new InterestAreaSiteVo("DMC리포트", "https://www.dmcreport.co.kr/"),
        new InterestAreaSiteVo("메조 미디어", "https://www.mezzomedia.co.kr/"),
        new InterestAreaSiteVo("나스 미디어", "https://www.nasmedia.co.kr/")
    )),
    PM_PO("PM/PO", List.of(
        new InterestAreaSiteVo("디스콰이엇", "https://disquiet.io/"),
        new InterestAreaSiteVo("브런치", "https://brunch.co.kr/"),
        new InterestAreaSiteVo("노션", "https://www.notion.so/"),
        new InterestAreaSiteVo("Product Hunt", "https://www.producthunt.com/"),
        new InterestAreaSiteVo("Figma", "https://www.figma.com/ko-kr/")
    )),
    DEV("Dev", List.of(
        new InterestAreaSiteVo("ChatGPT", "https://chatgpt.com/"),
        new InterestAreaSiteVo("백준", "https://www.acmicpc.net/"),
        new InterestAreaSiteVo("GitHub", "https://github.com/"),
        new InterestAreaSiteVo("Slack", "https://slack.com/intl/ko-kr/"),
        new InterestAreaSiteVo("Stack Overflow", "https://stackoverflow.com/")
    )),
    STUDENT("Student", List.of(
        new InterestAreaSiteVo("노션", "https://www.notion.so/"),
        new InterestAreaSiteVo("카피킬러라이트", "https://www.copykiller.com/"),
        new InterestAreaSiteVo("RISS", "https://www.riss.kr/"),
        new InterestAreaSiteVo("Google Drive", "https://drive.google.com/"),
        new InterestAreaSiteVo("Google Workspace", "https://meet.google.com/")
    )),
//    ENGINEER("Engineer", List.of(
//        new InterestAreaSiteVo("Wolfram Alpha", "https://www.wolframalpha.com/"),
//        new InterestAreaSiteVo("ChatGPT", "https://chatgpt.com/"),
//        new InterestAreaSiteVo("iLovePDF", "https://www.ilovepdf.com/ko"),
//        new InterestAreaSiteVo("반도체설계교육센터", "http://www.idec.or.kr/"),
//        new InterestAreaSiteVo("Wcalc", "https://wcalc.sourceforge.net/")
//    )),
    OTHERS("Others", List.of(
        new InterestAreaSiteVo("네이버 파파고", "https://papago.naver.com/"),
        new InterestAreaSiteVo("ChatGPT", "https://chatgpt.com/"),
        new InterestAreaSiteVo("iLovePDF", "https://www.ilovepdf.com/ko"),
        new InterestAreaSiteVo("HancomDocs", "https://www.hancomdocs.com/home"),
        new InterestAreaSiteVo("Gmail", "https://mail.google.com/mail/")
    ));

    private final String interestArea;
    private final List<InterestAreaSiteVo> areaSiteVos;

    InterestArea(String interestArea, List<InterestAreaSiteVo> areaSiteVos) {
        this.interestArea = interestArea;
        this.areaSiteVos = areaSiteVos;
    }

    public String getInterestArea() {
        return interestArea;
    }

    public List<InterestAreaSiteVo> getAreaSiteVos() {
        return areaSiteVos;
    }

    public static InterestArea fromValue(String value){
        for(InterestArea area : values()){
            if(area.getInterestArea().equalsIgnoreCase(value))
                return area;
        }
        return null;
    }

}
