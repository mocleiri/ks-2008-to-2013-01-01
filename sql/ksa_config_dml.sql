--------------------------------------------------------
--  File created - Tuesday-April-10-2012   
--------------------------------------------------------

-- INSERTING SEQUENCE VALUES


insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ACCOUNT_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ACTIVITY_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ACTIVITY_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ALLOCATION_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('BANK_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('CREDIT_PERMISSION_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('CURRENCY_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('DOCUMENT_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ELECTRONIC_CONTACT_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('FLAG_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('GL_BREAKDOWN_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('GL_BREAKDOWN_OVERRIDE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('GL_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('INFORMATION_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('LATE_PERIOD_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('PERSON_NAME_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ADDRESS_NAME_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ROLLUP_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('TAG_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('TAX_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('ID_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('TRANSACTION_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('LANGUAGE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('REFUND_TYPE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('REFUND_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('REFUND_MANIFEST_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('BATCH_RECEIPT_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('TRANSACTION_MASK_ROLE_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('GL_TRANSACTION_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('GL_TRANSMISSION_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('EXT_STATEMENT_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('BILL_AUTHORITY_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('KEYPAIR_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('LEARNING_UNIT_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('LEARNING_PERIOD_SEQ', 1001);
insert into KSSA_SEQUENCE_TABLE (SEQ_NAME, SEQ_VALUE) values ('GL_RECOGNITION_PERIOD_SEQ', 1001);


--  SYSTEM CONFIGURATION PARAMETERS --
-- Rice-specific parameters --
insert into KSSA_CONFIG (NAME, VALUE) values ('application.host', 'localhost');

-- KSA-specific parameters --
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.locale.lang', 'en');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.locale.country', 'US');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.import.single.batch.failure', 'false');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.logging.operation', 'true');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.deferment.type.id', 'DEF');

-- General Ledger settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.general.ledger.type', 'SAR');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.general.ledger.mode', 'Individual');

-- Combined Cash Limit Tracking Settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.system', 'On');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.amount', '10000');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.days', '365');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.cash.tracking.tag', 'cash');

-- Memo settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.memo.level', '2');

-- Account settings
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.account.status.type', 'N/A');
insert into KSSA_CONFIG (NAME, VALUE) values ('ksa.account.credit.limit', '20000');


