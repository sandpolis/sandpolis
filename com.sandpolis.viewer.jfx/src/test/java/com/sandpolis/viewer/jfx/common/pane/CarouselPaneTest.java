/******************************************************************************
 *                                                                            *
 *                    Copyright 2019 Subterranean Security                    *
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
package com.sandpolis.viewer.jfx.common.pane;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.function.Supplier;
import java.util.prefs.Preferences;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import com.sandpolis.core.instance.store.pref.PrefStore;
import com.sandpolis.viewer.jfx.PrefConstant.ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
class CarouselPaneTest {

	@BeforeAll
	private static void init() throws Exception {
		PrefStore.init(Preferences.userNodeForPackage(CarouselPaneTest.class));
		PrefStore.register(ui.animations, true);

		System.setProperty("java.awt.headless", "true");
		System.setProperty("testfx.headless", "true");
		System.setProperty("testfx.robot", "glass");
		System.setProperty("prism.order", "sw");
	}

	private Label view1;
	private Label view2;
	private Label view3;
	private CarouselPane carousel;

	@Start
	private void start(Stage stage) {
		view1 = new Label("1");
		view1.setId("view1");
		view2 = new Label("2");
		view2.setId("view2");
		view3 = new Label("3");
		view3.setId("view3");

		carousel = new CarouselPane("left", 100, view1, view2, view3);

		stage.setScene(new Scene(carousel, 100, 100));
		stage.show();
	}

	@Test
	@DisplayName("Try to construct a CarouselPane with an invalid direction")
	void carouselPane_1() {
		assertThrows(IllegalArgumentException.class, () -> new CarouselPane("invalid", 100, new Label()));
	}

	@Test
	@DisplayName("Try to construct a CarouselPane with an invalid duration")
	void carouselPane_2() {
		assertThrows(IllegalArgumentException.class, () -> new CarouselPane("right", -1, new Label()));
	}

	@Test
	@DisplayName("Test a multi-view carousel")
	void carouselPane_3(FxRobot robot) throws Exception {
		assertFalse(carousel.isMoving());
		assertThat(carousel).hasChild("#view1");
		assertThat(carousel).doesNotHaveChild("#view2");
		assertThat(carousel).doesNotHaveChild("#view3");

		// Move view and check state
		robot.interact(() -> carousel.moveForward());
		waitCondition(() -> !carousel.isMoving(), 5000);
		assertThat(carousel).doesNotHaveChild("#view1");
		assertThat(carousel).hasChild("#view2");
		assertThat(carousel).doesNotHaveChild("#view3");

		// Move view and check state
		robot.interact(() -> carousel.moveForward());
		waitCondition(() -> !carousel.isMoving(), 5000);
		assertThat(carousel).doesNotHaveChild("#view1");
		assertThat(carousel).doesNotHaveChild("#view2");
		assertThat(carousel).hasChild("#view3");

		// Move view and check state
		robot.interact(() -> carousel.moveBackward());
		waitCondition(() -> !carousel.isMoving(), 5000);
		assertThat(carousel).doesNotHaveChild("#view1");
		assertThat(carousel).hasChild("#view2");
		assertThat(carousel).doesNotHaveChild("#view3");

		// Move view and check state
		robot.interact(() -> carousel.moveBackward());
		waitCondition(() -> !carousel.isMoving(), 5000);
		assertThat(carousel).hasChild("#view1");
		assertThat(carousel).doesNotHaveChild("#view2");
		assertThat(carousel).doesNotHaveChild("#view3");
	}

	private void waitCondition(Supplier<Boolean> condition, int timeout) throws InterruptedException {
		int waited = 0;
		while (!condition.get()) {
			if (waited >= timeout)
				throw new RuntimeException();
			Thread.sleep(100);
			waited += 100;
		}

		// Wait a little longer for the animation hooks to finish
		// TODO come up with a better way
		Thread.sleep(400);
	}
}