package vitalcenter.osgi.persistence.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import vitalcenter.osgi.persistence.models.Client;
import vitalcenter.osgi.persistence.models.TemplateInfo;

/**
 * Singleton para la conexion a la base de dato.
 * 
 * @author cursor
 */
public class DBBridge {

	// public static String config =
	// "F:/dev/EOSWORKS/repositories/Vitalcenter/biometric/templates.db";
	public static String config = "C:/vitalcenter/templates.db";
	/**
	 * Consulta de insercion de datos de asistencia.
	 */
	private static final String SQL_INS_ASISTENCIA = "insert into asistencia(rut, hora, fecha) values (?, ?, ?)";

	/**
	 * Consulta que hace la busqueda de un rut.
	 */
	private static final String SQL_FND_CONTRATO = "select rut, folio, nombre, fecha_Inicio, fecha_Termino, fecha_congela, estado from Contratos where rut = ? order by fecha_Termino DESC";

	
	private static final String SQL_INS_TEMPLATE = "insert into templates (rut, finger, template) values (?, ?, ?)";
	private static final String SQL_DEL_TEMPLATE = "delete from templates where rut=?";
	private static final String SQL_FND_TEMPLATE = "select rut, template, finger from templates where rut=?";
	private static final String SQL_FND_TEMPLATES = "select rut, template, finger from templates";

	/**
	 * Instancia unicade la conexion de esta calse.
	 */
	private static DBBridge instance = null;

	/**
	 * Campo que indica si la conexion esta OK.
	 */
	private boolean connectedVitalcenter = false;
	private boolean connectedTemplates = false;
	/**
	 * Conexiona a la base de datos.
	 */
	private Connection dbVitalcenter = null;
	private Connection dbTemplates = null;

	/**
	 * Constructor de la clase.
	 */
	private DBBridge() {
		loadProperties();
		connectVitalcenter();
		connectTemplates();
	}

