SELECT mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id as original_version 
	FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id
    WHERE mfv.id = 19671
    GROUP BY mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id 
    ;

SELECT orig.original_filename, count(*) FROM (
SELECT mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id as original_version 
	FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id
    GROUP BY mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id) as orig
    group by orig.original_filename HAVING count(*)>1
    ;
    /** why no jpg? */

SET SQL_SAFE_UPDATES = 0;

UPDATE media_file_old SET original_filename = '' WHERE original_filename = '';
UPDATE media_file_old SET original_filename = '' WHERE original_filename = '';
UPDATE media_file_old SET original_filename = '' WHERE original_filename = '';
UPDATE media_file_old SET original_filename = '' WHERE original_filename = '';
UPDATE media_file_old SET original_filename = '' WHERE original_filename = '';
UPDATE media_file_old SET original_filename = '' WHERE original_filename = '';
   
SELECT mfo.original_filename, mfo.filehash, mfo.image_id, mfv.filetype, mfv.standalone, mfv.id as original_version 
	FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id
    WHERE mfv.id IN (
		SELECT origin.original_version FROM (
			SELECT mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id as original_version 
				FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id
				GROUP BY mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id
		) as origin
			group by origin.original_version HAVING count(*)>1
	) 
    GROUP BY mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id, mfo.filehash, mfo.image_id  
    ;	
   
SELECT count(*) FROM (
	SELECT orig.original_version FROM (
			SELECT mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id as original_version 
				FROM media_file_version mfv LEFT JOIN media_file_instance mfi ON mfi.media_file_id = mfv.id LEFT JOIN media_file_old mfo ON mfi.media_file_old_id = mfo.id
				GROUP BY mfo.original_filename, mfv.filetype, mfv.standalone, mfv.id
		) as orig
		group by orig.original_version HAVING count(*)>1
	) duplicates;
    
SELECT count(*) FROM (
	SELECT orig.image_id FROM (
		SELECT mfo.original_filename, mfo.image_id 
			FROM media_file_old mfo 
            WHERE mfo.image_id>3
			GROUP BY mfo.original_filename, mfo.image_id
		) as orig
		group by orig.image_id HAVING count(*)>1
    ) duplicates;    
    
SELECT mf.original_filename, mf.image_id, mf.shotnumber 
			FROM media_file_old mf 
WHERE mf.image_id in (SELECT orig2.image_id FROM (	SELECT orig.image_id, orig.shotnumber FROM (
		SELECT mfo.original_filename, mfo.image_id, mfo.shotnumber 
			FROM media_file_old mfo 
            WHERE mfo.image_id>3
			GROUP BY mfo.original_filename, mfo.image_id, mfo.shotnumber
		) as orig
		group by orig.image_id, orig.shotnumber HAVING count(*)>2
		) as orig2
)
 GROUP BY mf.original_filename, mf.image_id 
 ORDER BY 2,1;
 
 SELECT mfo.original_filename, REGEXP_REPLACE(mfo.original_filename, " ?\\(1\\)",'')
	FROM media_file_old mfo WHERE mfo.original_filename LIKE "%(1)%";


SET @camera_name := 'K610i' COLLATE utf8mb4_0900_as_cs;    
 SELECT mfo.original_filename, SUBSTRING_INDEX(mfo.original_filename, CONCAT("_", @camera_name, "-"),-1)
	FROM media_file_old mfo WHERE mfo.original_filename LIKE CONCAT("%_", @camera_name, "-%");
 UPDATE media_file_old mfo SET mfo.original_filename = SUBSTRING_INDEX(mfo.original_filename,CONCAT("_", @camera_name, "-"),-1) WHERE mfo.original_filename LIKE CONCAT("%_", @camera_name, "-%");  

SELECT mfo.*
	FROM media_file_old mfo WHERE mfo.original_filename = 'DSC00339.ARW' AND image_id = 103157;