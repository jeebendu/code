@Query("""
SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END
FROM PanelSettingsClinic pc
WHERE pc.clinic.id = :clinicId
  AND (:id IS NULL OR pc.id <> :id)
  AND (
        (:toDate IS NULL AND (pc.todate IS NULL OR pc.todate >= :fromDate))
        OR
        (:toDate IS NOT NULL AND (
              pc.todate IS NULL OR pc.fromdate <= :toDate
        ))
     )
  AND (
        pc.todate IS NULL OR :fromDate <= pc.todate
     )
  AND :toDate >= pc.fromdate
""")
boolean existOverlappingRecordExcludingId(
    @Param("fromDate") Date fromDate,
    @Param("toDate") Date toDate,
    @Param("id") Long id,
    @Param("clinicId") Long clinicId
);


@Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END FROM PanelSettingsClinic pc WHERE pc.clinic.id = :clinicId AND (:id IS NULL OR pc.id <> :id) AND pc.fromdate <= :toDate AND (pc.todate IS NULL OR pc.todate >= :fromDate)")
boolean existOverlappingRecordExcludingId(Date fromDate, Date toDate, Long id, Long clinicId);
