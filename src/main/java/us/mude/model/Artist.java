package us.mude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import us.mude.common.GlobalProps;
import us.mude.util.JsonScope;
import us.mude.common.Name;
import us.mude.util.URIConverter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {

    public Artist() {}

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Column(name="description")
    private String desc;

    @Column(name="birthdate")
    private Calendar birthdate;

    @Column(name="birthplace")
    private String birthplace;

    @Column(name="resourcePath")
    @Convert(converter = URIConverter.class)
    @JsonView(JsonScope.Public.class)
    private URI resourcePath;

    @OneToMany
    @JsonIgnore
    @JoinTable(
            name="filmography",
            joinColumns={ @JoinColumn(name="artistID", referencedColumnName="ID") },
            inverseJoinColumns={ @JoinColumn(name="mediaID", referencedColumnName="ID", unique=true) }
    )
    private List<Media> filmography;

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

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Calendar getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Calendar birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public URI getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(URI resourcePath) {
        this.resourcePath = resourcePath;
    }

    public List<Media> getFilmography() {
        return filmography;
    }

    public void setFilmography(List<Media> filmography) {
        this.filmography = filmography;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name=" + name +
//                ", desc='" + desc + '\'' +
//                ", birthdate=" + birthdate +
//                ", birthplace='" + birthplace + '\'' +
//                ", resourcePath=" + resourcePath +
//                ", filmography=" + filmography +
                '}';
    }
}
