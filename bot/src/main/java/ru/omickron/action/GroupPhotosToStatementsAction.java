package ru.omickron.action;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NonNull;
import ru.omickron.Statement;

public class GroupPhotosToStatementsAction {
    private static final int ACCURACY = 2;

    @NonNull
    public List<Statement> call( @NonNull Collection<Path> photos ) {
        Map<LocalDateTime, List<Path>> groupedByMinutePhotos = new HashMap<>();
        photos.stream().sorted().forEach( photo -> {
            String fileName = photo.getFileName().toString().toLowerCase();
            LocalDateTime photoTime =
                    LocalDateTime.parse( fileName, DateTimeFormatter.ofPattern( "yyyy-MM-dd HH-mm-ss'.jpg'" ) )
                            .withSecond( 0 );
            LocalDateTime adjustedTime = adjustTime( groupedByMinutePhotos.keySet(), photoTime );
            groupedByMinutePhotos.computeIfAbsent( adjustedTime, o -> new ArrayList<>() ).add( photo );
        } );

        return groupedByMinutePhotos.entrySet()
                .stream()
                .map( o -> createStatement( o.getKey(), o.getValue() ) )
                .collect( Collectors.toList() );
    }

    @NonNull
    private LocalDateTime adjustTime( @NonNull Set<LocalDateTime> usedTimes, @NonNull LocalDateTime photoTime ) {
        for (int i = -ACCURACY; i < ACCURACY; i++) {
            LocalDateTime adjustedTime = photoTime.plusMinutes( i );
            if (usedTimes.contains( adjustedTime )) {
                return adjustedTime;
            }
        }
        return photoTime;
    }

    @NonNull
    private Statement createStatement( @NonNull LocalDateTime time, @NonNull List<Path> photos ) {
        String directoryName = photos.get( 0 ).getParent().getFileName().toString();
        int firstSpace = directoryName.indexOf( " " );
        String number = directoryName.substring( 0, firstSpace ).trim();
        String address = directoryName.substring( firstSpace ).trim();

        Statement result = new Statement();
        result.setNumber( number );
        result.setAddress( address );
        result.setTime( time );
        result.setPhotos( photos );
        return result;
    }
}
