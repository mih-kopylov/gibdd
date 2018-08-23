package ru.omickron;

import com.beust.jcommander.Parameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {
    @Parameter(names = "-dir", description = "Directory with photos", required = true)
    private String directory;
    @Parameter(names = "-imap", description = "IMAP Host", required = true)
    private String imapHost;
    @Parameter(names = "-login", description = "Email login", required = true)
    private String mailLogin;
    @Parameter(names = "-password", description = "Email password", required = true)
    private String mailPassword;
    @Parameter(names = "-count", description = "Statements count to process")
    private Integer count;
    @Parameter(names = "-firstName", description = "First Name", required = true)
    private String firstName;
    @Parameter(names = "-lastName", description = "Last Name", required = true)
    private String lastName;
}
