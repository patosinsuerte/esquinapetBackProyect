package com.esquinaPet.veterinariabackend.domain.utils.enums;

public enum RolePermission {

    //Appointment permission
    READ_ALL_APPOINTMENTS,
    READ_ONE_APPOINTMENT,
    READ_OWN_APPOINTMENTS,
    CREATE_ONE_APPOINTMENT,
    UPDATE_ONE_APPOINTMENT,
    UPDATE_OWN_APPOINTMENT,
    DISABLED_ONE_APPOINTMENT,
    DISABLED_OWN_APPOINTMENT,
    LOGOUT,


    // services type
    READ_ALL_SERVICE_TYPES,
    READ_ONE_SERVICE_TYPE,
    CREATE_ONE_SERVICE_TYPES,
    UPDATE_ONE_SERVICE_TYPES,
    DISABLED_ONE_SERVICE_TYPES,

    READ_MY_PROFILE;
}
