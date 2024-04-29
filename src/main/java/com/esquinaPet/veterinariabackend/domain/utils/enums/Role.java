package com.esquinaPet.veterinariabackend.domain.utils.enums;


import java.util.Arrays;
import java.util.List;


public enum Role {
    ADMINISTRATOR(Arrays.asList(
            RolePermission.READ_ALL_APPOINTMENTS,
            RolePermission.READ_ONE_APPOINTMENT,
            RolePermission.CREATE_ONE_APPOINTMENT,
            RolePermission.UPDATE_ONE_APPOINTMENT,
            RolePermission.DISABLED_ONE_APPOINTMENT,
            RolePermission.LOGOUT,

            RolePermission.READ_ALL_SERVICE_TYPES,
            RolePermission.READ_ONE_SERVICE_TYPE,
            RolePermission.CREATE_ONE_SERVICE_TYPES,
            RolePermission.UPDATE_ONE_SERVICE_TYPES,
            RolePermission.DISABLED_ONE_SERVICE_TYPES,
            RolePermission.READ_MY_PROFILE
    )),
    ASSISTANT_ADMINISTRATOR(Arrays.asList(
            RolePermission.READ_ALL_APPOINTMENTS,
            RolePermission.READ_ONE_APPOINTMENT,
            RolePermission.CREATE_ONE_APPOINTMENT,
            RolePermission.UPDATE_ONE_APPOINTMENT,
            RolePermission.DISABLED_ONE_APPOINTMENT,
            RolePermission.LOGOUT,
            RolePermission.READ_ALL_SERVICE_TYPES,
            RolePermission.READ_ONE_SERVICE_TYPE,
            RolePermission.CREATE_ONE_SERVICE_TYPES,
            RolePermission.UPDATE_ONE_SERVICE_TYPES,
            RolePermission.DISABLED_ONE_SERVICE_TYPES,
            RolePermission.READ_MY_PROFILE
    )),
    USER(Arrays.asList(
            RolePermission.READ_OWN_APPOINTMENTS,
            RolePermission.READ_ONE_APPOINTMENT,
            RolePermission.CREATE_ONE_APPOINTMENT,
            RolePermission.UPDATE_ONE_APPOINTMENT,
            RolePermission.DISABLED_ONE_APPOINTMENT,
            RolePermission.READ_MY_PROFILE,
            RolePermission.LOGOUT
    ));


    private List<RolePermission> permissions;

    Role(List<RolePermission> permissions) {
        this.permissions = permissions;
    }

    public List<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RolePermission> permissions) {
        this.permissions = permissions;
    }
}
