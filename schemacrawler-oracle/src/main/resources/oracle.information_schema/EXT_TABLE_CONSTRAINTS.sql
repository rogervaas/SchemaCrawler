SELECT /*+ PARALLEL(AUTO) */
  NULL AS CONSTRAINT_CATALOG,
  OWNER AS CONSTRAINT_SCHEMA,
  CONSTRAINT_NAME,
  DBMS_METADATA.GET_DDL('CONSTRAINT',CONSTRAINT_NAME,OWNER) AS CHECK_CLAUSE
FROM
  ALL_CONSTRAINTS
WHERE
  OWNER NOT IN ('CTXSYS', 'DBSNMP', 'DMSYS', 'MDDATA', 'MDSYS', 'OLAPSYS', 'ORDPLUGINS', 'ORDSYS', 'OUTLN', 'SI_INFORMTN_SCHEMA', 'SYS', 'SYSMAN', 'SYSTEM', 'XDB', 'DBSNMP')
  AND OWNER NOT LIKE 'APEX%'
  AND TABLE_NAME NOT LIKE 'BIN$%'
  AND CONSTRAINT_TYPE IN ('C', 'U', 'P')
UNION ALL
SELECT
  NULL AS CONSTRAINT_CATALOG,
  OWNER AS CONSTRAINT_SCHEMA,
  CONSTRAINT_NAME,
  DBMS_METADATA.GET_DDL('REF_CONSTRAINT',CONSTRAINT_NAME,OWNER) AS CHECK_CLAUSE
FROM
  ALL_CONSTRAINTS
WHERE
  OWNER NOT IN ('CTXSYS', 'DBSNMP', 'DMSYS', 'MDDATA', 'MDSYS', 'OLAPSYS', 'ORDPLUGINS', 'ORDSYS', 'OUTLN', 'SI_INFORMTN_SCHEMA', 'SYS', 'SYSMAN', 'SYSTEM', 'XDB', 'DBSNMP')
  AND OWNER NOT LIKE 'APEX%'
  AND TABLE_NAME NOT LIKE 'BIN$%'
  AND CONSTRAINT_TYPE = 'R'
