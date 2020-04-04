-- create brand table
create table Brand(
    BrandID INTEGER auto_increment primary key,
    Name VARCHAR(50)
);
-- create country table
create table Country(
    CountryID INTEGER auto_increment primary key,
    Name VARCHAR(50)
);
-- create computer types table
create table ComputerType (
    TypeID INTEGER auto_increment primary key,
    Value VARCHAR(50)
);
-- create status table
create table Status (
    StatusID INTEGER auto_increment primary key,
    Value VARCHAR(50)
);
-- create computer table
create table Computer (
    ComputerID INTEGER auto_increment primary key,
    BrandID INTEGER,
    MadeIn INTEGER,
    Model VARCHAR(50),
    Price INTEGER,
    ScreenSize INTEGER,
    Type INTEGER,
    Status INTEGER,
    constraint fk_brandId
    foreign key (BrandID)
        references Brand(BrandID),
    constraint fk_madeIn
    foreign key (MadeIn)
        references Country(CountryID),
    constraint fk_type
    foreign key (Type)
        references ComputerType(TypeID),
    constraint fk_status
    foreign key (Status)
        references Status(StatusID)
);
