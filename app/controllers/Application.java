package controllers;

import models.Page;
import models.Settings;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.*;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.util.List;

import static play.data.Form.form;


public class Application extends Controller {

    /**
     * Homepage
     * @return index page
     */
    public static Result index() {

        return ok(index.render());
    }

    public static Result settings() {
        return ok(settings.render(form(Settings.class).bindFromRequest()));
    }


    public static Result register() {
        return ok(register.render(form(User.class).bindFromRequest()));
    }

    public static Result search() {
        return ok(index.render());
    }

    public static Result contact() {
        return ok(contact.render());
    }


    /**
     *
     * Search user by firstname
     * @return User info
     */
    public static Result searchFunction() {

        if (request().method().equals("GET")) {
            DynamicForm searchForm = form().bindFromRequest();
            if (searchForm.hasErrors()) {
                return badRequest(search.render(User.findAll()));
            } else {
                List<User> users = User.find
                        .where().like("firstName",  searchForm.get("search"))
                        .findList();


                Logger.error("new search: " + users.toString());
                return ok(search.render(users));
            }
        }
        return ok(index.render());
    }

}
