package ru.omickron;

import com.beust.jcommander.JCommander;

public class Main {
    public static void main( String[] args ) {
        Config config = new Config();
        JCommander.newBuilder().addObject( config ).build().parse( args );
        new Bot( config ).run();
    }
}
