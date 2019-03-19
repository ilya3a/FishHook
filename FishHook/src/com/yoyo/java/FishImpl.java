package com.yoyo.java;

import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import java.awt.Cursor;

import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.ComponentOrientation;
import java.awt.Frame;
import java.awt.Window.Type;
import javax.swing.JRadioButton;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;

public class FishImpl {
	private static JTextPane textField;
	private static boolean active = true;
	private static boolean busy = false;
	private static byte[] userReq = new byte[1];
	private static SerialPort devicePort = null;
	private static double param = 0;

	private static class FishHookOperation implements Runnable {

		@Override
		public void run() {
			if (!busy) {
				busy = true;
				Scanner deviceInputReader = new Scanner(devicePort.getInputStream());
				java.io.OutputStream deviceOutputWriter = devicePort.getOutputStream();
				try {
					deviceOutputWriter.write(userReq);
				} catch (IOException e1) {
					e1.printStackTrace();
					active = false;
				}
				String deviceMsg = deviceInputReader.nextLine();
				String m1 = new String();
				while (active) {
					double density = 0;
					String densityStr = null;

					if (deviceMsg.contains("Density_Calculated")) {
						m1 = deviceMsg;
						m1 = m1.trim();
						String denNumStr = m1.substring(m1.indexOf("=")).replaceAll("[^\\.0123456789]", "");
						if (!denNumStr.equals("")) {
							density = Double.parseDouble(denNumStr) * 1.7 / param;
							densityStr = new String("Density of color:" + density + "\n");
						}

					} else if (deviceMsg.contains("Conductivity_formulated")) {
						// deviceMsg = deviceMsg.substring(0, 24);
						deviceMsg = deviceMsg.trim();
						double cunductivity = Double.parseDouble(
								deviceMsg.substring(deviceMsg.indexOf("=")).replaceAll("[^\\.0123456789]", ""));
						String conductivityStr = new String("Conductivity of color:" + cunductivity + "\n");
						textField.setText(densityStr + "\n" + conductivityStr);
						// System.out.println(deviceMsg);
					}
					deviceMsg = deviceInputReader.nextLine();
				}

//device turned off				
				try {
					deviceOutputWriter.write(userReq);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
//				try {
//					Thread.sleep(150);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				deviceInputReader.close();
				try {
					deviceOutputWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				active = true;
				busy = false;
			}
		}
	}

	public static void operation() {
		if (!busy) {

		}
	}

	public static void main(String[] args) throws IOException {
		FishHookOperation fOperation = new FishHookOperation();

		JFrame frmCdmNew = new JFrame("A JFrame");
		frmCdmNew.setResizable(false);
		frmCdmNew.setVisible(true);
		frmCdmNew.setAlwaysOnTop(true);
		frmCdmNew.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCdmNew.getContentPane().setBackground(new Color(153, 204, 255));
		frmCdmNew.setTitle("CDM_v1.0");
		frmCdmNew.setSize(506, 512);
		frmCdmNew.setLocation(300, 200);
		final JButton colorButton = new JButton("");
		colorButton.setBorder(new RoundButton(100));
		colorButton.setEnabled(false);

		JLabel lblCdmDensity = new JLabel("CDM DENSITY & CONDUCTIVITY DIAGNOSE TOOL");
		lblCdmDensity.setFont(new Font("Serif", Font.PLAIN, 17));
		JButton btnStop = new JButton("STOP");
		btnStop.setEnabled(false);
		btnStop.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnStop.setBorder(new RoundButton(20));
		btnStop.setFont(new Font("Serif", Font.BOLD, 10));
		btnStop.setForeground(new Color(0, 0, 0));
		btnStop.setBackground(new Color(255, 0, 0));

		textField = new JTextPane();
		textField.setFont(new Font("Serif", Font.PLAIN, 13));
		textField.setBackground(new Color(240, 248, 255));

		JLabel lblSelected = new JLabel("Selected Ink:");
		lblSelected.setFont(new Font("Serif", Font.PLAIN, 17));

		JLabel lblSelectInkTo = new JLabel("Select ink to diagnose:");
		lblSelectInkTo.setFont(new Font("Serif", Font.PLAIN, 17));

		JPanel panel = new JPanel();
		panel.setOpaque(false);

		JSeparator separator = new JSeparator();

		JSeparator separator_1 = new JSeparator();

		JSeparator separator_2 = new JSeparator();

		JSeparator separator_3 = new JSeparator();
		
		JProgressBar progressBar = new JProgressBar();
		
		JToggleButton tglbtnCalibrate = new JToggleButton("Calibrate");

		GroupLayout groupLayout = new GroupLayout(frmCdmNew.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(55)
					.addComponent(lblCdmDensity, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
					.addGap(346))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSelectInkTo, GroupLayout.PREFERRED_SIZE, 459, GroupLayout.PREFERRED_SIZE)
						.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE))
					.addGap(474)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 1, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 470, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(498, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(490, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSelected, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(colorButton, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(805, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 480, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(488, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(tglbtnCalibrate)
							.addGap(18)
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(separator_3, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 478, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(490, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblCdmDensity, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(separator, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(separator_1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE))
					.addGap(5)
					.addComponent(lblSelectInkTo, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_2, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tglbtnCalibrate))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_3, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSelected)
						.addComponent(colorButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		JButton button_1 = new JButton("WHITE");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userReq[0] = 119;
				param = 2;
				if (!busy)
					colorButton.setBackground(button_1.getBackground());
			}
		});
		button_1.setPreferredSize(new Dimension(85, 85));
		button_1.setFont(new Font("Serif", Font.BOLD, 10));
		button_1.setEnabled(false);
		button_1.setBackground(Color.WHITE);
		panel.add(button_1);

		JButton button_2 = new JButton("CYAN");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userReq[0] = 99;
				param = 2.2;
				if (!busy)
					colorButton.setBackground(button_2.getBackground());

			}
		});
		button_2.setSelected(true);
		button_2.setPreferredSize(new Dimension(85, 85));
		button_2.setForeground(Color.WHITE);
		button_2.setFont(new Font("Serif", Font.BOLD, 10));
		button_2.setEnabled(true);
		button_2.setBackground(new Color(135, 206, 250));
		panel.add(button_2);

