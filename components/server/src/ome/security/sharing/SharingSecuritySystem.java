/*
 * ome.security.SecuritySystem
 *
 *   Copyright 2006 University of Dundee. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */

package ome.security.sharing;

// Java imports

// Third-party libraries

// Application-internal dependencies
import ome.annotations.RevisionDate;
import ome.annotations.RevisionNumber;
import ome.conditions.ApiUsageException;
import ome.conditions.SecurityViolation;
import ome.model.IObject;
import ome.model.internal.Details;
import ome.model.internal.Permissions;
import ome.model.internal.Token;
import ome.security.ACLEventListener;
import ome.security.AdminAction;
import ome.security.SecureAction;
import ome.security.SecuritySystem;
import ome.system.EventContext;
import ome.system.Principal;
import ome.system.Roles;

/**
 * central security interface. All queries and actions that deal with a secure
 * context should pass through an implementation of this interface.
 * 
 * @author Josh Moore, josh.moore at gmx.de
 * @version $Revision$, $Date$
 * @see Token
 * @see Details
 * @see Permissions
 * @see ACLEventListener
 * @since 3.0-M3
 */
@RevisionDate("$Date$")
@RevisionNumber("$Revision$")
public class SharingSecuritySystem implements SecuritySystem {

    public Details checkManagedDetails(IObject object, Details trustedDetails)
            throws ApiUsageException, SecurityViolation {
        // TODO Auto-generated method stub
        return null;
    }

    public void clearEventContext() {
        // TODO Auto-generated method stub

    }

    public void disable(String... ids) {
        // TODO Auto-generated method stub

    }

    public <T extends IObject> T doAction(SecureAction action, T... objs) {
        // TODO Auto-generated method stub
        return null;
    }

    public void enable(String... ids) {
        // TODO Auto-generated method stub

    }

    public EventContext getEventContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public Roles getSecurityRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasPrivilegedToken(IObject obj) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isDisabled(String id) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isEmptyEventContext() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isReady() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSystemType(Class<? extends IObject> klass) {
        // TODO Auto-generated method stub
        return false;
    }

    public void loadEventContext(boolean isReadyOnly) {
        // TODO Auto-generated method stub

    }

    public void login(Principal principal) {
        // TODO Auto-generated method stub

    }

    public int logout() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Details newTransientDetails(IObject object)
            throws ApiUsageException, SecurityViolation {
        // TODO Auto-generated method stub
        return null;
    }

    public void runAsAdmin(AdminAction action) {
        // TODO Auto-generated method stub

    }

    public void setEventContext(EventContext context) {
        // TODO Auto-generated method stub

    }

}
