package models;


import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;
import javax.persistence.*;
import javax.validation.Constraint;
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
     * Change user first and lastname
     * @return new name
     */
    public static String rename(Long userId, String newFirstName) {
        User user = findEM.ref(userId);
        user.firstName = newFirstName;
        user.update();
        return newFirstName;
    }

    public static String renameLast(Long userId, String newLastName) {
        User user = findEM.ref(userId);
        user.lastName = newLastName;
        user.update();
        return newLastName;
    }

    public static String updatePass(Long userId, String newMail) {
        User user = findEM.ref(userId);
        user.password = newMail;
        user.update();
        return newMail;
    }

    public static boolean updateActive(Long userId, boolean newisActive) {
        User user = findEM.ref(userId);
        user.isActive = newisActive;
        user.update();
        return newisActive;
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
     * Find user with string name
     */
    public static Model.Finder
            <String, User> find = new Model.Finder<>(String.class, User.class);

    /**
     * Find user with Long id
     */
    public static Model.Finder
            <Long, User> findEM = new Model.Finder<>(Long.class, User.class);
    /**
     * Retrive all users
     */
    public static List<User> findAll() {
        return find.all();
    }

    /**
     * Retrieve a User from email.
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

    /**
     * Save user with registration date
     */
    public void save() {
        this.createDate = new Date();
        super.save();
    }

    public long getId() {
        return id;
    }


    @Id
    @GeneratedValue
    public Long id;

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


