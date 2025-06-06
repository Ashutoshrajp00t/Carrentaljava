package ui;

import dao.CarDAO;
import model.Car;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CarRentalUI extends JFrame {
    private final CarDAO carDAO = new CarDAO();
    private final DefaultTableModel tableModel;
    private final JTable carTable;
    private final JTextField txtId, txtMake, txtModel, txtYear;
    private final JCheckBox chkAvailable;

    public CarRentalUI() {
        setTitle("Car Rental System 🚗");
        setSize(820, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("🚗 Car Rental System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);
        header.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Make", "Model", "Year", "Availability"}, 0);
        carTable = new JTable(tableModel);
        carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(carTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        formPanel.setBorder(new EmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel[] labels = {new JLabel("Car ID:"), new JLabel("Make:"), new JLabel("Model:"), new JLabel("Year:"), new JLabel("Available:")};
        txtId = new JTextField(12);
        txtMake = new JTextField(12);
        txtModel = new JTextField(12);
        txtYear = new JTextField(12);
        chkAvailable = new JCheckBox();

        JComponent[] components = {txtId, txtMake, txtModel, txtYear, chkAvailable};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; formPanel.add(labels[i], gbc);
            gbc.gridx = 1; formPanel.add(components[i], gbc);
        }

        JPanel btnPanel = new JPanel(new GridLayout(6,1,10,10));
        JButton[] buttons = {
                new JButton("Add Car"), new JButton("Update Car"),
                new JButton("Delete Car"), new JButton("Clear Fields"),
                new JButton("Refresh List"), new JButton("Rent Car")
        };
        Color btnColor = new Color(100, 149, 237);
        for (JButton b : buttons) {
            b.setBackground(btnColor);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            btnPanel.add(b);
        }
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);
        add(formPanel, BorderLayout.EAST);

        // Attach actions
        buttons[0].addActionListener(e -> wrapException(this::addCar));
        buttons[1].addActionListener(e -> wrapException(this::updateCar));
        buttons[2].addActionListener(e -> wrapException(this::deleteCar));
        buttons[3].addActionListener(e -> clearFields());
        buttons[4].addActionListener(e -> wrapException(this::refreshTable));
        buttons[5].addActionListener(e -> wrapException(this::rentCar));

        carTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = carTable.getSelectedRow();
                if (r >= 0) {
                    txtId.setText(tableModel.getValueAt(r,0).toString());
                    txtMake.setText(tableModel.getValueAt(r,1).toString());
                    txtModel.setText(tableModel.getValueAt(r,2).toString());
                    txtYear.setText(tableModel.getValueAt(r,3).toString());
                    chkAvailable.setSelected(tableModel.getValueAt(r,4).toString().equals("Available"));
                    txtId.setEnabled(false);
                }
            }
        });

        refreshTable(); // Initial load
        setVisible(true);
    }

    private void wrapException(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Car> cars = carDAO.getAllCars();
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                    car.getId(), car.getMake(), car.getModel(), car.getYear(),
                    car.isAvailable() ? "Available" : "Unavailable"
            });
        }
        clearFields();
    }

    private void addCar() {
        String id = txtId.getText().trim();
        String make = txtMake.getText().trim();
        String model = txtModel.getText().trim();
        int year = parseYear();
        if (id.isEmpty()||make.isEmpty()||model.isEmpty()) throw new IllegalArgumentException("All fields are required.");
        Car car = new Car(id, make, model, year, chkAvailable.isSelected());
        if (carDAO.addCar(car)) {
            JOptionPane.showMessageDialog(this,"Car added!");
            refreshTable();
        } else JOptionPane.showMessageDialog(this,"Failed to add (duplicate ID?)","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void updateCar() {
        if (txtId.getText().isEmpty()) throw new IllegalArgumentException("Select a car to update.");
        Car car = new Car(txtId.getText(), txtMake.getText(), txtModel.getText(), parseYear(), chkAvailable.isSelected());
        if (carDAO.updateCar(car)) {
            JOptionPane.showMessageDialog(this,"Car updated!");
            refreshTable();
        } else JOptionPane.showMessageDialog(this,"Update failed","Error",JOptionPane.ERROR_MESSAGE);
    }

    private void deleteCar() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) throw new IllegalArgumentException("Select a car to delete.");
        if (JOptionPane.showConfirmDialog(this,"Delete car "+id+"?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            if (carDAO.deleteCar(id)) {
                JOptionPane.showMessageDialog(this,"Car deleted!");
                refreshTable();
            } else JOptionPane.showMessageDialog(this,"Delete failed","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rentCar() {
        int r = carTable.getSelectedRow();
        if (r < 0) throw new IllegalArgumentException("Select a car to rent.");
        String id = tableModel.getValueAt(r,0).toString();
        Car car = carDAO.findById(id);
        if (car == null) throw new IllegalStateException("Selected car no longer exists.");
        if (!car.isAvailable()) {
            JOptionPane.showMessageDialog(this,"Car is unavailable","Info",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        car.setAvailable(false);
        if (carDAO.updateCar(car)) {
            JOptionPane.showMessageDialog(this,"Car rented!");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this,"Renting failed","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseYear() {
        try {
            int year = Integer.parseInt(txtYear.getText().trim());
            if (year < 1900 || year > 2100) throw new IllegalArgumentException("Year must be between 1900 and 2100.");
            return year;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Year must be a valid number.");
        }
    }

    private void clearFields() {
        txtId.setText(""); txtMake.setText(""); txtModel.setText("");
        txtYear.setText(""); chkAvailable.setSelected(false);
        txtId.setEnabled(true); carTable.clearSelection();
    }
}
