package dialogs;

import info.magnolia.module.blossom.annotation.DialogFactory;
import info.magnolia.module.blossom.annotation.TabFactory;
import info.magnolia.ui.form.config.TabBuilder;
import info.magnolia.ui.framework.config.UiConfig;
import org.springframework.stereotype.Component;

@Component
@DialogFactory(TextComponentDialog.ID)
public class TextComponentDialog {

    public static final String ID = "text-component-dialog";

    @TabFactory("Properties")
    public void propertiesTab(UiConfig uiConfig, TabBuilder tabBuilder) {
        tabBuilder.fields(
                uiConfig.fields.text("text").required().i18n()
        );
    }
}