SELECT umfo.original_filename, umfo.image_id, umfo.shotnumber, umfv.filetype, umfv.standalone, umfv.id as original_version 
	FROM media_file_version umfv LEFT JOIN media_file_instance umfi ON umfi.media_file_id = umfv.id LEFT JOIN media_file_old umfo ON umfi.media_file_old_id = umfo.id
WHERE umfv.id IN (
	SELECT orig.original_version FROM (
			SELECT mfo.original_filename, mfo.shotnumber, mfv.filetype, mfv.standalone, mfv.id as original_version 
				FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id
##                WHERE image_id > 3
				GROUP BY mfo.original_filename, mfo.shotnumber, mfv.filetype, mfv.standalone, mfv.id
		) as orig
		group by orig.original_version HAVING count(*)>1
	)
	GROUP BY umfo.original_filename, umfo.shotnumber, umfv.filetype, umfv.standalone, umfv.id;

UPDATE media_file_old mf SET
 mf.original_filename = '0007.MPG'
 WHERE mf.filehash = 'c4103f122d27677c9db144cae1394a66';

UPDATE media_file_old mf SET
 mf.title_in_filename = '030121',
 mf.original_filename = 'DSC01978.gif'
 WHERE mf.filehash = 'e34501d3a7fdb33c9c4dd2e5c92352b5';


SELECT mfo.filehash, f.drive_id, f.path, mfi.*, mfo.date_stored
	FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id  LEFT JOIN folder f ON f.id = mfi.folder_id WHERE mfv.id = 60453;


SELECT distinct original_filename FROM media_file_old WHERE image_id < 4 AND date_stored IS NULL;
SELECT distinct(f.path)	FROM media_file_old mfo LEFT JOIN folder f ON f.id = mfo.folder_id WHERE mfo.filename LIKE '%.gif';
SELECT f.drive_id, f.path, mfo.* FROM media_file_old mfo LEFT JOIN folder f ON f.id = mfo.folder_id WHERE mfo.filehash = 'e34501d3a7fdb33c9c4dd2e5c92352b5';

UPDATE media_file_old mfo SET mfo.filename = (SELECT mfi.filename FROM media_file_instance mfi WHERE mfi.media_file_old_id = mfo.id);
UPDATE media_file_old mfo SET mfo.folder_id = (SELECT mfi.folder_id FROM media_file_instance mfi WHERE mfi.media_file_old_id = mfo.id);
UPDATE media_file_old mfo SET mfo.size = (SELECT mfv.size FROM media_file_instance mfi LEFT JOIN media_file_version mfv ON mfv.id = mfi.media_file_id WHERE mfi.media_file_old_id = mfo.id);


SELECT umfo.filehash, umfo.original_filename, umfo.image_id, umfo.shotnumber, umfo.type, umfo.standalone
	FROM media_file_old umfo
WHERE umfo.filehash IN (
	SELECT orig.filehash FROM (
			SELECT mfo.original_filename, mfo.filehash
				FROM media_file_old mfo
##                WHERE image_id > 3
				GROUP BY mfo.original_filename, mfo.filehash
		) as orig
		group by orig.filehash HAVING count(*)>1
	)
	GROUP BY umfo.filehash, umfo.original_filename, umfo.image_id, umfo.shotnumber, umfo.type, umfo.standalone ORDER BY 1;
    
SELECT umfo.filehash, umfo.original_filename, umfo.image_id, umfo.shotnumber, umfo.type, umfo.standalone
	FROM media_file_old umfo
WHERE umfo.filehash IN (
	SELECT orig.filehash FROM (
			SELECT mfo.original_filename, mfo.filehash
				FROM media_file_old mfo
##                WHERE image_id > 3
				GROUP BY mfo.original_filename, mfo.filehash
		) as orig
		group by orig.filehash HAVING count(*)>1
	)
	GROUP BY umfo.filehash, umfo.original_filename, umfo.image_id, umfo.shotnumber, umfo.type, umfo.standalone ORDER BY 1;    
    
UPDATE media_file_old mf SET
mf.original_filename = (SELECT * FROM (SELECT distinct(mf2.original_filename) FROM media_file_old mf2 WHERE mf.filehash = mf2.filehash ORDER BY mf2.original_filename LIMIT 1) AS hidit2 )
 WHERE mf.original_filename LIKE '%.gif' AND 1 < (SELECT * FROM (SELECT count(distinct(mfo.original_filename)) FROM media_file_old mfo WHERE mf.filehash = mfo.filehash) AS hidit);

SELECT
LEFT((SELECT * FROM (SELECT distinct(mf2.original_filename) FROM media_file_old mf2 WHERE mf.filehash = mf2.filehash AND mf2.original_filename LIKE '%\_%.MTS') AS hidit2 ),6) AS seqN,
(SELECT * FROM (SELECT distinct(mf2.original_filename) FROM media_file_old mf2 WHERE mf.filehash = mf2.filehash AND mf2.original_filename NOT LIKE '%\_%.MTS') AS hidit2 )
FROM media_file_old mf 
 WHERE mf.original_filename LIKE '%.MTS' AND 1 < (SELECT * FROM (SELECT count(distinct(mfo.original_filename)) FROM media_file_old mfo WHERE mf.filehash = mfo.filehash) AS hidit);
    
UPDATE media_file_old mf SET
mf.title_in_filename =
LEFT((SELECT * FROM (SELECT distinct(mf3.original_filename) FROM media_file_old mf3 WHERE mf.filehash = mf3.filehash AND mf3.original_filename LIKE '%\_%.MTS') AS hidit2 ),6),
mf.original_filename =
(SELECT * FROM (SELECT distinct(mf2.original_filename) FROM media_file_old mf2 WHERE mf.filehash = mf2.filehash AND mf2.original_filename NOT LIKE '%\_%.MTS') AS hidit2 )
 WHERE mf.original_filename LIKE '%\_%.MTS' AND 1 < (SELECT * FROM (SELECT count(distinct(mfo.original_filename)) FROM media_file_old mfo WHERE mf.filehash = mfo.filehash) AS hidit);
 

SELECT
*
FROM media_file_old mf 
 WHERE mf.title_in_filename IS NOT NULL AND 1 < (SELECT * FROM (SELECT count(distinct(mfo.title_in_filename)) FROM media_file_old mfo WHERE mf.original_filename = mfo.original_filename) AS hidit);
