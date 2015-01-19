package cl.eos.util;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import com.sun.istack.internal.logging.Logger;

public class Utils {

	public static float MAX_PUNTAJE = 340f;
	private static Logger log = Logger.getLogger(Utils.class);

	/**
	 * M�todo Est�tico que valida si un rut es v�lido Fuente :
	 */
	public static boolean validarRut(String strRut) {
		boolean resultado = false;
		if (strRut.length() > 0) {
			// Creamos un arreglo con el rut y el digito verificador
			String[] rut_dv = strRut.split("-");
			// Las partes del rut (numero y dv) deben tener una longitud
			// positiva
			if (rut_dv.length == 2) {
				int rut = Integer.parseInt(rut_dv[0]);
				char dv = rut_dv[1].toUpperCase().charAt(0);

				int m = 0, s = 1;
				for (; rut != 0; rut /= 10) {
					s = (s + rut % 10 * (9 - m++ % 6)) % 11;
				}
				resultado = (dv == (char) (s != 0 ? s + 47 : 75));
			}
		}
		return resultado;
	}

	private static String getFileSuffix(final String path) {
		String result = null;
		if (path != null) {
			result = "";
			if (path.lastIndexOf('.') != -1) {
				result = path.substring(path.lastIndexOf('.'));
				if (result.startsWith(".")) {
					result = result.substring(1);
				}
			}
		}
		return result;
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	public static char getDecimalSeparator() {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		char sep = symbols.getDecimalSeparator();
		return sep;
	}

	public static float redondeo2Decimales(float parametro) {
		return Math.round(parametro * 100f) / 100f;
	}

	public static double redondeo2Decimales(double parametro) {
		return Math.round(parametro * 100d) / 100d;
	}

	   public static float redondeo1Decimal(float parametro) {
	        return Math.round(parametro * 10f) / 10f;
	    }

	    public static double redondeo1Decimal(double parametro) {
	        return Math.round(parametro * 10d) / 10d;
	    }
	
	public static boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	public static boolean isInteger(String s) {
		return s.matches("[-+]?\\d+");
	}

	/**
	 * Obtiene el directorio donde se almacenan todos los archivos. Retorna
	 * uset.home directorio.
	 * 
	 * @return
	 */
	public static File getDefaultDirectory() {
		String path = System.getProperty("user.home") + File.separator
				+ "Documents";
		path += File.separator + "Vitalcenter.Biometric";
		File customDir = new File(path);
		if (customDir.exists()) {
			log.info(customDir + " ya existe.");
		} else if (customDir.mkdirs()) {
			log.info(customDir + " fue creado.");
		} else {
			log.info(customDir + " no fue creado.");
			path = System.getProperty("user.home");
			customDir = new File(path);
		}
		return customDir;
	}

}
