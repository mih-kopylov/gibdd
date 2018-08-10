package ru.omickron;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;

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
        List<Statement> statements = Files.walk( path )
                .filter( Files :: isDirectory )
                .map( this :: readStatementsFromDirectory )
                .flatMap( Collection :: stream )
                .collect( Collectors.toList() );
    }

    @NonNull
    @SneakyThrows
    private List<Statement> readStatementsFromDirectory( @NonNull Path path ) {
        List<Path> photos = Files.walk( path ).filter( this :: isPhotoFile ).collect( Collectors.toList() );
        return Collections.emptyList();
    }

    private boolean isPhotoFile( @NonNull Path path ) {
        return path.toString().toLowerCase().endsWith( ".jpg" );
    }
}
