package hu.dushu.developers.popularmovies.sync;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by renfeng on 7/11/15.
 */
public class Movie {

    @Key
    private int page;

    @Key
    private List<Result> results;

    @Key("total_pages")
    private int totalPages;

    @Key("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
