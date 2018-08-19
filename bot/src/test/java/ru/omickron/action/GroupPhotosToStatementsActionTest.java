package ru.omickron.action;

import com.google.common.collect.ImmutableList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;
import org.junit.Test;
import ru.omickron.Statement;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class GroupPhotosToStatementsActionTest {
    private final GroupPhotosToStatementsAction action = new GroupPhotosToStatementsAction();

    @Test
    public void singleFileToSingleStatement() {
        List<Path> photos = ImmutableList.of( Paths.get( "C:\\а111аа178 Улица 5\\2018-01-01 15-00-00.jpg" ) );
        List<Statement> statements = action.call( photos );

        assertThat( statements, hasSize( 1 ) );
        checkStatement( statements.get( 0 ), "а111аа178", "Улица 5", LocalDateTime.of( 2018, 1, 1, 15, 0 ) );
    }

    @Test
    public void timeSecondsAreIgnored() {
        List<Path> photos = ImmutableList.of( Paths.get( "C:\\а111аа178 Улица 5\\2018-01-01 15-00-15.jpg" ) );
        List<Statement> statements = action.call( photos );

        assertThat( statements, hasSize( 1 ) );
        checkStatement( statements.get( 0 ), "а111аа178", "Улица 5", LocalDateTime.of( 2018, 1, 1, 15, 0 ) );
    }

    @Test
    public void closeTimesUnionedToSingleStatement() {
        List<Path> photos = ImmutableList.of( Paths.get( "C:\\а111аа178 Улица 5\\2018-01-01 15-00-16.jpg" ),
                Paths.get( "C:\\а111аа178 Улица 5\\2018-01-01 15-00-15.jpg" ) );
        List<Statement> statements = action.call( photos );

        assertThat( statements, hasSize( 1 ) );
        checkStatement( statements.get( 0 ), "а111аа178", "Улица 5", LocalDateTime.of( 2018, 1, 1, 15, 0 ) );
    }

    @Test
    public void farTimesGiveTwoStatements() {
        List<Path> photos = ImmutableList.of( Paths.get( "C:\\а111аа178 Улица 5\\2018-01-01 15-00-15.jpg" ),
                Paths.get( "C:\\а111аа178 Улица 5\\2018-01-01 15-10-15.jpg" ) );
        List<Statement> statements = action.call( photos );

        assertThat( statements, hasSize( 2 ) );
        checkStatement( statements.get( 0 ), "а111аа178", "Улица 5", LocalDateTime.of( 2018, 1, 1, 15, 0 ) );
        checkStatement( statements.get( 1 ), "а111аа178", "Улица 5", LocalDateTime.of( 2018, 1, 1, 15, 10 ) );
    }

    private void checkStatement( @NonNull Statement statement, @NonNull String number, @NonNull String address,
            @NonNull LocalDateTime time ) {
        assertThat( statement.getNumber(), equalTo( number ) );
        assertThat( statement.getAddress(), equalTo( address ) );
        assertThat( statement.getTime(), equalTo( time ) );
    }
}