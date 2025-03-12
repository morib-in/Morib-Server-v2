package org.morib.server.domain.user.infra.type;

import lombok.Getter;

import java.util.List;

@Getter
public enum RecommendSite {

    BUSINESS_OWNER_EXECUTIVE("Business Owner/Executive", List.of(
            new RecommendSiteVo("https://www.gstatic.com/trends/favicon.ico", "Google Trends", "Google Trends", "https://trends.google.com/trends/?geo=US"),
            new RecommendSiteVo("https://bxp-content-static.prod.public.atl-paas.net/img/favicon.ico", "Trello", "Capture, organize, and tackle your to-dos from anywhere | Trello", "https://trello.com/en"),
            new RecommendSiteVo("https://www.salesforce.com/c2/public/app/favicon.ico", "Salesforce", "Salesforce Korea", "https://www.salesforce.com/kr/?ir=1"),
            new RecommendSiteVo("https://a.slack-edge.com/80588/marketing/img/meta/favicon-32.png", "Slack", "AI 업무 관리 및 생산성 도구 | Slack", "https://slack.com/intl/ko-kr/"),
            new RecommendSiteVo("https://cfl.dropboxstatic.com/static/metaserver/static/images/favicon.ico", "Dropbox", "Dropbox for Professionals & Teams - Explore - Dropbox", "https://www.dropbox.com/business")
    )),

    DESIGNER("Designer", List.of(
            new RecommendSiteVo("https://static.figma.com/app/icon/1/favicon.ico", "피그마", "Figma: The Collaborative Interface Design Tool", "https://www.figma.com/"),
            new RecommendSiteVo("https://unsplash.com/favicon.ico", "언스플레쉬", "아름다운 무료 이미지 및 사진 | Unsplash", "https://unsplash.com/ko"),
            new RecommendSiteVo("", "프리픽", "freepik.com", "https://www.freepik.com/"),
            new RecommendSiteVo("https://s.pinimg.com/webapp/favicon_48x48-7470a30d.png", "핀터레스트", "Pinterest", "https://kr.pinterest.com/"),
            new RecommendSiteVo("https://www.behance.net//favicon.ico", "비핸스", "Behance", "https://www.behance.net/"),
            new RecommendSiteVo("https://cdn-bastani.stunning.kr/static/feature/notefolioFavicon/favicon.ico", "노트폴리오", "노트폴리오 :: 국내 최대 창작자/디자이너 플랫폼 by 스터닝", "https://notefolio.net/"),
            new RecommendSiteVo("https://cdn.dribbble.com/assets/favicon-452601365a822699d1d5db718ddf7499d036e8c2f7da69e85160a4d2f83534bd.ico", "드리블", "Dribbble - Discover the World’s Top Designers & Creative Professionals", "https://dribbble.com/")
    )),

    MARKETER("Marketer", List.of(
            new RecommendSiteVo("https://newneek.co//favicon.ico", "뉴닉", "뉴닉 - 쉽고 재밌는 지식 플랫폼", "https://newneek.co/"),
            new RecommendSiteVo("https://www.careet.net/content/images/favicon.ico", "캐릿", "캐릿 Careet", "https://www.careet.net/"),
            new RecommendSiteVo("https://tsn.dmcmedia.co.kr/dmcreportCDN/DMCReportFront/images/favicon.png", "DMC리포트", "DMC리포트", "https://www.dmcreport.co.kr/"),
            new RecommendSiteVo("", "메조 미디어", "mezzomedia.co.kr", "https://www.mezzomedia.co.kr/"),
            new RecommendSiteVo("https://www.nasmedia.co.kr/wp-content/themes/nasmedia/images/favicon/r-01.png", "나스 미디어", "나스미디어 - NASMEDIA", "https://www.nasmedia.co.kr/")
    )),

