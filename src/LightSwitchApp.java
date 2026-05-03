import java.awt.*;
import javax.swing.*;

public class LightSwitchApp {

    // A boolean to keep track of the light's current state
    private boolean isLightOn = false;
    private JLabel stateLabel;

    public LightSwitchApp() {
        
        JFrame frame = new JFrame("Light Switch Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());

        
        stateLabel = new JLabel("Light is OFF", SwingConstants.CENTER);
        stateLabel.setFont(new Font("Arial", Font.BOLD, 32));
        frame.add(stateLabel, BorderLayout.CENTER);

        
        JButton switchButton = new JButton("Toggle Light");
        switchButton.setFont(new Font("Arial", Font.PLAIN, 18));

        
        switchButton.addActionListener(e -> {
            // Flip the boolean state
            isLightOn = !isLightOn;
            
            // Update the label based on the new state
            if (isLightOn) {
                stateLabel.setText("Light is ON");
            } else {
                stateLabel.setText("Light is OFF");
            }
        });

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(switchButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run the GUI safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new LightSwitchApp());
    }
}