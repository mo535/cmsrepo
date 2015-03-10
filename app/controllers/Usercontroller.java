package controllers;

import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
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
        String message = flash("You've been logged out");
        return redirect(routes.Usercontroller.login());
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
                    regForm.reject("Password don't match");

                }
            }
            if (!regForm.field("email").valueOr("").isEmpty()){
                if (!regForm.field("email").valueOr("").equals
                        (regForm.field("confirm_email").value())) {
                    regForm.reject("Emails don't match");
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
    /** Loginform validate login
     *
     * @return redirect to loginpage
     */
    public static Result authenticate() {

        Form<Login> loginForm = new Form<>(Login.class).bindFromRequest();

        if(loginForm.field("mail").value().equals("") ||
                loginForm.field("password").value().equals("")) {
            loginForm.reject("Empty field");
        }

        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("mail", loginForm.get().mail);
            session("password", loginForm.get().password);
            return redirect(routes.Application.index());
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
                return "Wrong email or password!";
            }
            return null;
        }
    }
}
