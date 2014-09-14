package vitalcenter.osgi.rs232.provider;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import vitalcenter.osgi.rs232.IRS232Service;

public class RS232Service implements IRS232Service {

	private SerialPort serialPort;
	private OutputStream output;
	private boolean isOpen;

	public RS232Service() {
		isOpen = open();
	}

	@Override
	public boolean open() {
		boolean result = false;
		try {
			Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
			CommPortIdentifier portIdentifier = null;
			while (ports.hasMoreElements()) {
				CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
				if (port.getName().equalsIgnoreCase(COMM_PORT_NAME)) {
					portIdentifier = port;
				}
			}
			if (portIdentifier != null) {
				if (!portIdentifier.isCurrentlyOwned()) {
					serialPort = (SerialPort) portIdentifier.open(COMM_PORT_NAME, 2000);
					output = serialPort.getOutputStream();
					result = true;
				}
			} else {
				serialPort = null;
				output = null;
			}
		} catch (PortInUseException e) {
			result = false;
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	@Override
	public boolean close() {
		boolean result = true;
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				result = false;
			}
		}
		if (serialPort != null) {
			serialPort.close();
		}
		return result;
	}

	@Override
	public void write(byte[] bytes) throws IOException {
		if (serialPort != null && output != null) {
			output.write(bytes);
		}
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

}
