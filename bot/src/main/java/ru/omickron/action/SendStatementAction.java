package ru.omickron.action;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
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
    public Statement send( @NonNull WebDriver driver, @NonNull Statement statement ) {
        log.info( "Processng statement {}", statement );

        driver.get( GIBDD_URL );
        InitPage initPage = new InitPage( driver );
        StatementPage statementPage = initPage.agreeWithInfo();
        ConfirmPage confirmPage = statementPage.fillWithStatement( config, statement );
        confirmPage.sendConfirmationCode();
        String code = getCodeAction.getCode();
        confirmPage.sendStatement( code );

        return statement;
    }
}
