package hu.dushu.developers.popularmovies.sync;

import com.google.api.client.util.Key;

/**
 * Created by renfeng on 9/10/15.
 */
public class Review {

	@Key
	private String author;

	@Key
	private String content;

	@Key
	private String id;

	@Key
	private String url;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
