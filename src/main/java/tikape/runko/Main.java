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
        
        String jdbcOsoite = "jdbc:sqlite:metsapalasta808.db";
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        } 
        Database database = new Database(jdbcOsoite);

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

            return new ModelAndView(map, "avaus");
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

            map.put("teksti", "Alue: " + req.params("alue") + " --> " + req.queryParams("avaus"));
            map.put("viestit", viestiDao.findAllInThread(Integer.parseInt(req.params("avaus"))));

            return new ModelAndView(map, "viesti");
        }, new ThymeleafTemplateEngine());

        post("/:alue/:avaus", (req, res) -> {
            if (req.queryParams().contains("viesti")) {
                String kayttaja = req.queryParams("kayttaja");
                if (req.queryParams("kayttaja").isEmpty()) {
                    kayttaja = "anonyymi";
                }
                viestiDao.lisaaViesti(req.queryParams("viesti"),
                        kayttaja,
                        req.params("alue"),
                        Integer.parseInt(req.params("avaus")));
            }
            res.redirect("/" + req.params("alue") + "/" + req.params("avaus"));
            return "ok";
        });
    }
}
