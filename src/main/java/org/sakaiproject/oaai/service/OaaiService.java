/**
 * Copyright 2008 Sakaiproject Licensed under the
 * Educational Community License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.sakaiproject.oaai.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.AuthzGroupService;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.tool.api.Session;
import org.sakaiproject.tool.api.SessionManager;
import org.sakaiproject.user.api.UserDirectoryService;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * Handles the processing related to the permissions handler
 * 
 * @author Robert E. Long (rlong @ unicon.net)
 */
public class OaaiService {

    final protected Log log = LogFactory.getLog(getClass());

    public boolean isAdminSession() {
        String sessionId = sessionManager.getCurrentSession().getId();

        return isAdminSession(sessionId);
    }

    /**
     * Check to see if the session is for a super admin
     * @param sessionId the id of the session
     * @return true, if the session is owned by a super admin, false otherwise
     */
    public boolean isAdminSession(String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return false;
        }

        Session session = sessionManager.getSession(sessionId);
        String userId = session.getUserId();
        if (StringUtils.isNotBlank(userId)) {
            return securityService.isSuperUser(userId);
        } else {
            return false;
        }
    }

    /**
     * Get a translated string from a code and replacement args
     * 
     * @param code
     * @param args
     * @return the translated string
     */
    public String getMessage(String code, Object[] args) {
        String msg;
        try {
            msg = getMessageSource().getMessage(code, args, null);
        } catch (NoSuchMessageException e) {
            msg = "Missing message for code: "+code;
        }
        return msg;
    }


    public static String makeStringFromArray(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    public String createDatedDirectoryName() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH);
        String directoryName = sdf.format(date);

        return directoryName;
    }

    private AuthzGroupService authzGroupService;
    public void setAuthzGroupService(AuthzGroupService authzGroupService) {
        this.authzGroupService = authzGroupService;
    }

    private SessionManager sessionManager;
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    private SecurityService securityService;
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    UserDirectoryService userDirectoryService;
    public void setUserDirectoryService(UserDirectoryService userDirectoryService) {
        this.userDirectoryService = userDirectoryService;
    }

    private MessageSource messageSource;
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    public MessageSource getMessageSource() {
        return messageSource;
    }

}