package models;

import play.data.format.Formats;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Page extends Model{

    public Page(String pageName, boolean isActive){
        this.pageName = pageName;
        this.isActive = isActive;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Set default data while saving
     */
    public void save() {
        this.createDate = new Date();
        super.save();
    }

    public static Model.Finder<String,
            Page> find = new Model.Finder<>(String.class, Page.class);

    @Id
    @GeneratedValue
    public long id;

    public boolean isActive;

    public String pageName;

    @ManyToOne
    @Column(name="createdby")
    private User createdBy;


    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="createdate")
    public Date createDate;
}