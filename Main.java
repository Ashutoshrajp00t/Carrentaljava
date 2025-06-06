import ui.CarRentalUI;

public class Main {
    public static void main(String[] args) {
        // Swing UI must run on Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> new CarRentalUI());
    }
}
