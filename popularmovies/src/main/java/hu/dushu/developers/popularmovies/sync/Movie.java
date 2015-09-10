package hu.dushu.developers.popularmovies.sync;

import com.google.api.client.util.Key;

import java.util.List;

/**
 * Created by renfeng on 7/11/15.
 */
public class Movie {

    @Key
    private boolean adult;

    @Key("backdrop_path")
    private String backdropPath;

    @Key("genre_ids")
    private List<Integer> genreIDs;

    @Key
    private int id;

    @Key("original_language")
    private String originalLanguage;

    @Key("original_title")
    private String originalTitle;

    @Key
    private String overview;

    @Key("release_date")
    private String releaseDate;

    @Key("poster_path")
    private String posterPath;

    @Key
    private float popularity;

    @Key
    private String title;

    @Key
    private boolean video;

    @Key("vote_average")
    private float voteAverage;

    @Key("vote_count")
    private int voteCount;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getGenreIDs() {
        return genreIDs;
    }

    public void setGenreIDs(List<Integer> genreIDs) {
        this.genreIDs = genreIDs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
