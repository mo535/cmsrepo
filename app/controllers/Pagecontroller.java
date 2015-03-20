package controllers;

import models.Page;
import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.page;

public class Pagecontroller extends Controller {
    private static Form<Page> pageForm = Form.form(Page.class);

    /**
     * Validate page
     * if form is filled correctly @return create new page.
     */
    public static Result createPage() {

        if (request().method().equals("POST")) {
            Form<Page> filled_form = new Form<>(Page.class).bindFromRequest();

            if (filled_form.field("pageName").value().equals("") || filled_form.field("pageName").value().isEmpty()) {
                filled_form.reject("Page name cannot be empty!");
            }
            if (filled_form.hasErrors()) {
                return badRequest(page.render(Page.findOwner(session().get("mail")), filled_form));
            } else {
                Page newPage = new Page(filled_form.get().pageName,
                        filled_form.get().isActive);

                newPage.setCreatedBy(
                        User.findByEmail(session().get("mail")));

                newPage.save();
                return redirect(routes.Pagecontroller.pages());
            }
        } else{
            return redirect(routes.Application.index());
        }
    }

    public static Result deletePage(Long id) {
        Page.delete(id);
        return redirect(routes.Pagecontroller.pages());
    }

    @Security.Authenticated(Secured.class)
    public static Result pages() {
        return ok(page.render(Page.findOwner(request().username()), pageForm));
    }
}
