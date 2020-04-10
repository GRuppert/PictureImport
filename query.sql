-- Fájlok száma egy könyvtárban, ami csak a G:-n van meg és a Porsche-n nem
SELECT m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.drive_id = 3 AND 0 = (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id in (4,5)) group by m.folder_id;
-- Fájlok száma egy könyvtárban, ami többször van meg egy meghajtón
SELECT m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE 1 < (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id = m.drive_id) group by m.folder_id;
-- Fájlok száma egy könyvtárban, ami a G:-n más filehash-el van meg mint a Porsche-n
SELECT m.folder_id, f.path, count(*) FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.drive_id = 3 AND 0 < (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id in (4,5) AND m2.filehash <> m.filehash) group by m.folder_id;
-- Fájlok, ami a G:-n más filehash-el van meg mint a Porsche-n
SELECT m.folder_id, f.path, m.filename FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.drive_id = 3 AND 0 < (SELECT count(*) FROM media_file AS m2 WHERE m2.image_id = m.image_id AND m2.drive_id in (4,5) AND m2.filehash <> m.filehash);
-- Fájlok, amik csak egyszer szereplnek
SELECT m.folder_id, f.path, m.filename FROM pictureorganizer.media_file AS m LEFT JOIN pictureorganizer.folder f ON f.id = m.folder_id WHERE m.image_id IN (SELECT image_id FROM media_file group by image_id having count(*) = 1);
