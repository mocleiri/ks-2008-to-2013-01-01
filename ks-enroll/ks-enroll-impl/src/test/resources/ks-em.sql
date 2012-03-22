--
-- Copyright 2010 The Kuali Foundation Licensed under the
-- Educational Community License, Version 2.0 (the "License"); you may
-- not use this file except in compliance with the License. You may
-- obtain a copy of the License at
--
-- http://www.osedu.org/licenses/ECL-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an "AS IS"
-- BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
-- or implied. See the License for the specific language governing
-- permissions and limitations under the License.
--

-- Enumeration States
insert into KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) values ('kuali.enumeration.state.active', 'Active', 'kuali.enumeration.process', 'Indicates that this Enumeration is active', 0)
insert into KSEN_COMM_STATE (ID, NAME, PROCESS_KEY, DESCR, VER_NBR) values ('kuali.enumeration.state.inactive', 'Inactive', 'kuali.enumeration.process', 'Indicates that this Enumeration is inactive', 0)

-- Enumeration Types

-- Enumerations 
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.lu.subjectArea', 'Subject Area Enumeration', 'subjectArea.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.lu.campusLocation', 'Campus Location Enumeration', 'campusLocation.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.atptype.duration', 'ATP Durations', 'duration.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.Two');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.state', 'KS Data States', 'state.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.lu.fee.feeType', 'Learning Unit Fee Type', 'feeType.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.lu.fee.rateType', 'Learning Unit Rate Type', 'rateType.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.Two');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.enum.type.cip2010', 'CIP 2010', 'cip2010.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.enum.type.cip2000', 'CIP 2000', 'cip2000.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.enum.lu.program.level', 'Program Level', 'level.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.Two');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.lu.code.UniversityClassification', 'University Classification', 'classification.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');
insert into KSEN_ENUM_T (ENUM_KEY, NAME, DESCR_PLAIN, ENUM_STATE, ENUM_TYPE) values ('kuali.lu.finalExam.status', 'Final Exam Status', 'status.descr', 'kuali.enumeration.state.active', 'kuali.enumeration.type.One');

