package ru.omickron;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.omickron.action.CleanupStatementFilesAction;
import ru.omickron.action.GetCodeAction;
import ru.omickron.action.ReadStatementsFromDirectoryAction;
import ru.omickron.action.SendStatementAction;

import static java.util.Objects.nonNull;

@AllArgsConstructor
@Slf4j
public class Bot {
    @NonNull
    private final Config config;

    @SneakyThrows
    public void run() {
        Path path = Paths.get( config.getDirectory() );
        if (!Files.exists( path )) {
            throw new FileNotFoundException( String.format( "Directory '%s' does not exist", path ) );
        }

        GetCodeAction getCodeAction = new GetCodeAction( config );
        SendStatementAction sendStatementAction = new SendStatementAction( config, getCodeAction );
        CleanupStatementFilesAction cleanupStatementFilesAction = new CleanupStatementFilesAction();
        ReadStatementsFromDirectoryAction readStatementsFromDirectoryAction = new ReadStatementsFromDirectoryAction();
        Set<Statement> statements = readStatementsFromDirectoryAction.read( path );
        log.info( "Found {} statements: {}", statements.size(), statements );
        Stream<Statement> statementStream = statements.stream();
        if (nonNull( config.getCount() )) {
            statementStream = statementStream.limit( config.getCount() );
        }
        WebDriverManager.chromedriver().targetPath( "~/webdriver" ).setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
        statementStream.map( statement -> sendStatementAction.send( driver, statement ) )
                .forEach( cleanupStatementFilesAction :: cleanup );
        driver.close();
    }
}
