package controllers;

import models.Page;
import models.Settings;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.settings;


public class Settingscontroller extends Controller {

    public static Result updateSettings(){

        Form<Settings> filled_form = new Form<>(Settings.class).bindFromRequest();
        if (request().method().equals("POST")){
            if (filled_form.hasErrors()) {
                return badRequest(settings.render(filled_form));
            }
            else {
                Settings settings = new Settings();
                settings.save();
            }
        }
        return ok(settings.render(filled_form));
    }

}
