package tikape.runko;

import java.util.HashMap;
import java.util.*;
import java.util.List;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.*;
import tikape.runko.domain.Alue;

public class Main {

    public static void main(String[] args) throws Exception {

        
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:metsapalasta808.db");

        database.init();

        AlueDao alueDao = new AlueDao(database);
        AvausDao avausDao = new AvausDao(database);
        ViestiDao viestiDao = new ViestiDao(database);
        KayttajaDao kayttajaDao = new KayttajaDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        post("/", (req, res) -> {
            if (req.queryParams().contains("alue")) {
                alueDao.lisaaAlue(req.queryParams("alue"));
            }
            res.redirect("/");
            return "ok";
        });

        get("/:alue", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("teksti", "Alue: " + req.params("alue"));
            map.put("avaukset", avausDao.findLatest10(req.params("alue")));

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        post("/:alue", (req, res) -> {
            if (req.queryParams().contains("avaus") && req.queryParams().contains("nimi") 
                    && req.queryParams().contains("viesti")) {
                avausDao.lisaaAvaus(req.queryParams("avaus"), req.params("alue"),
                        req.queryParams("nimi"), req.queryParams("viesti"));
            }

            res.redirect("/" + req.params("alue"));
            return "ok";
        });

        get("/:alue/:avaus", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("teksti", "Alue: " + req.params("alue") + " --> " + req.queryParams("avaus.avaus"));
            map.put("viestit", viestiDao.findAllInThread(req.params("avaus")));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());

        post("/:alue/:avaus", (req, res) -> {
            if (req.queryParams().contains("viesti")) {
                viestiDao.lisaaViesti(req.queryParams("viesti"),
                        req.queryParams("kayttaja"),
                        req.params("alue"),
                        req.params("avaus"));
            }
            res.redirect("/" + req.params("alue") + "/" + req.params("avaus"));
            return "ok";
        });
    }
}
