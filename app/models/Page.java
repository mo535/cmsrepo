package models;

import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Page {

    @Id
    @GeneratedValue
    public long id;
    public boolean isActive;

    public String pageName;

    public Page(String pageName, boolean isActive){
        this.pageName = pageName;
        this.isActive = isActive;
    }

    @OneToMany
    @Column(name="createdby")
    public User createdBy;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Date createDate = new Date();
}