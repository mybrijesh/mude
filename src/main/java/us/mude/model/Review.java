package us.mude.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import us.mude.common.MemberType;
import us.mude.common.RatingType;
import us.mude.util.JsonScope;
import us.mude.util.URIConverter;

import javax.persistence.*;
import java.net.URI;
import java.util.Calendar;

@Entity
@Table(name = "reviews")
public class Review {

    public Review() {}
    public Review(Member member, Media meida, String comment, float score) {
        this.comment = comment;
        this.score = score;
        this.media = meida;
        this.member = member;
        if (member.getMemberType() == MemberType.CRITIC) {
            this.ratingType = RatingType.CRITIC;
        } else {
            this.ratingType = RatingType.AUDIENCE;
        }
        this.regDate = Calendar.getInstance();
    }

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="comment")
    private String comment;

    @Column(name="score")
    private float score;

    @Column(name="isReported")
    private boolean reported;

    @ManyToOne
    @JoinColumn(name = "mediaID")
    private Media media;

    @ManyToOne
    @JoinColumn(name = "memberID")
    private Member member;

    @Column(name="ratingType")
    private RatingType ratingType;

    @Column(name="externalLink")
    @Convert(converter = URIConverter.class)
    private URI externalLink;

    @Column(name="regDate")
    private Calendar regDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public RatingType getRatingType() {
        return ratingType;
    }

    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    public URI getExternalLink() {
        return externalLink;
    }

    public void setExternalLink(URI externalLink) {
        this.externalLink = externalLink;
    }

    public Calendar getRegDate() {
        return regDate;
    }

    public void setRegDate(Calendar regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", score=" + score +
                ", reported=" + reported +
                ", media=" + media.getMediaName() +
                ", member=" + member.getName().getFirstName() +
                ", ratingType=" + ratingType +
                ", externalLink=" + externalLink +
                ", regDate=" + regDate +
                '}';
    }
}
