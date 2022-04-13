CREATE TABLE POPULATION_STATS
(
    ID int not null primary key,
    PROVINCE_CODE varchar(255),
    PROVINCE_NAME varchar(255),
    CITY_CODE varchar(255),
    CITY_NAME varchar(255),
    DISTRICT_CODE varchar(255),
    DISTRICT_NAME varchar(255),
    NUMBER_OF_POPULATION long,
    NUMBER_OF_POPULATION_DECEASED long,
    REPORT_DATE varchar(255)
);

CREATE TABLE POPULATION_DELTA
(
    ID int not null primary key,
    PROVINCE_CODE varchar(255),
    PROVINCE_NAME varchar(255),
    CITY_CODE varchar(255),
    CITY_NAME varchar(255),
    DISTRICT_CODE varchar(255),
    DISTRICT_NAME varchar(255),
    NUMBER_OF_PEOPLE_BORNED long,
    NUMBER_OF_PEOPLE_DIED long,
    REPORT_TIME varchar(255),
    DT varchar(255)
);