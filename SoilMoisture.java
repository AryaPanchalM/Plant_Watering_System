import org.firmata4j.Pin;

public class SoilMoisture {
    // Thresholds
    private static final int DRY = 600;
    private static final int SEMI_DRY = 500;

    private Pin pin;
    private int reading;

    public SoilMoisture(Pin pin) {
        this.pin = pin;
        this.reading = 0;
    }

    // Update reading from sensor
    public void update() {
        try {
            reading = (int) pin.getValue();
        } catch (Exception e) {
            System.out.println("Sensor read error: " + e);
        }
    }

    // Get raw value
    public int getValue() {
        return reading;
    }

    // Interpret moisture level
    public String getStatus() {
        if (reading > DRY) {
            return "dry";
        }

        if (reading > SEMI_DRY) {
            return "slightly dry";
        }

        return "wet";
    }

    // Check if soil needs water
    public boolean needsWater() {
        return reading > SEMI_DRY;
    }
}
