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
        Database database = new Database("jdbc:sqlite:metsapalasta22.db");
        database.init();

        KayttajaDao kayttajaDao = new KayttajaDao(database);
        AlueDao alueDao = new AlueDao(database);
        AvausDao avausDao = new AvausDao(database);
        VastausDao vastausDao = new VastausDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            if (req.queryParams().contains("content")) {
                alueDao.lisaaAlue(req.queryParams("content"));
            }
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/:alue", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("teksti", "Alue: " + req.params("alue"));
            map.put("avaukset", avausDao.findLatest10(req.params("alue")));

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/:alue/:keskust_avaus", (req, res) -> {
            HashMap map = new HashMap<>();
<<<<<<< HEAD
            map.put("vastaukset", vastausDao.findAllInThread(req.params("id")));
=======
            map.put("teksti", "Alue: " + req.params("alue") + " --> " + req.params("avaus"));
            map.put("vastaukset", vastausDao.findAllInThread(req.params("keskust_avaus")));
>>>>>>> 6a83bd609d1e84e35fa1176cf2a214b16051c405

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());
    }
}
