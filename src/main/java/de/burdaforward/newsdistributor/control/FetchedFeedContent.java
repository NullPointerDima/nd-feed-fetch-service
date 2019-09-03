package de.burdaforward.newsdistributor.control;

import java.sql.Timestamp;
import java.util.Objects;

public class FetchedFeedContent {
    private String feedName;

    private String feedURL;

    private String feedContent;

    private Integer feedChecksum;

    private Boolean fetchSuccessful;

    private String errorMessage;

    private Timestamp finishedAt;

    public FetchedFeedContent() {
        // No args constructor
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedURL() {
        return feedURL;
    }

    public void setFeedURL(String feedURL) {
        this.feedURL = feedURL;
    }

    public String getFeedContent() {
        return feedContent;
    }

    public void setFeedContent(String feedContent) {
        this.feedContent = feedContent;
    }

    public Integer getFeedChecksum() {
        return feedChecksum;
    }

    public void setFeedChecksum(Integer feedChecksum) {
        this.feedChecksum = feedChecksum;
    }

    public Boolean isFetchSuccessful() {
        return fetchSuccessful;
    }

    public void setFetchSuccessful(Boolean fetchSuccessful) {
        this.fetchSuccessful = fetchSuccessful;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Timestamp getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Timestamp finishedAt) {
        this.finishedAt = finishedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FetchedFeedContent that = (FetchedFeedContent) o;
        return Objects.equals(feedName, that.feedName) &&
                Objects.equals(feedURL, that.feedURL) &&
                Objects.equals(feedContent, that.feedContent) &&
                Objects.equals(feedChecksum, that.feedChecksum) &&
                Objects.equals(fetchSuccessful, that.fetchSuccessful) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedName, feedURL, feedContent, feedChecksum, fetchSuccessful, errorMessage);
    }
}
