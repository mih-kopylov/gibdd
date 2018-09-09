package ru.omickron.action;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.omickron.Config;
import ru.omickron.Statement;
import ru.omickron.page.ConfirmPage;
import ru.omickron.page.InitPage;
import ru.omickron.page.StatementPage;

@AllArgsConstructor
@Slf4j
public class SendStatementAction {
    private static final String GIBDD_URL = "https://гибдд.рф/request_main";
    @NonNull
    private final Config config;
    @NonNull
    private final GetCodeAction getCodeAction;

    @NonNull
    public Statement send( @NonNull Statement statement ) {
        log.info( "Processng statement {}", statement );
        WebDriverManager.chromedriver().targetPath( "~/webdriver" ).setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );

        driver.get( GIBDD_URL );
        InitPage initPage = new InitPage( driver );
        StatementPage statementPage = initPage.agreeWithInfo();
        ConfirmPage confirmPage = statementPage.fillWithStatement( config, statement );
        confirmPage.sendConfirmationCode();
        String code = getCodeAction.getCode();
        confirmPage.sendStatement( code );

        driver.close();
        return statement;
    }
}
