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
        Database database = new Database("jdbc:sqlite:metsapalasta509.db");
        database.init();

        KayttajaDao kayttajaDao = new KayttajaDao(database);
        AlueDao alueDao = new AlueDao(database);
        AvausDao avausDao = new AvausDao(database);
        VastausDao vastausDao = new VastausDao(database);

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
            avausDao.lisaaAvaus(req.queryParams("kayttaja"), req.params("alue"), req.queryParams("avaus"));
            res.redirect("/");
            return "ok";
        });

        get("/:alue/:keskust_avaus", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("vastaukset", vastausDao.findAllInThread(req.params("id")));

            map.put("teksti", "Alue: " + req.params("alue") + " --> " + req.params("avaus"));
            map.put("vastaukset", vastausDao.findAllInThread(req.params("keskust_avaus")));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
        
        post("/:alue/:keskust_avaus", (req, res) -> {
            vastausDao.lisaaVastaus(req.queryParams("kayttaja"), req.params("alue"), req.queryParams("vastaus"), req.params("keskust_avaus"));
            res.redirect("/");
            return "ok";     
        });
    }
}