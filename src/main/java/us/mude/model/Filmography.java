package us.mude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import us.mude.common.ArtistType;
import javax.persistence.*;

@Entity
@Table(name = "filmography")
public class Filmography {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="artistRole")
    private String artistRole;

    @Column(name="artistType")
    private ArtistType artistType;

    @ManyToOne
    @JoinColumn(name="artistID")
    private Artist artist;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="mediaID")
    private Media media;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public String getArtistRole() {
        return artistRole;
    }

    public void setArtistRole(String artistRole) {
        this.artistRole = artistRole;
    }

    public ArtistType getArtistType() {
        return artistType;
    }

    public void setArtistType(ArtistType artistType) {
        this.artistType = artistType;
    }

    @Override
    public String toString() {
        return "Filmography{" +
                "id=" + id +
//                ", artist=" + artist +
//                ", media=" + media +
                ", artistRole='" + artistRole + '\'' +
                ", artistType=" + artistType +
                '}';
    }
}
