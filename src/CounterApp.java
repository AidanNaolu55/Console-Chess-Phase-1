import java.awt.*;
import javax.swing.*;

public class CounterApp {
    
    
    private int count = 0;
    private JLabel countLabel;

    public CounterApp() {
        
        JFrame frame = new JFrame("Simple Counter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new BorderLayout());

        
        countLabel = new JLabel("0", SwingConstants.CENTER);
        // Making the font larger so it's easier to read
        countLabel.setFont(new Font("Arial", Font.BOLD, 48)); 
        frame.add(countLabel, BorderLayout.CENTER);

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton decreaseButton = new JButton("Decrease");
        JButton increaseButton = new JButton("Increase");

        
        increaseButton.addActionListener(e -> {
            count++;
            countLabel.setText(String.valueOf(count));
        });

        decreaseButton.addActionListener(e -> {
            count--;
            countLabel.setText(String.valueOf(count));
        });

        
        buttonPanel.add(decreaseButton);
        buttonPanel.add(increaseButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

       
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run the GUI creation on the Event Dispatch Thread (EDT) for thread safety
        SwingUtilities.invokeLater(() -> new CounterApp());
    }
}