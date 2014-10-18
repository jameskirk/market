package controllers;

import models.User;
import play.data.Form;
import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;

import static play.data.Form.form;
import static play.libs.Json.toJson;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your workout application is ready."));
    }

    public static Result sample() {
        return ok(index.render("It is just sample, guys."));
    }

    public static Result addUser() {
        Form<User> form = form(User.class).bindFromRequest();
        User user = form.get();
        user.save();
        return redirect(routes.Application.index());
    }

    public static Result getUsers() {
        List<User> users = new Model.Finder(String.class, User.class).all();
        return ok(toJson(users));
    }

}
