package templates;

import com.magnoliales.handlebars.annotations.SingletonTemplate;
import components.TextComponent;
import info.magnolia.module.blossom.annotation.Area;
import info.magnolia.module.blossom.annotation.AvailableComponentClasses;
import info.magnolia.module.blossom.annotation.AvailableComponents;
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

    @Area("footer")
    @Controller
    @AvailableComponentClasses({TextComponent.class})
    public static class FooterArea {

        @RequestMapping("/home-page/footer")
        public String render() {
            return "areas/footer";
        }
    }
}
