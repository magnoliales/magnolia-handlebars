package templates;

import com.magnoliales.handlebars.annotations.SingletonTemplate;
import info.magnolia.module.blossom.annotation.Template;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Template(id = HomePageTemplate.ID, title = "Home Page")
@SingletonTemplate
public class HomePageTemplate {

    public static final String ID = "handlebars-example:pages/home-page";

    @RequestMapping("/home-page")
    public String render(Model model) {
        model.addAttribute("name", "World");
        return "home-page";
    }
}
