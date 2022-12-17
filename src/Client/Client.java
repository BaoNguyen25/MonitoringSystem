package Client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
    private JTextField port, ip, name;
    private JButton connectButton;

    public Client() {
        Container container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        //header
        JLabel header= new JLabel("Client");
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
        JLabel ipLabel = new JLabel("IP");
        ipLabel.setBounds(42,0,50,50);
        JLabel portLabel = new JLabel("Port");
        portLabel.setBounds(38,50,100,50);
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(35,100,80,50);

        ipLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
        portLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
        nameLabel.setFont(new Font("Gill Sans MT", Font.BOLD, 15));
        ipLabel.setPreferredSize(new Dimension(70, 30));
        portLabel.setPreferredSize(new Dimension(70, 30));
        nameLabel.setPreferredSize(new Dimension(70,30));
        textPanel.add(ipLabel);
        textPanel.add(portLabel);
        textPanel.add(nameLabel);


        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setPreferredSize(new Dimension(100, 70));
        textFieldPanel.setLayout(null);
        ip = new JTextField("");
        port = new JTextField("");
        name = new JTextField("");
        ip.setBounds(0,12,280,30);
        ip.setFont(new Font("Gill Sans MT", Font.PLAIN, 15));
        ip.setPreferredSize(new Dimension(100, 30));
        port.setFont(new Font("Gill Sans MT", Font.PLAIN, 15));
        port.setBounds(0,62,280,30);
        port.setPreferredSize(new Dimension(100, 30));
        name.setFont(new Font("Gill Sans MT", Font.PLAIN, 15));
        name.setBounds(0,112,280,30);
        name.setPreferredSize(new Dimension(100, 30));
        textFieldPanel.add(port);
        textFieldPanel.add(ip);
        textFieldPanel.add(name);

        form.add(textPanel);
        form.add(textFieldPanel);


        //Connect button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(81,80,106));

        connectButton = new JButton("Connect");
        connectButton.addActionListener(this);
        connectButton.setFocusable(false);
        connectButton.setAlignmentX(CENTER_ALIGNMENT);
        connectButton.setForeground(Color.RED);
        connectButton.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        connectButton.setBackground(Color.WHITE);
        bottomPanel.add(connectButton);

        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(header);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(form);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(bottomPanel);

        this.setTitle("Client");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}