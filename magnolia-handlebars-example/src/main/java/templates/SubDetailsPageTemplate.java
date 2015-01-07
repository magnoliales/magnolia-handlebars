package templates;

import com.magnoliales.handlebars.annotations.ParentTemplate;
import components.TextComponent;
import dialogs.HeaderAreaDialog;
import info.magnolia.module.blossom.annotation.Area;
import info.magnolia.module.blossom.annotation.AvailableComponentClasses;
import info.magnolia.module.blossom.annotation.Template;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Template(id = SubDetailsPageTemplate.ID, title = "Sub Details Page")
@ParentTemplate(DetailsPageTemplate.class)
public class SubDetailsPageTemplate {

    public static final String ID = "handlebars-example:pages/sub-details-page";

    @Area(value = "menu", dialog = HeaderAreaDialog.ID)
    @Controller
    @AvailableComponentClasses({TextComponent.class})
    public static class MenuArea {

        @RequestMapping("/sub-page/menu")
        public String render() {
            return "areas/menu";
        }
    }

    @RequestMapping("/sub-details-page")
    public String render(Model model) {
        model.addAttribute("name", "World");
        return "sub-details-page";
    }
}
