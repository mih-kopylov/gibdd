package ru.omickron;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Statement implements Comparable<Statement> {
    @NonNull
    private String number;
    @NonNull
    private String address;
    @NonNull
    private LocalDateTime time;
    @NonNull
    private List<Path> photos = new ArrayList<>();

    @Override
    public int compareTo( @NonNull Statement other ) {
        return Comparator.comparing( Statement :: getTime )
                .thenComparing( Statement :: getNumber )
                .thenComparing( Statement :: getAddress )
                .compare( this, other );
    }
}
