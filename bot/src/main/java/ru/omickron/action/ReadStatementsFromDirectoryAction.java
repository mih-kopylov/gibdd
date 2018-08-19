package ru.omickron.action;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.omickron.Statement;

public class ReadStatementsFromDirectoryAction {
    @NonNull
    @SneakyThrows
    public List<Statement> read( @NonNull Path directory ) {
        return Files.walk( directory )
                .filter( o -> !o.equals( directory ) )
                .filter( Files :: isDirectory )
                .map( this :: readStatementsFromDirectory )
                .flatMap( Collection :: stream )
                .collect( Collectors.toList() );
    }

    @NonNull
    @SneakyThrows
    private List<Statement> readStatementsFromDirectory( @NonNull Path directory ) {
        List<Path> photos = Files.walk( directory ).filter( this :: isPhotoFile ).collect( Collectors.toList() );
        return new GroupPhotosToStatementsAction().call( photos );
    }

    private boolean isPhotoFile( @NonNull Path path ) {
        return path.toString().toLowerCase().endsWith( ".jpg" );
    }
}
