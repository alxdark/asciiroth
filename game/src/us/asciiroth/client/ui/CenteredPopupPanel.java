/**
 * Copyright 2008 Alx Dark
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.    
 */
package us.asciiroth.client.ui;

import us.asciiroth.client.core.Game;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for consistent layout and visual appearance of dialogs, as well as
 * some behavior for closing. Subclasses should implement the focus method and
 * focus the field or button that should be selected when the dialog is opened
 * or when it is returned to the top of the stack.
 */
public abstract class CenteredPopupPanel extends PopupPanel {

	private HorizontalPanel hpanel;
	private FlowPanel panel;

	/**
	 * Constructor.
	 */
	protected CenteredPopupPanel() {
		this(true, true);
	}

	/**
	 * Constructor.
	 * 
	 * @param autoHide
	 * @param modal
	 */
	protected CenteredPopupPanel(boolean autoHide, boolean modal) {
		super(autoHide, modal);
		setStyleName("n-dialog");
		panel = new FlowPanel();
		add(panel);
		addCloseHandler(DialogManager.get());
	}

	/**
	 * Add a component to the dialog.
	 * 
	 * @param component
	 */
	protected void addComponent(Widget component) {
		panel.add(component);
	}

	/**
	 * Add a label component to the dialog.
	 * 
	 * @param text
	 */
	protected Label addLabel(String text) {
		Label label = new Label(text);
		panel.add(label);
		return label;
	}

	/**
	 * Add a button to the dialog. You should add these last, after you add
	 * them, adding other components will put them after the buttons, which
	 * isn't what you want.
	 * 
	 * @param button
	 */
	protected void addButton(Widget button) {
		if (hpanel == null) {
			hpanel = new HorizontalPanel();
			hpanel.setStyleName("n-buttonBar");
			panel.add(hpanel);
		}
		hpanel.add(button);
	}

	protected MousyButton addButton(String label, ClickHandler handler) {
		MousyButton btn = new MousyButton("5em", label);
		btn.addClickHandler(handler);
		addButton(btn);
		return btn;
	}

	protected MousyButton addMainButton(String label, ClickHandler handler) {
		MousyButton btn = new MousyButton("10em", label);
		btn.addClickHandler(handler);
		addComponent(btn);
		return btn;
	}

	/**
	 * Add a cancel button. Most (all?) dialogs have one.
	 */
	protected void addCancelButton() {
		addCancelButton("Cancel");
	}

	/**
	 * Add a cancel button. Most (all?) dialogs have one.
	 */
	protected MousyButton addCloseButton() {
		return addCancelButton("Close");
	}

	/**
	 * Add a cancel button with the indicated label
	 * 
	 * @param label
	 */
	protected MousyButton addCancelButton(String label) {
		MousyButton cancelButton = new MousyButton("5em", label);
		cancelButton.addClickHandler(DialogManager.CLOSE_DIALOG_HANDLER);
		addButton(cancelButton);
		return cancelButton;
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent preview) {
		if (preview.getTypeInt() == Event.ONKEYDOWN) {
			int charCode = preview.getNativeEvent().getKeyCode();
			if (charCode == InputManager.KEY_ESC) {
				DialogManager.get().pop();
				preview.cancel();
			}
		}
	}

	public abstract void focus();

	/**
     * "Center" the dialog but only relative to the body of the document, not the 
     * window as a whole.
	 */	
	public void center() {
		setPopupPositionAndShow(new PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) >> 1;
				int top = (Game.get().getHeightOfGameScreen() - offsetHeight) >> 1;
				setPopupPosition(left, top);
				show();
			}
		});
	}
}