--Enumerated Values
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('1', 'ROBT', 'ROBT', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '1', 'Robotics');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('2', 'CMPS', 'CMPS', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '2', 'CompSci');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('3', 'MECH', 'MECH', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '3', 'Mechanical');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('4', 'AACH', 'AACH', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '4', 'Architecture');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('5', 'ACCT', 'ACCT', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '5', 'Accounting');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('6', 'ARTS', 'ARTS', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '6', 'FineArts');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('7', 'BENG', 'BENG', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '7', 'Biomed');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('8', 'BIOC', 'BIOC', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '8', 'Biochemistry');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('9', 'BIOL', 'BIOL', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '9', 'Biology');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('10', 'BOTA', 'BOTA', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '10', 'Botany');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('11', 'CHEE', 'CHEE', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '11', 'Chemical');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('12', 'CHEM', 'CHEM', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '12', 'Chemistry');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('13', 'CIVI', 'CIVI', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '13', 'Civil');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('14', 'CSCI', 'CSCI', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '14', 'CompSci');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('15', 'EDUC', 'EDUC', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '15', 'Education');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('16', 'ENGL', 'ENGL', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '16', 'English');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('17', 'FINA', 'FINA', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '17', 'Finance');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('18', 'FREN', 'FREN', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '18', 'French');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('19', 'GEOG', 'GEOG', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '19', 'Geography');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('20', 'GEOL', 'GEOL', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '20', 'Geology');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('21', 'HIST', 'HIST', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '21', 'History');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('22', 'INTB', 'INTB', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '22', 'InternationalBusiness');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('23', 'LING', 'LING', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '23', 'Linguistics');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('24', 'MARK', 'MARK', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '24', 'Marketing ');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('25', 'MENG', 'MENG', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '25', 'Mechanical');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('26', 'MUSC', 'MUSC', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '26', 'Music');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('27', 'PHIL', 'PHIL', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '27', 'Philosophy');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('28', 'POLI', 'POLI', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '28', 'PolySci');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('29', 'PSYC', 'PSYC', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '29', 'Psychology');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('30', 'PUAD', 'PUAD', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '30', 'PubAdmin');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('31', 'SOCI', 'SOCI', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '31', 'Sociology');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('32', 'SOWK', 'SOWK', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '32', 'Social Work');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('59', 'AASP', 'AASP', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '59', 'African American Studies');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('60', 'AAST', 'AAST', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.subjectArea', {ts '2000-01-01 00:00:00.0'}, '60', 'Asian American Studies');

insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('33', 'North', 'NO', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.campusLocation', {ts '2000-01-01 00:00:00.0'}, '31', 'North');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('34', 'South', 'SO', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.campusLocation', {ts '2000-01-01 00:00:00.0'}, '32', 'South');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('64', 'Extended', 'EX', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.campusLocation', {ts '2000-01-01 00:00:00.0'}, '64', 'Extended');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('65', 'All', 'AL', {ts '2000-01-01 00:00:00.0'}, 'kuali.lu.campusLocation', {ts '2000-01-01 00:00:00.0'}, '65', 'All');

insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('35', 'per day', 'kuali.atp.duration.day', {ts '2000-01-01 00:00:00.0'}, 'kuali.atptype.duration', {ts '2000-01-01 00:00:00.0'}, '35', 'per day');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('36', 'per week', 'kuali.atp.duration.week', {ts '2000-01-01 00:00:00.0'}, 'kuali.atptype.duration', {ts '2000-01-01 00:00:00.0'}, '36', 'per week');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('37', 'per month', 'kuali.atp.duration.month', {ts '2000-01-01 00:00:00.0'}, 'kuali.atptype.duration', {ts '2000-01-01 00:00:00.0'}, '37', 'per month');

insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('38', 'template', 'template',{ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '38', 'A template holds configuration for defaults for creating a new course');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('39', 'draft', 'draft', {ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '39', 'Draft');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('41', 'submitted', 'submitted', {ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '41', 'Submitted but not yet approved');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('42', 'withdrawn', 'withdrawn', {ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '42', 'Withdrawn (anytime before Active)');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('43', 'approved', 'approved', {ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '43', 'Approved');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('44', 'rejected', 'rejected', {ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '44', 'Not approved (rejected)');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('45', 'Active', 'Active', {ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '45', 'Active and ready to be published');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('46', 'retired', 'retired', {ts '2000-01-01 00:00:00.0'}, 'kuali.state', {ts '2000-01-01 00:00:00.0'}, '46', 'Retired/No longer Active');

insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('47', 'Lab Fee', 'kuali.enum.type.feeTypes.labFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '47', 'Laboratory Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('48', 'Material Fee', 'kuali.enum.type.feeTypes.materialFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '48', 'Materials Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('49', 'Studio Fee', 'kuali.enum.type.feeTypes.studioFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '49', 'Studio Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('50', 'Field Trip Fee', 'kuali.enum.type.feeTypes.fieldTripFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '50', 'Field Trip Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('51', 'Field Study Fee', 'kuali.enum.type.feeTypes.fieldStudyFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '51', 'Field Study Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('52', 'Admin Fee', 'kuali.enum.type.feeTypes.administrativeFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '52', 'Administrative Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('53', 'Coop Fee', 'kuali.enum.type.feeTypes.coopFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '53', 'Coop Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('54', 'Greens Fee', 'kuali.enum.type.feeTypes.greensFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.feeType', {ts '2011-04-07 00:00:00.0'}, '54', 'Greens Fee');

insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('55', 'Variable Rate Fee', 'variableRateFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.rateType', {ts '2011-04-07 00:00:00.0'}, '55', 'Variable Rate Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('56', 'Fixed Rate Fee', 'fixedRateFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.rateType', {ts '2011-04-07 00:00:00.0'}, '56', 'Fixed Rate Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('57', 'Multiple Rate Fee', 'multipleRateFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.rateType', {ts '2011-04-07 00:00:00.0'}, '57', 'Multiple Rate Fee');
insert into KSEN_ENUM_VAL_T (ID, ABBREV_VAL, CD, EFF_DT, ENUM_KEY, EXPIR_DT, SORT_KEY, VAL) values ('58', 'Per Credit Fee', 'perCreditFee', {ts '2010-04-07 00:00:00.0'}, 'kuali.lu.fee.rateType', {ts '2011-04-07 00:00:00.0'}, '58', 'Per Credit Fee');


--Contexts
insert into KSEN_CTX_T (ID, CTX_KEY, CTX_VAL) values ('1', 'kuali.lu.subjectArea', 'default');
insert into KSEN_CTX_T (ID, CTX_KEY, CTX_VAL) values ('2', 'kuali.lu.campusLocation', 'default');
insert into KSEN_CTX_T (ID, CTX_KEY, CTX_VAL) values ('3', 'kuali.atptype.duration', 'default');
insert into KSEN_CTX_T (ID, CTX_KEY, CTX_VAL) values ('4', 'kuali.state', 'default');
insert into KSEN_CTX_T (ID, CTX_KEY, CTX_VAL) values ('5', 'kuali.lu.fee.feeType', 'default');
insert into KSEN_CTX_T (ID, CTX_KEY, CTX_VAL) values ('6', 'kuali.lu.fee.rateType', 'default');
