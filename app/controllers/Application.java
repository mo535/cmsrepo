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

    public static Result page() {
        return ok(page.render(form(Page.class).bindFromRequest()));
    }

    public static Result register() {
        return ok(register.render(form(User.class).bindFromRequest()));
    }

    public static Result search() {
        return ok(index.render());
    }

    /**
     * Search user by firstname
     * @return User info
     */
    public static Result searchMethod() {

        if (request().method().equals("POST")) {
            DynamicForm searchForm = form().bindFromRequest();
            if (searchForm.hasErrors()) {
                return badRequest(search.render(User.findAll()));
            } else {
                    List<User> users = User.find
                            .where().eq("firstName", searchForm.get("search")).findList();

                Logger.error("new search: " + users.toString());
                return ok(search.render(users));
            }
        }
        return null;
    }

    /**
     * Validate page
     * @return new page
     */
    public static Result pageValid() {
        if (request().method().equals("POST")) {
            Form<Page> page_form = new Form<>(Page.class).bindFromRequest();

            if (page_form.field("pageName").value().isEmpty()) {
                page_form.reject("pageName", "Page name cannot be empty!");
            }

            if (page_form.hasErrors()) {
                return badRequest(page.render(page_form));
            } else {
                Page newPage = new Page(page_form.get().pageName,
                        page_form.get().isActive);

                newPage.setCreatedBy(
                        User.findByEmail(session().get("mail")));

                Logger.error("new page: " + newPage.toString());
                newPage.save();

                return ok(page.render(form(Page.class).bindFromRequest()));
            }
        } else{
                return ok(index.render() + "Page created");
            }
        }

    /**
     * register validation
     * @return info or new user
     */
    public static Result registerValidation() {
        if (request().method().equals("POST")) {
            Form<User> regForm = new Form<>(User.class).bindFromRequest();

            if(!regForm.field("password").valueOr("").isEmpty()) {
                if(!regForm.field("password").valueOr
                        ("").equals(regForm.field("confirm_password").value())) {
                    regForm.reject("confirm_password", "Password don't match");
                }
            }
            if (regForm.field("email").valueOr("").isEmpty()){
                if (regForm.field("email").valueOr("").equals
                        (regForm.field("confirm_email").value())) {
                    regForm.reject("confirm_email", "Emails don't match");
                }
            }
            if (regForm.hasErrors()) {
                return badRequest(register.render(regForm));
            } else {
                User newUser = new User(regForm.get().firstName,
                        regForm.get().lastName,
                        regForm.get().email,
                        regForm.get().password,
                        regForm.get().isActive);

                Logger.error("new user: " + newUser.toString());
                newUser.save();
                return ok(login.render(form(Login.class).bindFromRequest()));
            }
        }else {
            return ok(register.render(form(User.class)));
        }
    }

    public static Result welcome() {
        String message = flash("success");
        if (message == null) {
            message = "Welcome ";
        }
        return ok(message);
    }

    /** Loginform validate login
     *
     * @return redirect to loginpage
     */
    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("mail", loginForm.get().mail);
            session("password", loginForm.get().password);
            return redirect(routes.Application.page());
        }
    }
    public static Result login() {
        return ok(login.render(Form.form(Login.class).bindFromRequest()));
    }

    /**
     * Logout method
     * @return direct to login page
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.login());
    }

    /**
     * Login class
     */
    public static class Login {

        @Constraints.Required
        public String mail;
        @Constraints.Required
        public String password;

        public String validate() {
            if (User.authenticate(mail, password) == null) {
                return "Wrong email or password!";
            }
            return null;
        }
    }
}
