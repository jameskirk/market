package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your workout application is ready!"));
    }

    public static Result sample() {
        return ok(index.render("It is just sample, guys."));
    }

}
