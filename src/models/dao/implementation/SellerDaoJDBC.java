package models.dao.implementation;

import db.DB;
import db.DBException;
import models.dao.SellerDao;
import models.entities.Department;
import models.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private final Connection connection = DB.getConnection();

    private Seller instantiationSeller(ResultSet resultSet, Department department) throws SQLException {
        return new Seller(resultSet.getInt("Id"),
                resultSet.getString("Name"),
                resultSet.getString("Email"),
                resultSet.getDate("BirthDate"),
                resultSet.getDouble("BaseSalary"),
                department);
    }

    private Department instantiationDepartment(ResultSet resultSet) throws SQLException {
        return new Department(resultSet.getInt("DepartmentId"), resultSet.getString("DepName"));
    }

    @Override
    public void insert(Seller obj) {

        String query = "INSERT INTO coursejdbc.Seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());

            if (ps.executeUpdate() > 0) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    obj.setId(resultSet.getInt(1));
                }
                DB.closeResultSet(resultSet);
                System.out.println("Done! New id = " + obj.getId());
            } else {
                throw new DBException("Unexpected error! No rows Affected.");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void update(Seller obj) {

        String query = "UPDATE coursejdbc.seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, obj.getName());
            ps.setString(2, obj.getEmail());
            ps.setDate(3, new Date(obj.getBirthDate().getTime()));
            ps.setDouble(4, obj.getBaseSalary());
            ps.setInt(5, obj.getDepartment().getId());
            ps.setInt(6, obj.getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Done! Rows affected: " + rowsAffected);
            } else {
                System.out.println("No Rows Affected!");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Integer id) {

        String query = "DELETE FROM coursejdbc.seller WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Done! Rows affected: " + rowsAffected);
            } else {
                throw new DBException("No rows affected");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Seller findById(Integer id) {

        String query = "SELECT seller.*, department.Name AS DepName FROM seller "
                + "INNER JOIN department ON seller.DepartmentId = department.Id WHERE seller.Id = ?";
        ResultSet resultSet = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                Department department = instantiationDepartment(resultSet);
                return instantiationSeller(resultSet, department);
            }
            return null;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findAll() {

        ResultSet resultSet = null;

        try (Statement statement = connection.createStatement()) {
            List<Seller> list = new ArrayList<>();

            resultSet = statement.executeQuery("SELECT seller.*, department.Name as DepName FROM seller "
                    + "INNER JOIN department ON seller.DepartmentId = department.Id");

            while (resultSet.next()) {
                Department department = instantiationDepartment(resultSet);
                Seller seller = instantiationSeller(resultSet, department);
                list.add(seller);
            }
            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {

        String query = "SELECT seller.*, department.Name as DepName FROM seller "
                + "INNER JOIN department ON seller.DepartmentId = department.Id WHERE DepartmentId = ? ORDER BY Id ASC";
        ResultSet resultSet = null;
        List<Seller> list = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, department.getId());

            department = null;
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                if (department == null) {
                    department = instantiationDepartment(resultSet);
                }
                list.add(instantiationSeller(resultSet, department));
            }
            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeResultSet(resultSet);
        }
    }

}