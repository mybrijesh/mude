package us.mude.model;

import com.fasterxml.jackson.annotation.JsonView;
import us.mude.util.JsonScope;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "critic_requests")
public class CriticRequest {

    public CriticRequest() {
    }

    public CriticRequest(Member member, boolean accepted) {
        this.member = member;
        this.accepted = accepted;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonView(JsonScope.Public.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberID")
    private Member member;

    @Column(name="isAccepted")
    private boolean accepted;

    @Column(name="regDate")
    private Date regDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "CriticRequest{" +
                "id=" + id +
                ", member=" + member.getName() +
                ", accepted=" + accepted +
                '}';
    }
}
