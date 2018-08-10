package ru.omickron;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Statement {
    @NonNull
    private String number;
    @NonNull
    private String address;
    @NonNull
    private LocalDateTime time;
    @NonNull
    private List<Path> photos = new ArrayList<>();
}
