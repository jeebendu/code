public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Page<Object[]> getAppointments(SearchForm searchForm, Pageable pageable) {
        List<Object[]> results = appointmentRepository.getAppointmentsWithCounts(
            searchForm.getStatus(),
            searchForm.getCaseManagerId(),
            searchForm.getType(),
            searchForm.getValue(),
            searchForm.getDateType(),
            searchForm.getFromDate(),
            searchForm.getToDate(),
            pageable.getPageNumber() + 1, // Spring Pageable is 0-based
            pageable.getPageSize()
        );

        // Extract total count and filtered count from the first row
        int totalCount = results.isEmpty() ? 0 : (int) results.get(0)[0];
        int filteredCount = results.isEmpty() ? 0 : (int) results.get(0)[1];

        // Return as a Page object
        return new PageImpl<>(results, pageable, filteredCount);
    }
}








@Entity
@NamedStoredProcedureQuery(
    name = "GetAppointmentsWithCounts",
    procedureName = "GetAppointmentsWithCounts",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "status", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "caseManagerId", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "type", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "value", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "dateType", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "fromDate", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "toDate", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "pageNumber", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "pageSize", type = Integer.class)
    }
)
public class Apptrequeststatusrecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Other fields and mappings...
}





@Repository
public interface AppointmentRepository extends JpaRepository<Apptrequeststatusrecord, Long> {

    @Procedure(name = "GetAppointmentsWithCounts")
    List<Object[]> getAppointmentsWithCounts(
        @Param("status") String status,
        @Param("caseManagerId") Integer caseManagerId,
        @Param("type") String type,
        @Param("value") String value,
        @Param("dateType") String dateType,
        @Param("fromDate") String fromDate,
        @Param("toDate") String toDate,
        @Param("pageNumber") Integer pageNumber,
        @Param("pageSize") Integer pageSize
    );
}





