import java.util.TimerTask;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Watering extends TimerTask {
    // Components
    private SoilMoisture soil;
    private Pump pump;
    private OLED display;

    // Data storage
    private Map<LocalDateTime, Integer> history;

    // Constructor
    public Watering(SoilMoisture soil, Pump pump, OLED display) {
        this.soil = soil;
        this.pump = pump;
        this.display = display;
        this.history = new HashMap<>();
    }

    @Override
    public void run() {
        // Get data
        soil.update();
        int val = soil.getValue();
        String status = soil.getStatus();

        // Log data
        history.put(LocalDateTime.now(), val);
        log("Moisture: " + val + ", Status: " + status);

        // Handle irrigation
        if (status.equals("dry")) {
            handleDrySoil();
        } else if (status.equals("slightly dry")) {
            handlePartialDry();
        } else {
            handleWetSoil();
        }
    }

    // Handle completely dry condition
    private void handleDrySoil() {
        log("ALERT: Soil dry - watering");
        pump.start();
        display.write("Soil dry");
        pause(2000);
    }

    // Handle partially dry condition
    private void handlePartialDry() {
        log("INFO: Soil slightly dry - light watering");
        pump.start();
        display.write("Soil partial dry");
        pause(1000);
    }

    // Handle wet condition
    private void handleWetSoil() {
        log("INFO: Soil wet - no watering needed");
        pump.stop();
        display.write("Soil wet");
    }

    // Helper method for sleeping
    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            log("Sleep error");
        }
    }

    // Helper method for logging
    private void log(String msg) {
        System.out.println("[" + LocalDateTime.now() + "] " + msg);
    }
}
