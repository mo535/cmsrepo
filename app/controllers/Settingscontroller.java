package controllers;

import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.settings;


public class Settingscontroller extends Controller {
    public static final DynamicForm dynForm = play.data.Form.form();
    public static Form<User> Form = new Form<>(User.class).bindFromRequest();

    /**
     * Update user first and lastname
     * @param user = current signed in user
     * @return new updated name
     */
    public static Result updateName(Long user) {

        Form<User> filled_form = new Form<>(User.class).bindFromRequest();

            if (dynForm.hasGlobalErrors()) {
                return badRequest(settings.render(filled_form, filled_form,
                        User.findByEmail(session().get("mail"))));
            } else if (!filled_form.field("firstName").value().equals("") &&
                    filled_form.field("lastName").value().equals("")) {
                User.rename(
                        user, dynForm.bindFromRequest().get("firstName")
                );
                return redirect(routes.Settingscontroller.userSettings());
            } else if (!filled_form.field("lastName").value().equals("") &&
                    filled_form.field("firstName").value().equals("")) {
                User.renameLast(
                        user, dynForm.bindFromRequest()
                                .get("lastName"));
                return ok(settings.render(filled_form, filled_form,
                        User.findByEmail(session().get("mail"))));

            } else if (filled_form.field("lastName").value().equals("") &&
                    filled_form.field("firstName").value().equals("")) {
                filled_form.reject("No value entered!");

            } else  {
                User.rename(
                        user, dynForm.bindFromRequest()
                                .get("firstName"));
                User.renameLast(
                        user, dynForm.bindFromRequest()
                                .get("lastName"));


            }
        return ok(settings.render(filled_form, filled_form,
                User.findByEmail(session().get("mail"))));
    }

    /**
     * Update user password + validate form.
     * @param user Long id
     * @return new password
     */
    public static Result updatePass(Long user) {
        Form<User> passForm = new Form<>(User.class).bindFromRequest();


        if (!passForm.field("current_password").valueOr("")
                    .equals(session().get("password")) && !passForm.field("password").value().equals("")) {
                passForm.reject("Check your current password again!");

        } if (!passForm.field("password").value()
                    .equals(passForm.field("confirm_password").value())){
                    passForm.reject("Passwords does not match!");

        } if (passForm.field("password").valueOr("").isEmpty()
                || passForm.field("current_password").valueOr("").isEmpty()
                || passForm.field("confirm_password").valueOr("").isEmpty()){
            passForm.reject("Password fields cannot be empty!");

        } if (passForm.hasGlobalErrors()) {
            return badRequest(settings.render(passForm, passForm,
                    User.findByEmail(session().get("mail"))));

        } else {
                    User.updatePass(
                            user, dynForm.bindFromRequest().get("password"));
            session().put("password", passForm.field("confirm_password").value());
            flash("success", "Your password has been changed!");
            }
            return ok(settings.render(passForm, passForm,
                    User.findByEmail(session().get("mail"))));
    }

    /**
     * Render settings page with forms.
     * @return ok
     */
    public static Result userSettings() {
        return ok(settings.render(Form, Form,
                User.findByEmail(session().get("mail"))));
    }
}