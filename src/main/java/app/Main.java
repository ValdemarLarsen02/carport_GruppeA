package app;

import app.config.SessionConfig;
import app.config.ThymeleafConfig;
import app.controllers.DatabaseController;
import app.controllers.StripePayment;
import app.utils.Product;
import app.utils.Scrapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.List;

public class Main {
    public static void main(String[] args)
    {
        // Initializing Javalin and Jetty webserver

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing


        StripePayment.registerRoutes(app);


        app.get("/", ctx ->  ctx.render("index.html"));
        app.get("/test", ctx -> ctx.render("payment.html"));

        DatabaseController dbController = new DatabaseController();

        dbController.initialize();

        //Test af scrapper:
        Scrapper scrapper = new Scrapper();

        // Søg efter produkter
        List<Product> products = scrapper.searchProducts("45x195 540");

        // Udskriv resultaterne
        for (Product product : products) {
            System.out.println(product);
            System.out.println("----------------------------");
        }

    }
}