package us.mude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import us.mude.common.ArtistType;
import us.mude.common.AwardType;
import us.mude.common.GlobalProps;
import us.mude.common.Name;
import us.mude.util.JsonScope;
import us.mude.util.URIConverter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name = "awards")
public class Award {

    public Award() {
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "awardType")
    private AwardType awardType;

    @Column(name = "awardYear")
    private int awardYear;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mediaID")
    private Media media;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AwardType getAwardType() {
        return awardType;
    }

    public void setAwardType(AwardType awardType) {
        this.awardType = awardType;
    }

    public int getAwardYear() {
        return awardYear;
    }

    public void setAwardYear(int awardYear) {
        this.awardYear = awardYear;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "Award{" +
                "id=" + id +
                ", awardType=" + awardType +
                ", awardYear=" + awardYear +
                ", media=" + media.getMediaName() +
                '}';
    }
}
