/**
 * Most of the code in the Qalingo project is copyrighted Hoteia and licensed
 * under the Apache License Version 2.0 (release version 0.8.0)
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *                   Copyright (c) Hoteia, 2012-2014
 * http://www.hoteia.com - http://twitter.com/hoteia - contact@hoteia.com
 *
 */
package org.hoteia.qalingo.core.web.mvc.controller.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hoteia.qalingo.core.domain.AttributeDefinition;
import org.hoteia.qalingo.core.domain.Customer;
import org.hoteia.qalingo.core.domain.CustomerAttribute;
import org.hoteia.qalingo.core.domain.EngineSetting;
import org.hoteia.qalingo.core.domain.EngineSettingValue;
import org.hoteia.qalingo.core.domain.Market;
import org.hoteia.qalingo.core.domain.MarketArea;
import org.hoteia.qalingo.core.domain.enumtype.CustomerNetworkOrigin;
import org.hoteia.qalingo.core.domain.enumtype.FoUrls;
import org.hoteia.qalingo.core.domain.enumtype.OAuthType;
import org.hoteia.qalingo.core.mapper.JsonMapper;
import org.hoteia.qalingo.core.pojo.RequestData;
import org.hoteia.qalingo.core.security.helper.SecurityUtil;
import org.hoteia.qalingo.core.security.util.SecurityRequestUtil;
import org.hoteia.qalingo.core.service.AttributeService;
import org.hoteia.tools.scribe.mapping.oauth.googleplus.json.pojo.UserPojo;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Google2Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 */
@Controller("callBackOAuthGoogleAccountController")
public class CallBackOAuthGoogleAccountController extends AbstractOAuthFrontofficeController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	protected AttributeService attributeService;
	
    @Autowired
    protected SecurityRequestUtil securityRequestUtil;
    
    @Autowired
    protected SecurityUtil securityUtil;

    @Autowired
    protected JsonMapper jsonMapper;
	
	@RequestMapping("/callback-oauth-google-account.html*")
	public ModelAndView callBackGoogleAccount(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
		final RequestData requestData = requestUtil.getRequestData(request);
		
		// SANITY CHECK
		if(!requestUtil.hasKnownCustomerLogged(request)){
			try {

			    // CLIENT ID
			    EngineSetting clientIdEngineSetting = engineSettingService.getSettingOAuthAppKeyOrId();
			    EngineSettingValue clientIdEngineSettingValue = clientIdEngineSetting.getEngineSettingValue(OAuthType.GOOGLE_ACCOUNT.name());
			    
			    // CLIENT SECRET
			    EngineSetting clientSecretEngineSetting = engineSettingService.getSettingOAuthAppSecret();
			    EngineSettingValue clientSecretEngineSettingValue = clientSecretEngineSetting.getEngineSettingValue(OAuthType.GOOGLE_ACCOUNT.name());
			    
			    // CLIENT PERMISSIONS
			    EngineSetting permissionsEngineSetting = engineSettingService.getSettingOAuthAppPermissions();
			    EngineSettingValue permissionsEngineSettingValue = permissionsEngineSetting.getEngineSettingValue(OAuthType.GOOGLE_ACCOUNT.name());
			    
			    if(clientIdEngineSettingValue != null
			    		&& clientSecretEngineSetting != null
			    		&& permissionsEngineSettingValue != null){
					final String clientId = clientIdEngineSettingValue.getValue();
					final String clientSecret = clientSecretEngineSettingValue.getValue();
					final String permissions = permissionsEngineSettingValue.getValue();

                    final String googleAccountCallBackURL = urlService.buildAbsoluteUrl(requestData, urlService.buildOAuthCallBackUrl(requestData, OAuthType.GOOGLE_ACCOUNT.getPropertyKey().toLowerCase()));

                    OAuthService service = new ServiceBuilder()
                    .provider(Google2Api.class)
                    .apiKey(clientId)
                    .apiSecret(clientSecret)
                    .scope(permissions)
                    .callback(googleAccountCallBackURL)
                    .build();

                    final String code = request.getParameter("code");
                    if (StringUtils.isNotEmpty(code)) {
                        Verifier verifier = new Verifier(code);
                        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
                        OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, GOOGLE_ME_URL);
                        service.signRequest(accessToken, oauthRequest);
                        Response oauthResponse = oauthRequest.send();
                        int responseCode = oauthResponse.getCode();
                        String responseBody = oauthResponse.getBody();

                        if (responseCode == 200) {
                             handleAuthenticationData(request, response, requestData, OAuthType.GOOGLE_ACCOUNT, responseBody);
                        } else {
                            logger.error("Callback With " + OAuthType.GOOGLE_ACCOUNT.name() + " failed!");
                        }
                    } else {
                        logger.error("Callback With " + OAuthType.GOOGLE_ACCOUNT.name() + " failed!");
                    }
			    }
					
			} catch (Exception e) {
				logger.error("Callback With " + OAuthType.GOOGLE_ACCOUNT.name() + " failed!");
			}
		}
		
		// DEFAULT FALLBACK VALUE
		if(!response.isCommitted()){
			response.sendRedirect(urlService.generateUrl(FoUrls.LOGIN, requestData));
		}

		return null;
	}
	
    protected void handleAuthenticationData(HttpServletRequest request, HttpServletResponse response, RequestData requestData, OAuthType type, String jsonData) throws Exception {
        UserPojo userPojo = null;
        try {
            userPojo = jsonMapper.getJsonMapper().readValue(jsonData, UserPojo.class);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        }
        if (userPojo != null) {
            final String email = userPojo.getEmailAccount();
            final String firstName = userPojo.getFirstName();
            final String lastName = userPojo.getLastName();
            final String gender = userPojo.getGender();
            final String username = userPojo.getNickname();
            Customer customer = customerService.getCustomerByLoginOrEmail(email);

            if(customer == null){
                final Market currentMarket = requestData.getMarket();
                final MarketArea currentMarketArea = requestData.getMarketArea();
                
                // CREATE A NEW CUSTOMER
                customer = new Customer();
//                customer = setCommonCustomerInformation(request, customer);

                customer.setLogin(email);
                customer.setPassword(securityUtil.generatePassword());
                customer.setEmail(email);
                customer.setFirstname(firstName);
                customer.setLastname(lastName);
                if (StringUtils.isNotEmpty(gender)) {
                    customer.setGender(gender);
                }

                customer.setNetworkOrigin(CustomerNetworkOrigin.GOOGLE_ACCOUNT.getPropertyKey());

                CustomerAttribute attribute = new CustomerAttribute();
                AttributeDefinition attributeDefinition = attributeService.getAttributeDefinitionByCode(CustomerAttribute.CUSTOMER_ATTRIBUTE_SCREENAME);
                attribute.setAttributeDefinition(attributeDefinition);
                String screenName = username;
                if (StringUtils.isEmpty(screenName)) {
                    if (StringUtils.isNotEmpty(lastName)) {
                        if (StringUtils.isNotEmpty(lastName)) {
                            screenName = lastName;
                            if (screenName.length() > 1) {
                                screenName = screenName.substring(0, 1);
                            }
                            if (!screenName.endsWith(".")) {
                                screenName = screenName + ". ";
                            }
                        }
                    }
                    screenName = screenName + firstName;
                }
                attribute.setShortStringValue(screenName);
                customer.getAttributes().add(attribute);

                // Save the new customer
                customer = webManagementService.buildAndSaveNewCustomer(requestData, currentMarket, currentMarketArea, customer);
                
                // Save the email confirmation
                webManagementService.buildAndSaveCustomerNewAccountMail(requestData, customer);
            }

            // Redirect to the edit page
            if (StringUtils.isNotEmpty(customer.getEmail())) {

                // Login the new customer
                securityRequestUtil.authenticationCustomer(request, customer);

                // Update the customer session
                requestUtil.updateCurrentCustomer(request, customer);

                response.sendRedirect(urlService.generateUrl(FoUrls.PERSONAL_EDIT, requestData));
            }

        }
    }

}