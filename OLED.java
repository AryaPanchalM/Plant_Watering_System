import org.firmata4j.ssd1306.SSD1306;

public class OLED {
    private SSD1306 display;

    public OLED(SSD1306 display) {
        this.display = display;
    }

    /**
     * Write message to display
     */
    public void write(String text) {
        clearScreen();
        display.getCanvas().setTextsize(1);
        display.getCanvas().drawString(0, 0, text);
        display.display();
    }

    /**
     * Clear the display
     */
    public void clearScreen() {
        display.clear();
        display.display();
    }

    /**
     * Write two lines of text
     */
    public void write(String line1, String line2) {
        clearScreen();
        display.getCanvas().setTextsize(1);
        display.getCanvas().drawString(0, 0, line1);
        display.getCanvas().drawString(0, 10, line2);
        display.display();
    }

    /**
     * Write text with custom size
     */
    public void write(String text, int size) {
        clearScreen();
        display.getCanvas().setTextsize(size);
        display.getCanvas().drawString(0, 0, text);
        display.display();
    }
}