    PM_PO("PM/PO", List.of(
            new RecommendSiteVo("https://disquiet.io/favicon.ico", "디스콰이엇", "Disquiet*", "https://disquiet.io/"),
            new RecommendSiteVo("https://t1.daumcdn.net/brunch/static/icon/favicon/brunchstory/favicon_20230406.ico", "브런치", "브런치스토리", "https://brunch.co.kr/"),
            new RecommendSiteVo("https://www.notion.com/front-static/favicon.ico", "노션", "Your connected workspace for wiki, docs & projects | Notion", "https://www.notion.so/"),
            new RecommendSiteVo("https://ph-static.imgix.net/ph-favicon-brand-500.ico?auto=format", "Product Hunt", "Product Hunt – The best new products in tech.", "https://www.producthunt.com/"),
            new RecommendSiteVo("https://static.figma.com/app/icon/1/favicon.ico", "Figma", "Figma: 협업을 위한 인터페이스 디자인 도구", "https://www.figma.com/ko-kr/")
    )),

    DEV("Dev", List.of(
            new RecommendSiteVo("", "ChatGPT", "chatgpt.com", "https://chatgpt.com/"),
            new RecommendSiteVo("https://www.acmicpc.net/favicon-32x32.png", "백준", "Baekjoon Online Judge", "https://www.acmicpc.net/"),
            new RecommendSiteVo("https://github.githubassets.com/favicons/favicon.svg", "GitHub", "GitHub · Build and ship software on a single, collaborative platform", "https://github.com/"),
            new RecommendSiteVo("https://a.slack-edge.com/80588/marketing/img/meta/favicon-32.png", "Slack", "Slack", "https://slack.com/intl/ko-kr/"),
            new RecommendSiteVo("https://cdn.sstatic.net/Sites/stackoverflow/Img/favicon.ico?v=ec617d715196", "Stack Overflow", "Newest Questions", "https://stackoverflow.com/")
    )),

    STUDENT("Student", List.of(
            new RecommendSiteVo("https://www.notion.com/front-static/favicon.ico", "노션", "Your connected workspace for wiki, docs & projects | Notion", "https://www.notion.so/"),
            new RecommendSiteVo("https://www.copykiller.com/common/img/logo/favicon_ck_platform_16x16.png", "카피킬러라이트", "표절검사의 기준! 카피킬러", "https://www.copykiller.com/"),
            new RecommendSiteVo("https://www.riss.kr//favicon.ico", "RISS", "'한국교육학술정보원(KERIS)'에서 제공하는 '학술연구정보서비스(RISS)'", "https://www.riss.kr/"),
            new RecommendSiteVo("https://workspace.google.com/static/favicon.ico?cache=4926369", "Google Drive", "Google Drive: Share Files Online with Secure Cloud Storage | Google Workspace", "https://drive.google.com/"),
            new RecommendSiteVo("https://workspace.google.com/static/favicon.ico?cache=4926369", "Google Workspace", "Google Meet: 온라인 웹 및 화상 회의 통화 | Google Workspace", "https://meet.google.com/")
    )),

    OTHERS("Others", List.of(
            new RecommendSiteVo("https://papago.naver.com/favicon.ico", "네이버 파파고", "Free translation service, Papago", "https://papago.naver.com/"),
            new RecommendSiteVo("", "ChatGPT", "chatgpt.com", "https://chatgpt.com/"),
            new RecommendSiteVo("https://www.ilovepdf.com/img/favicons-pdf/favicon-32x32.png", "iLovePDF", "iLovePDF | PDF를 즐겨 쓰시는 분들을 위한 온라인 PDF 툴", "https://www.ilovepdf.com/ko"),
            new RecommendSiteVo("https://www.hancomdocs.com/favicon.ico", "HancomDocs", "Hancom Docs - Hancom Inc. ㅣ A Subscription-Type Hancom Office", "https://www.hancomdocs.com/home"),
            new RecommendSiteVo("https://mail.google.com/mail//favicon.ico", "Gmail", "Gmail", "https://mail.google.com/mail/")
    ));

    private final String interestArea;
    private final List<RecommendSiteVo> recommendSiteVos;

    RecommendSite(String interestArea, List<RecommendSiteVo> recommendSiteVos) {
        this.interestArea = interestArea;
        this.recommendSiteVos = recommendSiteVos;
    }
}
