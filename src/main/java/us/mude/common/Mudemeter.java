package us.mude.common;

import com.fasterxml.jackson.annotation.JsonView;
import us.mude.util.JsonScope;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.PROPERTY)
public class Mudemeter {

    public Mudemeter() {}

    @Column(name="audienceAvgScore")
    private float audienceAvgScore = 0 ;

    @Column(name="audienceMudemeter")
    private float audienceMudemeter = 0 ;

    @Column(name="criticAvgScore")
    private float criticAvgScore = 0 ;

    @Column(name="criticMudemeter")
    private float criticMudemeter = 0 ;

    public float getAudienceAvgScore() {
        return audienceAvgScore;
    }

    public void setAudienceAvgScore(float audienceAvgScore) {
        this.audienceAvgScore = audienceAvgScore;
    }

    public float getAudienceMudemeter() {
        return audienceMudemeter;
    }

    public void setAudienceMudemeter(float audienceMudemeter) {
        this.audienceMudemeter = audienceMudemeter;
    }

    public float getCriticAvgScore() {
        return criticAvgScore;
    }

    public void setCriticAvgScore(float criticAvgScore) {
        this.criticAvgScore = criticAvgScore;
    }

    public float getCriticMudemeter() {
        return criticMudemeter;
    }

    public void setCriticMudemeter(float criticMudemeter) {
        this.criticMudemeter = criticMudemeter;
    }

    @Override
    public String toString() {
        return "Mudemeter{" +
                "audienceAvgScore=" + audienceAvgScore +
                ", audienceMudemeter=" + audienceMudemeter +
                ", criticAvgScore=" + criticAvgScore +
                ", criticMudemeter=" + criticMudemeter +
                '}';
    }
}
