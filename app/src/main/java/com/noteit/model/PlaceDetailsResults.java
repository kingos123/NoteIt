package com.noteit.model;


/**
 * Created by rdanino on 9/2/2016.
 */
public class PlaceDetailsResults {
    private String[] html_attributions;
    private String next_page_token;
    private PlaceDetails[] results;
    private PlaceDetails result;

    public PlaceDetailsResults() {

    }

    public PlaceDetails getResult() {
        return this.result;
    }

    public void setResult(PlaceDetails result) {
        this.result = result;
    }

    public String[] getHtml_attributions() {
        return this.html_attributions;
    }

    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    public String getNext_page_token() {
        return this.next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public PlaceDetails[] getResults() {
        return this.results;
    }

    public void setResults(PlaceDetails[] results) {
        this.results = results;
    }
}
