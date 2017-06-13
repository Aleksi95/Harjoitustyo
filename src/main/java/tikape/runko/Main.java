package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:metsapalasta22.db");
        database.init();

        KayttajaDao kayttajaDao = new KayttajaDao(database);
        AlueDao alueDao = new AlueDao(database);
        AvausDao avausDao = new AvausDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/:alue", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("avaukset", avausDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttaja", kayttajaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "kayttaja");
        }, new ThymeleafTemplateEngine());
    }
}
