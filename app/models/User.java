package models;


import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User extends Model {

    public User(String firstName, String lastName, String email,
                String password, boolean isActive){

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    /**
     * Authenticate a User.
     */
    public static User authenticate(String email, String password) {
        return find.where()
                .eq("email", email)
                .eq("password", password)
                .findUnique();
    }

    /**
     * Find user
     */
    public static Model.Finder
            <String, User> find = new Model.Finder<>(String.class, User.class);
    /**
     * Retrive all users
     */
    public static List<User> findAll() {
        return find.all();
    }

    /**
     * Retrieve a User from email.
     * @return a user
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
    public static User findByLastname(String lastName){
        return find.where().eq("lastName", lastName).findUnique();
    }
    public static User findByFirstname(String firstName){
        return find.where().eq("firstName", firstName).findUnique();
    }

    public void save() {
        this.createDate = new Date();
        super.save();
    }

    public long getId() {
        return id;
    }


    @Id
    @GeneratedValue
    public long id;

    @Constraints.Required
    public String email;

    @Constraints.Required
    public String password;

    public boolean isActive;

    @Column(name="firstname")
    public String firstName;

    @Column(name="lastname")
    public String lastName;


    @Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="createdate")
    public Date createDate;

}


