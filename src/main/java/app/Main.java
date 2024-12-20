package app;

import app.models.Admin;
import app.services.*;
import app.config.*;
import app.controllers.*;
import app.utils.RequestParser;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;


public class Main {
    public static void main(String[] args) {
        // Initializing Javalin and Jetty webserver
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler -> handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(8080);
        DatabaseController dbController = new DatabaseController();
        dbController.initialize();


        RequestParser requestParser = new RequestParser();
        ErrorLoggerService errorLogger = new ErrorLoggerService(dbController);
        CustomerService customerService = new CustomerService(dbController, errorLogger);
        InquiryService inquiryService = new InquiryService(customerService, dbController, errorLogger);
        EmailService emailService = new EmailService(dbController, errorLogger);
        SalesmanService salesmanService = new SalesmanService(errorLogger, dbController);
        Admin admin = new Admin(errorLogger, dbController);
        AdminController adminController = new AdminController(admin, dbController);
        SVGController svgController = new SVGController();


        //Service
        PredefinedCarportsService predefinedCarportsService = new PredefinedCarportsService(dbController);

        // Læser vores start side.
        app.get("/", ctx -> ctx.render("index.html"));



        PredefinedCarports predefinedCarports = new PredefinedCarports(dbController, predefinedCarportsService);

        InquiryController inquiryController = new InquiryController(inquiryService, salesmanService, requestParser, emailService, customerService, dbController);
        EmailController emailController = new EmailController(emailService, dbController);
        SalesmanController salesmanController = new SalesmanController(salesmanService, inquiryController, admin, dbController);
        salesmanController.registerRoutes(app);
        inquiryController.registerRoutes(app);
        emailController.registerRoutes(app);
        adminController.registerRoutes(app);
        svgController.registerRoutes(app); // Tegning af svg'er
        predefinedCarports.registerRoutes(app);


    }
}
