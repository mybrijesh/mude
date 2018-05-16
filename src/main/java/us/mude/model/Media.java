package us.mude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.javamoney.moneta.Money;
import us.mude.common.*;
import us.mude.util.MoneyConverter;
import us.mude.util.URIConverter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "media")
public class Media {

    public Media() {}

    public Media(Long id) {
        this.id = id;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mediaName")
    private String mediaName;

    @Column(name="description")
    private String desc;

    @Column(name="mediaFrom")
    private String from;

    @Column(name="runTime")
    private int runtime;

    @Embedded
    private Mudemeter mudemeter = new Mudemeter();

    @Embedded
    private ReleaseDate releaseDate = new ReleaseDate();

    @Column(name="resourcePath")
    @Convert(converter = URIConverter.class)
    private URI resourcePath;

    @Column(name="reviewCount")
    private Long reviewCount;

    @Column(name="mediaType")
    @Enumerated(EnumType.ORDINAL)
    private MediaType mediaType;

    @Column(name="mediaRating")
    @Enumerated(EnumType.ORDINAL)
    private FilmRatingType filmRating;

    @Column(name="boxOffice")
    @Convert(converter = MoneyConverter.class)
    private Money boxOffice;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "parentID", nullable = true)
    private Media parent;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name="mediaID")
    @OrderBy("regDate")
    private List<Review> reviews;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name="mediaID")
    private List<Filmography> casts;

    @ElementCollection
    @CollectionTable(
            name="media_genre",
            joinColumns={ @JoinColumn(name="mediaID", referencedColumnName="ID") })
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "genreID")
    private List<GenreType> genre;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name="parentID")
    private List<Media> submedia;

    @ElementCollection
    @CollectionTable(
            name="MediaTrailers",
            joinColumns={ @JoinColumn(name="mediaID", referencedColumnName="ID") })
    @Column(name = "trailerURL")
    private List<String> trailers;

    public String parseDate(Calendar c) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
        String date = sdf.format(c.getTimeInMillis());
        return date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Mudemeter getMudemeter() {
        return mudemeter;
    }

    public void setMudemeter(Mudemeter mudemeter) {
        this.mudemeter = mudemeter;
    }

    public ReleaseDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ReleaseDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public URI getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(URI resourcePath) {
        this.resourcePath = resourcePath;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public FilmRatingType getFilmRating() {
        return filmRating;
    }

    public void setFilmRating(FilmRatingType filmRating) {
        this.filmRating = filmRating;
    }

    public Money getBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(Money boxOffice) {
        this.boxOffice = boxOffice;
    }

    public Media getParent() {
        return parent;
    }

    public void setParent(Media parent) {
        this.parent = parent;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Filmography> getCasts() {
        return casts;
    }

    public void setCasts(List<Filmography> casts) {
        this.casts = casts;
    }

    public List<GenreType> getGenre() {
        return genre;
    }

    public void setGenre(List<GenreType> genre) {
        this.genre = genre;
    }

    public List<Media> getSubmedia() {
        return submedia;
    }

    public void setSubmedia(List<Media> submedia) {
        this.submedia = submedia;
    }

    public List<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<String> trailers) {
        this.trailers = trailers;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", mediaName='" + mediaName + '\'' +
                ", from='" + from + '\'' +
                ", runtime=" + runtime +
                ", mudemeter=" + mudemeter +
                ", mediaType=" + mediaType +
                ", filmRating=" + filmRating +
                ", genre=" + genre +
                ", trailers=" + trailers +
                '}';
    }
}
