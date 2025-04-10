package com.java.assessment.JAVA_ASSESSMENT.constant;

public class AppConstants {

    private AppConstants() {}

    public static class BooleanConstants {

        private BooleanConstants() {}

        public static final String TRUE = "true";

    }

    public static class StringConstants {

        private StringConstants() {}

        public static final String EMPTY = "";

    }

    public static class TokenContants {

        private TokenContants() {}

        public  static final String ACCESS_TOKEN = "Access Token";

        public  static final String REFRESH_TOKEN = "Refresh Token";
    }

    public static class UserConstants {

        private UserConstants() {}

        public static final String USER_SAVED_SUCCESSFULLY = "User saved successfully!!!";
    }

    public static class Role {

        private Role() {}

        public static final String USER = "User";

        public static final String USER_CREATE = "user:create";

        public static final String MANAGER_CREATE = "manager:read";

        public static final String OIDC_USER = "OIDC_USER";

        public static final String OAUTH2_USER = "OAUTH2_USER";

    }

    public static class OAuthClient {

        private OAuthClient() {}

        public static final String GOOGLE = "google";

        public static final String FACEBOOK = "facebook";

    }

    public static class TokenType {

        private TokenType() {}

        public static final String ACCESS_TOKEN = "Access token";

        public static final String REFRESH_TOKEN = "Refresh token";

        public static final String RESET_PASSWORD_TOKEN = "Reset password token";

    }

    public static class Route {

        private Route() {}

        public static final String RESET_PASSWORD_ROUTE = "/api/v1/auth/resetPasswordAfterVerification";
    }

    public static class PasswordType {

        private PasswordType() {}

        public static final String NEW_PASSWORD = "New password";

        public static final String OLD_PASSWORD = "Old password";

        public static final String CONFIRM_PASSWORD = "Confirm password";
    }

    public static class Pagination{

        private Pagination() {}

        public static final String PAGE_NUMBER_DEFAULT_VALUE = "0";

        public static final String PAGE_SIZE_DEFAULT_VALUE = "10";

        public static final String ASCENDING_ORDER = "asc";

        public static final String DESCENDING_ORDER = "dsc";
    }

    public static class Entity {

        public static final String VEHICLE = "Vehicle";

        public static final String REGISTRATION_DATA = "RegistrationData";

        public static final String INSURANCE_DATA = "InsuranceData";

        public static final String POLLUTION_DATA = "PollutionData";

        public static final String TAX_DATA = "TaxData";

        public static final String BASE = "Base";

        public static final String TOKEN = "Token";

        public static final String USER = "User";

    }

    public static class RequestDto {

        public static final String VEHICLE_REQUEST_DTO = "VehicleRequestDto";

        public static final String REGISTRATION_DATA_REQUEST_DTO = "RegistrationDataRequestDto";

        public static final String INSURANCE_DATA_REQUEST_DTO = "InsuranceDataRequestDto";

        public static final String POLLUTION_DATA_REQUEST_DTO = "PollutionDataRequestDto";

        public static final String TAX_DATA_REQUEST_DTO = "TaxDataRequestDto";

    }

    public static class UniqueNumber {

        public static final String POLICY_NUMBER = "policyNumber";

        public static final String CERTIFICATE_NUMBER = "certificateNumber";

        public static final String REGISTRATION_NUMBER = "registrationNumber";

        public static final String DOCUMENT_NUMBER = "documentNumber";

        public static final String CHASSIS_NUMBER = "chassisNumber";

        public static final String VEHICLE_MODEL_NUMBER = "vehicleModelNumber";

    }
}
