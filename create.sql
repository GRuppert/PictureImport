create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) type=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) type=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) type=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime, updater varchar(255), upddate datetime, altitude varchar(255), date_mod datetime, date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) type=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored varchar(255), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, valid bit not null, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, valid bit not null, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, odid varchar(255) not null, latitude varchar(255), longitude varchar(255), original_file_hash varchar(255), original_filename varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), filehash varchar(255), filename varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, original_filename varchar(255), size bigint, exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
create table drive (id integer not null auto_increment, backup bit not null, description varchar(255), volumeSN varchar(255), primary key (id)) engine=InnoDB
create table folder (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), name varchar(255), path varchar(255), drive_id integer, parent_id integer, primary key (id)) engine=InnoDB
create table image (id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), bestimate_file_hash varchar(255), bestimate_filename varchar(255), camera_make varchar(255), camera_model varchar(255), date_corrected varchar(255), date_taken varchar(255), duration bigint, orig_exif tinyblob, odid varchar(255) not null, keyword varchar(255), latitude varchar(255), longitude varchar(255), orientation integer, original_file_hash varchar(255), original_filename varchar(255), rating integer, title varchar(255), type varchar(255) not null, valid bit not null, parent_id integer, primary key (id)) engine=InnoDB
create table media_file (type varchar(31) not null, id integer not null auto_increment, creator varchar(255), credate datetime(6), updater varchar(255), upddate datetime(6), altitude varchar(255), date_mod datetime(6), date_stored_local datetime(6), date_stored varchar(255), date_stored_tz varchar(255), date_stored_utc datetime(6), exif tinyblob, filehash varchar(255), filename varchar(255), keyword varchar(255), latitude varchar(255), longitude varchar(255), name_version integer, orientation integer, original_filename varchar(255), rating integer, size bigint, title varchar(255), exifbackup bit, standalone bit, XMPattached bit, duration bigint, drive_id integer not null, folder_id integer not null, image_id integer, primary key (id)) engine=InnoDB
alter table folder add constraint UK6lb6n57pellfqqldmov7h61cl unique (drive_id, path)
alter table image add constraint UKn8snfdddek1s32eo4kongr4e8 unique (odid, type)
alter table media_file add constraint UKkl7noxvf10wrf52lcfk025iv7 unique (drive_id, folder_id, filename)
alter table folder add constraint FKfngugwoftw50m8ugyqxv6x3b1 foreign key (drive_id) references drive (id)
alter table folder add constraint FKn0cjh1seljcp0mc4tj1ufh99m foreign key (parent_id) references folder (id)
alter table image add constraint FKmui3ecb0wix4tmjwcadosreio foreign key (parent_id) references image (id)
alter table media_file add constraint FKh349dxs3864syg0mdqkkxrkv6 foreign key (drive_id) references drive (id)
alter table media_file add constraint FKc7fhkhb5n0t61rxugerxkiqi5 foreign key (folder_id) references folder (id)
alter table media_file add constraint FKbx2l8vb5gscxl1i9dsd6yy1ae foreign key (image_id) references image (id)
