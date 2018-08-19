package ru.omickron.page;

import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ConfirmPage extends PageObject {
    @FindBy(id = "confirm_but")
    private WebElement sendCodeButton;
    @FindBy(css = "input.confirm-mail[name=code]")
    private WebElement confirmCodeInput;
    @FindBy(id = "confirm_mail")
    private WebElement confirmCodeInputButton;
    @FindBy(id = "correct")
    private WebElement correctCheckBox;
    @FindBy(id = "form-submit")
    private WebElement submitButton;
    @FindBy(id = "message")
    private WebElement mailConfirmedMessage;

    public ConfirmPage( @NonNull WebDriver webDriver ) {
        super( webDriver );
    }

    public void sendConfirmationCode() {
        new WebDriverWait( getWebDriver(), 10 ).until( ExpectedConditions.visibilityOf( sendCodeButton ) );
        sendCodeButton.click();
    }

    public void sendStatement( @NonNull String code ) {
        confirmCodeInput.sendKeys( code );
        confirmCodeInputButton.click();
        new WebDriverWait( getWebDriver(), 10 ).until(
                webDriver -> mailConfirmedMessage.getText().equals( "Почта подтверждена!" ) );
        correctCheckBox.click();
        submitButton.click();
    }
}
