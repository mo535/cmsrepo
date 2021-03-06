package models;


import play.db.ebean.Model;

import javax.persistence.*;

@Entity
public class Settings extends Model{

    public static Model.Finder<Long,
            User> find = new Model.Finder<>(Long.class, User.class);

    @Id
    @GeneratedValue
    public Long id;

    public String email;

    public String address;

    public String name;

    @Column(name="pagename")
    public String pageName;

}