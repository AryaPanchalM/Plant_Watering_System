import org.firmata4j.Pin;

public class Pump {
    private Pin pin;
    private boolean isRunning;

    public Pump(Pin pin) {
        this.pin = pin;
        this.isRunning = false;
    }

    /**
     * Start the water pump
     */
    public void start() {
        if (!isRunning) {
            try {
                pin.setValue(1);
                isRunning = true;
                System.out.println("Pump started");
            } catch (Exception e) {
                System.out.println("Failed to start pump: " + e.getMessage());
            }
        }
    }

    /**
     * Stop the water pump
     */
    public void stop() {
        if (isRunning) {
            try {
                pin.setValue(0);
                isRunning = false;
                System.out.println("Pump stopped");
            } catch (Exception e) {
                System.out.println("Failed to stop pump: " + e.getMessage());
            }
        }
    }

    /**
     * Check if pump is currently running
     */
    public boolean isActive() {
        return isRunning;
    }

    /**
     * Toggle pump state
     */
    public void toggle() {
        if (isRunning) {
            stop();
        } else {
            start();
        }
    }
}
