package us.mude.common;

import com.fasterxml.jackson.annotation.JsonView;
import us.mude.util.JsonScope;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    public Name() {}

    public Name(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Column(name="firstName")
    @JsonView(JsonScope.Public.class)
    private String firstName;

    @Column(name="lastName")
    @JsonView(JsonScope.Public.class)
    private String lastName;

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public void setLastname(String lastName) {
      this.lastName = lastName;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    @Override
    public String toString() {
        return "Name{" +
                "firstName=" + firstName +
                ", lastName=" + lastName +
                '}';
    }
}
