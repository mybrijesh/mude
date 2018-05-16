package us.mude.model;

import com.fasterxml.jackson.annotation.JsonView;
import us.mude.util.JsonScope;

import javax.persistence.*;

@Entity
@Table(name = "want_to_see")
public class WantToSee {

    public WantToSee() {}

    public WantToSee(Long mediaID, Long memberID, boolean isWantToSee ) {
        this.isWantToSee = isWantToSee;
        this.mediaID = mediaID;
        this.memberID = memberID;
    }

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="isWantToSee")
    private boolean isWantToSee;

    @JoinColumn(name="mediaID", unique = false)
    private Long mediaID;

    @JoinColumn(name="memberID", unique = false)
    private Long memberID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isWantToSee() {
        return isWantToSee;
    }

    public boolean getIsWantToSee() {
        return isWantToSee;
    }

    public void setWantToSee(boolean wantToSee) {
        isWantToSee = wantToSee;
    }

    public Long getMediaID() {
        return mediaID;
    }

    public void setMediaID(Long mediaID) {
        this.mediaID = mediaID;
    }

    public Long getMemberID() {
        return memberID;
    }

    public void setMemberID(Long memberID) {
        this.memberID = memberID;
    }

    @Override
    public String toString() {
        return "WantToSee{" +
                "id=" + id +
                ", isWantToSee=" + isWantToSee +
                ", mediaID=" + mediaID +
                ", memberID=" + memberID +
                '}';
    }
}
