package hu.dushu.developers.popularmovies.sync;

import com.google.api.client.util.Key;

/**
 * Created by renfeng on 9/10/15.
 */
public class Video {

	@Key
	private String id;

	@Key
	private String iso_639_1;

	@Key
	private String key;

	@Key
	private String name;

	@Key
	private String site;

	@Key
	private int size;

	@Key
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIso_639_1() {
		return iso_639_1;
	}

	public void setIso_639_1(String iso_639_1) {
		this.iso_639_1 = iso_639_1;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
