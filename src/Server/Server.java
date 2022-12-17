package Server;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame implements ActionListener {
    private JTextField port;
    private JButton startButton;

    public Server() {
        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        //header
        JLabel header= new JLabel("Server");
        header.setFont(new Font("Gill Sans MT", Font.PLAIN, 35));
        header.setForeground(Color.orange);
        header.setAlignmentX(CENTER_ALIGNMENT);

        //form
        JPanel form = new JPanel();
        form.setPreferredSize(new Dimension(120,150));
        form.setLayout(new BoxLayout(form, BoxLayout.X_AXIS));

        JPanel textPanel = new JPanel();
        textPanel.setPreferredSize(new Dimension(-120, 80));
        textPanel.setLayout(null);
        JLabel portLabel = new JLabel("Port");
        portLabel.setBounds(38,40,100,50);
        portLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
        portLabel.setPreferredSize(new Dimension(70, 30));
        textPanel.add(portLabel);

        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setPreferredSize(new Dimension(100, 70));
        textFieldPanel.setLayout(null);
        port = new JTextField("");
        port.setFont(new Font("Gill Sans MT", Font.PLAIN, 15));
        port.setBounds(0,50,280,30);
        port.setPreferredSize(new Dimension(100, 30));
        textFieldPanel.add(port);

        form.add(textPanel);
        form.add(textFieldPanel);

        //Connect button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(81,80,106));

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setFocusable(false);
        startButton.setAlignmentX(CENTER_ALIGNMENT);
        startButton.setForeground(Color.RED);
        startButton.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        startButton.setBackground(Color.WHITE);
        bottomPanel.add(startButton);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(header);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(form);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(bottomPanel);

        this.setTitle("Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
