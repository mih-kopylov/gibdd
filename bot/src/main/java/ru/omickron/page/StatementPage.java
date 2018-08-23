package ru.omickron.page;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.omickron.Config;
import ru.omickron.Statement;

public class StatementPage extends PageObject {
    @FindBy(css = ".select2-selection.select2-selection--single")
    private List<WebElement> dropdowns;
    @FindBy(css = "input.select2-search__field")
    private WebElement dropdownInput;
    @FindBy(id = "surname_check")
    private WebElement surname;
    @FindBy(id = "firstname_check")
    private WebElement firstname;
    @FindBy(id = "email_check")
    private WebElement email;
    @FindBy(css = "#message_check .textarea")
    private WebElement message;
    @FindBy(css = "input[name=captcha]")
    private WebElement captcha;
    @FindBy(id = "fileupload-input")
    private WebElement fileupload;
    @FindBy(css = ".u-form__sbt.js-u-form__sbt")
    private WebElement sendStatementButton;

    public StatementPage( @NonNull WebDriver webDriver ) {
        super( webDriver );
    }

    @SneakyThrows
    @NonNull
    public ConfirmPage fillWithStatement( @NonNull Config config, @NonNull Statement statement ) {
        fillDropdown( 0, "78" );
        new WebDriverWait( getWebDriver(), 5 ).until( this :: subunitValuesUpdated );
        fillDropdown( 1, "Лен" );

        surname.sendKeys( config.getLastName() );
        firstname.sendKeys( config.getFirstName() );
        email.sendKeys( config.getMailLogin() + "@yandex.ru" );
        message.sendKeys( getMessage( statement ) );

        uploadPhotos( statement.getPhotos() );

        captcha.sendKeys( "" );
        new WebDriverWait( getWebDriver(), 30 ).until( this :: userInputsCaptha );
        sendStatementButton.click();

        return new ConfirmPage( getWebDriver() );
    }

    @SneakyThrows
    private void uploadPhotos( @NonNull Collection<Path> photos ) {
        for (Path photo : photos) {
            TimeUnit.SECONDS.sleep( 1 );
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents( new StringSelection( photo.toString() ), null );
            fileupload.click();
            Robot robot = new Robot();
            robot.delay( 1000 );
            robot.keyPress( KeyEvent.VK_CONTROL );
            robot.keyPress( KeyEvent.VK_V );
            robot.keyRelease( KeyEvent.VK_V );
            robot.keyRelease( KeyEvent.VK_CONTROL );
            robot.keyPress( KeyEvent.VK_ENTER );
        }
    }

    private void fillDropdown( int dropdownNumber, @NonNull String text ) {
        dropdowns.get( dropdownNumber ).click();
        dropdownInput.sendKeys( text );
        dropdownInput.sendKeys( Keys.RETURN );
    }

    private boolean subunitValuesUpdated( @NonNull WebDriver webDriver ) {
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        Long optionsCount =
                (Long) executor.executeScript( "return document.querySelectorAll(\"#subunit_check option\").length;" );
        return optionsCount > 1;
    }

    private boolean userInputsCaptha( @NonNull WebDriver webDriver ) {
        JavascriptExecutor executor = (JavascriptExecutor) webDriver;
        String text =
                (String) executor.executeScript( "return document.querySelector(\"input[name=captcha]\").value;" );
        return text.length() == 5;
    }

    @NonNull
    private String getMessage( @NonNull Statement statement ) {
        String date = statement.getTime().format( DateTimeFormatter.ofPattern( "dd.MM.yyyy" ) );
        String time = statement.getTime().format( DateTimeFormatter.ofPattern( "HH:mm" ) );
        return String.format( "%s в %s я обнаружил, что водитель автомобиля %s нарушил п. 12.2 ПДД "
                        + "и поставил свой автомобиль на стоянку на тротуаре по адресу %s.\n"
                        + "Прошу принять меры по ч. 6 ст. 12.19 КоАП РФ.", date, time, statement.getNumber(),
                statement.getAddress() );
    }
}
