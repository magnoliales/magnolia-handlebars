package templates;

import com.magnoliales.handlebars.annotations.ParentTemplate;
import info.magnolia.module.blossom.annotation.Template;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Template(id = SubDetailsPageTemplate.ID, title = "Sub Details Page")
@ParentTemplate(DetailsPageTemplate.class)
public class SubDetailsPageTemplate {

    public static final String ID = "handlebars-example:pages/sub-details-page";

    @RequestMapping("/sub-details-page")
    public String render(Model model) {
        model.addAttribute("name", "World");
        return "sub-details-page";
    }
}
