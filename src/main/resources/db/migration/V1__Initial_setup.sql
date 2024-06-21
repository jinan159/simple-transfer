-- BaseEntity Columns
CREATE TABLE simple_transfers
(
    id                      VARCHAR2(255) NOT NULL PRIMARY KEY,
    guid                    VARCHAR2(255) NOT NULL UNIQUE,
    status                  VARCHAR2(50)  NOT NULL,
    sender_id               VARCHAR2(255) NOT NULL,
    sender_account_number   VARCHAR2(255) NOT NULL,
    sender_transfer_id      VARCHAR2(255),
    receiver_id             VARCHAR2(255) NOT NULL,
    receiver_account_number VARCHAR2(255),
    receiver_transfer_id    VARCHAR2(255),
    pending_account_number  VARCHAR2(255) NOT NULL,
    requested_at            TIMESTAMP(6)  NOT NULL,
    received_at             TIMESTAMP(6),
    canceled_at             TIMESTAMP(6),
    expires_at              TIMESTAMP(6)  NOT NULL,
    created_at              TIMESTAMP(6)  NOT NULL,
    created_by              VARCHAR2(255) NOT NULL,
    updated_at              TIMESTAMP(6),
    updated_by              VARCHAR2(255),
    deleted_at              TIMESTAMP(6),
    CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES users (id),
    CONSTRAINT fk_receiver FOREIGN KEY (receiver_id) REFERENCES users (id)
);

CREATE TABLE users
(
    id          VARCHAR2(255) NOT NULL PRIMARY KEY,
    social_type VARCHAR2(50)  NOT NULL,
    social_id   VARCHAR2(255) NOT NULL,
    created_at  TIMESTAMP(6)  NOT NULL,
    created_by  VARCHAR2(255) NOT NULL,
    updated_at  TIMESTAMP(6),
    updated_by  VARCHAR2(255),
    deleted_at  TIMESTAMP(6)
);

CREATE TABLE transfers
(
    id                              VARCHAR2(255) NOT NULL PRIMARY KEY,
    sender_account_id               VARCHAR2(255) NOT NULL,
    sender_account_transaction_id   VARCHAR2(255),
    receiver_bank_code              VARCHAR2(50)  NOT NULL,
    receiver_account_number         VARCHAR2(255) NOT NULL,
    receiver_account_transaction_id VARCHAR2(255),
    transfer_amount                 NUMBER(12, 2) NOT NULL,
    status                          VARCHAR2(50)  NOT NULL,
    transferred_date                DATE,
    transferred_time                TIMESTAMP(6),
    failed_date                     DATE,
    failed_time                     TIMESTAMP(6),
    failed_reason                   VARCHAR2(255),
    created_at                      TIMESTAMP(6)  NOT NULL,
    created_by                      VARCHAR2(255) NOT NULL,
    updated_at                      TIMESTAMP(6),
    updated_by                      VARCHAR2(255),
    deleted_at                      TIMESTAMP(6)
);

CREATE TABLE accounts
(
    id                    VARCHAR2(255) NOT NULL PRIMARY KEY,
    name                  VARCHAR2(255) NOT NULL,
    account_number        VARCHAR2(255) NOT NULL UNIQUE,
    balance               NUMBER(14, 2) NOT NULL,
    transfer_limit_amount NUMBER(12, 2) NOT NULL,
    transfer_fee          NUMBER(12, 2) NOT NULL,
    created_at            TIMESTAMP(6)  NOT NULL,
    created_by            VARCHAR2(255) NOT NULL,
    updated_at            TIMESTAMP(6),
    updated_by            VARCHAR2(255),
    deleted_at            TIMESTAMP(6)
);

CREATE TABLE account_transactions
(
    id               VARCHAR2(255) NOT NULL PRIMARY KEY,
    account_id       VARCHAR2(255) NOT NULL,
    sequence         NUMBER(19)    NOT NULL,
    withdraw_amount  NUMBER(12, 2) NOT NULL,
    deposit_amount   NUMBER(12, 2) NOT NULL,
    balance          NUMBER(12, 2) NOT NULL,
    description      VARCHAR2(255),
    transaction_date DATE,
    transaction_time TIMESTAMP(6),
    created_at       TIMESTAMP(6)  NOT NULL,
    created_by       VARCHAR2(255) NOT NULL,
    updated_at       TIMESTAMP(6),
    updated_by       VARCHAR2(255),
    deleted_at       TIMESTAMP(6),
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts (id)
);
