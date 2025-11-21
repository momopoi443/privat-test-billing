--liquibase formatted sql

--changeset oleksandr:1
CREATE SCHEMA payment;

--changeset oleksandr:2
CREATE SEQUENCE IF NOT EXISTS payment.regular_payments_seq START WITH 1 INCREMENT BY 50;

--changeset oleksandr:3
CREATE SEQUENCE IF NOT EXISTS payment.payment_transactions_seq START WITH 1 INCREMENT BY 50;

--changeset oleksandr:4
CREATE TABLE payment.regular_payments (
    id BIGINT NOT NULL DEFAULT nextval('payment.regular_payments_seq'),
    payer_full_name VARCHAR(255) NOT NULL,
    payer_tax_number VARCHAR(10) NOT NULL,
    payer_card_number VARCHAR(16) NOT NULL,
    recipient_account VARCHAR(29) NOT NULL,
    recipient_bank_code VARCHAR(6) NOT NULL,
    recipient_edrpou VARCHAR(8) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    write_off_interval BIGINT NOT NULL,
    payment_amount NUMERIC(15, 2) NOT NULL,
    is_active BOOLEAN NOT NULL,
    CONSTRAINT pk_regular_payments PRIMARY KEY (id)
);

--changeset oleksandr:5
CREATE INDEX idx_regular_payments_tax_number
    ON payment.regular_payments(payer_tax_number);

--changeset oleksandr:6
CREATE INDEX idx_regular_payments_edrpou
    ON payment.regular_payments(recipient_edrpou);

--changeset oleksandr:7
--comment: Create payment_transactions table
CREATE TABLE payment.payment_transactions (
    id BIGINT NOT NULL DEFAULT nextval('payment.payment_transactions_seq'),
    transaction_datetime TIMESTAMP NOT NULL,
    regular_payment_id BIGINT NOT NULL,
    payment_amount NUMERIC(15, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT pk_payment_transactions PRIMARY KEY (id),
    CONSTRAINT fk_payment_transactions_regular_payment
        FOREIGN KEY (regular_payment_id)
        REFERENCES payment.regular_payments(id)
);

--changeset oleksandr:8
CREATE INDEX idx_payment_transactions_regular_payment_id
    ON payment.payment_transactions(regular_payment_id);