package tikape.runko;

import java.util.HashMap;
import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KayttajaDao;
import tikape.runko.database.AlueDao;


public class Main {

    public static void main(String[] args) throws Exception {

        Database database = new Database("jdbc:sqlite:metsapalsta3.db");
      
        database.init();
       
        AlueDao alueDao = new AlueDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("alueet", alueDao.findAll());

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());

       // get("/opiskelijat", (req, res) -> {
            //HashMap map = new HashMap<>();
            //map.put("kayttajat", kaDao.findAll());

            //return new ModelAndView(map, "kayttajat");
        //}, new ThymeleafTemplateEngine());

        //get("/kayttajat/:id", (req, res) -> {
           // HashMap map = new HashMap<>();
           // map.put("opiskelija", kayttajaDao.findOne(Integer.parseInt(req.params("id"))));

           // return new ModelAndView(map, "kayttaja");
        //}, new ThymeleafTemplateEngine());
    }
}
