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

import static play.data.Form.form;


public class Application extends Controller {

    /**
     *
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
        if (request().method().equals("GET")){
            User.findByEmail("email");
        }

        return redirect(routes.Application.search());
    }

    /**
     * Validate page
     * @return new page
     */
    public static Result pageValid() {
        if (request().method().equals("POST")) {
            Form<Page> page_form = new Form<>(Page.class).bindFromRequest();

            if (page_form.field("pageName").value().isEmpty()) {
                page_form.reject("pageName", "Enter a page name");
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
            Form<User> filled_form = new Form<>(User.class).bindFromRequest();

            if(!filled_form.field("password").valueOr("").isEmpty()) {
                if(!filled_form.field("password").valueOr
                        ("").equals(filled_form.field("confirm_password").value())) {
                    filled_form.reject("confirm_password", "Password don't match");
                }
            }
            if (filled_form.field("email").valueOr("").isEmpty()){
                if (filled_form.field("email").valueOr("").equals
                        (filled_form.field("confirm_email").value())) {
                    filled_form.reject("confirm_email", "Emails don't match");
                }
            }
            if (filled_form.hasErrors()) {
                return badRequest(register.render(filled_form));
            } else {
                User newUser = new User(filled_form.get().firstName,
                        filled_form.get().lastName,
                        filled_form.get().email,
                        filled_form.get().password,
                        filled_form.get().isActive);

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
