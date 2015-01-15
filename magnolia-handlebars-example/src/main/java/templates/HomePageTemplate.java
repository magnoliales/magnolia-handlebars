package templates;

import com.magnoliales.handlebars.annotations.SingletonTemplate;
import components.TextComponent;
import dialogs.HeaderAreaDialog;
import dialogs.HomePageDialog;
import info.magnolia.module.blossom.annotation.Area;
import info.magnolia.module.blossom.annotation.AvailableComponentClasses;
import info.magnolia.module.blossom.annotation.Template;
import bean.ExampleBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Template(id = HomePageTemplate.ID, title = "Home Page", dialog = HomePageDialog.ID)
@SingletonTemplate
public class HomePageTemplate {

    public static final String ID = "handlebars-example:pages/home-page";

    @RequestMapping("/home-page")
    public String render(Model model) {
        model.addAttribute("name", "World");
        model.addAttribute("exampleBean", new ExampleBean());
        return "home-page";
    }

    @Area(value = "menu", dialog = HeaderAreaDialog.ID)
    @Controller
    @AvailableComponentClasses({TextComponent.class})
    public static class MenuArea {

        @RequestMapping("/home-page/menu")
        public String render() {
            return "areas/menu";
        }
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
