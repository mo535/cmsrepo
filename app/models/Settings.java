package models;


import javax.persistence.*;

@Entity
public class Settings {
    @Id
    @GeneratedValue
    public Long id;

    public String email;

    public String address;

    public String name;

    @Column(name="pagename")
    public String pageName;

}