		JButton button_3 = new JButton("MAGENTA");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userReq[0]= 109;
				param=2.2;
				if(!busy)
					colorButton.setBackground(button_3.getBackground());
			}
		});
		button_3.setPreferredSize(new Dimension(85, 85));
		button_3.setForeground(Color.WHITE);
		button_3.setFont(new Font("Serif", Font.BOLD, 10));
		button_3.setEnabled(false);
		button_3.setBackground(Color.MAGENTA);
		panel.add(button_3);

		JButton button_4 = new JButton("YELLOW");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userReq[0]= 121;
				param=2;
				if(!busy)
					colorButton.setBackground(button_4.getBackground());
			}
		});
		button_4.setPreferredSize(new Dimension(85, 85));
		button_4.setFont(new Font("Serif", Font.BOLD, 10));
		button_4.setEnabled(false);
		button_4.setBackground(Color.YELLOW);
		panel.add(button_4);

		JButton button_5 = new JButton("BLACK");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				userReq[0]= 107;
				param=1.8;
				if(!busy)
					colorButton.setBackground(button_5.getBackground());
			}
		});
		button_5.setPreferredSize(new Dimension(85, 85));
		button_5.setForeground(Color.WHITE);
		button_5.setFont(new Font("Serif", Font.BOLD, 10));
		button_5.setEnabled(false);
		button_5.setBackground(Color.BLACK);
		panel.add(button_5);
		frmCdmNew.getContentPane().setLayout(groupLayout);

		while (devicePort == null) {
			SerialPort[] ports = SerialPort.getCommPorts();
			for (int i = 0; i < ports.length; ++i) {
				if (ports[i].getPortDescription().equalsIgnoreCase("ft232r usb uart")) {
					devicePort = ports[i];
					i = 100;// go out of for loop after finding comPort
				}
			}
			if (devicePort != null && devicePort.openPort()) {
				textField.setText("CDM FishHook is connected");
			}

			else {
				textField.setText("Please conect the CDM FishHook to the computer");
			}
		}
//device setup
		devicePort.setBaudRate(115200);
		devicePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}
}
