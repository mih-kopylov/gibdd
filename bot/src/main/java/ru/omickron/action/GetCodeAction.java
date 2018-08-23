package ru.omickron.action;

import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import ru.omickron.Config;

@AllArgsConstructor
public class GetCodeAction {
    private static final String CODE_REGEXP = ">([А-Я0-9]{5})<";
    private static final String CHECK_CODE_SENDER_EMAIL = "appeal@noreply.mvd.ru";
    private static final String CHECK_CODE_SUBJECT = "Проверка почты";
    private static final int MAX_TRIES = 20;
    @NonNull
    private final Config config;

    @NonNull
    @SneakyThrows
    public String getCode() {
        int tryNumber = 0;
        while (true) {
            tryNumber++;
            Session session = Session.getDefaultInstance( new Properties() );
            Store store = session.getStore( "imaps" );
            store.connect( config.getImapHost(), 993, config.getMailLogin(), config.getMailPassword() );
            try {
                Folder folder = store.getFolder( "INBOX" );
                folder.open( Folder.READ_WRITE );
                try {
                    Optional<Message> messageOptional =
                            Arrays.stream( folder.getMessages() ).filter( this :: isConfirmationMessage ).findAny();
                    if (messageOptional.isPresent()) {
                        Message message = messageOptional.get();
                        String code = getCode( message );
                        message.setFlag( Flags.Flag.DELETED, true );
                        return code;
                    }
                } finally {
                    folder.close( true );
                }
            } finally {
                store.close();
            }
            if (tryNumber > MAX_TRIES) {
                throw new RuntimeException( "Can't get confirmation code from email" );
            }
            TimeUnit.SECONDS.sleep( 1 );
        }
    }

    @NonNull
    @SneakyThrows
    private String getCode( @NonNull Message message ) {
        String content = message.getContent().toString();
        Matcher matcher = Pattern.compile( CODE_REGEXP ).matcher( content );
        if (matcher.find()) {
            return matcher.group( 1 );
        }
        throw new RuntimeException( "Message does not contain any code" );
    }

    @SneakyThrows
    private boolean isConfirmationMessage( @NonNull Message message ) {
        boolean senderMatches = Arrays.stream( message.getFrom() )
                .map( InternetAddress.class :: cast )
                .map( InternetAddress :: getAddress )
                .anyMatch( o -> o.equals( CHECK_CODE_SENDER_EMAIL ) );
        boolean subjectMatches = message.getSubject().equals( CHECK_CODE_SUBJECT );
        return senderMatches && subjectMatches;
    }
}
