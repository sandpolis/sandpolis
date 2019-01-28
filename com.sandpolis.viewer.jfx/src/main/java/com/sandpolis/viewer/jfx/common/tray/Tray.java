/******************************************************************************
 *                                                                            *
 *                    Copyright 2016 Subterranean Security                    *
 *                                                                            *
 *  Licensed under the Apache License, Version 2.0 (the "License");           *
 *  you may not use this file except in compliance with the License.          *
 *  You may obtain a copy of the License at                                   *
 *                                                                            *
 *      http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                            *
 *  Unless required by applicable law or agreed to in writing, software       *
 *  distributed under the License is distributed on an "AS IS" BASIS,         *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 *  See the License for the specific language governing permissions and       *
 *  limitations under the License.                                            *
 *                                                                            *
 *****************************************************************************/
package com.sandpolis.viewer.jfx.common.tray;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A singleton for interacting with the AWT system tray.
 * 
 * @author cilki
 * @since 2.0.0
 */
public final class Tray {

	/**
	 * The current system tray.
	 */
	private static TrayIcon tray;

	/**
	 * Attempt to move the application to the system tray if supported.
	 * 
	 * @throws AWTException
	 */
	public static synchronized void background() throws AWTException {
		if (tray != null)
			throw new IllegalStateException();
		if (!isSupported())
			throw new UnsupportedOperationException();

		MenuItem restoreItem = new MenuItem("Restore");
		restoreItem.addActionListener(event -> foreground());

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.addActionListener(event -> System.exit(0));

		PopupMenu popup = new PopupMenu();
		popup.add(restoreItem);
		popup.add(exitItem);

		try {
			tray = new TrayIcon(ImageIO.read(Tray.class.getResourceAsStream("/image/icon16/common/crimson.png")),
					"Sandpolis Viewer", popup);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		tray.setImageAutoSize(true);

		SystemTray.getSystemTray().add(tray);

		// TODO hide interface
	}

	/**
	 * Attempt to move the application out of the system tray.
	 */
	public static void foreground() {
		if (tray == null)
			throw new IllegalStateException();
		if (!isSupported())
			throw new UnsupportedOperationException();

		SystemTray.getSystemTray().remove(tray);
		tray = null;

		// TODO restore interface
	}

	/**
	 * Get whether the system tray is supported.
	 * 
	 * @return Whether the system tray is supported.
	 */
	public static boolean isSupported() {
		return SystemTray.isSupported();
	}

	/**
	 * Get whether the application is currently backgrounded.
	 * 
	 * @return Whether the application is running in the background
	 */
	public static boolean isBackgrounded() {
		return tray != null;
	}

	private Tray() {
	}
}