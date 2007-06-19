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

import org.javaongems.std.client.EastWestHorizPanels;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WelcomeView extends Composite {
	private Image ibatisImg;
	private Image gwtImg;
	private Image springImg;
	private Label poweredByLabel;
	private Label authorLabel;
	private HTML descLabel;
	private Label welcomeLabel;
	private EastWestHorizPanels groupBarContainer;
	private VerticalPanel container;
	private HorizontalPanel gwtLibs, mainLibs;
	
	public WelcomeView() {
		container = new VerticalPanel();
		initWidget(container);
		
		groupBarContainer = new EastWestHorizPanels();
		container.add(groupBarContainer);
		groupBarContainer.setStyleName("ps-ewp-mainGroupSections");

		Label title = new Label("Welcome");
		groupBarContainer.getWest().add(title);
		title.setStyleName("ps-ewp-mainGroupSections-txt");

		welcomeLabel = new Label("gwt-Petstore");
		welcomeLabel.setStyleName("ps-text-large");
		container.add(welcomeLabel);

		authorLabel = new Label("by Ashin Wimalajeewa & Dimax Pradi");
		authorLabel.setStyleName("ps-text-medium");
		container.add(authorLabel);

		descLabel = new HTML();
		container.add(descLabel);

		poweredByLabel = new Label("Powered by");
		poweredByLabel.setStyleName("ps-text-large");
		container.add(poweredByLabel);

		
		gwtLibs = new HorizontalPanel();
		container.add(gwtLibs);
		Label gems = new Label("javaongems");
		gems.setStyleName("ps-text-large");
		Label gwtSl = new Label("gwt-widgets Server Library");
		gwtSl.setStyleName("ps-text-large");
		gwtLibs.add(gems);
		gwtLibs.add(gwtSl);
		
		mainLibs = new HorizontalPanel();
		mainLibs.setVisible(false);
		container.add(mainLibs);
		gwtImg = new Image();
		mainLibs.add(gwtImg);
		gwtImg.setUrl("powerByGwt.png");

		springImg = new Image();
		mainLibs.add(springImg);
		springImg.setUrl("poweredBySpring.gif");
		
		ibatisImg = new Image();
		mainLibs.add(ibatisImg);
		ibatisImg.setUrl("poweredByIBatis.gif");
	}
	
	public void setAboutInfo(String txt) {
		descLabel.setHTML(txt);
	}
	
	public HorizontalPanel getGwtLibsContainer() {
		return gwtLibs;
	}
	
	public HorizontalPanel getMainLibsContainer() {
		return mainLibs;
	}
}
