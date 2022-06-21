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
		SELECT mfo.original_filename, mfo.image_id, mfo.shotnumber  
			FROM media_file_old mfo 
            WHERE mfo.image_id>3
			GROUP BY mfo.original_filename, mfo.image_id, mfo.shotnumber  
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
		group by orig.image_id, orig.shotnumber HAVING count(*)>1
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

SELECT mfo.*
	FROM media_file_old mfo WHERE image_id = 64841;

SELECT count(distinct(image_id))	FROM media_file_old mfo WHERE mfo.original_filename LIKE '%Copy%';

SELECT count(distinct(image_id))	FROM media_file_old mfo WHERE mfo.original_filename LIKE '2008\_%';

UPDATE media_file_old mf SET mf.original_filename = (SELECT * FROM (SELECT distinct(mfu.original_filename) FROM media_file_old mfu WHERE mfu.original_filename LIKE '2008\_%' AND mfu.image_id = mf.image_id) AS hidit2)
WHERE mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename NOT LIKE '2008\_%' AND mfo.image_id IN (SELECT distinct(image_id) FROM media_file_old mfo WHERE mfo.original_filename LIKE '2008\_%')) AS hidit
);

UPDATE media_file_old mf SET mf.original_filename = (SELECT * FROM (SELECT distinct(mfu.original_filename) FROM media_file_old mfu WHERE mfu.original_filename LIKE '%.jpeg' AND mfu.image_id = mf.image_id) AS hidit2)
WHERE mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename NOT LIKE '%.jpeg' AND mfo.image_id IN (SELECT distinct(image_id) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%.jpeg')) AS hidit
);
SELECT * FROM media_file_old mfo WHERE mfo.original_filename NOT LIKE '%.jpeg' AND mfo.image_id IN (SELECT distinct(image_id) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%.jpeg');



UPDATE media_file_old mf SET
 mf.original_filename = 'DSCF6398.JPG',
 mf.title_in_filename = 'száratlan bábakalács'
 WHERE mf.image_id = 87240;

UPDATE media_file_old mf SET mf.shotnumber = 1 WHERE mf.image_id = 137620 AND mf.original_filename = 'DSC02026.ARW';
UPDATE media_file_old mf SET mf.shotnumber = 2 WHERE mf.image_id = 137620 AND mf.original_filename = 'DSC02027.ARW';
UPDATE media_file_old mf SET mf.shotnumber = 3 WHERE mf.image_id = 137620 AND mf.original_filename = 'DSC02028.ARW';

SELECT * FROM media_file_old mfo WHERE mfo.image_id = 87240;

SELECT mf.original_filename, mf.image_id FROM media_file_old mf 
WHERE mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = CONCAT('DSC0', SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE 'DSC1%'), 5)))as i1) 
GROUP BY mf.original_filename, mf.image_id
ORDER BY 2 DESC, 1;

UPDATE media_file_old mf SET mf.original_filename = (SELECT * FROM (SELECT distinct(mf1.original_filename) FROM media_file_old mf1 WHERE mf.image_id = mf1.image_id AND mf1.original_filename LIKE 'DSC0%') AS hidit2)
WHERE mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = CONCAT('DSC0', SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE 'DSC1%'), 5))) as hidit1
);


SELECT mf.original_filename, mf.image_id FROM media_file_old mf 
WHERE mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE '\_DSC%'), 2)) AS hidit)
GROUP BY mf.original_filename, mf.image_id
ORDER BY 2 DESC, 1;

UPDATE media_file_old mf SET mf.original_filename = (SELECT * FROM (SELECT distinct(mfo.original_filename) FROM media_file_old mfo WHERE mf.image_id = mfo.image_id AND mfo.original_filename LIKE 'DSC%') AS hidit2 )
WHERE mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE '\_DSC%'), 2)) AS hidit
);

## RENDBEN 

SELECT count(distinct(image_id)) FROM media_file_old mfo WHERE mfo.original_filename LIKE '\_DSC%';
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE '\_DSC%'), 2);

SELECT image_id, count(*) FROM (
SELECT mf.original_filename, mf.image_id FROM media_file_old mf 
WHERE mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE '%\_DSC%'), 8)) AS hidit)
GROUP BY mf.original_filename, mf.image_id
ORDER BY 2 DESC, 1
) triplets GROUP BY image_id HAVING count(*)>2;

