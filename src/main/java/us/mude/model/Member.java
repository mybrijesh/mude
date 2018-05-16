package us.mude.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;
import us.mude.common.GlobalProps;
import us.mude.common.MemberType;
import us.mude.util.JsonScope;
import us.mude.util.MailConverter;
import us.mude.common.Name;
import us.mude.util.URIConverter;

import javax.mail.internet.InternetAddress;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "members")
public class Member {

    public Member(){}
    public Member(InternetAddress email, String firstName, String lastName, String password) {
        this.email = email;
        this.password = password;
        this.name = new Name(firstName, lastName);
        this.memberType = MemberType.AUDIENCE;
        this.regDate = Calendar.getInstance();
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Column(name="email")
    @Convert(converter = MailConverter.class)
    private InternetAddress email;

    @Column(name="password")
    private String password;

    @Column(name="description")
    private String desc;

    @Column(name="resourcePath")
    @Convert(converter = URIConverter.class)
    private URI profileImage;

    @Column(name="regDate")
    @Temporal(TemporalType.DATE)
    private Calendar regDate;

    @Column(name="memberType")
    @Enumerated(EnumType.ORDINAL)
    private MemberType memberType = MemberType.AUDIENCE;

    @Column(name="isActive")
    private boolean activated;

    @Column(name="isTopCritic")
    private boolean topCritic;

    @Column(name="isPrivate")
    private boolean isPrivate;

    @OneToMany
    @JsonIgnore
    @JoinTable(
            name="want_to_see",
            joinColumns={ @JoinColumn(name="memberID", referencedColumnName="ID") },
            inverseJoinColumns={ @JoinColumn(name="mediaID", referencedColumnName="ID", unique=true) }
    )
    @WhereJoinTable(clause = "isWantToSee = 1")
    private List<Media> wantToSee;

    @OneToMany
    @JsonIgnore
    @JoinTable(
            name="want_to_see",
            joinColumns={ @JoinColumn(name="memberID", referencedColumnName="ID") },
            inverseJoinColumns={ @JoinColumn(name="mediaID", referencedColumnName="ID", unique=true) }
    )
    @WhereJoinTable(clause = "isWantToSee = 0")
    private List<Media> notInterested;

    @OneToMany
    @JsonIgnore
    @JoinTable(
            name="reviews",
            joinColumns={ @JoinColumn(name="memberID", referencedColumnName="ID") },
            inverseJoinColumns={ @JoinColumn(name="ID") }
    )
    @OrderBy("regDate")
    private List<Review> ratingHistory;

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

    public InternetAddress getEmail() {
        return email;
    }

    public void setEmail(InternetAddress email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public URI getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(URI profileImage) {
        this.profileImage = profileImage;
    }

    public Calendar getRegDate() {
        return regDate;
    }

    public void setRegDate(Calendar regDate) {
        this.regDate = regDate;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isTopCritic() {
        return topCritic;
    }

    public void setTopCritic(boolean topCritic) {
        this.topCritic = topCritic;
    }

    public List<Media> getWantToSee() {
        return wantToSee;
    }

    public void setWantToSee(List<Media> wantToSee) {
        this.wantToSee = wantToSee;
    }

    public List<Media> getNotInterested() {
        return notInterested;
    }

    public void setNotInterested(List<Media> notInterested) {
        this.notInterested = notInterested;
    }

    public List<Review> getRatingHistory() {
        return ratingHistory;
    }

    public void setRatingHistory(List<Review> ratingHistory) {
        this.ratingHistory = ratingHistory;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
//                ", password='" + password + '\'' +
//                ", desc='" + desc + '\'' +
//                ", profileImage=" + profileImage +
//                ", regDate=" + regDate +
                ", memberType=" + memberType +
                ", activated=" + activated +
                ", topCritic=" + topCritic +
//                ", wantToSee=" + wantToSee +
//                ", notInterested=" + notInterested +
//                ", ratingHistory=" + ratingHistory +
                '}';
    }
}

