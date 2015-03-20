package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;
import views.html.register;

import static play.data.Form.form;


public class Usercontroller extends Controller {

    public static Result login() {
        return ok(login.render(form(Login.class).bindFromRequest()));
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Usercontroller.login());
    }

    /**
     * register validation
     * @return info or new user
     */
    public static Result registerValidation() {
            Form<User> regForm = form(User.class).bindFromRequest();

        if (regForm.field("email").valueOr("").isEmpty() ||
                regForm.field("confirm_email").valueOr("").isEmpty()) {
            regForm.reject("Email field cannot be empty");
        }
            if (!regForm.field("email").valueOr("").equals
                    (regForm.field("confirm_email").value())) {
                regForm.reject("Emails don't match");
            }

        if(regForm.field("password").valueOr("").isEmpty() ||
                regForm.field("confirm_password").valueOr("").isEmpty()) {
            regForm.reject("Password field cannot be empty");
        }
            if (!regForm.field("password").value().equals(regForm.field("confirm_password").value())) {
                regForm.reject("Passwords don't match");
            }
        if (regForm.hasGlobalErrors()) {
                return badRequest(register.render(regForm));
            } else {
                User newUser = new User(regForm.get().firstName,
                        regForm.get().lastName,
                        regForm.get().email,
                        regForm.get().password,
                        regForm.get().isActive);

                Logger.error("new user: " + newUser.toString());
                newUser.save();
                flash("success", "User created, you can now sign in!");
                return ok(login.render(form(Login.class).bindFromRequest()));
            }
    }
    /** Loginform validate login
     *
     * @return redirect to loginpage
     */
    public static Result authenticate() {

        Form<Login> loginForm = new Form<>(Login.class).bindFromRequest();

        if(loginForm.field("mail").value().equals("") ||
                loginForm.field("password").value().equals("")) {
                loginForm.reject("All the fields must be filled in!");
        }

        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("mail", loginForm.get().mail);
            session("password", loginForm.get().password);
            return redirect(routes.Pagecontroller.pages());
        }
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
                return "Oh no! Wrong combination of email/password!";
            }
            return null;
        }
    }
}
