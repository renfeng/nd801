package hu.dushu.developers.popularmovies.sync;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by renfeng on 9/10/15.
 */
public class VideosResponse {

	/**
	 * movie id
	 */
	@Key
	private int id;

	@Key
	private List<Video> results;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Video> getResults() {
		return results;
	}

	public void setResults(List<Video> results) {
		this.results = results;
	}
}
