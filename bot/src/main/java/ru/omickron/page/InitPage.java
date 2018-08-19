package ru.omickron.page;

import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InitPage extends PageObject {
    @FindBy(css = ".f-left.checkError .checkbox")
    private WebElement notifiedCheckBox;
    @FindBy(css = "button.u-form__sbt")
    private WebElement submitButton;

    public InitPage( @NonNull WebDriver webDriver ) {
        super( webDriver );
    }

    @NonNull
    public StatementPage agreeWithInfo() {
        notifiedCheckBox.click();
        submitButton.click();
        return new StatementPage( getWebDriver() );
    }
}
