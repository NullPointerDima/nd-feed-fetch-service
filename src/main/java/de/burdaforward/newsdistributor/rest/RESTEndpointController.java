package de.burdaforward.newsdistributor.rest;

import de.burdaforward.newsdistributor.control.FetchedFeedContent;
import de.burdaforward.newsdistributor.control.RawContentFetchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class RESTEndpointController {
    private RawContentFetchService rawContentFetchService;

    private String[] feedURLs = {"http://api.filmstarts.de/msn/news/movie",
            "http://api.filmstarts.de/msn/news/tvseries",
            "http://blackbird.zoomin.tv/rss/.mrss?pid=focusdedp&vtype=direct",
            "http://cdn-api.ooyala.com/v2/syndications/818e886d10514b5597164208ee918528/feed?pcode=8xZGoyOr7gjVJSXpB9Pj5Z1Yrpil",
            "http://dev.telvi.de/portale/jenatv.de/feeds/index.rss",
            "http://freedr.promipool.de/focus-short.xml",
            "http://ftp.kameraone.com/rss/focus/german/news/index.rss",
            "http://ftp.sz-online.de/Szo.FullTextFeedExport/792.rss",
            "http://meinbrandenburg.web1tv.de/mediathek/kategorie/focus/feed/focus/",
            "http://mk-online.de/focus_rss_feed.php",
            "http://rss.dw.com/syndication/feeds/VBEU_Focus_All.28151-mediahd.xml",
            "http://rss.dw.com/syndication/feeds/VEU_Focus-Artikel_DE_Europa.29099-media.xml",
            "http://rss.dw.com/syndication/feeds/VEU_Focus-Artikel_DE_Kultur.29107-media.xml",
            "http://rss.dw.com/syndication/feeds/VEU_Focus-Artikel_DE_Welt.28577-media.xml",
            "http://rss.dw.com/syndication/feeds/VEU_Focus-Artikel_DE_Wirtschaft.29103-media.xml",
            "http://rss.dw.com/syndication/feeds/VEU_Focus-Artikel_DE_WissenschaftUmwelt.29105-media.xml",
            "http://rss.nexx.cloud/TAK2HM8KR1XN0YD",
            "http://smalltalk-entertainment.de/?call_custom_simple_rss=1&csrp_key=12345",
            "http://twc-rss.com/pcp/twc-rss-text",
            "http://www.abendzeitung-muenchen.de/rss/focus-online",
            "http://www.abendzeitung-muenchen.de/rss/focus-online-fcbayern",
            "http://www.abendzeitung-muenchen.de/rss/focus-online-promis",
            "http://www.koelnsport.de/feed/",
            "http://www.kulturmd.de/index.php?format=feed&type=rss",
            "http://www.weltderwunder.de/syndication/v1/feeds/10",
            "https://a.volksfreund.de/focus/feed/",
            "https://basket.de/feed",
            "https://box-sport.de/news/rss.xml",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=auto",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=buch",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=buzz",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=digital",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=kino",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=lifestyle",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=musik",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=people",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=reisen",
            "https://concast.airmotion.de/feed/7hgLxDZweXqBQgT8IqFA9cjPrBrZvgcq?filter=tv",
            "https://efahrer-stg.chip.de/rss/rss_conunity.xml",
            "https://efahrer.chip.de/rss/rss_conunity.xml",
            "https://feeds.teleschau.de/focus-article-digital-games/list",
            "https://feeds.teleschau.de/focus-article-musik/list",
            "https://feeds.teleschau.de/focus-article-service-lifestyle/list",
            "https://feeds.teleschau.de/focus-article-tv-kino-stars/list",
            "https://feeds.teleschau.de/focus-videos/list",
            "https://floatmagazin.de/feed/focusfeed",
            "https://focusapi.mfdr.gmbh/feed1/",
            "https://h-eins.tv/mediathek/h1rss.xml",
            "https://kraken.condenastdigital.de/api/feed/Glamour-Beauty-Feed-FO",
            "https://kraken.condenastdigital.de/api/feed/Glamour-Frisuren-Feed-FO",
            "https://kraken.condenastdigital.de/api/feed/Glamour-Mode-Feed-FO",
            "https://magazin.nebenan.de/feed",
            "https://mallorcamagazin.com/focus.xml",
            "https://markteinblicke.de/category/extern/feed/",
            "https://merkurist.de/frankfurt/newsfeed/focus?apiKey=E14C5FC0-DF8C-4446-8298-968F36420998",
            "https://middleware.7tv.de/focus/v1/galleries/us-sport%3Anfl?key=8db37a7b19eb019647224760d1390075&fdsfdsfs",
            "https://middleware.7tv.de/focus/v1/news/us-sport%3Anfl?key=8db37a7b19eb019647224760d1390075&fdsfsdffsdf",
            "https://nautilus.condenastdigital.de/api/feed/Glamour-Beziehung-Singles-Feed-FO",
            "https://nautilus.condenastdigital.de/api/feed/Glamour-Glam-Slam-Stilblog",
            "https://nautilus.condenastdigital.de/api/feed/Glamour-Karriere-Feed-FO",
            "https://nautilus.condenastdigital.de/api/feed/Glamour-Reise-Feed-FO",
            "https://nautilus.condenastdigital.de/api/feed/Glamour-Stars-Feed-FO",
            "https://nautilus.condenastdigital.de/api/feed/gq-autos-fo",
            "https://nautilus.condenastdigital.de/api/feed/gq-digitales-fo",
            "https://nautilus.condenastdigital.de/api/feed/gq-fitness-fo",
            "https://nautilus.condenastdigital.de/api/feed/gq-fussball-fo",
            "https://nautilus.condenastdigital.de/api/feed/gq-karriere-fo",
            "https://nautilus.condenastdigital.de/api/feed/gq-stil-fo",
            "https://nautilus.condenastdigital.de/api/feed/gq-unterhaltung-fo",
            "https://netmomsimgmi-a.akamaihd.net/content/uploads/sites/30/2019/05/netmomsPCP-2.xml",
            "https://news.stockpulse.de/de/category/news_stream_f/feed/",
            "https://osthessen-news.de/cronjobs/mrss/focus.php",
            "https://osthessen-news.de/include/focus/feed.php",
            "https://partner.br.de/external/feed/cached",
            "https://perspective-daily.de/article/feed/focus",
            "https://rss.dw.com/syndication/feeds/VEU_Focus-Artikel_DE_Brexit.29195-media.xml",
            "https://rss.golem.de/feeds/syndication/feed_syndication_fulltext_full-ATOMCONTENT.xml",
            "https://s3-eu-west-1.amazonaws.com/focus-de-rss/1.rss",
            "https://s3-eu-west-1.amazonaws.com/focus-de-rss/2.rss",
            "https://staging.viralhog.com/licensees/feed_focus.php",
            "https://utopia.de/partnerfeeds/focus",
            "https://wize.life/feed/chatfuel/get/channel/19",
            "https://www.abenteuer-reisen.de/feed/focusOnline?tag=fol",
            "https://www.aerotelegraph.com/feed/burda",
            "https://www.ansbachplus.de/tag/folo/feed/",
            "https://www.berliner-kurier.de/feed/berlin/focus.rss",
            "https://www.blinker.de/feed/burdaforward",
            "https://www.btc-echo.de/feed/burda/",
            "https://www.chip.de/rss/cd_rss_feed_focus_141992827.xml",
            "https://www.choices.de/rss/burda/index.php",
            "https://www.connect.de/rss/focus.xml",
            "https://www.der-zukunftsfonds.de/zaster/rss-2.rss",
            "https://www.dewezet.de/feed/109-feed-focus-online-dewezet.xml",
            "https://www.die-tagespost.de/storage/rss/rss/burdaforward.xml",
            "https://www.domradio.de/focus-de/rss.xml",
            "https://www.domradio.de/focus-de/video-rss.xml",
            "https://www.dortmund24.de/dortmund/feed",
            "https://www.engels-kultur.de/rss/burda/index.php",
            "https://www.express.de/feed/focus.rss",
            "https://www.family.de/themen/ccn-feed/?feed=ccn",
            "https://www.fitforfun.de/fff/XML/rss_fff_focus.xml",
            "https://www.fleischglueck.de/feed/mrss",
            "https://www.fool.de/partner-feeds/focus-de/feed/?utm_source=focus",
            "https://www.footballr.at/category/focus/feed/",
            "https://www.frankenfernsehen.tv/storage/focus/video.xml",
            "https://www.frankenpost.de/storage/rss/rss/fp/nachrichten_frankenpost_focus.xml",
            "https://www.hfv-online.de/mediarss.php",
            "https://www.homify.de/articles.focus",
            "https://www.infranken.de/fileadmin/services/rss.php",
            "https://www.inside-handy.de/feed/burda",
            "https://www.insuedthueringen.de/storage/rss/rss/th/nachrichten_insuedthueringen_focus.xml",
            "https://www.ka-news.de/storage/rss/rss/focus.xml",
            "https://www.kfz-rueckrufe.de/burda.rss",
            "https://www.klatsch-tratsch.de/feed/focus/",
            "https://www.kleingeldhelden.com/?feed=focus",
            "https://www.konradin-service.de/wissenschaft4focus/wissenschaft-feed.xml",
            "https://www.ksta.de/feed/focus.rss",
            "https://www.kurier.de/focus-online-rss",
            "https://www.lausitz-tv.de/rss_fokus",
            "https://www.lead-digital.de/focus-feed.rss",
            "https://www.leichtathletik.de/la-rss-focus-online.xml",
            "https://www.magazin-schule.de/feed/magazin",
            "https://www.mdr.de/export/media-feeds/burda-test-102.xml",
            "https://www.mopo.de/feed/hamburg/ausgehen/ausfluege/focus.rss",
            "https://www.mopo.de/feed/hamburg/ausgehen/buehne---show/focus.rss",
            "https://www.mopo.de/feed/hamburg/ausgehen/essen---trinken/focus.rss",
            "https://www.mopo.de/feed/hamburg/ausgehen/events/focus.rss",
            "https://www.mopo.de/feed/hamburg/ausgehen/musik---partys/focus.rss",
            "https://www.mopo.de/feed/hamburg/focus.rss",
            "https://www.mopo.de/feed/hamburg/kauf-lokal/focus.rss",
            "https://www.mopo.de/feed/hamburg/politik/focus.rss",
            "https://www.mopo.de/feed/hamburg/polizei/focus.rss",
            "https://www.mopo.de/feed/hamburg/promi---show/focus.rss",
            "https://www.mopo.de/feed/im-norden/bremen/focus.rss",
            "https://www.mopo.de/feed/im-norden/kiel/focus.rss",
            "https://www.mopo.de/feed/im-norden/kreis-herzogtum-lauenburg/focus.rss",
            "https://www.mopo.de/feed/im-norden/kreis-pinneberg/focus.rss",
            "https://www.mopo.de/feed/im-norden/kreis-segeberg/focus.rss",
            "https://www.mopo.de/feed/im-norden/kreis-steinburg/focus.rss",
            "https://www.mopo.de/feed/im-norden/kreis-stormarn/focus.rss",
            "https://www.mopo.de/feed/im-norden/landkreis-harburg/focus.rss",
            "https://www.mopo.de/feed/im-norden/landkreis-lueneburg/focus.rss",
            "https://www.mopo.de/feed/im-norden/landkreis-stade/focus.rss",
            "https://www.mopo.de/feed/im-norden/luebeck/focus.rss",
            "https://www.mopo.de/feed/im-norden/mecklenburg-vorpommern/focus.rss",
            "https://www.mopo.de/feed/im-norden/niedersachsen/focus.rss",
            "https://www.mopo.de/feed/im-norden/rostock/focus.rss",
            "https://www.mopo.de/feed/im-norden/schleswig-holstein/focus.rss",
            "https://www.mopo.de/feed/sport/crocodiles/focus.rss",
            "https://www.mopo.de/feed/sport/fc-st-pauli/focus.rss",
            "https://www.mopo.de/feed/sport/fussball/focus.rss",
            "https://www.mopo.de/feed/sport/handball-hamburg/focus.rss",
            "https://www.mopo.de/feed/sport/hsv/focus.rss",
            "https://www.mopo.de/feed/sport/lokalsport/focus.rss",
            "https://www.mopo.de/feed/sport/towers/focus.rss",
            "https://www.motorsport-total.com/xml/out/focus/focus_dtm.xml",
            "https://www.motorsport-total.com/xml/out/focus/focus_f1.xml",
            "https://www.motorsport-total.com/xml/out/focus/focus_motorrad.xml",
            "https://www.motorsport-total.com/xml/out/focus/focus_rallye.xml",
            "https://www.moz.de/service/weiteres/rss-feeds/?type=578&tx_rsmretrescorss_pi1%5bfocus%5d=182fc5da4185ca4fd652707e15284c68",
            "https://www.mucbook.de/tag/fokussiert/feed",
            "https://www.mz-web.de/xml/23578008-asFocusFulltextFeed.xml",
            "https://www.mz-web.de/xml/32619548-asFocusFulltextFeed.xml",
            "https://www.np-coburg.de/storage/rss/rss/np/nachrichten_coburg_focus.xml",
            "https://www.onmeda.de/focus_rss.html",
            "https://www.osthessen-zeitung.de/rss/",
            "https://www.presseportal.de/rss/st/focusde.rss2?langid=1&full=1&formatted=1&encoding=UTF-8",
            "https://www.realtotal.de/focusfeed.xml",
            "https://www.rnf.de/storage/focus_feed.xml",
            "https://www.sachsen-fernsehen.de/storage/focus/chemnitz_text.xml",
            "https://www.sachsen-fernsehen.de/storage/focus/chemnitz_video.xml",
            "https://www.sachsen-fernsehen.de/storage/focus/dresden_text.xml",
            "https://www.sachsen-fernsehen.de/storage/focus/dresden_video.xml",
            "https://www.sachsen-fernsehen.de/storage/focus/leipzig_text.xml",
            "https://www.sachsen-fernsehen.de/storage/focus/leipzig_video.xml",
            "https://www.schwaebische-post.de/rss/focus-local/",
            "https://www.scinexx.de/rss/focus_rss.xml",
            "https://www.seniorenportal.de/feed/burdaforward",
            "https://www.sohomen.de/feed/synba",
            "https://www.sport1.de/api/playlist/videos-focus.rss",
            "https://www.swity.de/tag/folo/feed/",
            "https://www.talerbox.com/feed/full/",
            "https://www.tennismagazin.de/feed/burdaforward",
            "https://www.trailer-ruhr.de/rss/burda/index.php",
            "https://www.traunsteiner-tagblatt.de/feed/50-focus-online.xml",
            "https://www.united-interface.de/feed.php?channel=1",
            "https://www.united-interface.de/feed.php?channel=3",
            "https://www.united-interface.de/feed.php?channel=4",
            "https://www.united-interface.de/feed.php?channel=5",
            "https://www.united-interface.de/feed.php?channel=6",
            "https://www.united-interface.de/feed.php?channel=7",
            "https://www.unna24.de/unna/feed/",
            "https://www.wallstreet-online.de/rss/nachrichten-focus",
            "https://www.wallstreet-online.de/rss/nachrichten/kapitalmarkt-wallstreet-online?f100=1",
            "https://www.weltderwunder.de/syndication/v1/feeds/9?size=1000",
            "https://www.wochenkurier.info/rss_feed_for_focusonline?type=100",
            "https://www.wuerzburgerleben.de/tag/folo/feed/"
    };

    @Autowired
    public RESTEndpointController(RawContentFetchService rawContentFetchService) {
        this.rawContentFetchService = rawContentFetchService;
    }

    @GetMapping(value = "/fetch", produces = "text/html; charset=utf-8")
    public String fetchContentFromURL() throws Exception {
        long startTime = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        List<CompletableFuture<FetchedFeedContent>> futureList = new ArrayList<>();

        for (String feedURL : feedURLs) {
            futureList.add(rawContentFetchService.fetchRawContentFromFeed(feedURL));
        }

        futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        for (CompletableFuture<FetchedFeedContent> fetchedFeedContentCompletableFuture : futureList) {
            FetchedFeedContent fetchedContent = fetchedFeedContentCompletableFuture.get();

            if (fetchedContent.isFetchSuccessful()) {
                sb.append("SUCCESS");
                sb.append("<p>FEED URL: ").append(fetchedContent.getFeedURL()).append("</p>");
                sb.append("<p>FEED CONTENT NOT NULL: ").append(StringUtils.isNotBlank(fetchedContent.getFeedContent())).append("</p>");
                sb.append("<p>FINISHED: ").append(fetchedContent.getFinishedAt()).append("</p>");
                sb.append("<p>-------------------------------------------------------------</p>");
            } else {
                sb.append("ERROR");
                sb.append("<p>FEED URL: ").append(fetchedContent.getFeedURL()).append("</p>");
                sb.append("<p>FEED ERROR MESSAGE: ").append(fetchedContent.getErrorMessage()).append("</p>");
                sb.append("<p>FINISHED: ").append(fetchedContent.getFinishedAt()).append("</p>");
                sb.append("<p>-------------------------------------------------------------</p>");
            }
        }

        sb.append("<p>Elapsed time: ").append(elapsedTime).append("</p>");
        sb.append("<p>Size: ").append(futureList.size()).append("</p>");

        return sb.toString();
    }
}
