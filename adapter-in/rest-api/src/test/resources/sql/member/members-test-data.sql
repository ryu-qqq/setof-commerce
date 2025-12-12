-- Members Test Data
-- Generated for MemberApiIntegrationTest and AuthApiIntegrationTest

-- Clean up existing data to avoid duplicate key violations
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE refresh_tokens;
TRUNCATE TABLE members;
SET FOREIGN_KEY_CHECKS = 1;

-- Member 1: Active LOCAL member (for login test)
-- Password: "Password1!" BCrypt hash
INSERT INTO members (
    id, phone_number, email, password_hash, name,
    date_of_birth, gender, provider, social_id, status,
    privacy_consent, service_terms_consent, ad_consent,
    withdrawal_reason, withdrawn_at,
    created_at, updated_at, deleted_at
) VALUES (
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    '01012345678',
    'active@example.com',
    '$2a$10$v6JM8vGbCAO67WWo6Ni4neK5MxghDeetpCq5MHIi7Fe9BF4xYglLq',
    '활성회원',
    '1990-01-15',
    'M',
    'LOCAL',
    NULL,
    'ACTIVE',
    TRUE,
    TRUE,
    FALSE,
    NULL,
    NULL,
    '2025-01-01 00:00:00',
    '2025-01-01 00:00:00',
    NULL
);

-- Member 2: Active KAKAO member
INSERT INTO members (
    id, phone_number, email, password_hash, name,
    date_of_birth, gender, provider, social_id, status,
    privacy_consent, service_terms_consent, ad_consent,
    withdrawal_reason, withdrawn_at,
    created_at, updated_at, deleted_at
) VALUES (
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d02',
    '01087654321',
    'kakao@example.com',
    NULL,
    '카카오회원',
    '1995-05-20',
    'W',
    'KAKAO',
    'kakao_12345678',
    'ACTIVE',
    TRUE,
    TRUE,
    TRUE,
    NULL,
    NULL,
    '2025-01-02 00:00:00',
    '2025-01-02 00:00:00',
    NULL
);

-- Member 3: Withdrawn member
INSERT INTO members (
    id, phone_number, email, password_hash, name,
    date_of_birth, gender, provider, social_id, status,
    privacy_consent, service_terms_consent, ad_consent,
    withdrawal_reason, withdrawn_at,
    created_at, updated_at, deleted_at
) VALUES (
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d03',
    '01099998888',
    'withdrawn@example.com',
    '$2a$10$v6JM8vGbCAO67WWo6Ni4neK5MxghDeetpCq5MHIi7Fe9BF4xYglLq',
    '탈퇴회원',
    '1985-12-25',
    'M',
    'LOCAL',
    NULL,
    'WITHDRAWN',
    TRUE,
    TRUE,
    FALSE,
    'RARELY_USED',
    '2025-01-10 00:00:00',
    '2024-01-01 00:00:00',
    '2025-01-10 00:00:00',
    '2025-01-10 00:00:00'
);

-- Member 4: Inactive (dormant) member
INSERT INTO members (
    id, phone_number, email, password_hash, name,
    date_of_birth, gender, provider, social_id, status,
    privacy_consent, service_terms_consent, ad_consent,
    withdrawal_reason, withdrawn_at,
    created_at, updated_at, deleted_at
) VALUES (
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d04',
    '01044445555',
    'inactive@example.com',
    '$2a$10$v6JM8vGbCAO67WWo6Ni4neK5MxghDeetpCq5MHIi7Fe9BF4xYglLq',
    '휴면회원',
    '1992-06-10',
    'M',
    'LOCAL',
    NULL,
    'INACTIVE',
    TRUE,
    TRUE,
    FALSE,
    NULL,
    NULL,
    '2023-01-01 00:00:00',
    '2023-06-01 00:00:00',
    NULL
);

-- Refresh Token for Member 1
INSERT INTO refresh_tokens (
    member_id, token, expires_at, created_at
) VALUES (
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d01',
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test-refresh-token-member1',
    '2025-01-08 00:00:00',
    '2025-01-01 00:00:00'
);

-- Refresh Token for Member 2
INSERT INTO refresh_tokens (
    member_id, token, expires_at, created_at
) VALUES (
    '01936ddc-8d37-7c6e-8ad6-18c76adc9d02',
    'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test-refresh-token-member2',
    '2025-01-08 00:00:00',
    '2025-01-01 00:00:00'
);
