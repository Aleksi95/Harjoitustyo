package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KayttajaDao;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:metsapalasta15.db");
        database.init();

        KayttajaDao kayttajaDao = new KayttajaDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttajat", kayttajaDao.findAll());

            return new ModelAndView(map, "kayttajat");
        }, new ThymeleafTemplateEngine());

        get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kayttaja", kayttajaDao.findOne(Integer.parseInt(req.params("kayttaja_id"))));

            return new ModelAndView(map, "kayttaja");
        }, new ThymeleafTemplateEngine());
    }
}
