import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.IODevice;
import org.firmata4j.Pin;
import org.firmata4j.I2CDevice;
import org.firmata4j.ssd1306.SSD1306;
import javax.swing.*;
import java.util.Timer;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MainSystem {
    public static void main(String[] args) {
        // Constants
        final String COM = "COM5";
        final int SOIL_PIN = 15;
        final int RELAY_PIN = 2;
        final byte OLED_ADDR = 0x3C;

        // Initialize device
        IODevice board = null;
        try {
            board = new FirmataDevice(COM);
            board.start();
            board.ensureInitializationIsDone();
            System.out.println("Board OK");

            // Configure hardware
            setupAndRun(board, SOIL_PIN, RELAY_PIN, OLED_ADDR);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void setupAndRun(IODevice board, int soilPin, int relayPin, byte oledAddr) {
        try {
            // Setup pins
            Pin soil = board.getPin(soilPin);
            soil.setMode(Pin.Mode.ANALOG);

            Pin relay = board.getPin(relayPin);
            relay.setMode(Pin.Mode.OUTPUT);

            // Setup OLED
            I2CDevice i2c = board.getI2CDevice(oledAddr);
            SSD1306 screen = new SSD1306(i2c, SSD1306.Size.SSD1306_128_64);
            screen.init();

            // Init components
            SoilMoisture moisture = new SoilMoisture(soil);
            Pump motor = new Pump(relay);
            OLED display = new OLED(screen);

            // Create and schedule system
            Watering system = new Watering(moisture, motor, display);
            Timer t = new Timer();
            t.schedule(system, 0, 5000);

            // Initialize graph for live soil moisture levels
            XYSeries series = new XYSeries("Soil Moisture");
            XYSeriesCollection dataset = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Soil Moisture Levels",
                    "Time (s)",
                    "Moisture Level",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
            );

            // Set up JFrame to display the chart
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
            JFrame frame = new JFrame("Live Soil Moisture Graph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(chartPanel);
            frame.pack();
            frame.setVisible(true);

            // Track time and update graph
            int timeElapsed = 0;
            while (timeElapsed < 60) { // Run for 1 minute
                int moistureValue = moisture.getValue();  // Get soil moisture reading
                series.add(timeElapsed, moistureValue);  // Update graph
                timeElapsed += 5;
                Thread.sleep(5000);  // Wait for 5 seconds
            }

            // Clean up
            t.cancel();
            motor.stop();
            display.write("System stopped");
            board.stop();

        } catch (Exception e) {
            System.out.println("Setup error: " + e.getMessage());
        }
    }
}
