package models;

import play.data.format.Formats;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Page extends Model{

    public Page(String pageName, boolean isActive){
        this.pageName = pageName;
        this.isActive = isActive;
    }

    public static String rename(Long pageId, String newName) {
        Page page = find.ref(pageId);
        page.pageName = newName;
        page.setLastUpdate(new Date());
        page.update();
        return newName;
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

    public static void delete(Long id){
        find.ref(id).delete();
    }

    public static List<Page> all() {
        return find.all();
    }

    /***
     * Find page creator
     * @param user id
     * @return Page creator
     */
    public static List<Page> findOwner(String user) {
        return find.fetch("createdBy").where()
                .eq("email", user)
                .findList();
    }

    public static List<Page> findPpage(Long user) {
        return find.fetch("createdBy").where()
                .eq("id", user)
                .findList();
    }

    public static Model.Finder<Long,
            Page> find = new Model.Finder<>(Long.class, Page.class);

    public Long getId() {
        return id;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     * add date of last update
     * @param lastUpdate = page last updated
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Id
    @GeneratedValue
    public Long id;

    public boolean isActive;

    public String pageName;

    @ManyToOne
    @Column(name="createdby")
    public User createdBy;

    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="lastupdate")
    public Date lastUpdate;

    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(name="createdate")
    public Date createDate;
}