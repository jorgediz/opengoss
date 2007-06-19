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
 * @author Dimax Pradi
 */
package au.com.gworks.gwt.petstore.server;

import org.opengoss.petstore.dao.AccountDao;
import org.opengoss.petstore.dao.domain.Account;

import au.com.gworks.gwt.petstore.client.service.AccountInfo;
import au.com.gworks.gwt.petstore.client.service.AccountRpcController;

public class AccountRpcControllerImpl extends BaseServlet implements AccountRpcController {

	private AccountDao getAccountDao() {
		return getDaoFacade().getDao(AccountDao.class);
	}

	public AccountInfo createAccount(AccountInfo info) {
		Account account = new Account();
		account.setUsername(info.username);
		account.setPassword(info.password);
		account.setFirstName(info.firstName);
		account.setLastName(info.lastName);
		account.setEmail(info.email);
		account.setAddress1("default");
		account.setCity("default");
		account.setState("default");
		account.setZip("default");
		account.setCountry("default");
		account.setPhone("default");
		account.setLanguagePreference("default");
		account.setFavouriteCategoryId("DOGS");
		getAccountDao().insertAccount(account);
		return retrieveAccount(info.username);
	}

	public AccountInfo retrieveAccount(String username) {
		Account account = getAccountDao().getAccount(username);
		if (account == null)
			return null;
		AccountInfo info = new AccountInfo(account.getUsername(), account
				.getPassword(), account.getEmail(), account.getFirstName(),
				account.getLastName());
		return info;
	}

	public AccountInfo updateAccount(AccountInfo info) {
		Account account = new Account();
		account.setUsername(info.username);
		account.setPassword(info.password);
		account.setFirstName(info.firstName);
		account.setLastName(info.lastName);
		account.setEmail(info.email);
		account.setAddress1("default");
		account.setCity("default");
		account.setState("default");
		account.setZip("default");
		account.setCountry("default");
		account.setPhone("default");
		account.setLanguagePreference("default");
		account.setFavouriteCategoryId("DOGS");
		getAccountDao().updateAccount(account);
		return retrieveAccount(info.username);
	}

	public AccountInfo signIn(AccountInfo info) {
		// TODO Need to getServletContext to put acc obj in session.
		// request.getSession().setAttribute("accountForm", acctForm);

		Account account = getAccountDao().getAccount(info.username,
				info.password);
		if (account == null)
			return null;
		AccountInfo result = new AccountInfo(account.getUsername(), account
				.getPassword(), account.getEmail(), account.getFirstName(),
				account.getLastName());
		return result;
	}

	public void signOut() {
		// request.getSession().invalidate();
	}

}
