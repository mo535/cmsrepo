package controllers;

import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.settings;


public class Settingscontroller extends Controller {
    public static final DynamicForm dynForm = play.data.Form.form();
    public static Form<User> Form = new Form<>(User.class).bindFromRequest();

    /**
     * Update user account active/inactive, and change user email adress.
     * @param id current user
     * @return new status on active or new updated email(or both)
     */
    public static Result changeAccount(Long id){

        Form<User> dis_form = new Form<>(User.class).bindFromRequest();
        if (dis_form.hasGlobalErrors()) {
            return badRequest(settings.render(dis_form, dis_form, dis_form,
                    User.findByEmail(session().get("mail"))));
        } else if (dis_form.field("is_active").value().equals("1") &&
                dis_form.field("email").value().equals("")) {
            User.updateActive(
                    id, true);
            return ok((settings.render(dis_form, dis_form, dis_form,
                    User.findByEmail(session().get("mail")))));

        }else if (dis_form.field("email").value().equals("") &&
                dis_form.field("is_active").value().equals("0")){
            User.updateActive(
                    id, false);
            return ok((settings.render(dis_form, dis_form, dis_form,
                    User.findByEmail(session().get("mail")))));

        } else if (!dis_form.field("email").value().equals("") &&
                dis_form.field("is_active").value().equals("1")){
            User.updateActive(id, true);
            User.updateMail(id, dynForm.bindFromRequest().get("email"));

            session().put("mail", dis_form.field("email").value());
            flash("success", "Din epost har ändrats");
            return ok((settings.render(dis_form, dis_form, dis_form,
                    User.findByEmail(session().get("mail")))));
        }else {
            User.updateActive(id, false);
            User.updateMail(id, dynForm.bindFromRequest().get("email"));

            session().put("mail", dis_form.field("email").value());
            flash("success", "Din epost har ändrats");
            return ok((settings.render(dis_form, dis_form, dis_form,
                    User.findByEmail(session().get("mail")))));
        }
    }

    /**
     * Update users first and lastname
     * @param user = id of current signed in user
     * @return new updated name
     */
    public static Result updateName(Long user) {

        Form<User> filled_form = new Form<>(User.class).bindFromRequest();

        if (dynForm.hasGlobalErrors()) {
            return badRequest(settings.render(filled_form, filled_form, filled_form,
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
            return ok(settings.render(filled_form, filled_form, filled_form,
                    User.findByEmail(session().get("mail"))));

        } else if (filled_form.field("lastName").value().equals("") &&
                filled_form.field("firstName").value().equals("")) {
            filled_form.reject("Tomma fält!");

        } else  {
            User.rename(
                    user, dynForm.bindFromRequest()
                            .get("firstName"));
            User.renameLast(
                    user, dynForm.bindFromRequest()
                            .get("lastName"));
        }
        return ok(settings.render(filled_form, filled_form, filled_form,
                User.findByEmail(session().get("mail"))));
    }

    /**
     * Update user password + validate form.
     * @param user id of current signed in user
     * @return new password
     */
    public static Result updatePass(Long user) {
        Form<User> passForm = new Form<>(User.class).bindFromRequest();


        if (!passForm.field("current_password").valueOr("")
                .equals(session().get("password")) && !passForm.field("password").value().equals("")) {
            passForm.reject("Kontrollera ditt nuvarande lösenord!");

        } if (!passForm.field("password").value()
                .equals(passForm.field("confirm_password").value())){
            passForm.reject("Lösenorden matchar inte varandra!");

        } if (passForm.field("password").valueOr("").isEmpty()
                || passForm.field("current_password").valueOr("").isEmpty()
                || passForm.field("confirm_password").valueOr("").isEmpty()){
            passForm.reject("Lösenord fält får inte vara tomma");

        } if (passForm.hasGlobalErrors()) {
            return badRequest(settings.render(passForm, passForm, passForm,
                    User.findByEmail(session().get("mail"))));

        } else {
            User.updatePass(
                    user, dynForm.bindFromRequest().get("password"));
            session().put("password", passForm.field("confirm_password").value());
            flash("success", "Ditt lösenord har ändrats");
        }
        return ok(settings.render(passForm, passForm, passForm,
                User.findByEmail(session().get("mail"))));
    }

    /**
     * Render settings page with forms.
     * @return ok
     */
    public static Result userSettings() {
        return ok(settings.render(Form, Form, Form,
                User.findByEmail(session().get("mail"))));
    }
}