package finki.ukim.mk.wbs.web;

import finki.ukim.mk.wbs.service.DataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/home")
public class HomeController {
    private final DataService dataService;

    public HomeController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping
    public String getHomePage(){
        return "home";
    }
    @PostMapping
    public String getActorPage(@RequestParam String name,
                               @RequestParam String surname,
                               Model model){
        model.addAttribute("name",name);
        model.addAttribute("surname",surname);

        dataService.makeModel("http://dbpedia.org/resource/Tom_Cruise");
//        dataService.makeModel("http://dbpedia.org/resource/"+name+"_"+surname);
//        dataService.printModel();
        model.addAttribute("actor",dataService.getActorInfo());

        return "actorInfo";
    }
    @GetMapping("/{movie}")
    public String getMoviePage(@PathVariable String movie,
                           Model model){
        String movieUrl = "http://dbpedia.org/resource/"+movie.replace(" ","_");
        model.addAttribute("movie",dataService.getMovieInfo(movieUrl));
        return "movieInfo";
    }//TODO: show top 10 movies with same genre [button]

    @GetMapping("/composer/{composer}")
    public String getComposerPage(@PathVariable String composer,
                           Model model){

        String composerUrl = "http://dbpedia.org/resource/"+composer.replace(" ","_");
        model.addAttribute("composer",dataService.getComposerInfo(composerUrl));
        return "composerInfo";
    }
}
