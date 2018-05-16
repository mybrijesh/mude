package us.mude.common;

import com.fasterxml.jackson.annotation.JsonView;
import us.mude.util.JsonScope;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Calendar;
import java.util.Date;


@Embeddable
public class ReleaseDate {

    public ReleaseDate() {}

    @Column(name = "releaseDateTheatre")
    @JsonView(JsonScope.Public.class)
    private Date theatre;

    @Column(name = "releaseDateDVD")
    @JsonView(JsonScope.Public.class)
    private Date dvd;

    @Column(name = "isComingSoon")
    @JsonView(JsonScope.Public.class)
    private boolean isComingSoon;

    public boolean isComingSoon() {
        return isComingSoon;
    }

    public void setComingSoon(boolean comingSoon) {
        isComingSoon = comingSoon;
    }

    public Date getTheatre() {
        return theatre;
    }

    public void setTheatre(Date theatre) {
        this.theatre = theatre;
    }

    public Date getDvd() {
        return dvd;
    }

    public void setDvd(Date dvd) {
        this.dvd = dvd;
    }
}
