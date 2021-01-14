package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement ps = null;

		Connection conn = DB.getConnection();

		try {
			ps = conn.prepareStatement(
					"INSERT INTO department " 
					+ "(Name) "
					+ "VALUES " 
					+ "(?)"
					, Statement.RETURN_GENERATED_KEYS
				);

			ps.setString(1, obj.getName());			

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();

				if (rs.next()) {
					obj.setId(rs.getInt(1));
				}

				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected Error: No rows affected");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement ps = null;
		Connection conn = DB.getConnection();

		try {
			ps = conn.prepareStatement(
					"UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?"
				);

			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement ps = null;

		Connection conn = DB.getConnection();

		try {
			ps = conn.prepareStatement(
						"DELETE FROM department "
						+ "WHERE Id = ?"
					);

			ps.setInt(1, id);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"SELECT * " 
					+ "FROM department "					 
					+ "WHERE Id = ?"
				);

			ps.setInt(1, id);

			rs = ps.executeQuery();

			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}

			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = conn.prepareStatement(
					"SELECT * " 
					+ "FROM department "
				);

			rs = ps.executeQuery();

			List<Department> listDepartments = new ArrayList<>();
			
			while (rs.next()) {

				Department dep = instantiateDepartment(rs);
				listDepartments.add(dep);
			}

			return listDepartments;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
}
