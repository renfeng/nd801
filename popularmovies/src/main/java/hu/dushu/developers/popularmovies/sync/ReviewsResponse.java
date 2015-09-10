package hu.dushu.developers.popularmovies.sync;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by renfeng on 9/10/15.
 */
public class ReviewsResponse {

	@Key
	private int id;

	@Key
	private int page;

	@Key
	private List<Review> results;

	@Key
	private int total_pages;

	@Key
	private int total_results;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<Review> getResults() {
		return results;
	}

	public void setResults(List<Review> results) {
		this.results = results;
	}

	public int getTotal_pages() {
		return total_pages;
	}

	public void setTotal_pages(int total_pages) {
		this.total_pages = total_pages;
	}

	public int getTotal_results() {
		return total_results;
	}

	public void setTotal_results(int total_results) {
		this.total_results = total_results;
	}
}
