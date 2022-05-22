package org.sid.authjwt.security.RequestBodyClasses;

import lombok.Data;

@Data
public class RoleUserForm {
    private String username;
    private String roleName;
}
