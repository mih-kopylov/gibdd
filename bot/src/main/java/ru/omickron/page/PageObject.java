package ru.omickron.page;

import lombok.Getter;
import lombok.NonNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Getter
public abstract class PageObject {
    @NonNull
    private final WebDriver webDriver;

    public PageObject( @NonNull WebDriver webDriver ) {
        this.webDriver = webDriver;
        PageFactory.initElements( webDriver, this );
    }
}
