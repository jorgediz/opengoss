/**
 * Copyright 2006 gworks.com.au
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. (http://www.apache.org/licenses/LICENSE-2.0)
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed 
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for 
 * the specific language governing permissions and limitations under the License.
 *
 * <dhamma-message src="Atisha (11th century Tibetan Buddhist master)">
 *  The greatest achievement is selflessness. The greatest worth is self-mastery.
 *  The greatest quality is seeking to serve others. The greatest precept is continual awareness.
 *  The greatest medicine is the emptiness of everything. The greatest action is not conforming with the worlds ways.
 *  The greatest magic is transmuting the passions. The greatest generosity is non-attachment.
 *  The greatest goodness is a peaceful mind. The greatest patience is humility.
 *  The greatest effort is not concerned with results. The greatest meditation is a mind that lets go.
 *  The greatest wisdom is seeing through appearances. 
 * </dhamma-message>
 * 
 * @author Ashin Wimalajeewa (ash)
 */
package au.com.gworks.gwt.petstore.client;

import org.javaongems.gwk.client.AbstractPageController;
import org.javaongems.rico.client.OpacityEffect;
import org.javaongems.rico.client.Transition;
import org.javaongems.rico.client.Transition.Effect;

import au.com.gworks.gwt.petstore.client.service.WelcomeRpcController;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class WelcomeController extends AbstractPageController implements Transition.EffectEventListener {
	private WelcomeView view;
	private boolean openned = false;
	private OpacityEffect fadeEffect = new OpacityEffect(null, 0.0f);
	private Element gwt, main, toggle;
	
	public void setUp(StoreCoordinator coord) {
		super.setUp(coord);
		view = new WelcomeView();
		setPagePanel(coordinator.getPagePanel());
		coordinator.registerPageController(view, this);
		fadeEffect.setEffectListener(this);
		gwt = view.getGwtLibsContainer().getElement();
		main = view.getMainLibsContainer().getElement();
		toggle = gwt;
	}

	// template methods
	protected String getPageName() {
		return "welcome";
	}
	
	protected Widget getPageView() {
		return view;
	}
	
	public void mountPageView() {
		coordinator.getPagePanel().add(view, null, "Welcome", false);
	}
	
	protected void openPage() {
		if (!openned) {
			ControllerCall call = new ControllerCall();
			WelcomeRpcController.Util.getInstance().getAboutInfo(call);
			openned = true;
		}
		Widget w = ((StoreCoordinator)coordinator).getCartController().getSummaryView();
		coordinator.getPagePanel().setPageContext(w);
		super.openPage();
		OpacityEffect.setElementOpacity(gwt, 1); UIObject.setVisible(gwt, true);
		OpacityEffect.setElementOpacity(main, 0); UIObject.setVisible(main, true);
		toggle = gwt;
		fadeEffect.revive();
		fadeEffect.setElement(gwt);
		runEffect();
	}

	protected void closePage() {
		fadeEffect.abort();
	}

	protected void onControllerCallSuccess(ControllerCall call, Object result) {
		String txt = (String) result;
		view.setAboutInfo(txt);
	}

	public void onAbort(Effect sender) {
	}

	public void onStepCompleted(Effect sender, int step, int remainingSteps, int stepDuration) {
	}

	public void onEnd(Effect sender) {
		rotate();
		fadeEffect.reverse();
		fadeEffect.setElement(toggle);
		runEffect();
	}

	public void onStart(Effect sender) {
	}
	
	private void rotate() {
		toggle = toggle == gwt ? main: gwt;
	}

	private void runEffect() {
		Transition.run(fadeEffect, 2000, 10);
	}
}