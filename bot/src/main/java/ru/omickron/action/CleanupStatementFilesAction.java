package ru.omickron.action;

import java.nio.file.Files;
import java.nio.file.Path;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.omickron.Statement;

public class CleanupStatementFilesAction {
    @SneakyThrows
    public void cleanup( @NonNull Statement statement ) {
        Path directory = statement.getPhotos()
                .stream()
                .map( Path :: getParent )
                .distinct()
                .findAny()
                .orElseThrow( RuntimeException ::new );
        for (Path photo : statement.getPhotos()) {
            Files.delete( photo );
        }
        long filesLeft = Files.walk( directory ).filter( Files :: isRegularFile ).count();
        if (filesLeft == 0) {
            Files.delete( directory );
        }
    }
}
