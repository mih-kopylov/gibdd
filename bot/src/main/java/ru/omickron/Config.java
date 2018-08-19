package ru.omickron;

import com.beust.jcommander.Parameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {
    @Parameter(names = "-dir", description = "Directory with photos", required = true)
    private String directory;
    @Parameter(names = "-login", description = "Email login", required = true)
    private String mailLogin;
    @Parameter(names = "-password", description = "Email password", required = true)
    private String mailPassword;
}
