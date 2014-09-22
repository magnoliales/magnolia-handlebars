package dialogs;

import info.magnolia.module.blossom.annotation.DialogFactory;
import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.stereotype.Component;

@Component
@DialogFactory(HomePageDialog.ID)
public class HomePageDialog {

    public static final String ID = "home-page-dialog";

    @TabFactory("Properties")
    public void propertiesTab(UiConfig uiConfig, TabBuilder tabBuilder) {
        tabBuilder.fields(
                uiConfig.fields.text("title").required().i18n()
        );
    }
}