	private void loadProperties() {
		String sName = System.getProperty("user.home") + "\\config.ini";
		File file = new File(sName);
		if (!file.exists()) {
			sName = System.getProperty("user.dir") + "\\config.ini";
			file = new File(sName);
		}
		FileReader reader;
		try {
			reader = new FileReader(file);
			Properties properties = new Properties();
			properties.load(reader);
			config = properties.getProperty("templates", config);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static DBBridge getInstance() {
		if (instance == null) {
			instance = new DBBridge();
		}
		return instance;
	}

	public boolean connectVitalcenter() {
		try {

			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String url = "jdbc:odbc:vitalcenter";

			if (this.dbVitalcenter != null) {
				this.dbVitalcenter.close();
				this.dbVitalcenter = null;
			}
			this.dbVitalcenter = DriverManager.getConnection(url, "", "");
			this.connectedVitalcenter = true;
		} catch (Exception e) {
			this.connectedVitalcenter = false;
		}
		return this.connectedVitalcenter;
	}

	public boolean connectTemplates() {
		try {

			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:" + config;

			if (this.dbTemplates != null) {
				this.dbTemplates.close();
				this.dbTemplates = null;
			}
			this.dbTemplates = DriverManager.getConnection(url);
			this.connectedTemplates = true;
		} catch (Exception e) {
			this.connectedTemplates = false;
		}
		return this.connectedTemplates;
	}

	public boolean close() {
		boolean bExito = false;
		try {
			if (this.dbVitalcenter != null) {
				this.dbVitalcenter.close();
			}
			this.dbVitalcenter = null;
			this.connectedVitalcenter = false;
			bExito = true;
		} catch (Exception e) {
			this.dbVitalcenter = null;
			this.connectedVitalcenter = false;
			bExito = false;
		}
		return bExito;
	}

	public boolean isConnectedVitalcenter() {
		return this.connectedVitalcenter;
	}

	public boolean isConnectedTemplates() {
		return this.connectedTemplates;
	}

	public Client getClient(String rut) throws SQLException {
		Client client = null;
		if (dbVitalcenter != null) {
			PreparedStatement pstmt = dbVitalcenter.prepareStatement(SQL_FND_CONTRATO);
			pstmt.setString(1, rut);
			ResultSet res = pstmt.executeQuery();
			while (res.next()) {
				//rut, folio, nombre, fecha_Inicio, fecha_Termino, fecha_congela, estado
				if (client == null) {
					client = new Client();
					client.setRut(res.getString(1));
					client.setId(res.getLong(2));
					client.setName(res.getString(3));
					client.setInscription(res.getDate(4));
					client.setExpiration(res.getDate(5));
					client.setFrozen(res.getDate(6));
					client.setState(res.getString(7));
				}
			}
			res.close();
			pstmt.close();
			if (client != null) {
				TemplateInfo template = (TemplateInfo) getTemplate(client);
				client.setTemplate(template);
			}
		}
		return client;
	}

	/**
	 * Agrega un registro de asistencia.
	 * 
	 * @param client
	 *            Cliente al que se le registra la asistencia.
	 * @throws SQLException
	 */
	public void saveAssistance(Client client) throws SQLException {

		long tiempo = System.currentTimeMillis();

		java.sql.Time time = new java.sql.Time(tiempo);
		java.sql.Date date = new java.sql.Date(tiempo);
		PreparedStatement pstmt = dbVitalcenter.prepareStatement(SQL_INS_ASISTENCIA);
		pstmt.clearParameters();
		pstmt.setString(1, client.getRut());
		pstmt.setTime(2, time);
		pstmt.setDate(3, date);
		pstmt.executeUpdate();
		pstmt.close();
		dbVitalcenter.commit();
	}

	/**
	 * Almacena el template para el cliente.
	 * 
	 * @param client
	 *            Cliente conn el template.
	 * @throws SQLException
	 */
	public void saveTemplate(Client client) throws SQLException {
		if (isConnectedTemplates() && dbTemplates != null && !dbTemplates.isClosed() && !dbTemplates.isReadOnly()) {

			PreparedStatement pstmt = dbTemplates.prepareStatement(SQL_INS_TEMPLATE);
			pstmt.clearParameters();

			byte[] bytes = client.getTemplate().getTemplate();
			pstmt.setString(1, client.getRut());

			pstmt.setInt(2, client.getTemplate().getFingerIndex());
			pstmt.setBytes(3, bytes);
			pstmt.executeUpdate();
			pstmt.close();

		}
	}

	public void deleteTemplate(Client client) throws SQLException {
		if (isConnectedTemplates() && dbTemplates != null && !dbTemplates.isClosed() && !dbTemplates.isReadOnly()) {

			PreparedStatement pstmt = dbTemplates.prepareStatement(SQL_DEL_TEMPLATE);
			pstmt.clearParameters();
			pstmt.setString(1, client.getRut());
			pstmt.executeUpdate();
			pstmt.close();
		}
	}

	public Object getTemplate(Client client) throws SQLException {
		Object resultado = null;
		if (isConnectedTemplates() && dbTemplates != null && !dbTemplates.isClosed()) {
			TemplateInfo tInfo = null;
			PreparedStatement pstmt = dbTemplates.prepareStatement(SQL_FND_TEMPLATE);
			pstmt.setString(1, client.getRut());
			ResultSet res = pstmt.executeQuery();
			while (res.next()) {
				tInfo = new TemplateInfo();
				tInfo.setFingerIndex(res.getInt(3));
				byte[] bytes = res.getBytes(2);
				tInfo.setTemplate(bytes);
				resultado = tInfo;
			}
			pstmt.close();
		}
		return resultado;
	}
	/**
	 * Obtiene la informacion de los templates, asociados al rut.
	 * @return
	 * @throws SQLException
	 */
	public List<Client> getTemplates() throws SQLException {
		List<Client> resultado =  null;
		if (isConnectedTemplates() && dbTemplates != null && !dbTemplates.isClosed()) {
			TemplateInfo tInfo = null;
			PreparedStatement pstmt = dbTemplates.prepareStatement(SQL_FND_TEMPLATES);
			ResultSet res = pstmt.executeQuery();
			while (res.next()) {
				if(resultado == null)
				{
					resultado = new ArrayList<Client>();
				}
				tInfo = new TemplateInfo();
				tInfo.setFingerIndex(res.getInt(3));
				byte[] bytes = res.getBytes(2);
				tInfo.setTemplate(bytes);
				Client cliente = new Client();
				cliente.setRut(res.getString(1));
				cliente.setTemplate(tInfo);
				resultado.add(cliente);
			}
			pstmt.close();
		}
		return resultado;
	}
}
