package models.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import db.DBIntegrityException;
import models.dao.DepartmentDao;
import models.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {


    private final Connection connection = DB.getConnection();

    @Override
    public void insert(Department obj) {

        String query = "INSERT INTO coursejdbc.department (Name) VALUES (?)";

        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, obj.getName());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet resultSet = ps.getGeneratedKeys();
                if (resultSet.next()) {
                    obj.setId(resultSet.getInt("Id"));
                }
                DB.closeResultSet(resultSet);
            } else {
                throw new DBException("Unexpected error! No rows Affected.");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void update(Department obj) {

        String query = "UPDATE coursejdbc.department SET name = ? WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {

        String query = "DELETE FROM coursejdbc.department WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBIntegrityException(e);
        }
    }

    @Override
    public Department findById(Integer id) {

        String query = "SELECT * FROM department WHERE id = ?";
        ResultSet resultSet = null;

        try (PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);
            resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new Department(resultSet.getInt("Id"), resultSet.getString("Name"));
            } else {
                throw new DBIntegrityException("Error");
            }
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Department> findAll() {
        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM coursejdbc.department");
            List<Department> list = new ArrayList<>();

            while (resultSet.next()) {
                list.add(new Department(resultSet.getInt("Id"), resultSet.getString("Name")));
            }
            DB.closeResultSet(resultSet);
            return list;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

}