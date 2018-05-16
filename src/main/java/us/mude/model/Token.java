package us.mude.model;

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
import java.util.List;

@Entity
@Table(name = "tokens")
public class Token {

  @Id
  @Column(name="ID")
  private Long id;

  @Column(name="token")
  private String token;

  @OneToOne
  @JoinColumn(name="memberID")
  private Member member;

  public Token( ) { }

  public Token( Member member, String token ) {
    this.id = member.getId( );
    this.member = member;
    this.token  = token;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(Member member) {
    this.member = member;
  }
}
