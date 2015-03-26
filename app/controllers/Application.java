package controllers;

import models.Page;
import models.User;
import play.Logger;
import play.data.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.*;

import static play.data.Form.form;


public class Application extends Controller {

    /**
     * Homepage
     * @return index page
     */
    @Security.Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render(Page.all(), User.findEM.all()));
    }

    @Security.Authenticated(Secured.class)
    public static Result settings() {
        return ok(settings.render(form(User.class).bindFromRequest(), form(User.class).bindFromRequest(), form(User.class).bindFromRequest(), User.findByEmail(session().get("mail"))));
    }


    public static Result register() {
        return ok(register.render(form(User.class).bindFromRequest()));
    }

    public static Result search() {
        return ok(index.render(Page.all(), User.findAll()));
    }

    public static Result contact() {
        return ok(contact.render());
    }

    public static Result estates() {
        return ok(estates.render());
    }
    @Security.Authenticated(Secured.class)
    public static Result profile(Long user){



        return ok(profile.render(User.findEM.byId(user)));
    }

    public static Result myProfile(){
        return ok(profile.render(User.findByEmail(session().get("mail"))));
    }

    /**
     * Search user by firstname
     * @return User info
     */
    public static Result searchFunction() {
        List<User> users;
        List<User> lastUsers;
        List<User> mailUsers;
        List<User> userList = new ArrayList<>();

        if (request().method().equals("GET")) {
            DynamicForm searchForm = form().bindFromRequest();
            if (searchForm.hasErrors()) {
                return badRequest(search.render(User.findAll()));
            } else {
                users = User.find.where()
                        .eq("firstName",  searchForm.get("search"))
                        .findList();

                lastUsers = User.find.where()
                        .eq("lastName",  searchForm.get("search"))
                        .findList();

                mailUsers = User.find.where()
                        .eq("email",  searchForm.get("search"))
                        .findList();

                users.removeAll(lastUsers);
                users.removeAll(mailUsers);
                lastUsers.removeAll(mailUsers);

                userList.addAll(users);
                userList.addAll(lastUsers);
                userList.addAll(mailUsers);

                Logger.error("new search: " + users.toString());
                return ok(search.render(userList));
            }
        }
        return ok(index.render(Page.all(), User.findAll()));
    }
}
