package crud.usuarios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.manolito.entidades.Usuario;

public class DaoImpl implements Dao {

	public DaoImpl() {
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(utilidades.JDBC.URL, utilidades.JDBC.USER, utilidades.JDBC.PASSWORD);
	}

	public void create(Usuario usuario) {

		Connection conn = null;
		try {
			conn = getConnection();
			String sqlDas = "SELECT MAX(DAS) FROM PROYECTO.USUARIOS";
			// int das = Integer.parseInt(sqlDas);

			String insert = "INSERT INTO USUARIOS (DAS, NOMBRE, APELLIDOS, PASSWORD, ROL, PASSCAMBIADA VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sqlDas);
			PreparedStatement ps1 = conn.prepareStatement(insert);

			ResultSet rs = ps.executeQuery(sqlDas);
			int das = rs.getInt(1);
			das++;
			ps1.setInt(1, das);
			ps1.setString(2, usuario.getNombre());
			ps1.setString(3, usuario.getApellido());
			ps1.setString(4, usuario.getPassword());
			ps1.setString(5, usuario.getRol());
			ps1.setString(6, usuario.getPasscambiada());

			rs.close();
			ps1.executeQuery();

		} catch (SQLException e) {
			System.err.println("Fallo al insertar en bbdd");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.err.println("Fallo al cerrar la conexion");
				}
			}
		}

	}

	public Usuario read(int dasUsuario) {

		Connection conn = null;
		Usuario usuario = null;
		try {

			conn = getConnection();

			String read = "SELECT DAS, NOMBRE, APELLIDOS, ROL FROM USUARIOS WHERE DAS = ?";

			PreparedStatement ps = conn.prepareStatement(read);

			ps.setInt(1, dasUsuario);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int das = rs.getInt("DAS");
				String nombre = rs.getString("NOMBRE");
				String apellido = rs.getString("APELLIDOS");
				String rol = rs.getString("ROL");

				usuario = new Usuario(das, nombre, apellido, rol);
			} else {
				System.err.println("No existe usuario con das: " + dasUsuario);
			}

		} catch (SQLException e) {
			System.err.println("Fallo al leer de bbdd");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.err.println("Fallo al cerrar la conexion");
				}
			}
		}
		return usuario;

	}

	public void update(Usuario usuario) {

		Connection conn = null;

		try {
			conn = getConnection();

			String update = "UPDATE USUARIOS SET NOMBRE=?, APELLIDOS=?, ROL=?, PASSWORD=? WHERE DAS=?";

			PreparedStatement ps = conn.prepareStatement(update);

			ps.setString(1, usuario.getNombre());
			ps.setString(2, usuario.getApellido());
			ps.setString(3, usuario.getRol());
			ps.setString(4, usuario.getPassword());
			ps.setInt(5, usuario.getDas());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println("Fallo al actualizar campos de bbdd");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.err.println("Fallo al cerrar la conexion");
				}
			}
		}

	}

	public void delete(Usuario usuario) {

		Connection conn = null;
		try {
			conn = getConnection();

			String delete = "DELETE FROM USUARIOS WHERE DAS = ?";
			PreparedStatement ps = conn.prepareStatement(delete);

			ps.setInt(1, usuario.getDas());

			ps.executeUpdate();

		} catch (SQLException e) {
			System.err.println("Fallo al borrar campos de bbdd");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.err.println("Fallo al cerrar la conexion");
				}
			}
		}

	}

	public List<Usuario> readAll() {
		Connection conn = null;
		List<Usuario> usuarios= null;
		try {
			conn = getConnection();
			String select_all = "SELECT DAS, NOMBRE, APELLIDOS, ROL, PASSCAMBIADA FROM USUARIOS";
			PreparedStatement ps = conn.prepareStatement(select_all);

			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {

				usuarios = new ArrayList<Usuario>();

				int das = 0;
				String nombre = null;
				String apellido = null;
				String rol = null;
				String passcambiada = null;

				do {
					das = rs.getInt("DAS");
					nombre = rs.getString("NOMBRE");
					apellido = rs.getString("APELLIDOS");
					rol = rs.getString("ROL");
					passcambiada = rs.getString("PASSCAMBIADA");

					usuarios.add(new Usuario(das, nombre, apellido, rol, passcambiada));

				} while (rs.next());


			} else {
				System.err.println("No existe usuario");
			}

		} catch (SQLException e) {
			System.err.println("Fallo al borrar campos de bbdd");
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.err.println("Fallo al cerrar la conexion");
				}
			}
		}
		return usuarios;
		
	}

}