UPDATE media_file_old mfu SET mfu.original_filename = (SELECT * FROM (SELECT distinct(mf2.original_filename) FROM media_file_old mf2 WHERE mf.image_id = mf2.image_id AND mf2.original_filename LIKE 'DSC%') AS hidit2 )
WHERE mfu.image_id > 3 AND mfu.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE '%\_DSC%'), 8)) AS hidit)
;


SELECT count(distinct(image_id)) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%\_DSC%';
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE '%\_DSC%'), 8);

SELECT count(distinct(image_id)) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%-C0%';
SELECT mfo.original_filename, mfo.image_id, SUBSTRING(mfo.original_filename, 1, 5), SUBSTRING(mfo.original_filename, 7) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%-C0%' GROUP BY mfo.original_filename, mfo.image_id ORDER BY 2, 1;

UPDATE media_file_old mf SET
 mf.title_in_filename = SUBSTRING(mf.original_filename, 1, 5),
 mf.original_filename = SUBSTRING(mf.original_filename, 7)
 WHERE mf.original_filename LIKE '%-C0%';
 
UPDATE media_file_old mf SET mf.original_filename = (SELECT * FROM (SELECT distinct(mfo.original_filename) FROM media_file_old mfo WHERE mf.image_id = mfo.image_id AND mfo.original_filename NOT LIKE '%\_WP\_%') AS hidit2 )
WHERE mf.image_id > 3 AND mf.image_id IN (SELECT * FROM (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%\_WP\_%') as toto)
;


SELECT count(distinct(image_id)) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%_WP%';

SELECT count(*) FROM (SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename = SUBSTRING((SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id = mfo.image_id AND mf.original_filename LIKE '%\_WP\_%'), 8)) as inn1;

-- SELECT image_id, count(*) FROM (
SELECT mf.original_filename, mf.image_id FROM media_file_old mf 
WHERE mf.image_id > 3 AND mf.image_id IN (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename LIKE 'DSCF%')
AND mf.original_filename LIKE '%[^0-9]%.JPG'
GROUP BY mf.original_filename, mf.image_id
ORDER BY 2 DESC, 1
-- ) triplets GROUP BY image_id HAVING count(*)>1
;

SELECT mf.original_filename, mf.image_id FROM media_file_old mf 
WHERE mf.image_id > 3 AND mf.image_id IN (
SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE mfo.original_filename LIKE '%_\_DSC%'
) GROUP BY mf.original_filename, mf.image_id
ORDER BY 1 DESC, 2
;
SELECT mfi.filename FROM media_file_instance mfi LEFT JOIN media_file_old mfo ON mfo.id = mfi.media_file_old_id WHERE mfo.image_id = 180262 GROUP BY mfi.filename;

UPDATE media_file_old mf SET
 mf.title_in_filename = SUBSTRING(mf.original_filename, 1, 6)
 WHERE mf.original_filename LIKE '%_\_DSC%';

UPDATE media_file_old mf SET
mf.original_filename = (SELECT * FROM (SELECT distinct(mf2.original_filename) FROM media_file_old mf2 WHERE mf.image_id = mf2.image_id AND mf2.original_filename LIKE 'DSC%') AS hidit2 )
 WHERE mf.original_filename LIKE '%_\_DSC%' and mf.image_id > 3;

SELECT * FROM media_file_old mfo WHERE mfo.title_in_filename IS NOT NULL;

SELECT mf2.original_filename, mf2.image_id FROM media_file_old mf2 WHERE mf2.image_id IN (SELECT distinct(mf.image_id) FROM media_file_old mf WHERE mf.original_filename LIKE '%_\_DSC%') AND mf2.original_filename LIKE 'DSC%'
GROUP BY mf2.original_filename, mf2.image_id
ORDER BY 1 DESC, 2
;

SELECT f.*, mfi.filename FROM media_file_instance mfi LEFT JOIN media_file_old mfo ON mfo.id = mfi.media_file_old_id LEFT JOIN folder f ON mfi.folder_id = f.id WHERE mfo.filehash = '3fe70d21bf598e554def089045ef170c' GROUP BY mfi.filename;

SELECT distinct(mfo.image_id) FROM media_file_old mfo WHERE 1 < (SELECT distinct(mf.original_filename) FROM media_file_old mf WHERE mf.image_id > 3 AND mf.original_filename LIKE '%.gif');
SELECT mf.original_filename, mf.image_id FROM media_file_old mf WHERE mf.image_id > 3 AND mf.original_filename LIKE '%.gif' GROUP BY mf.original_filename, mf.image_id;
