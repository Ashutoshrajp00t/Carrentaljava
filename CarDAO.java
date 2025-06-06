package dao;

import model.Car;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getString("id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            logSQLException(e, "Error fetching all cars");
        }
        return cars;
    }

    public boolean addCar(Car car) {
        String sql = "INSERT INTO cars (id, make, model, year, is_available) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, car.getId());
            ps.setString(2, car.getMake());
            ps.setString(3, car.getModel());
            ps.setInt(4, car.getYear());
            ps.setBoolean(5, car.isAvailable());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logSQLException(e, "Error adding car " + car.getId());
            return false;
        }
    }

    public boolean updateCar(Car car) {
        String sql = "UPDATE cars SET make=?, model=?, year=?, is_available=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.setBoolean(4, car.isAvailable());
            ps.setString(5, car.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logSQLException(e, "Error updating car " + car.getId());
            return false;
        }
    }

    public boolean deleteCar(String id) {
        String sql = "DELETE FROM cars WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            logSQLException(e, "Error deleting car " + id);
            return false;
        }
    }

    public Car findById(String id) {
        String sql = "SELECT * FROM cars WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Car(
                            rs.getString("id"),
                            rs.getString("make"),
                            rs.getString("model"),
                            rs.getInt("year"),
                            rs.getBoolean("is_available")
                    );
                }
            }
        } catch (SQLException e) {
            logSQLException(e, "Error finding car " + id);
        }
        return null;
    }

    // Utility to log SQLExceptions
    private void logSQLException(SQLException e, String msg) {
        System.err.println(msg + " - SQLState: " + e.getSQLState() + ", ErrorCode: " + e.getErrorCode());
        e.printStackTrace();
    }
}
