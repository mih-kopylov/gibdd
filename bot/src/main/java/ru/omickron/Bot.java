package ru.omickron;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.omickron.action.CleanupStatementFilesAction;
import ru.omickron.action.GetCodeAction;
import ru.omickron.action.ReadStatementsFromDirectoryAction;
import ru.omickron.action.SendStatementAction;

@AllArgsConstructor
public class Bot {
    @NonNull
    private final Config config;

    @SneakyThrows
    public void run() {
        Path path = Paths.get( config.getDirectory() );
        if (!Files.exists( path )) {
            throw new FileNotFoundException( String.format( "Directory '%s' does not exist", path ) );
        }
        ReadStatementsFromDirectoryAction readStatementsFromDirectoryAction = new ReadStatementsFromDirectoryAction();
        Set<Statement> statements = readStatementsFromDirectoryAction.read( path );

        GetCodeAction getCodeAction = new GetCodeAction( config );
        SendStatementAction sendStatementAction = new SendStatementAction( getCodeAction );
        CleanupStatementFilesAction cleanupStatementFilesAction = new CleanupStatementFilesAction();
        statements.stream().map( sendStatementAction :: call ).forEach( cleanupStatementFilesAction :: cleanup );
    }
}
