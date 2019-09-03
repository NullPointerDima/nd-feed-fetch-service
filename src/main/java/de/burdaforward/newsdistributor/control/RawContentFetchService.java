package de.burdaforward.newsdistributor.control;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;

@Service
public class RawContentFetchService {
    @Async
    public CompletableFuture<FetchedFeedContent> fetchRawContentFromFeed(String feedURL) {
        FetchedFeedContent contentObject = new FetchedFeedContent();

        try {
            String rawContent = HTTPService.executeGetRequest(feedURL);
            rawContent = normalizeXMLString(rawContent);

            contentObject.setFeedName(feedURL);
            contentObject.setFeedURL(feedURL);
            contentObject.setFeedContent(rawContent);
            contentObject.setFeedChecksum(rawContent.hashCode());
            contentObject.setFetchSuccessful(true);
        } catch (IOException | IllegalStateException ex) {
            contentObject.setFeedURL(feedURL);
            contentObject.setFetchSuccessful(false);
            contentObject.setErrorMessage(ex.getMessage());
        }

        contentObject.setFinishedAt(new Timestamp(System.currentTimeMillis()));
        return CompletableFuture.completedFuture(contentObject);
    }

    /**
     * Sanitise an XML string as much as possible.
     *
     * @param xmlString XML string which should be sanitised.
     * @return Sanitized xml string.
     */
    private String normalizeXMLString(String xmlString) {
        xmlString = escapeXmlEntities(xmlString);
        xmlString = removeUTF8BOM(xmlString);
        xmlString = trimXmlString(xmlString);

        return xmlString;
    }

    /**
     * Remove BOM if its present at the beginning of the xml string.
     *
     * @param xmlString xml string which should be stripped of its BOM.
     * @return xml string without BOM.
     */
    private String removeUTF8BOM(String xmlString) {
        if (xmlString.startsWith("\uFEFF")) {
            xmlString = xmlString.substring(1);
        }

        return xmlString;
    }

    /**
     * Remove all characters before the start of the XML root.
     *
     * @param xmlString xml string which should be stripped of all chars before the start of the XML root.
     * @return xml string without any chars before the start of the XML root.
     */
    private String trimXmlString(String xmlString) {
        // Remove all chars before XML root
        if (xmlString.contains("<?xml")) {
            xmlString = xmlString.substring(xmlString.indexOf("<?xml"));
        } else if (xmlString.contains("<rss")) {
            xmlString = xmlString.substring(xmlString.indexOf("<rss"));
        }

        return xmlString;
    }

    /**
     * Escape all possible & chars in the xml string.
     *
     * @param xmlString xml string where the & chars should be escaped.
     * @return xml string with escaped & chars.
     */
    private String escapeXmlEntities(String xmlString) {
        // Try to escape & chars which are not yet escaped
        xmlString = xmlString.replaceAll("&(?!amp;|gt;|lt;|#)", "&amp;");

        return xmlString;
    }
}
